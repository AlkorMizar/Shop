package by.bsuir.shop.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import by.bsuir.shop.Item;
import by.bsuir.shop.data.loaders.DB;
import by.bsuir.shop.data.loaders.Filter;
import by.bsuir.shop.data.loaders.Loader;

public class Distributor implements DAO{
    DB db;
    public Distributor(){
        Loader loader=new Loader();
        db=loader.loadData();
    }

    @Override
    public ArrayList<Item> getAllItems() {
        //lock.readLock().lock();
        ArrayList<Item> list=db.getList();
        //lock.readLock().unlock();
        return list;
    }

    @Override
    public ArrayList<Item> getFilteredList(Filter filter) {
        //lock.readLock().lock();
        ArrayList<Item> list=db.getFilteredList(filter);
        //lock.readLock().unlock();
        return list;
    }

    @Override
    public ArrayList<Item> getFavItems(String user) {
        //lock.readLock().lock();
        ArrayList list=db.getFavItems(user);
        //lock.readLock().unlock();
        return list;
    }

    @Override
    public String getCategoryId(String name) {
        return db.getCategoryId(name);
    }

    @Override
    public List<String> getCategories() {
        //lock.readLock().lock();
        List list=db.getCategories();
        //lock.readLock().unlock();
        return list;

    }

    @Override
    public Item getItem(Filter filter) {
        //lock.readLock().lock();
        Item item=db.getItem(filter);
        //lock.readLock().unlock();
        return item;
    }

    @Override
    public boolean isInFavorite(Item item, String user) {
        return db.IsInFavorite(item,user);
    }

    @Override
    public void addItemToFav(Item item, String user) {
        //lock.readLock().lock();
        db.addFavItem(item,user);
        //lock.readLock().unlock();
    }

    @Override
    public void deleteItemFromFav(Item item, String user) {
        //lock.readLock().lock();
        db.deleteFavItem(item,user);
        //lock.readLock().unlock();
    }

    @Override
    public void updateComment(Item item, String user, String comment) {
        db.updateComment(item,user,comment);
    }

    @Override
    public String getComment(Item item, String user) {
        return db.getComment(item,user);
    }
}
