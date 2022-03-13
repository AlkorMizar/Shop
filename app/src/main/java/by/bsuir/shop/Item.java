package by.bsuir.shop;

import android.content.ContentValues;
import android.graphics.Bitmap;

public class Item {
    public String id;
    public String name;
    public int price;
    public String imgURL;
    public String categoryID;
    public  String description;
    public String specification;
    public byte[] img;
    public String articul;


    public Item(){}

    public void SetAll(String id, String name, int price, String imgURL, String categoryID, String description, String specification,String articul) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgURL = imgURL;
        this.categoryID = categoryID;
        this.description = description;
        this.specification = specification;
        this.articul=articul;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imgURL='" + imgURL + '\'' +
                ", category='" + categoryID + '\'' +
                ", description='" + description + '\'' +
                ", specification='" + specification + '\'' +
                '}';
    }

    public ContentValues getContentValues(){

        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("id",id);
        cv.put("description",description);
        cv.put("price",price);
        cv.put("img",ImageLoder.loadBitmap(imgURL));
        cv.put("category_id",categoryID);
        cv.put("specification",specification);
        cv.put("articul",articul);

        return cv;
    }

}
