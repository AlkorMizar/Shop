package by.bsuir.shop.ui.favorite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import by.bsuir.shop.Item;
import by.bsuir.shop.R;
import by.bsuir.shop.data.DAOFactory;
import by.bsuir.shop.databinding.FragmentFavoriteBinding;
import by.bsuir.shop.ui.home.HomeFragment;
import by.bsuir.shop.ui.item.ItemFragment;


public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Context ctx=this.getContext();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ctx);
        String name;
        if (account != null) {
            name=account.getDisplayName();
        }else {
            name="unknown";
        }
        binding.registerLbl.setVisibility(View.INVISIBLE);
        ArrayList<Item> list=DAOFactory.getDAO().getFavItems(name);
        final FavoriteAdapter adapter = new FavoriteAdapter(ctx, list);
        ListView listView=binding.listViewFav;
        listView.setAdapter(adapter);
        System.out.println(list);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            final Item item = (Item) parent.getItemAtPosition(position);
            FragmentTransaction trans = FavoriteFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment_container, new ItemFragment(item));
            trans.commit();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
