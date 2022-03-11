package by.bsuir.shop.data.loaders.net;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import by.bsuir.shop.data.Pair;

public class GetCategoriesTask extends Thread {
    HashMap<String,Pair> categories;
    HashMap<String,String> idOfCat;
    private String key="685223f8dadc499ed93ae013fabc67c1";

    GetCategoriesTask(HashMap<String, Pair> categories, HashMap<String,String> idOfCat){
        super("getSections");
        this.categories=categories;
        this.idOfCat=idOfCat;
    }

    public void run(){

        try{
            URL url = new URL(String.format("https://www.galacentre.ru/api/v2/catalog/xml/?key=%s&active=1&section=%s&price_from=100000",key,14));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();

            // Instantiate the Factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {

                DocumentBuilder db = dbf.newDocumentBuilder();

                Document doc = db.parse(con.getInputStream());

                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("category");

                for (int temp = 0; temp < list.getLength(); temp++) {

                    Node node = list.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element element = (Element) node;
                        String id = element.getAttribute("id");
                        String parentId = element.getAttribute("parentId");
                        categories.put(id,new Pair(element.getTextContent(),parentId) );
                        idOfCat.put(element.getTextContent(), id);
                    }
                }

            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }


            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

