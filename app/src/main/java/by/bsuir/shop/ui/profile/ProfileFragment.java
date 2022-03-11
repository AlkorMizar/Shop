package by.bsuir.shop.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import by.bsuir.shop.ImageLoder;
import by.bsuir.shop.MainActivity;
import by.bsuir.shop.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    Context ctx;
    private GoogleSignInClient mGoogleSignInClient;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleSignInResult(task);
            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            completedTask.getResult(ApiException.class);
            Intent intent = new Intent(ctx, MainActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            Log.w("ERROR", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void signIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(intent);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ctx = container.getContext();
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ctx);
        if (account != null) {
            loadUser(account);
        }

        binding.signInButton.setOnClickListener(view -> {
            signIn();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(ctx, gso);

        return root;

    }


    @SuppressLint("MissingPermission")
    private void loadUser(GoogleSignInAccount account) {
        binding.signInButton.setVisibility(View.INVISIBLE);
        binding.mapView.setVisibility(View.VISIBLE);
        binding.avatar.setVisibility(View.VISIBLE);

        if (account.getPhotoUrl() != null) {
            ImageLoder.loadImageOnImageView(binding.avatar, account.getPhotoUrl().toString());
        }

        binding.name.setVisibility(View.VISIBLE);
        binding.name.setText(account.getDisplayName());

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), location -> {
                    if (location != null) {
                        // Logic to handle location object
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}