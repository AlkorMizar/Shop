package by.bsuir.shop.data.loaders.net;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import by.bsuir.shop.Item;
import by.bsuir.shop.data.Pair;

public class CreateItemTask extends Thread {
    HashMap<String,Pair> categories;
    Element el;
    Item item;


    CreateItemTask(Element el,Item item,HashMap<String, Pair> categories){
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
        String cat=el.getElementsByTagName("categoryId").item(0).getTextContent();
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
}