package by.bsuir.shop.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import by.bsuir.shop.R;
import by.bsuir.shop.data.DAOFactory;
import by.bsuir.shop.data.loaders.Filter;
import by.bsuir.shop.databinding.FragmentSearchBinding;
import by.bsuir.shop.ui.home.HomeFragment;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item,
                                                                    DAOFactory.getDAO().getCategories());
        binding.categoriesSpinner.setAdapter(spinnerAdapter);
        View root = binding.getRoot();
        binding.searchButton.setOnClickListener(view -> {

            Filter filter=new Filter();

            filter.setName(binding.searchName.getText().toString());
            filter.setCategoryName(binding.categoriesSpinner.getSelectedItem().toString());

            filter.setPriceFrom(Integer.parseInt(binding.editTextNumberDecimalFrom.getText().toString()));
            filter.setPriceTo(Integer.parseInt(binding.editTextNumberDecimalTo.getText().toString()));
            FragmentTransaction trans = SearchFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment_container, new HomeFragment(filter));
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