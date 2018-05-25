package com.example.cheshta.wendornavigationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.cheshta.wendornavigationproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mView;
    private Context mContext;

    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
        mView = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){

        String title = marker.getTitle();
        TextView tvMapTitle = view.findViewById(R.id.tvMapTitle);

        if(!title.equals("")){
            tvMapTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvMapSnippet = view.findViewById(R.id.tvMapSnippet);

        if(!snippet.equals("")){
            tvMapSnippet.setText(snippet);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mView);
        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mView);
        return mView;
    }
}
