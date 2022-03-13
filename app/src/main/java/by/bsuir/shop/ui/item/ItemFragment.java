package by.bsuir.shop.ui.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import by.bsuir.shop.ImageLoder;
import by.bsuir.shop.Item;
import by.bsuir.shop.R;
import by.bsuir.shop.databinding.FragmentItemBinding;
import by.bsuir.shop.ui.home.HomeFragment;
import by.bsuir.shop.ui.search.SearchFragment;

public class ItemFragment extends Fragment {
    private FragmentItemBinding binding;
    private Item item;


    public ItemFragment(Item item){
        this.item = item;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageLoder.loadFromBytes(binding.img,item.img,getContext().getDrawable(R.drawable.ic_loading));
        binding.name.setText(item.name);
        binding.price.setText(item.price+"");
        binding.category.setText(item.categoryID);
        binding.description.setText(item.description);
        binding.specification.setText(item.specification);

        binding.viewWebBtn.setOnClickListener(view -> {

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
