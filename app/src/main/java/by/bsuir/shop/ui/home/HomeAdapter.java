package by.bsuir.shop.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import by.bsuir.shop.ImageLoder;
import by.bsuir.shop.Item;
import by.bsuir.shop.R;
import by.bsuir.shop.data.DAOFactory;
import by.bsuir.shop.data.loaders.Filter;
import by.bsuir.shop.ui.item.ItemFragment;

public class HomeAdapter extends BaseAdapter {
    Context ctx;
    FragmentActivity activity;
    String name;
    AlertDialog.Builder alertName;
    LayoutInflater lInflater;
    ArrayList<Item> objects;
    EditText editText;

    EditText txt; // user input bar

    HomeAdapter(Context context,FragmentActivity activity, ArrayList<Item> products) {
        ctx = context;
        this.activity = activity;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ctx);
        if (account != null) {
            name = account.getDisplayName();
        } else {
            name = "unknown";
        }
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    public void collectInput(Item item){
        // convert edit text to string
        String getInput = txt.getText().toString();

        // ensure that user input bar is not empty
        if (getInput ==null || getInput.trim().equals("")){
            Toast.makeText(ctx, "Please enter comment", Toast.LENGTH_LONG).show();
        }
        else {
            DAOFactory.getDAO().updateComment(item,name,getInput);
        }
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

        view.setOnClickListener(view1 -> {
            System.out.println("dddddddddddddd");
            Filter filter=new Filter();
            filter.setId(p.id);
            Item item=DAOFactory.getDAO().getItem(filter);
            FragmentTransaction trans = activity.getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment_container, new ItemFragment(item));
            trans.commit();
        });

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        ImageLoder.loadFromBytes(view.findViewById(R.id.image),p.img,ctx.getDrawable(R.drawable.ic_loading));
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
        view.findViewById(R.id.comment).setOnClickListener(v -> {
            alertName= new AlertDialog.Builder(ctx);
            editText = new EditText(ctx);
            editText.setText(DAOFactory.getDAO().getComment(p,name));
            alertName.setTitle("Comment");
            alertName.setView(editText);
            LinearLayout layoutName = new LinearLayout(ctx);
            layoutName.setOrientation(LinearLayout.VERTICAL);
            layoutName.addView(editText); // displays the user input bar
            alertName.setView(layoutName);
            alertName.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    txt = editText; // variable to collect user input
                    collectInput(p); // analyze input (txt) in this method
                }
            });

            alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel(); // closes dialog
                }
            });
                alertName.show(); // display the dialog
        });
        if(DAOFactory.getDAO().isInFavorite(p,name)){
            cbBuy.callOnClick();
            view.findViewById(R.id.comment).setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.comment).setVisibility(View.INVISIBLE);
        }
        return view;
    }

    // товар по позиции
    Item getProduct(int position) {
        return ((Item) getItem(position));
    }


}