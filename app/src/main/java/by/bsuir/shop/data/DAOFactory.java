package by.bsuir.shop.data;

public class DAOFactory {
    static DAO dao=new Distributor();

    public static DAO getDAO(){
        return dao;
    }
}
