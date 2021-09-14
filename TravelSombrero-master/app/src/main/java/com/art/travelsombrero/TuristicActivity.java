package com.art.travelsombrero;

import android.app.Activity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class TuristicActivity extends Activity implements OnItemClickListener {

    private final String FILENAME = "news_feed.xml";

    private RSSFeed feed;

    private TextView titleTextView;
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turistic);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.turisticListView);

        itemsListView.setOnItemClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String URL_STRING = extras.getString("turistic");
            new TuristicActivity.DownloadFeed().execute("https://www.aladyinlondon.com/feed");
        }
    }

    class DownloadFeed extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            try{
                // get the URL
                URL url = new URL(params[0]);

                // get the input stream
                InputStream in = url.openStream();

                // get the output stream
                Context context = TuristicActivity.this;
                FileOutputStream out =
                        context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

                // read input and write output
                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);
                while (bytesRead != -1)
                {
                    out.write(buffer, 0, bytesRead);
                    bytesRead = in.read(buffer);
                }
                out.close();
                in.close();
            }
            catch (IOException e) {
                Log.e("News reader", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed downloaded: " + new Date());
            new TuristicActivity.ReadFeed().execute();
        }
    }

    class ReadFeed extends AsyncTask<Void, Void, RSSFeed> {

        @Override
        protected RSSFeed doInBackground(Void... params) {
            try {
                // get the XML reader
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader xmlreader = parser.getXMLReader();

                // set content handler
                RSSFeedHandler theRssHandler = new RSSFeedHandler();
                xmlreader.setContentHandler(theRssHandler);

                // read the file from internal storage
                FileInputStream in = openFileInput(FILENAME);

                // parse the data
                InputSource is = new InputSource(in);
                xmlreader.parse(is);

                // return the feed
                RSSFeed feed = theRssHandler.getFeed();
                return feed;
            }
            catch (Exception e) {
                Log.e("News reader", e.toString());
                return null;
            }
        }

        // This is executed after the feed has been read
        @Override
        protected void onPostExecute(RSSFeed result) {
            Log.d("News reader", "Feed read: " + new Date());

            // update the display for the activity
            TuristicActivity.this.feed = result;
            TuristicActivity.this.updateDisplay();
        }
    }

    public void updateDisplay()
    {
        if (feed == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }

        // get the items for the feed
        ArrayList<RSSItem> items = feed.getAllItems();

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        int i=0;
        for (RSSItem item : items) {
            if (i<15 && (item.getPubDate() != null && item.getTitle() != null && item.getDescription() != null && item.getLink() != null)) {
                HashMap<String, String> map = new HashMap<String, String>();
                String[] e = item.getDescription().split("</p>");
                String[] r = e[0].split("<p>");
                String d = item.getPubDate().substring(0,22);
                map.put("date", d);
                map.put("title", item.getTitle());
                map.put("description", r[1]);
                map.put("link", item.getLink());
                data.add(map);
                i++;
            }
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"date", "title", "description"};
        int[] to = {R.id.pubDateTextView, R.id.titleTextView, R.id.descriptionTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("News reader", "Feed displayed: " + new Date());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        // get the intent
        Intent intent = getIntent();
        RSSItem item = feed.getItem(position);
        intent.putExtra("link", item.getLink());

        // get the Uri for the link
        Uri viewUri = Uri.parse(item.getLink());

        // create the intent and start it
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, viewUri);
        startActivity(viewIntent);
    }
}