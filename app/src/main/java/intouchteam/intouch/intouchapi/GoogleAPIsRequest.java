package intouchteam.intouch.intouchapi;

import android.content.Context;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.LatLng;

import intouchteam.intouch.R;

public class GoogleAPIsRequest {

    public static String getAddress(com.google.android.gms.maps.model.LatLng coords, Context context) {
        GeoApiContext geoContext = new GeoApiContext().setApiKey(context.getString(R.string.maps_api_key));
        String result = "";
        GeocodingApiRequest req = GeocodingApi.newRequest(geoContext).latlng(new LatLng(coords.latitude, coords.longitude));
        try {
            result = req.await()[0].formattedAddress;
        } catch (Exception e) {
        }
        return result;
    }

    public static com.google.android.gms.maps.model.LatLng getCoords(String address, final Context context, com.google.android.gms.maps.model.LatLng onDefault) {
        GeoApiContext geoContext = new GeoApiContext().setApiKey(context.getString(R.string.maps_api_key));
        com.google.android.gms.maps.model.LatLng result = new com.google.android.gms.maps.model.LatLng(onDefault.latitude, onDefault.longitude);
        GeocodingApiRequest req = GeocodingApi.newRequest(geoContext).address(address);
        try {
            LatLng coords = req.await()[0].geometry.location;
            result = new com.google.android.gms.maps.model.LatLng(coords.lat, coords.lng);
        } catch (Exception e) {
        }
        return result;
    }
}
