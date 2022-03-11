package by.bsuir.shop.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import by.bsuir.shop.Item;
import by.bsuir.shop.MainActivity;
import by.bsuir.shop.data.DAOFactory;
import by.bsuir.shop.data.loaders.Filter;
import by.bsuir.shop.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private Filter filter;

    public HomeFragment(){
        super();
    }

    public HomeFragment(Filter filter){
        super();
        this.filter=filter;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<Item> list;
        if (filter!=null){
            list=DAOFactory.getDAO().getFilteredList(filter);
        }else{
            list=DAOFactory.getDAO().getAllItems();
        }
        final HomeAdapter adapter = new HomeAdapter(this.getContext(), list);
        ListView listView=binding.listView;
        listView.setAdapter(adapter);
        System.out.println(list);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            final Item item = (Item) parent.getItemAtPosition(position);
            System.out.println(item);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}