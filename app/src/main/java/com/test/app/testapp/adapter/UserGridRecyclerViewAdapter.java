package com.test.app.testapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.app.androidtest.R;
import com.test.app.testapp.listener.OnItemClickListener;
import com.test.app.testapp.model.User;

import java.util.ArrayList;

public class UserGridRecyclerViewAdapter extends RecyclerView.Adapter<UserGridRecyclerViewAdapter.ViewHolder> {

    private ArrayList<User> mUserArrayList;
    private ArrayList<User> mSelectedList;
    private Context mContext;
    private OnItemClickListener mListener;

    public UserGridRecyclerViewAdapter(Context context, ArrayList<User> values,ArrayList<User> selectedValues, OnItemClickListener itemListener) {

        mUserArrayList = values;
        mSelectedList = selectedValues;
        mContext = context;
        mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewName;
        public TextView textViewEmail;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        User item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textViewName = (TextView) v.findViewById(R.id.textViewNameValue);
            textViewEmail = (TextView) v.findViewById(R.id.textViewEmailValue);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);

        }

        public void setData(User item) {
            this.item = item;

            textViewName.setText(item.getName());
            textViewEmail.setText(item.getEmail());

            if (mSelectedList.size() != 0) {
                if (mSelectedList.contains(item)) {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_green_tick));
                } else {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_unselect));
                }
            }
        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                if (mSelectedList.contains(item)){
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_unselect));
                } else {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_green_tick));
                }
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public UserGridRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_user_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mUserArrayList.get(position));
    }

    @Override
    public int getItemCount() {

        return mUserArrayList.size();
    }
}
