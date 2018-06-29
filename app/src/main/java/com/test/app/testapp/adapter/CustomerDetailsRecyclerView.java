package com.test.app.testapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.test.app.androidtest.R;
import com.test.app.testapp.listener.OnItemClickListener;
import com.test.app.testapp.model.Address;
import com.test.app.testapp.model.User;

import java.util.ArrayList;

public class CustomerDetailsRecyclerView extends RecyclerView.Adapter<CustomerDetailsRecyclerView.ViewHolder> {

    private ArrayList<User> mValues;
    private Context mContext;
    private OnItemClickListener mListener;

    public CustomerDetailsRecyclerView(Context context, ArrayList<User> values, OnItemClickListener itemListener) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewAddress;
        private Button btnShowMap;
        User item;

        private ViewHolder(View v) {
            super(v);
            textViewAddress = v.findViewById(R.id.textViewAddress);
            btnShowMap = v.findViewById(R.id.btnShowMap);
            btnShowMap.setOnClickListener(this);

        }

        public void setData(User item) {
            this.item = item;
            Address addressObj = item.getAddress();
            String address = "Address: " + addressObj.getStreet() + ", "
                    + addressObj.getSuite() + ", \n"
                    + addressObj.getCity() + "-"
                    + addressObj.getZipcode();

            textViewAddress.setText(address);
        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public CustomerDetailsRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_user_details, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }
}


