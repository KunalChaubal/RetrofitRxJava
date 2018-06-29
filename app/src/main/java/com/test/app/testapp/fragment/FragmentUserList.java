package com.test.app.testapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.test.app.androidtest.R;
import com.test.app.testapp.adapter.UserGridRecyclerViewAdapter;
import com.test.app.testapp.listener.OnFragmentInteractionListener;
import com.test.app.testapp.listener.OnItemClickListener;
import com.test.app.testapp.listener.RequestInterface;
import com.test.app.testapp.model.User;
import com.test.app.testapp.util.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentUserList extends Fragment implements OnItemClickListener {

    private RecyclerView mRecyclerView;
    private Button mBtnShowDetails;

    private CompositeDisposable mCompositeDisposable;
    private ArrayList<User> mUserArrayList = new ArrayList<>();
    private ArrayList<User> mSelectedItemList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private UserGridRecyclerViewAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    public FragmentUserList() {
    }

    public static FragmentUserList newInstance() {
        FragmentUserList fragment = new FragmentUserList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);

        mListener.customizeActionBar(Constant.FRAGMENT_TAG_USER_LIST, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mBtnShowDetails = rootView.findViewById(R.id.btnShowDetails);

        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new UserGridRecyclerViewAdapter(getActivity(), mUserArrayList, mSelectedItemList, this);

        mRecyclerView.setAdapter(mAdapter);

        mBtnShowDetails.setOnClickListener(v -> {
            if (mSelectedItemList.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userList", mSelectedItemList);
                mListener.changeFragment(Constant.FRAGMENT_TAG_USER_DETAILS, bundle);
            } else {
                Toast.makeText(getActivity(), "Please select atleast one item", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mUserArrayList.size() == 0) {
            loadJSON();
        } else {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void loadJSON() {
        mListener.showProgressBar();
        RequestInterface requestInterface = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface.class);

        mCompositeDisposable.add(requestInterface.register()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(List<User> androidList) {

        mListener.hideProgressBar();

        mUserArrayList.clear();
        mSelectedItemList.clear();
        mUserArrayList.addAll(androidList);

        mAdapter.notifyDataSetChanged();
    }

    private void handleError(Throwable error) {
        mListener.hideProgressBar();
        if (error instanceof IOException) {
            showAlert("Please check your internet connection and try again.");
        }
    }

    public void showAlert(final String alertMsg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(alertMsg)
                .setCancelable(false)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        loadJSON();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(User item) {
        if (mSelectedItemList.contains(item)) {
            mSelectedItemList.remove(item);
        } else {
            mSelectedItemList.add(item);
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
