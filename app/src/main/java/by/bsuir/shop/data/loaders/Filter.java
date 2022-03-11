package by.bsuir.shop.data.loaders;

import by.bsuir.shop.data.DAOFactory;

public class Filter {
    String id="";
    String name="";
    int priceFrom;
    int priceTo;
    String categoryName ="";

    public Filter(){
        priceFrom=0;
        priceTo=1000_000;
    }

    public String getFilter(){
        boolean andFl=false;
        StringBuilder res=new StringBuilder();

        if (!id.equals("")){
            andFl=true;
            res.append("items.id='"+id+"'");
        }
        if (!name.trim().equals("")){
            if (andFl){
                res.append(" AND ");
            }
            andFl=true;
            res.append("items.name LIKE '%"+name+"%'");
        }
        if (andFl){
            res.append(" AND ");
        }
        res.append("items.price>="+priceFrom+" AND "+"items.price<="+priceTo);

        return res.toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
