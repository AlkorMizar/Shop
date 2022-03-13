package by.bsuir.shop.data.loaders;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.bsuir.shop.ImageLoder;
import by.bsuir.shop.Item;
import by.bsuir.shop.ShopApplication;
import by.bsuir.shop.data.Pair;
import by.bsuir.shop.data.tree.Tree;

public class DB {

    private SQLiteDatabase db;
    private Tree tree;

    public DB(){
        try {
            db = ShopApplication.getAppContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS categories ("+"" +
                        "name TEXT NOT NULL,"+
                        "id TEXT NOT NULL,"+
                        "parent_id TEXT,"+
                        "PRIMARY KEY (id))");

            db.execSQL("CREATE TABLE IF NOT EXISTS items ("+"" +
                        "name TEXT NOT NULL,"+
                        "id TEXT NOT NULL PRIMARY KEY,"+
                        "description TEXT," +
                        "price INTEGER NOT NULL," +
                        "img BLOB," +
                        "specification TEXT," +
                        "articul TEXT ,"+
                        "category_id TEXT NOT NULL,"+
                        "FOREIGN KEY (category_id)" +
                                "REFERENCES categories (id) " +
                                "ON DELETE CASCADE " +
                                "ON UPDATE CASCADE)");

            db.execSQL("CREATE TABLE IF NOT EXISTS favorite ("+"" +
                        "name TEXT NOT NULL,"+
                        "item_id TEXT NOT NULL,"+
                        "comment TEXT,"   +
                        "PRIMARY KEY (name, item_id),"+
                        "FOREIGN KEY (item_id)" +
                            "REFERENCES items (id) " +
                                "ON DELETE NO ACTION " +
                                "ON UPDATE NO ACTION)");
        }catch (Exception e){
            e.printStackTrace();
        }
        resetTree();
    }

    @Override
    protected void finalize() throws Throwable {
        db.close();
        super.finalize();
    }

    public String getComment(Item item, String user){
        Cursor query = db.rawQuery("SELECT comment FROM favorite WHERE name = ? AND item_id = ?",
                new String[] {user, item.id});
        if (query.moveToNext()){
            return query.getString(0);
        }
        return "";
    }

    public void updateComment(Item item, String user,String comment){
        db.execSQL(
                "UPDATE favorite SET comment=? WHERE name = ? AND item_id = ?",
                new String[] {comment,user, item.id}
        );
    }

    public void addFavItem(Item item, String user){
        String query= String.format("INSERT OR IGNORE INTO favorite VALUES ('%s', '%s',NULL);",user,item.id);
        db.execSQL(query);
    }

    public void deleteFavItem(Item item,String user){
        String query= String.format("DELETE FROM favorite WHERE name='%s' AND item_id='%S';",user,item.id);
        db.execSQL(query);
    }

    public void saveList(List<Item> list) {
        /*String query= "DELETE FROM items;";
        db.execSQL(query);
        for (Item item:list ) {
            System.out.println("save item");
            db.insert("items",null,item.getContentValues());
        insert or replace into
        }*/
        for (Item item : list) {
            db.execSQL(
                    "INSERT OR REPLACE INTO items (id,name,description,price,img,specification,articul,category_id) VALUES (?,?,?,?,?,?,?,?)",
                    new Object[]{item.id,item.name,item.description,item.price, ImageLoder.loadBitmap(item.imgURL),item.specification,item.articul,item.categoryID}
            );
        }
    }

    public ArrayList<Item> getList(){
        Cursor query = db.rawQuery("SELECT name,id,price,img,category_id FROM items;", null);
        ArrayList<Item> items=new ArrayList<>(query.getCount());
        while(query.moveToNext()){
            System.out.println("getItem");
            Item item=new Item();
            item.name=query.getString(0);
            item.id=query.getString(1);
            item.price=query.getInt(2);
            try {
                item.img= query.getBlob(3);
            }catch (Exception e){}

            items.add(item);
        }
        return items;
    }

    public ArrayList getCategories(){
        Cursor query = db.rawQuery("SELECT name FROM categories ORDER BY id;", null);
        ArrayList categories=new ArrayList<String>(query.getCount());
        while(query.moveToNext()){

            categories.add(query.getString(0));
        }
        return categories;
    }

    public String getCategoryId(String name){
        Cursor query = db.rawQuery("SELECT id FROM categories WHERE name='"+name+"';", null);
        query.moveToNext();
        return query.getString(0);
    }

    public void saveCategories(HashMap<String, Pair> categories){
        String query= "DELETE FROM categories;";
        db.execSQL(query);
        for (Map.Entry<String,Pair> category:categories.entrySet() ) {
            ContentValues cv=new ContentValues();
            cv.put("id",category.getKey());
            cv.put("parent_id",category.getValue().getParentId());
            cv.put("name",category.getValue().getName());
            db.insert("categories",null,cv);
        }

        resetTree();
    }

    private void resetTree(){
        Cursor query = db.rawQuery("SELECT id,parent_id FROM categories ORDER BY id;", null);
        if(!query.moveToNext()) {
            return;
        }
        tree=new Tree(query.getString(0));
        while(query.moveToNext()){
            tree.add(query.getString(1),query.getString(0));
        }
    }

    public ArrayList<Item> getFilteredList(Filter filter){
        Cursor query = db.rawQuery("SELECT name,id,price,img,category_id FROM items WHERE "+filter.getFilter()+";", null);
        ArrayList<Item> items=new ArrayList<>(query.getCount());
        String parentId = getCategoryId(filter.categoryName);
        while(query.moveToNext()){
            if(tree.isChild(query.getString(4),parentId)){
                Item item=new Item();
                item.name=query.getString(0);
                item.id=query.getString(1);
                item.price=query.getInt(2);
                item.img= query.getBlob(3);

                items.add(item);
            }
        }
        return items;
    }

    public Item getItem(Filter filter){
        Cursor query = db.rawQuery("SELECT items.name,items.id,items.description,items.price,items.img,items.specification,categories.name,items.articul " +
                "FROM items " +
                "JOIN categories ON categories.id=items.category_id " +
                "WHERE "+filter.getFilter()+";", null);
        Item item=new Item();
        if (query.moveToNext()){
            item.name=query.getString(0);
            item.id=query.getString(1);
            item.description=query.getString(2);
            item.price=query.getInt(3);
            item.img= query.getBlob(4);
            item.specification=query.getString(5);
            item.categoryID=query.getString(6);
            item.articul=query.getString(7);
        }
        return item;
    }

    public ArrayList<Item> getFavItems(String user) {
        Cursor query = db.rawQuery("SELECT items.name,items.id,items.price,items.img FROM items " +
                "INNER JOIN favorite ON items.id=favorite.item_id AND favorite.name='" +user+"';", null);
        ArrayList<Item> items=new ArrayList<>(query.getCount());
        while(query.moveToNext()){
            Item item=new Item();
            item.name=query.getString(0);
            item.id=query.getString(1);
            item.price=query.getInt(2);
            item.img= query.getBlob(3);

            items.add(item);
        }
        return items;
    }

    public boolean IsInFavorite(Item item,String user){
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM favorite WHERE name = ? AND item_id = ?",
                new String[] {user, item.id}
        );
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
