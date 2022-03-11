package by.bsuir.shop.ui.favorite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import by.bsuir.shop.Item;
import by.bsuir.shop.data.DAOFactory;
import by.bsuir.shop.databinding.FragmentFavoriteBinding;


public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Context ctx=this.getContext();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ctx);
        if (account != null) {
            binding.registerLbl.setVisibility(View.INVISIBLE);
            String name=account.getDisplayName();
            ArrayList<Item> list=DAOFactory.getDAO().getFavItems(name);
            final FavoriteAdapter adapter = new FavoriteAdapter(ctx, list);
            ListView listView=binding.listViewFav;
            listView.setAdapter(adapter);
            System.out.println(list);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                final Item item = (Item) parent.getItemAtPosition(position);
                System.out.println(item);
            });
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
