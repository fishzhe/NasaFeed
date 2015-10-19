package com.nasafeed.handlers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.nasafeed.domain.ImageContainer;
import com.nasafeed.utils.LoadImageUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Zhe Yu on 2015/10/11.
 */
public class IotHandler {
    private String url = "http://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss";
    private List<ImageContainer> images;
    private Point imageSize;

    public IotHandler() {
    }

    public IotHandler(Point size) {
        this.imageSize = size;
    }

    public void processFeed() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
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

    private void read(NodeList nodeList) {
        images = new ArrayList<ImageContainer>();
//nodeList.getLength() / 10
        for (int i = 0; i < 2; i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            Element source = (Element)element.getElementsByTagName("enclosure").item(0);
            Bitmap image = getBitmap(source.getAttribute("url"));
            ImageContainer imageContainer= new ImageContainer();
            imageContainer.setImage(image);
            imageContainer.setDescription(element.getElementsByTagName("description").item(0).getTextContent());
            imageContainer.setPubDate(element.getElementsByTagName("pubDate").item(0).getTextContent());
            imageContainer.setTitle(element.getElementsByTagName("title").item(0).getTextContent());
            images.add(imageContainer);
        }
    }

    private Bitmap getBitmap(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            Bitmap bitmap = null;
            bitmap = LoadImageUtils.decodeSampledBitmapFromStream(inputStream, null, imageSize.x, imageSize.y);
            inputStream.close();

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ImageContainer> getImages() {
        return images;
    }
}
