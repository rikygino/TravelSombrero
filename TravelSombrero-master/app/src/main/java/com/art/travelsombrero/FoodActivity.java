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

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class FoodActivity extends Activity implements AdapterView.OnItemClickListener {

    private final String URL_STRING = "https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/food/rss.xml";
    private final String FILENAME = "news_feed.xml";

    private RSSFeed feed;

    private TextView titleTextView;
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.FoodListView);

        itemsListView.setOnItemClickListener(this);

        new FoodActivity.DownloadFeed().execute(URL_STRING);
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
                Context context = FoodActivity.this;
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
            new FoodActivity.ReadFeed().execute();
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
            FoodActivity.this.feed = result;
            FoodActivity.this.updateDisplay();
        }
    }

    public void updateDisplay()
    {
        if (feed == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }

        // set the title for the feed
        titleTextView.setText("Food and Drink");

        // get the items for the feed
        ArrayList<RSSItem> items = feed.getAllItems();

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        int i=0;
        for (RSSItem item : items) {
            if (i<15 && item.getPubDate() !=null && item.getTitle() !=null) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", item.getPubDate());
                map.put("title", item.getTitle());
                data.add(map);
                i++;
            }
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"date", "title"};
        int[] to = {R.id.pubDateTextView, R.id.titleTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("News reader", "Feed displayed: " + new Date());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        // get the item at the specified position
        RSSItem item = feed.getItem(position);

        // create an intent
        Intent intent = new Intent(this, ItemActivity.class);

        intent.putExtra("pubdate", item.getPubDate());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("link", item.getLink());

        this.startActivity(intent);
    }

}
