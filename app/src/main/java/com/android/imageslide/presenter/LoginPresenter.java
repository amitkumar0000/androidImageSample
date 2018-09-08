package com.android.imageslide.presenter;

import android.content.Context;
import android.content.Intent;

import com.android.imageslide.contract.ILoginPresenter;
import com.android.imageslide.contract.ILoginViewInterface;
import com.android.imageslide.interfaces.GoogleCallback;
import com.android.imageslide.model.loginModel.FacebookLogin;
import com.android.imageslide.model.loginModel.GoogleLogin;
import com.android.imageslide.views.MainActivity;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginPresenter implements ILoginPresenter,GoogleCallback {

    ILoginViewInterface iLoginViewInterface;
    Context context;

    GoogleLogin googleLogin;
    FacebookLogin facebookLogin;


    public LoginPresenter(ILoginViewInterface iLoginViewInterface, Context context) {
        this.iLoginViewInterface = iLoginViewInterface;
        this.context = context;
        googleLogin = new GoogleLogin(context);
        facebookLogin = new FacebookLogin(context,this);
    }


    @Override
    public void googleSignIn() {
        GoogleSignInAccount account = googleLogin.getLastSignInAccount(context);
        if (account != null) {
            iLoginViewInterface.startMainActivity();
        } else {
            iLoginViewInterface.startGoogleSignActivity(googleLogin.getSignInIntent());
        }
    }

    @Override
    public void facebookSignIn() {
        AccessToken accessToken = facebookLogin.getAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            iLoginViewInterface.startMainActivity();
        }else {
            facebookLogin.startFacebookLoginWithPermission(context);
        }
    }

    @Override
    public void handleFacebookSignIn() {
        iLoginViewInterface.startMainActivity();
    }

    @Override
    public void handleFaceSignInOnActivityResult(int requestCode, int resultCode, Intent data) {
        facebookLogin.handleActivityOnResult(requestCode,resultCode,data);

    }

    @Override
    public void handleGoogleSignOnActivityResult(Intent data) {
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        googleLogin.handleActivityOnResult(data,this);
    }

    @Override
    public void handleActivityResultCallback() {
        iLoginViewInterface.startMainActivity();
    }

}
