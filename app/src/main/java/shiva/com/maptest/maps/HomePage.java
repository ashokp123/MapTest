/*
package shiva.com.maptest.maps;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.renderscript.Sampler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import shiva.com.maptest.R;
import shiva.com.maptest.mapNavigatn.DrawMarker;
import shiva.com.maptest.mapNavigatn.DrawRouteMaps;

import static android.content.Context.WINDOW_SERVICE;
import static android.os.Build.VERSION.SDK_INT;

*
 * Created by android_2 on 1/7/2017.



public class HomePage extends Fragment implements OnMapReadyCallback {

    CustomAutoCompleteTextView hmpg_current_loctn_edtxt,hmpg_destinatn_loctn_edtxt;
    PlacesTask placesTask;
    ParserTask parserTask;

    Context mContext;
    GPSTracker gpsTracker;
    Double latitude_value=0.0;
    Double langitude_value=0.0;
    MarkerOptions markerOptions;
    private GoogleMap mMap;

    Document doc;
    LatLng srcLatLng;
    LatLng destLatLng;
    double slat=17.3850;
    double slon=78.4867;
    double dlat=4.2105;
    double dlon=101.9758;
    String Lat, Long, type_location, received_address;

    Button click;
    static TextView show_txt,show_lng_txt;
    static TextView show_dsntn_txt,show_dsntn_txt_new;

    Double  soure_lat,soure_lon,dstntn_lat,dstntn_lon = 0.0;
    String src_lat,src_lon,destn_lat,destn_lon;
    TextView distnce_txt;
    String distnce;

    ArrayList<LatLng> markerPoints;

    SupportMapFragment mapFragment;

    ListView loction_lv;
    CustomAdapter adapter;
    String[] restaurants={"Bijan Bar & Restaurant","Songket Restaurant","The 39 Restaurant","Bijan Bar & Restaurant"};

    ImageView homepage_menu_btn,homepage_search_icon;
    int restrnts_imgs[] = {R.drawable.rst_sample_imge,R.drawable.rst_sample_imge_one,
            R.drawable.rst_sample_imge_two,R.drawable.rst_sample_imge};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.current_location,container,false);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mContext = this.getActivity();
        markerOptions = new MarkerOptions();

srcLatLng = new LatLng(gpsTracker.latitude, gpsTracker.longitude);
        destLatLng = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Long));


        gpsTracker = new GPSTracker(getActivity(),getActivity());
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        StoredObjects.width = display.getWidth();
        StoredObjects.height=display.getHeight();

        Log.i("","lattitude:--"+gpsTracker.getLatitude()+"<>longitude:--"+gpsTracker.getLongitude());

        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gpsTracker = new GPSTracker(mContext,getActivity());

            // Check if GPS enabled
            if (gpsTracker.canGetLocation()) {

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                Log.i("","gpsvalues:--"+gpsTracker.getLatitude()+"<>longitude:--"+gpsTracker.getLongitude());

langitude_value=100.0;
                      latitude_value=3.0;


                latitude_value=latitude;
                langitude_value=longitude;

                //Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude_value + "\nLong: " + langitude_value, Toast.LENGTH_LONG).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gpsTracker.showSettingsAlert();
            }
        }

        initialvalues(rootview);
        return rootview;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initialvalues(View rootView) {

        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }

        homepage_menu_btn=(ImageView)rootView.findViewById(R.id.homepage_menu_btn);
        homepage_search_icon=(ImageView)rootView.findViewById(R.id.homepage_search_icon);

        homepage_search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcalling(new RestaurantsList());
            }
        });

        homepage_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    MainActivity.drawablecount++;
                    if (MainActivity.drawablecount == 1) {
                        MainActivity.mDrawerLayout.openDrawer(MainActivity.mDrawerList);
                        //title.setText(mTitle);
                    } else {
                        MainActivity.drawablecount = 0;
                        MainActivity.mDrawerLayout.closeDrawer(MainActivity.mDrawerList);

                    }

                }catch (Exception e){

                }

            }

        });


        hmpg_current_loctn_edtxt = (CustomAutoCompleteTextView)rootView.findViewById(R.id.hmpg_current_loctn_edtxt);
        hmpg_destinatn_loctn_edtxt = (CustomAutoCompleteTextView)rootView.findViewById(R.id.hmpg_destinatn_loctn_edtxt);

        click = (Button)rootView.findViewById(R.id.click);
        distnce_txt = (TextView)rootView.findViewById(R.id.distnce_txt);


        show_txt = (TextView)rootView.findViewById(R.id.show_txt);
        show_lng_txt = (TextView)rootView.findViewById(R.id.show_lng_txt);

        show_dsntn_txt = (TextView)rootView.findViewById(R.id.show_dsntn_txt);
        show_dsntn_txt_new = (TextView)rootView.findViewById(R.id.show_dsntn_txt_new);

        hmpg_current_loctn_edtxt.setThreshold(1);
        hmpg_destinatn_loctn_edtxt.setThreshold(1);

        hmpg_current_loctn_edtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(s.length()==0){

                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();

                    latitude_value=latitude;
                    langitude_value=longitude;
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

                String address = hmpg_current_loctn_edtxt.getText().toString();
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address, getActivity(), new GeocoderHandler());

            }
        });

        hmpg_destinatn_loctn_edtxt.addTextChangedListener(new TextWatcher() {
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

                String address_lng = hmpg_destinatn_loctn_edtxt.getText().toString();
                GeocodingLocationNew locationAddress_new = new GeocodingLocationNew();
                locationAddress_new.getAddressFromLocation(address_lng, getActivity(), new GeocoderHandlerNew());

            }
        });

        //StoredObjects.LogMethod("Details", "hmpg_current_loctn_edtxt:----"+hmpg_current_loctn_edtxt);

       //MainActivity.actionbar_lay.setBackgroundColor(getResources().getColor(R.color.red));
        MainActivity.actionbar_lay.setBackgroundResource(0);
        MainActivity.actionbar_lay.setVisibility(View.GONE);
        MainActivity.menu_btn.setVisibility(View.VISIBLE);
        MainActivity.back_btn.setVisibility(View.GONE);
        MainActivity.title.setVisibility(View.INVISIBLE);
        MainActivity.action_search_icon.setVisibility(View.VISIBLE);
        MainActivity.mycart_icon.setVisibility(View.GONE);
        MainActivity.mycart_icon_one.setVisibility(View.GONE);


        loction_lv=(ListView)rootView.findViewById(R.id.loction_lv);
        StoredObjects.currnt_locatn_list_lst.clear();

        for (int j = 0; j < restaurants.length; j++) {

            DumpData data = new DumpData();

            data.restaurants = restaurants[j];
            data.restrnts_imgs = restrnts_imgs[j];

            StoredObjects.currnt_locatn_list_lst.add(data);

            //Log.i("listsize", "listsnames" + data.mycrt_serial_no_txt + "--" + data.mycrt_rm_txt);

        }

        adapter = new CustomAdapter(getActivity(), StoredObjects.currnt_locatn_list_lst, "currnt_loctn");
        loction_lv.setAdapter(adapter);


        hmpg_destinatn_loctn_edtxt.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            ProgressBarClass.hide_keyboard(getActivity());

                            //fetchData(slat,slon,dlat,dlon);

Intent intent = new Intent(getActivity(), GoogleMapNavigation.class);
                            intent.putExtra("type","areadirection");
                            intent.putExtra("address","malaysia");
                            intent.putExtra("lat","4.2105");
                            intent.putExtra("long","101.9758");
                            getActivity().startActivity(intent);


                            soure_lat = Double.parseDouble(show_txt.getText().toString());
                            soure_lon = Double.parseDouble(show_lng_txt.getText().toString());

                            dstntn_lat = Double.parseDouble(show_dsntn_txt.getText().toString());
                            dstntn_lon = Double.parseDouble(show_dsntn_txt_new.getText().toString());

                            serach_datatomap(soure_lat,soure_lon,dstntn_lat,dstntn_lon);

                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });


        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

String address = hmpg_current_loctn_edtxt.getText().toString();
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address, getActivity(), new GeocoderHandler());

                String address_lng = hmpg_destinatn_loctn_edtxt.getText().toString();
                GeocodingLocationNew locationAddress_new = new GeocodingLocationNew();
                locationAddress_new.getAddressFromLocation(address_lng, getActivity(), new GeocoderHandlerNew());


src_lat = show_txt.getText().toString();
                src_lon = show_lng_txt.getText().toString();

                destn_lat = show_dsntn_txt.getText().toString();
                destn_lon = show_dsntn_txt_new.getText().toString();



soure_lat = Double.parseDouble(sh
ow_txt.getText().toString());
                soure_lon = Double.parseDouble(show_lng_txt.getText().toString());

                dstntn_lat = Double.parseDouble(show_dsntn_txt.getText().toString());
                dstntn_lon = Double.parseDouble(show_dsntn_txt_new.getText().toString());

                serach_datatomap(soure_lat,soure_lon,dstntn_lat,dstntn_lon);


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

LatLng Malaysia = new LatLng(4.2105, 101.9758);
        mMap.addMarker(new MarkerOptions().position(Malaysia).title("Marker in Malaysia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Malaysia));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Malaysia,12));


LatLng origin = new LatLng(-7.788969, 110.338382);
        LatLng destination = new LatLng(-7.781200, 110.349709);

        DrawRouteMaps.getInstance(getActivity()).draw(origin, destination, mMap);
        DrawMarker.getInstance(getActivity()).draw(mMap, origin, R.drawable.placeholder, "Origin Location");
        DrawMarker.getInstance(getActivity()).draw(mMap, destination, R.drawable.placeholder_one, "Destination Location");

        LatLngBounds bounds = new LatLngBounds.Builder().include(origin).include(destination).build();
        Point displaySize = new Point();

        final WindowManager windowManager = (WindowManager)getActivity().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(displaySize);

        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 14));


    }


    LatLng origin,destination;
    public void serach_datatomap(final Double src_lat,final Double src_lon,final Double destn_lat,final Double destn_lon){

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.clear();
                Marker newMarker;

                origin = new LatLng(src_lat, src_lon);
                destination = new LatLng(destn_lat, destn_lon);

                DrawRouteMaps.getInstance(getActivity()).draw(origin, destination, mMap);
                DrawMarker.getInstance(getActivity()).draw(mMap, origin, R.drawable.placeholder, "Origin Location");
                DrawMarker.getInstance(getActivity()).draw(mMap, destination, R.drawable.placeholder_one, "Destination Location");

                //For finding Distance between two points(Aerial)
                CalculationByDistance(origin,destination);


                //To get the distance and Time for the Selected Places
                //Getting URL to the Google Directions API
                String url = getDirectionsUrlOne(origin, destination);
                DownloadTask downloadTask = new DownloadTask();
                //Start downloading json data from Google Directions API
                downloadTask.execute(url);


                LatLngBounds bounds = new LatLngBounds.Builder().include(origin).include(destination).build();
                Point displaySize = new Point();

                final WindowManager windowManager = (WindowManager)getActivity().getSystemService(WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getSize(displaySize);

                mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 100000, 14));


mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng destination) {

                         markerOptions= new MarkerOptions().position(
                                new LatLng(destination.latitude, destination.longitude)).title("New Marker");

                        googleMap.addMarker(markerOptions);

                        //System.out.println(destination.latitude+"---"+ destination.longitude);

                        Toast.makeText(getActivity(),destination.latitude+"---"+ destination.longitude,Toast.LENGTH_SHORT).show();

                    }
                });




                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng latLng) {

                        // Creating a marker
                        markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        // Clears the previously touched position
                        googleMap.clear();

                        // Animating to the touched position
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        googleMap.addMarker(markerOptions);
                    }
                });

            }
        });

    }


    //To create animation for the marker
 private void pulseMarker(final Bitmap markerIcon, final Marker marker, final long onePulseDuration) {
        final Handler handler = new Handler();
        final long startTime = System.currentTimeMillis();

        final Interpolator interpolator = new CycleInterpolator(1f);
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / onePulseDuration);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(scaleBitmap(markerIcon, 1f + 0.05f * t)));
                handler.postDelayed(this, 16);
            }
        });
    }
    public Bitmap scaleBitmap(Bitmap bitmap, float scaleFactor) {
        final int sizeX = Math.round(bitmap.getWidth() * scaleFactor);
        final int sizeY = Math.round(bitmap.getHeight() * scaleFactor);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);
        return bitmapResized;
    }



    public double CalculationByDistance(LatLng StartP, LatLng EndP) {

        int Radius = 6371;// radius of earth in Km

        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;

        int meterInDec = Integer.valueOf(newFormat.format(meter));

        //distnce_txt.setText(kmInDec + "Kms");

        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;

    }


    public double DistancebyDriving(Double src_lat, Double src_lon, Double destn_lat, Double destn_lon){

        int  R = 6371; // km
        Double dLat = toRad(destn_lat-src_lat);
        Double dLon = toRad(destn_lon-src_lon);
        Double lat1 = toRad(src_lat);
        Double lat2 = toRad(destn_lat);

        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double d = R * c;

        DecimalFormat newFormat = new DecimalFormat("####");
        int meterInDec = Integer.valueOf(newFormat.format(d));

        distnce_txt.setText(meterInDec + "Kms");

        return d;

    }

    double toRad(Double d)
    {
        return d * Math.PI / 180;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        try {


            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //	mGoogleMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);


            mMap.animateCamera(CameraUpdateFactory.newLatLng(srcLatLng));


            //	mGoogleMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(srcLatLng,12));


            if (type_location.equalsIgnoreCase("fulladdress")) {
                mMap.addMarker(new MarkerOptions().position(srcLatLng).title(received_address));
            }
            if (type_location.equalsIgnoreCase("areadirection")) {
                mMap.addMarker(new MarkerOptions().position(srcLatLng).title("source"));
                mMap.addMarker(new MarkerOptions().position(destLatLng).title("destination"));

                fetchData(gpsTracker.latitude,gpsTracker.longitude,Double.parseDouble(Lat),Double.parseDouble(Long));

                direction(Double.parseDouble(Lat), Double.parseDouble(Long));
            }

        }catch (Exception e){

        }


        //Line between two places with in the same map.


        LatLng origin = new LatLng(-7.788969, 110.338382);
        LatLng destination = new LatLng(-7.781200, 110.349709);
        DrawRouteMaps.getInstance(getActivity())
                .draw(origin, destination, mMap);
        DrawMarker.getInstance(getActivity()).draw(mMap, origin, R.drawable.placeholder, "Origin Location");
        DrawMarker.getInstance(getActivity()).draw(mMap, destination, R.drawable.placeholder_one, "Destination Location");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();

        Point displaySize = new Point();

        WindowManager windowManager = (WindowManager)getActivity().getSystemService(WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));



    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }




* A method to download json data from url

    private String downloadUrl(String strUrl) throws IOException {
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
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        //StoredObjects.LogMethod("Details", "data:----"+data);
        return data;

    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key="+"AIzaSyAhZQInvzvpf__o3zK67tbJ9j7eZ1d7kys";

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

           // https://maps.googleapis.com/maps/api/place/autocomplete/json?input=Amoeba&types=establishment&location=37.76999,-122.44696&radius=500&key=AIzaSyAhZQInvzvpf__o3zK67tbJ9j7eZ1d7kys

            StoredObjects.LogMethod("Details", "url:----"+url);

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
* A class to parse the Google Places in JSON format

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            List<HashMap<String, String>> routes = null;

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
            hmpg_current_loctn_edtxt.setAdapter(adapter);
            hmpg_destinatn_loctn_edtxt.setAdapter(adapter);

        }

    }





    private void fetchData(double slat,double slon,double dlat,double dlon) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps/api/directions/xml?origin=");
        urlString.append(slat);
        urlString.append(",");
        urlString.append(slon);
        urlString.append("&destination=");//to
        urlString.append(dlat);
        urlString.append(",");
        urlString.append(dlon);
        urlString.append("&sensor=true&mode=driving");
        Log.d("url","::"+urlString.toString());
        HttpURLConnection urlConnection= null;
        URL url = null;
        try
        {
            url = new URL(urlString.toString());
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = (Document) db.parse(urlConnection.getInputStream());//Util.XMLfromString(response);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void direction(double dlat,double dlon) {

        if(doc!=null){
            NodeList _nodelist = doc.getElementsByTagName("status");
            Node node1 = _nodelist.item(0);
            String _status1 = node1.getChildNodes().item(0).getNodeValue();
            if(_status1.equalsIgnoreCase("OK")){
                NodeList _nodelist_path = doc.getElementsByTagName("overview_polyline");
                Node node_path = _nodelist_path.item(0);
                Element _status_path = (Element)node_path;
                NodeList _nodelist_destination_path = _status_path.getElementsByTagName("points");

                Node _nodelist_dest = _nodelist_destination_path.item(0);
                String _path = _nodelist_dest.getChildNodes().item(0).getNodeValue();
                List<LatLng> directionPoint = decodePoly(_path);

                PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.rgb(40, 33,238));
                // PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.rgb(238, 156, 33));
                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                markerOptions = new MarkerOptions();
                // Adding route on the map
                mMap.addPolyline(rectLine);
                markerOptions.position(new LatLng(dlat, dlon));
                markerOptions.draggable(true);
                mMap.addMarker(markerOptions);
            }else{
                Toast.makeText(getActivity(), "unable to draw path", 0).show();
            }

        }else{
            Toast.makeText(getActivity(), "unable to draw path", 0).show();

        }
    }

    public static  ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }

        return poly;

    }



    public void fragmentcalling(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
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
           // show_txt.setText(locationAddress);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    show_txt.setText(lattitude_val);
                    show_lng_txt.setText(longitude_val);
                }
            });
        }
    }


    static String lattitude_val;
    static  String longitude_val;
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
                            final Address address = (Address) addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            sb.append(address.getLatitude()).append("\n");
                            sb.append(address.getLongitude()).append("\n");

                            lattitude_val = address.getLatitude()+"";
                            longitude_val = address.getLongitude()+"";

                            result = sb.toString();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Unable to connect to Geocoder", e);
                    }
                    finally {
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


    static String dstntn_lattitude_val;
    static  String dstntn_longitude_val;
    private class GeocoderHandlerNew extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            //show_dsntn_txt.setText(locationAddress);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    show_dsntn_txt.setText(dstntn_lattitude_val);
                    show_dsntn_txt_new.setText(dstntn_longitude_val);
                }
            });

        }
    }



    public static class GeocodingLocationNew {

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



    private String getDirectionsUrlOne(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrlOne(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTaskOne parserTask = new ParserTaskOne();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


* A method to download json data from url

    private String downloadUrlOne(String strUrl) throws IOException{
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

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

* A class to parse the Google Places in JSON format

    private class ParserTaskOne extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if(result.size()<1){
                Toast.makeText(getActivity(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            distnce_txt.setText("Distance:"+distance + ", Duration:"+duration);

            //distnce_txt.setText(kmInDec + "Kms");

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }







}
*/
