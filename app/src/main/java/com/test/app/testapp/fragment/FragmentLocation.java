package com.test.app.testapp.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.test.app.androidtest.R;
import com.test.app.testapp.listener.OnFragmentInteractionListener;
import com.test.app.testapp.model.GeoLocation;
import com.test.app.testapp.model.User;
import com.test.app.testapp.util.Constant;

public class FragmentLocation extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private User mUserObj;
    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;
    private TextView mTextViewAddress;

    public FragmentLocation() {
    }

    public static FragmentLocation newInstance() {
        FragmentLocation fragment = new FragmentLocation();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserObj = (User) getArguments().getSerializable("userObj");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_home:
                getFragmentManager().popBackStack(Constant.FRAGMENT_TAG_USER_DETAILS, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        mTextViewAddress = rootView.findViewById(R.id.textViewAddress);
        mListener.customizeActionBar(Constant.FRAGMENT_TAG_LOCATION, true);
        setHasOptionsMenu(true);

        initializeMap();

        return rootView;
    }

    public void initializeMap() {
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        mMapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mListener.hideProgressBar();
        mGoogleMap = googleMap;

        try {
            if (getActivity() != null)
                mGoogleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.maps_style));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        GeoLocation geoLocation = mUserObj.getAddress().getGeo();

        double latitude = Double.valueOf(geoLocation.getLat());
        double longitude = Double.valueOf(geoLocation.getLng());

        LatLng latLng = new LatLng(latitude, longitude);

        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));

            String address = mUserObj.getAddress().getStreet() + ", "
                    + mUserObj.getAddress().getSuite() + ", "
                    + mUserObj.getAddress().getCity() + "-"
                    + mUserObj.getAddress().getZipcode();
            mTextViewAddress.setText("Address: " + address);
            markerOptions.title(address);
            googleMap.addMarker(markerOptions);
        }
    }
}
