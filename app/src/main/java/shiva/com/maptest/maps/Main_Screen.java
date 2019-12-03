package shiva.com.maptest.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import shiva.com.maptest.autoComplete.PlaceJSONParser;
import shiva.com.maptest.constants.Constants;
import shiva.com.maptest.R;
import shiva.com.maptest.directionhelpers.FetchURL;
import shiva.com.maptest.directionhelpers.TaskLoadedCallback;
import shiva.com.maptest.mapNavigatn.DrawMarker;
import shiva.com.maptest.mapNavigatn.DrawRouteMaps;

import static android.content.ContentValues.TAG;
import static android.content.Context.WINDOW_SERVICE;

public class Main_Screen extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mGoogleMap;
    MapFragment mapFragment;

    private MarkerOptions markerOptions;
    private MarkerOptions place1, place2, destntn;
    private Polyline currentPolyline;
    Button getPickLocation,btnShowMoreMarkers,btnShowDrirections,btnGeoFencing;

    Document doc;
    PlacesTask placesTask;
    ParserTask parserTask;
    AutoCompleteTextView dstntn_edtxt;
    TextView btn_go;

    //GPS
    Context mContext;
    GPSTracker gpsTracker;
    public static Double latitude_value=0.0;
    public static Double langitude_value=0.0;
    public static String cur_lat="";
    public static String cur_lng="";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initilizeMap(View rooView) {

        if (mGoogleMap == null) {

            mapFragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.mapview_navigation);
            mapFragment.getMapAsync(this);
            if (mGoogleMap == null) {
                Toast.makeText(getActivity(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_screen, container, false);

        init(rootView);
        initilizeMap(rootView);
        return  rootView;

    }

    public void init(View v){

        mContext = getActivity();
        gpsTracker = new GPSTracker(getActivity(),getActivity());

        btnShowMoreMarkers = (Button)v.findViewById(R.id.btnShowMoreMarkers);
        btnShowDrirections = (Button)v.findViewById(R.id.btnShowDrirections);
        btnGeoFencing = (Button)v.findViewById(R.id.btnGeoFencing);
        getPickLocation = (Button)v.findViewById(R.id.getPickLocation);

        dstntn_edtxt = (AutoCompleteTextView)v.findViewById(R.id.dstntn_edtxt);
        btn_go = (TextView)v.findViewById(R.id.btn_go);

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gpsTracker = new GPSTracker(mContext,getActivity());

            // Check if GPS enabled
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                Log.i("","gpsvalues:--"+gpsTracker.getLatitude()+"<>longitude:--"+gpsTracker.getLongitude());

                try {
                    latitude_value = latitude;
                    langitude_value = longitude;
                }catch (Exception e){
                    latitude_value = 0.0;
                    langitude_value = 0.0;
                }

                cur_lat = String.valueOf(latitude_value);
                cur_lng = String.valueOf(langitude_value);

                Constants.LogMethod("latitude_value","latitude_value_is:----"+latitude_value);
                Constants.LogMethod("langitude_value","langitude_value_is:----"+langitude_value);

            } else {
                gpsTracker.showSettingsAlert();
            }

        }

        place1 = new MarkerOptions().position(new LatLng(latitude_value, langitude_value)).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        place2 = new MarkerOptions().position(new LatLng(17.500010, 78.401527)).title("Destination Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        btnShowMoreMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreMarkers();
            }
        });

        btnShowDrirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FetchURL(Main_Screen.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                mGoogleMap.addMarker(place1);
                mGoogleMap.addMarker(place2);

            }
        });

        btnGeoFencing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showFencing();
            }
        });

        getPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickedLoctn();
            }
        });

        dstntn_edtxt.setThreshold(1);
        dstntn_edtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                String address = dstntn_edtxt.getText().toString();
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address, getActivity(), new GeocoderHandler());
            }
        });

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double d_lat = Double.parseDouble(dstntn_lattitude_val);
                Double d_lng = Double.parseDouble(dstntn_longitude_val);

                place1 = new MarkerOptions().position(new LatLng(latitude_value, langitude_value)).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                destntn = new MarkerOptions().position(new LatLng(d_lat, d_lng)).title("Destination Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                new FetchURL(Main_Screen.this).execute(getUrl(place1.getPosition(), destntn.getPosition(), "driving"), "driving");
                mGoogleMap.addMarker(place1);
                mGoogleMap.addMarker(destntn);

                dstntn_edtxt.setText("");
            }
        });

    }

    /*double[] lat_ = {77.1,72.1,76.3};
    double[] lon_ = {17.1,12.1,16.3};*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        //To show current location
        showCurrentLocation();

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.Google_API_KEY);
        return url;
    }

    public void showCurrentLocation(){

        MarkerOptions options=new MarkerOptions();
        LatLng Destination = new LatLng(latitude_value, langitude_value);
        options.position(Destination);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Destination,14));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mGoogleMap.addMarker(options);

    }

    public void showMoreMarkers(){

        try {
            test_method();
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

            // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(srcLatLng));

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                             int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                mGoogleMap.setMyLocationEnabled(true);
                return;
            }

            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            mGoogleMap.setTrafficEnabled(true);
            // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(srcLatLng,12));

        }catch (Exception e){

        }
    }


    //String[] lati_tude = {"17.385", "17.395"};
    //String[] long_tude = {"78.547", "78.557"};

    Double[] lati_tude = {17.385, 17.395};
    Double[] long_tude = {78.547, 78.557};

    public void test_method() {

        mGoogleMap.clear();
        LatLng latLng = null;

        for (int i = 0; i < lati_tude.length; i++) {

            MarkerOptions markerOptions = new MarkerOptions();

            //double lat = Double.parseDouble(lati_tude[i]);
            //double lng = Double.parseDouble(long_tude[i]);

            double lat = lati_tude[i];
            double lng = long_tude[i];

            latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title("text..");
            mGoogleMap.addMarker(markerOptions);

        }

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));

    }

    public void showPickedLoctn(){

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    public void showFencing(){

        MarkerOptions options=new MarkerOptions();
        LatLng latlng = new LatLng(latitude_value, langitude_value);
        options.position(latlng);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,14));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mGoogleMap.addMarker(options);

        CircleOptions circleOptions = new CircleOptions()
                .center( new LatLng(latitude_value, langitude_value) )
                .radius( 500.0f )
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);
        mGoogleMap.addCircle(circleOptions);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
    }


    /** A method to download json data from url */
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyAhZQInvzvpf__o3zK67tbJ9j7eZ1d7kys";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            dstntn_edtxt.setAdapter(adapter);
        }
    }







    static String dstntn_lattitude_val;
    static  String dstntn_longitude_val;

    public static class GeocodingLocation {

        private static final String TAG = "GeocodingLocation";

        public static void getAddressFromLocation(final String locationAddress,
                                                  final Context context, final Handler handler) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    String result = null;
                    try {
                        List
                            addressList = geocoder.getFromLocationName(locationAddress, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = (Address) addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            sb.append(address.getLatitude()).append("\n");
                            sb.append(address.getLongitude()).append("\n");

                            dstntn_lattitude_val = address.getLatitude()+"";
                            dstntn_longitude_val = address.getLongitude()+"";

                            result = sb.toString();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Unable to connect to Geocoder", e);
                    } finally {
                        Message message = Message.obtain();
                        message.setTarget(handler);
                        if (result != null) {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            //result = "Address: " + locationAddress + "\n\nLatitude and Longitude :\n" + result;
                            result = "Latitude and Longitude :" + result;
                            bundle.putString("address", result);
                            message.setData(bundle);
                        } else {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            result = "Address: " + locationAddress + "\n Unable to get Latitude and Longitude for this address location.";
                            bundle.putString("address", result);
                            message.setData(bundle);
                        }
                        message.sendToTarget();
                    }
                }
            };
            thread.start();
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();

                    Log.i("fsf","bundle_data_toget"+ bundle.toString());

                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            //dstntn_edtxt.setText(locationAddress);

            Constants.LogMethod("","Addresss:====="+locationAddress+"");

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    //show_txt.setText(lattitude_val);
                    //show_lng_txt.setText(longitude_val);
                }
            });
        }
    }

    public static void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }


}

