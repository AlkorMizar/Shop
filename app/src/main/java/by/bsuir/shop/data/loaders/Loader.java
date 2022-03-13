package by.bsuir.shop.data.loaders;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import by.bsuir.shop.Item;
import by.bsuir.shop.data.Pair;
import by.bsuir.shop.data.loaders.net.NetLoader;

public class Loader {
    Timer updater;
    DB db;
    NetLoader netLoader;

    public Loader(){
        netLoader=new NetLoader();
        db=new DB();
        //updateData();
        updater=new Timer();
    }

    public DB loadData(){

        updater.schedule(new TimerTask() {
            @Override
            public void run() {
                updateData();
            }
        },0,TimeUnit.MINUTES.toMillis(2));
        return db;
    }

    private void updateData(){

        if(netLoader.isNetworkAvailable()){
            HashMap<String, Pair> categories=netLoader.getCategories();
            if (categories==null || categories.size()==0){
                return;
            }

            List<Item> list=netLoader.getList();
            if (list==null || list.size()==0){
                return;
            }
            //lock.writeLock().lock();
            db.saveCategories(categories);
            db.saveList(list);
            //lock.writeLock().unlock();
        }
    }
}
