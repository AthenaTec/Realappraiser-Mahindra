package com.realappraiser.gharvalue.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.model.places.InfoWindowData;

import java.util.Objects;

public class CustomInfoWindowGoogleMap  implements GoogleMap.InfoWindowAdapter{

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_info_window, null);

        TextView name_tv = view.findViewById(R.id.tvTitle);
        TextView details_tv = view.findViewById(R.id.tvSnippet);
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        name_tv.setText(Objects.requireNonNull(infoWindowData).getTitle());
        details_tv.setText(infoWindowData.getAddress());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
