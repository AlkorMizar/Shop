package by.bsuir.shop.ui.favorite;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import by.bsuir.shop.ImageLoder;
import by.bsuir.shop.Item;
import by.bsuir.shop.R;
import by.bsuir.shop.data.DAOFactory;

public class FavoriteAdapter extends BaseAdapter {
    Context ctx;
    String name;
    LayoutInflater lInflater;
    ArrayList<Item> objects;

    FavoriteAdapter(Context context, ArrayList<Item> products) {
        ctx = context;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ctx);
        if (account != null) {
            name=account.getDisplayName();
        }else{
            name="unknown";
        }
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("view");
        // используем созданные, но не используемые view
        View view = convertView;
        System.out.println(view);
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }

        Item p = getProduct(position);

        ImageLoder.loadFromBytes(view.findViewById(R.id.image),p.img,ctx.getDrawable(R.drawable.ic_loading));
        ((TextView) view.findViewById(R.id.name_lable)).setText(p.name);
        ((TextView) view.findViewById(R.id.price)).setText(p.price +"");

        ImageButton cbBuy = view.findViewById(R.id.favorite);

        // присваиваем чекбоксу обработчик
        cbBuy.setBackgroundColor(Color.TRANSPARENT);
        BaseAdapter adapter=this;
        cbBuy.setOnClickListener(v -> {
            DAOFactory.getDAO().deleteItemFromFav(p,name);
            objects.remove(p);
            adapter.notifyDataSetChanged();
        });
        return view;
    }

    // товар по позиции
    Item getProduct(int position) {
        return ((Item) getItem(position));
    }

}