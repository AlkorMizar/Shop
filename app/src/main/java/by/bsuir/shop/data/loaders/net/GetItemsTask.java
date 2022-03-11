package by.bsuir.shop.data.loaders.net;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import by.bsuir.shop.Item;
import by.bsuir.shop.data.Pair;

public class GetItemsTask extends Thread {
    ArrayList<Item> list;
    HashMap<String, Pair> categories;
    HashMap<String,String> idOfCat;

    private String key="685223f8dadc499ed93ae013fabc67c1";

    GetItemsTask(ArrayList<Item> list, HashMap<String,String> idOfCat, HashMap<String,Pair> categories){
        super("getList");
        this.idOfCat=idOfCat;
        this.list=list;
        this.categories=categories;
    }

    public void run(){
        LinkedList<Thread> ts=new LinkedList<>();

        list.clear();
        try{
            String urlStr=String.format("https://www.galacentre.ru/api/v2/catalog/xml/?key=%s&active=1&section=%s",key,14);
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {

                DocumentBuilder db = dbf.newDocumentBuilder();

                Document doc = db.parse(con.getInputStream());

                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("offer");

                for (int temp = 0; temp < list.getLength(); temp++) {

                    Node node = list.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element element = (Element) node;
                        Item item=new Item();
                        this.list.add(item);
                        Thread t=new CreateItemTask(element,item,categories);
                        t.start();
                        ts.push(t);

                    }
                }

                for (Thread t:ts) {
                    t.join();
                }

            } catch (ParserConfigurationException | SAXException | IOException e) {
                System.err.println(e);
            }


            con.disconnect();
        }catch (Exception e){
            System.err.println(e);
        }
    }

}
