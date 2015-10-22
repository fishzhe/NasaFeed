package com.nasafeed.handlers;

import com.nasafeed.domain.ImageInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
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
    private List<ImageInfo> imageInfos;

    public IotHandler() {
        imageInfos = new ArrayList<ImageInfo>();
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

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            Element source = (Element) element.getElementsByTagName("enclosure").item(0);

            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setUrl(source.getAttribute("url"));
            imageInfo.setDescription(element.getElementsByTagName("description").item(0).getTextContent());
            imageInfo.setPubDate(element.getElementsByTagName("pubDate").item(0).getTextContent());
            imageInfo.setTitle(element.getElementsByTagName("title").item(0).getTextContent());

            imageInfos.add(imageInfo);
        }
    }

    public List<ImageInfo> getImageInfos() {
        return this.imageInfos;
    }
}
