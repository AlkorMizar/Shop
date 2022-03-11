package by.bsuir.shop.data.tree;

public class Tree {
    Node root;

    public Tree(String id){
        root=new Node(Integer.parseInt(id));
    }

    public void add(String parent,String id){
        int p=Integer.parseInt(parent);
        int i=Integer.parseInt(id);
        Node parentNode=root.getById(p);
        parentNode.addChild(i);
    }

    public boolean isChild(String id,String parent){
        int p=Integer.parseInt(parent);
        int i=Integer.parseInt(id);
        Node parentNode=root.getById(p);
        return parentNode.getById(i)!=null;

    }
}
