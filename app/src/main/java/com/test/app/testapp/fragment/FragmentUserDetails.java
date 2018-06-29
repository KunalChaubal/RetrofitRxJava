package com.test.app.testapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.app.androidtest.R;
import com.test.app.testapp.adapter.CustomerDetailsRecyclerView;
import com.test.app.testapp.listener.OnFragmentInteractionListener;
import com.test.app.testapp.listener.OnItemClickListener;
import com.test.app.testapp.model.User;
import com.test.app.testapp.util.Constant;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUserDetails extends Fragment implements OnItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<User> mList;
    private CustomerDetailsRecyclerView mAdapter;
    private OnFragmentInteractionListener mListener;

    public FragmentUserDetails() {
    }

    public static FragmentUserDetails newInstance() {
        return new FragmentUserDetails();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mList = (ArrayList<User>) getArguments().getSerializable("userList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
        mListener.customizeActionBar(Constant.FRAGMENT_TAG_USER_DETAILS, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        mAdapter = new CustomerDetailsRecyclerView(getActivity(), mList, this);

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return rootView;
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("userObj", item);
        mListener.changeFragment(Constant.FRAGMENT_TAG_LOCATION, bundle);
        mListener.showProgressBar();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
