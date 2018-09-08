package com.android.imageslide.contract;

import android.content.Intent;

public interface ILoginPresenter {
    void googleSignIn();
    void facebookSignIn();

    void handleFacebookSignIn();

    void handleFaceSignInOnActivityResult(int requestCode, int resultCode, Intent data);

    void handleGoogleSignOnActivityResult(Intent data);
}
