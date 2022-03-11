package by.bsuir.shop.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import by.bsuir.shop.Item;
import by.bsuir.shop.data.loaders.Filter;

public interface DAO {
    ArrayList<Item> getAllItems();
    ArrayList<Item> getFilteredList(Filter filter);
    ArrayList<Item> getFavItems(String user);
    String getCategoryId(String name);
    List<String> getCategories();
    Item getItem(Filter filter);
    boolean isInFavorite(Item item,String user);
    void addItemToFav(Item item,String user);
    void deleteItemFromFav(Item item,String user);
}
