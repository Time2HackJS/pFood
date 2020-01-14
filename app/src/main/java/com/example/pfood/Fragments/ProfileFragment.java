package com.example.pfood.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pfood.R;
import com.example.pfood.utils.FirebaseUtils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    public static final String KEY_IS_OPEN_FIREBASE = "key_is_open_firebase";

    private static final int LOGIN_REQUEST_CODE = 256;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks loginCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: Complete " + phoneAuthCredential.toString());
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d(TAG, "onVerificationFailed: failed " + e.toString());
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().getBoolean(KEY_IS_OPEN_FIREBASE)) {
            goToLoginScreen();
        }
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button loginButton = view.findViewById(R.id.profile_login_button);
        EditText profileNameEt = view.findViewById(R.id.profile_name_et);
        EditText profileAddress = view.findViewById(R.id.profile_addres_et);

        if (FirebaseUtils.isUserLoggedIn()) {
            loginButton.setText(R.string.save);
            profileAddress.setEnabled(true);
            profileNameEt.setEnabled(true);
        } else {
            loginButton.setText(R.string.login);
            profileAddress.setEnabled(false);
            profileNameEt.setEnabled(false);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FirebaseUtils.isUserLoggedIn()) {
                    goToLoginScreen();
                } else {
                    //TODO: сохранение

                    Snackbar.make(view, R.string.alread_logged_in, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(getView(), getContext().getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    return;
                }

                Snackbar.make(getView(), "Error", Snackbar.LENGTH_LONG).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private void goToLoginScreen() {
        List<String> whitelistedCountries = new ArrayList<>();
        whitelistedCountries.add("ru");

        AuthUI.IdpConfig phoneLoginConfig = new AuthUI.IdpConfig.PhoneBuilder()
                .setWhitelistedCountries(whitelistedCountries)
                .build();

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(
                                phoneLoginConfig))
                        .build(),
                LOGIN_REQUEST_CODE);
    }
}
