package com.test.app.testapp.listener;

import android.os.Bundle;

public interface OnFragmentInteractionListener {

    void customizeActionBar(String title, boolean isBackButtonEnabled);
    void changeFragment(String fragmentTag, Bundle bundle);
    void showProgressBar();
    void hideProgressBar();
}
