package by.bsuir.shop.data.tree;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> children;
    private int id;

    public Node(int id) {
        children=new ArrayList<>();
        this.id = id;
    }

    public Node getById(int id){
        if (this.id==id){
            return this;
        }
        if (this.id>id){
            return null;
        }
        for (Node n:children) {
            Node n2=n.getById(id);
            if (n2!=null){
                return n2;
            }
        }
        return null;
    }

    public void addChild(int id){
        children.add(new Node(id));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
