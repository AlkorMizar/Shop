/*package by.bsuir.shop.data.loaders;

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
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import by.bsuir.shop.Item;


public class NetworkLoadData {

    private String key="685223f8dadc499ed93ae013fabc67c1";
    private HashMap<String,String> categories;
    private HashMap<String,String> idOfCat;
    private ArrayList<Item> list;

    private boolean isNetworkAvailable() {
        try {
            URL url = new URL("http://www.galacentre.ru/api/v2/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            con.disconnect();
            return true;
        }catch (Exception e){
            return false;
        }

    }

    private void  loadDataFromSql(){

    }

    public LoadData() {
        list = new ArrayList<>();
        categories = new HashMap<>();
        idOfCat = new HashMap<>();
        if (!isNetworkAvailable()) {
            loadDataFromSql();
        }
    }

    public List<String> getSections() {
        if (!categories.isEmpty()){
            return new ArrayList<>(categories.values()) ;
        }
        Thread t = new TasksGetSections(categories,idOfCat);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(categories.values());
    }

    public ArrayList<Item> getData(HashMap<String,String> filter) {
        if (!list.isEmpty()){
            return list;
        }
        Thread t = new TasksGetCatalog(list,filter,idOfCat,categories);
        t.start();
        try {
            t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  list;

    }
}

public class TasksGetSections extends Thread {
    HashMap<String,String> categories,idOfCat;
    private String key="685223f8dadc499ed93ae013fabc67c1";

    TasksGetSections(HashMap<String,String> categories,HashMap<String,String> idOfCat){
        super("getSections");
        this.categories=categories;
        this.idOfCat=idOfCat;
    }

    public void run(){

        try{
            URL url = new URL(String.format("https://www.galacentre.ru/api/v2/sections/xml/?key=%s&active=1",key));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();

            // Instantiate the Factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {

                DocumentBuilder db = dbf.newDocumentBuilder();

                Document doc = db.parse(con.getInputStream());

                doc.getDocumentElement().normalize();

                // get <staff>
                NodeList list = doc.getElementsByTagName("category");

                for (int temp = 0; temp < list.getLength()/10; temp++) {

                    Node node = list.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element element = (Element) node;
                        String id = element.getAttribute("id");
                        if (id.equals("18")) {
                            break;
                        }
                        categories.put(id, element.getTextContent());
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


public class TasksGetCatalog extends Thread {
    ArrayList<Item> list;
    HashMap<String,String> filter,idOfCat,categories;

    private String key="685223f8dadc499ed93ae013fabc67c1";

    TasksGetCatalog(ArrayList<Item> list,HashMap<String,String> filter,HashMap<String,String> idOfCat,HashMap<String,String> categories){
        super("getCat");
        this.idOfCat=idOfCat;
        this.list=list;
        this.filter=filter;
        this.categories=categories;
    }

    public void run(){
        LinkedList<Thread> ts=new LinkedList<>();

        String id=idOfCat.get(filter.get("section"));
        list.clear();
        try{
            String urlStr=String.format("https://www.galacentre.ru/api/v2/catalog/xml/?key=%s&active=1&section=%s",key,id);
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

                for (int temp = 0; temp < list.getLength()/10; temp++) {

                    Node node = list.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element element = (Element) node;
                        System.out.println(element);
                        Item item=new Item();
                        this.list.add(item);
                        Thread t=new CreateItem(element,item,categories);
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

class CreateItem extends Thread {
    static Semaphore lock=new Semaphore(8);
    HashMap<String,String> categories;
    Element el;
    Item item;


    CreateItem(Element el,Item item,HashMap<String,String> categories){
        super("createItem");
        this.categories=categories;
        this.el=el;
        this.item=item;
    }



    public void run(){

        String name=el.getElementsByTagName("name").item(0).getTextContent();
        String id=el.getAttribute("id");
        String imgUrl=el.getElementsByTagName("picture").item(0).getTextContent();
        String descr=el.getElementsByTagName("description").item(0).getTextContent();
        String cat=categories.get(el.getElementsByTagName("categoryId").item(0).getTextContent());
        String brand="";
        int price=0;
        NodeList list = el.getElementsByTagName("param");

        for (int temp = 0,fl=0; temp < list.getLength() && fl<2; temp++) {

            Node node = list.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element param= (Element) node;
                switch (param.getAttribute("name")){
                    case "brand":
                        fl++;
                        brand=param.getTextContent();
                        break;
                    case "price_base":
                        fl++;
                        price=(int)(Math.ceil( Float.parseFloat(param.getTextContent())));
                        break;
                }

            }
        }
        item.SetAll(id,name,price,imgUrl,cat,descr,brand);
    }
}*/