package com.android.imageslide.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.imageslide.R;
import com.android.imageslide.Utils.Const;
import com.android.imageslide.contract.ILoginPresenter;
import com.android.imageslide.contract.ILoginViewInterface;
import com.android.imageslide.presenter.LoginPresenter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,ILoginViewInterface{


    int RC_SIGN_IN = 200;
    int FB_SIGN_IN = 64206;
    LoginButton loginButton;
    private static final String EMAIL = "email";

    ILoginPresenter iloginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iloginPresenter = new LoginPresenter(this,this);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(this);

        loginButton = findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setOnClickListener(this);



    }



    public void onClick(View view){
        switch (view.getId()) {
            case R.id.sign_in_button: {
                iloginPresenter.googleSignIn();
                break;
            }
            case R.id.facebook_login_button:{
                iloginPresenter.facebookSignIn();
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
           iloginPresenter.handleGoogleSignOnActivityResult(data);
        }else if(requestCode  == FB_SIGN_IN){
            iloginPresenter.handleFaceSignInOnActivityResult(requestCode,resultCode,data);
        }
    }



    @Override
    public void startMainActivity() {
        Toast.makeText(this,"You have successfully Logged in...",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public void startGoogleSignActivity(Intent signInIntent) {
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
