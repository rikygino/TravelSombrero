package com.art.travelsombrero

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.art.travelsombrero.databinding.ActivityDetailsOfTripBinding
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DetailsOfTripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsOfTripBinding
    private lateinit var dest : DestinationDataModel
    private lateinit var entries : MeteoParser.Dato
    private var print:String ="pd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsOfTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextButton.setOnClickListener {
            var intent = Intent( this, InsertDataTripActivity::class.java)
            val city = dest.city
            intent.putExtra("city", city)
            startActivity(intent)
        }

        var extras = intent.extras
        if (extras != null) {
            dest = DestinationDataModel(
                extras.getSerializable("alpha_3").toString(),
                extras.getSerializable("city").toString(),
                extras.getSerializable("imageUrl").toString(),
                extras.getSerializable("locCode").toString(),
                extras.getSerializable("state").toString())
        }
        loadPage()

    }

    companion object {

        const val WIFI = "Wi-Fi"
        const val ANY = "Any"
        const val SO_URL = "http://rss.accuweather.com/rss/liveweather_rss.asp?locCode=NAM|US|NY|NEW%20YORK"
        // Whether there is a Wi-Fi connection.
        private var wifiConnected = false
        // Whether there is a mobile connection.
        private var mobileConnected = false
        // Whether the display should be refreshed.
        var refreshDisplay = true
        // The user's current network preference setting.
        var sPref: String? = null
    }

    // Uses AsyncTask subclass to download the XML feed from stackoverflow.com.
    // Uses AsyncTask to download the XML feed from stackoverflow.com.
    fun loadPage() {
        if (sPref.equals(ANY) && (wifiConnected || mobileConnected)) {
            DownloadXmlTask().execute(SO_URL)
            Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        }
        else if (sPref.equals(WIFI) && wifiConnected) {
            Log.d("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
            DownloadXmlTask().execute(SO_URL)
        }
        else {
            Log.d("CCCCCCCCCCCCCCCCCCCCCCCCC", "CCCCCCCCCCCCCCCCCCCCCCCCC")
            // show error, ma anche no
        }
    }

    // Implementation of AsyncTask used to download XML feed from meteo
    private inner class DownloadXmlTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String {
            return try {
                loadXmlFromNetwork(urls[0])
                } catch (e: IOException) {
                     resources.getString(R.string.connection_error)
                } catch (e: XmlPullParserException) {
                      resources.getString(R.string.xml_error)
            }
        }

        override fun onPostExecute(result: String) {
            //todo
            //devo mettere la pagina xml giusta, non quella della home

            setContentView(R.layout.activity_details_of_trip)
            // Displays the HTML string in the UI via a WebView
            findViewById<WebView>(R.id.webview)?.apply {
                loadData(result, "text/html", null)
            }
        }
    }

    // Uploads XML parses it, and combines it with
    @Throws(XmlPullParserException::class, IOException::class)
    private fun loadXmlFromNetwork(urlString: String): String {
        // Checks whether the user set the preference to include summary text
        val pref: Boolean = PreferenceManager.getDefaultSharedPreferences(this)?.run {
            getBoolean("summaryPref", false)
        } ?: false

        entries = downloadUrl(urlString)?.use { stream ->
            // Instantiate the parser
            MeteoParser().parse(stream)
        } ?: MeteoParser.Dato()
        print = StringBuilder().apply {
            append("<h3>${entries.temperatura}</h3>")
            append("<h3>${entries.meteo}</h3>")
            append("<h3>${entries.link}</h3>")
        }.toString()

        binding.textView.text = print
        return print
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): InputStream? {
        val url = URL(urlString)
        return (url.openConnection() as? HttpURLConnection)?.run {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            // Starts the query
            connect()
            inputStream
        }
    }


}


