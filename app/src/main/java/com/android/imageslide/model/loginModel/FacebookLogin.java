package com.android.imageslide.model.loginModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.imageslide.Utils.Const;
import com.android.imageslide.presenter.LoginPresenter;
import com.android.imageslide.views.LoginActivity;
import com.android.imageslide.views.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class FacebookLogin {
    AccessToken accessToken;
    CallbackManager callbackManager;
    Context context;
    LoginPresenter loginPresenter;

    public FacebookLogin(Context context,LoginPresenter loginPresenter){
        this.context = context;
        this.loginPresenter = loginPresenter;
        initFacebookLogin();
    }

    private void initFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(Const.TAG," OnSucess");
                loginPresenter.handleFacebookSignIn();

            }

            @Override
            public void onCancel() {
                // App code
                Log.d(Const.TAG," onCancel");

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(Const.TAG," onError");

            }
        });
    }


    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public void startFacebookLoginWithPermission(Context context) {
        LoginManager.getInstance().logInWithReadPermissions((LoginActivity)context, Arrays.asList("public_profile"));
    }

    public void handleActivityOnResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
