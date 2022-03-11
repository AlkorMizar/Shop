package by.bsuir.shop.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import by.bsuir.shop.ImageLoder;
import by.bsuir.shop.Item;
import by.bsuir.shop.R;
import by.bsuir.shop.data.DAOFactory;

public class HomeAdapter extends BaseAdapter {
    Context ctx;
    String name;
    LayoutInflater lInflater;
    ArrayList<Item> objects;

    HomeAdapter(Context context, ArrayList<Item> products) {
        ctx = context;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ctx);
        if (account != null) {
            name=account.getDisplayName();
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
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }

        Item p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        ImageLoder.loadFromBytes(view.findViewById(R.id.image),p.img);
        ((TextView) view.findViewById(R.id.name_lable)).setText(p.name);
        ((TextView) view.findViewById(R.id.price)).setText(p.price +"");

        ImageButton cbBuy = view.findViewById(R.id.favorite);

        // присваиваем чекбоксу обработчик
        cbBuy.setBackgroundColor(Color.TRANSPARENT);
        cbBuy.setOnClickListener(new View.OnClickListener() {
            boolean checked;
            @Override
            public void onClick(View v) {
                if (!checked){
                    checked=true;
                    cbBuy.setBackgroundColor(Color.argb(100,100,100,100));
                    DAOFactory.getDAO().addItemToFav(p,name);
                }else{
                    checked=false;
                    cbBuy.setBackgroundColor(Color.TRANSPARENT);
                    DAOFactory.getDAO().deleteItemFromFav(p,name);
                }
            }
        });
        if (name == null) {
            cbBuy.setVisibility(View.INVISIBLE);
        }else if(DAOFactory.getDAO().isInFavorite(p,name)){
            cbBuy.callOnClick();
        }
        return view;
    }

    // товар по позиции
    Item getProduct(int position) {
        return ((Item) getItem(position));
    }

}