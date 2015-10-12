package com.nasafeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Zhe Yu on 2015/10/11.
 */
public class IotHandler {
    private String url = "http://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss";
    private boolean inUrl = false;
    private boolean inTitle = false;
    private boolean inDescription = false;
    private boolean inItem = false;
    private boolean inDate = false;
    private Bitmap image = null;
    private String title = null;
    private String description = null;
    private String date = null;

    public void processFeed() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder  = factory.newDocumentBuilder();
            InputStream inputStream = new URL(url).openStream();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");
            read(nList);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


    }

    private Bitmap getBitmap(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void read(NodeList nodeList) {
        Node node = nodeList.item(0);
        Element element = (Element)node;
        Element source = (Element) element.getElementsByTagName("enclosure").item(0);
        image = getBitmap(source.getAttribute("url"));
        title = element.getElementsByTagName("title").item(0).getTextContent();
        description = element.getElementsByTagName("description").item(0).getTextContent();
        date = element.getElementsByTagName("pubDate").item(0).getTextContent();
    }

    public Bitmap getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
