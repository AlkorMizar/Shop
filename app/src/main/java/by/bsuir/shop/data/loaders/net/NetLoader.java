package by.bsuir.shop.data.loaders.net;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import by.bsuir.shop.Item;
import by.bsuir.shop.data.Pair;

public class NetLoader {

    private HashMap<String, Pair> categories=new HashMap<>();
    private HashMap<String,String> idOfCat=new HashMap<>();
    private ArrayList<Item> list=new ArrayList<>();

    public boolean isNetworkAvailable() {
        try {
            URL url = new URL("https://www.galacentre.ru");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            con.disconnect();
            return status!=-1 && status<=500;
        }catch (Exception e){
            return false;
        }

    }

    public HashMap<String,Pair> getCategories(){
        categories.clear();
        idOfCat.clear();
        Thread t = new GetCategoriesTask(categories,idOfCat);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public ArrayList<Item> getList() {
        list.clear();
        Thread t = new GetItemsTask(list,idOfCat,categories);
        t.start();
        try {
            t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  list;

    }
}
