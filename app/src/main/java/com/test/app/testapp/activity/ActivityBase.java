package com.test.app.testapp.activity;

import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.app.androidtest.R;
import com.test.app.testapp.fragment.FragmentLocation;
import com.test.app.testapp.fragment.FragmentUserDetails;
import com.test.app.testapp.fragment.FragmentUserList;
import com.test.app.testapp.listener.OnFragmentInteractionListener;
import com.test.app.testapp.util.Constant;

public class ActivityBase extends AppCompatActivity implements OnFragmentInteractionListener {

    Fragment mUserListFragment, mUserDetailsFragment, mLocationFragment;
    ActionBar mActionBar;
    ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setupView();

        if (savedInstanceState == null) {
            if (mUserListFragment == null)
                mUserListFragment = FragmentUserList.newInstance();
            addFragment(R.id.fragment_container, mUserListFragment, Constant.FRAGMENT_TAG_USER_LIST);
        }
    }

    private void setupView() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActionBar = getSupportActionBar();
        mProgressbar = findViewById(R.id.networkProgressBar);
    }

    private void addFragment(int containerViewId, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .commit();
    }

    private void replaceFragment(int containerViewId,
                                 Fragment fragment,
                                 String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(fragmentTag)
                .replace(containerViewId, fragment, fragmentTag)
                .commit();
    }

    @Override
    public void customizeActionBar(String title, boolean isBackButtonEnabled) {
        TextView textViewTitle = new TextView(getApplicationContext());

        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        textViewTitle.setLayoutParams(lp);
        textViewTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textViewTitle.setText(title);

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(textViewTitle);

        if (isBackButtonEnabled) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            Toolbar parent = (Toolbar) textViewTitle.getParent();
            parent.setContentInsetStartWithNavigation(0);
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(false);
        }


    }

    @Override
    public void showProgressBar() {
        if (mProgressbar != null) {
            mProgressbar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mProgressbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            } else {
                mProgressbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    @Override
    public void hideProgressBar() {
        if (mProgressbar != null)
            mProgressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void changeFragment(String fragmentTag, Bundle bundle) {
        switch (fragmentTag) {
            case Constant.FRAGMENT_TAG_USER_DETAILS:
                if (mUserDetailsFragment == null)
                    mUserDetailsFragment = FragmentUserDetails.newInstance();
                mUserDetailsFragment.setArguments(bundle);
                replaceFragment(R.id.fragment_container, mUserDetailsFragment, Constant.FRAGMENT_TAG_USER_DETAILS);
                break;

            case Constant.FRAGMENT_TAG_LOCATION:
                if (mLocationFragment == null)
                    mLocationFragment = FragmentLocation.newInstance();
                mLocationFragment.setArguments(bundle);
                replaceFragment(R.id.fragment_container, mLocationFragment, Constant.FRAGMENT_TAG_LOCATION);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("currentFragment", "");
    }
}
