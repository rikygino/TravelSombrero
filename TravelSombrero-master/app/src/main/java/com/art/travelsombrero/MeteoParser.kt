package com.art.travelsombrero

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private val ns: String? = null

class MeteoParser {

    //istanzia parser e inizia analisi
    fun parse(inputStream: InputStream): Dato {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    //metodo di parse vero e proprio
    private fun readFeed(parser: XmlPullParser): Dato {
        var dato = Dato()
        parser.require(XmlPullParser.START_TAG, ns, "feed")
        //var di controllo mi fa vedere solo il primo item, gli altri li ignora
        var controllo = false
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "item" && controllo == false) {
                dato = Dato(readEntry(parser))
                controllo = true
            }else{
                skip(parser)
            }
        }
        return dato
    }

    class Dato {

        val temperatura: Int
        val meteo: String
        val link: String

        constructor(temperatura: Int, meteo: String, link: String) {
            this.temperatura = temperatura
            this.meteo       = meteo
            this.link        = link
        }

        constructor(d: Dato) {
            this.temperatura = d.temperatura
            this.meteo       = d.meteo
            this.link        = d.link
        }

        constructor():this(0,"","")
    }


    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): Dato {
        parser.require(XmlPullParser.START_TAG, ns, "entry")
        var dato = Dato()
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "description" -> dato = readAll(parser)
                else -> skip(parser)
            }
        }
        return dato
    }

    // Legge tutto e restituisce
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readAll(parser: XmlPullParser): Dato {
        var temperatura=0
        var meteo: String?
        var link: String?
        parser.require(XmlPullParser.START_TAG, ns, "description")
        var s = parser.toString().trim()
        var j=0
        while (s.get(j)<'0' || s.get(j)>'9') {j++}
        var tempe = s.substring(j,j+2).trim().toInt()
        //devo convertire temperatura da F a C
        temperatura=(tempe-32)*(5/9)
        //continuo a raccogliere gli altri due dati
        s=s.substring(j+10).trim()
        s.split(" ")
        meteo= s[0].toString()
        link=s[2].toString().substring(4,(s[2].toString().length)-1)
        parser.require(XmlPullParser.END_TAG, ns, "description")
        var dato = Dato(temperatura,meteo,link)
        return dato
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}