package com.android.imageslide.model.loginModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.imageslide.Utils.Const;
import com.android.imageslide.presenter.LoginPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleLogin {
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    Context context;
    Intent intent;

    public GoogleLogin(Context context){
        this.context = context;
        initGoogleObject();
    }

    private void initGoogleObject() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public GoogleSignInAccount getLastSignInAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public Intent getSignInIntent(){
        return mGoogleSignInClient!=null?mGoogleSignInClient.getSignInIntent():null;
    }

    public void handleActivityOnResult(Intent data, LoginPresenter loginPresenter) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleSignInResult(task,loginPresenter);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask, LoginPresenter loginPresenter) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.d(Const.TAG,"Account name::" +account.getDisplayName()
                    +" photoUrl::" + account.getPhotoUrl());
            loginPresenter.handleActivityResultCallback();
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(Const.TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
