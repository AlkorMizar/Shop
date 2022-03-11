package by.bsuir.shop;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.SurfaceControl;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.maps.SupportMapFragment;

import by.bsuir.shop.data.loaders.Filter;
import by.bsuir.shop.databinding.ActivityMainBinding;
import by.bsuir.shop.ui.favorite.FavoriteFragment;
import by.bsuir.shop.ui.home.HomeFragment;
import by.bsuir.shop.ui.profile.ProfileFragment;
import by.bsuir.shop.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new ProfileFragment());
        binding.bottomNavigationView.setOnItemSelectedListener((item)->{

            switch (item.getItemId()){
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navigation_profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.navigation_search:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.navigation_favorite:
                    replaceFragment(new FavoriteFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        if(fragment!=null){
            FragmentTransaction trans= getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment_container,fragment);
            trans.commit();
        }

    }

}