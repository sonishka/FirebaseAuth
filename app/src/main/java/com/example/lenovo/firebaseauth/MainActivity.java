package com.example.lenovo.firebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_LOGIN =1000;

    private String TAG =" error";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ABC ");
        super.onCreate(savedInstanceState);

        Log.d(TAG, "DEF ");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.d(TAG, "IJK ");
        if(auth.getCurrentUser() != null)
        {
            //if already login
            if(!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty())
            {
                startActivity(new Intent(this,SignIn.class)
                    .putExtra("phone",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty())
                );
                finish();

            }
            else
            {Log.d(TAG, "LMN ");
                startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder().setAvailableProviders(
                        Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                        )).build(),REQUEST_LOGIN);

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            //Successfully signed in
            if (resultCode == RESULT_OK)
            {
                if(!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty())
                {
                    startActivity(new Intent(this,SignIn.class).putExtra("phone",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()));
                    finish();
                    return;
                }
                else // Sign in failde
                {
                    if (response == null)
                    {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.getErrorCode() == ErrorCodes.NO_NETWORK)
                    {
                        Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR)
                    {
                        Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                Toast.makeText(this, "Unknown Sign In Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
