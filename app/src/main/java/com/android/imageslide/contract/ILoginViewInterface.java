package com.android.imageslide.contract;

import android.content.Intent;

public interface ILoginViewInterface {

    void startMainActivity();

    void startGoogleSignActivity(Intent intent);
}
