package com.example.yoyo.databaseapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teo on 6/1/2016.
 */
public class MapsActivity  extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private ArrayList<Notes> array_list_notes;
   // private String[] location_details;
   // private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

       // location_details= new String[3];
        array_list_notes = new ArrayList<>();

        Intent i = getIntent();
//        Bundle extras = i.getExtras();
//        if (extras != null)
//        {
//            Toast.makeText(MapsActivity.this, extras.getString(Display_Info.Show_Single_Location)+" I am right", Toast.LENGTH_SHORT).show();
////
//            flag = true;//only one pin_point
//           String transporter_info = extras.getString(Display_Info.Show_Single_Location);
//           for(int j =0;j<transporter_info.length();j++)
//           {
//               location_details  =  transporter_info.split(",");
//           }
       // }
  //  else
       // {
           // flag = false;//all points
            array_list_notes = i.getParcelableArrayListExtra("notes");
       // }
       // getCoordinatesFromMemo(array_list_notes);
//        Bundle b = this.getIntent().getExtras();
//        if(b!=null)
//        {
//
//        array_list_notes = b.getParcelableArrayList("notes");
       // Toast.makeText(MapsActivity.this, "list size "+array_list_notes.size(), Toast.LENGTH_SHORT).show();
//           // Toast.makeText(MapsActivity.this, "new toast", Toast.LENGTH_SHORT).show();
//        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        Toast.makeText(MapsActivity.this, "list size from onMapReady "+array_list_notes.size(), Toast.LENGTH_SHORT).show();
       // if(flag==false) {
            getCoordinatesFromMemo(array_list_notes);
       // }
       // if(flag==true)
//       // {
//            double latitude = Double.parseDouble(location_details[0]);
//            double longitude = Double.parseDouble(location_details[1]);
//            String topic = location_details[2];
//
//            LatLng sydney = new LatLng(latitude, longitude);
//            mMap.addMarker(new MarkerOptions().position(sydney).title(topic));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //}
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
            return;
        }
        */

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);

        } else
        {
            //Toast.makeText(MapsActivity.this, "wewe", Toast.LENGTH_SHORT).show();
        }

      //   Add a marker in Copenhagen and move the camera
//        LatLng sydney = new LatLng(55.6761, 12.5683);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Copenhagen"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Toast.makeText(MapsActivity.this, "I pressed finish ", Toast.LENGTH_SHORT).show();
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public void finish()
    {
        setResult(MainActivity.CHECK_LOCATION_INTENT);
        super.finish();
    }

    public void getCoordinatesFromMemo(List<Notes> notes)
    {
        notes = array_list_notes;
        for(int i = 0; i<notes.size();i++)
        {
            Notes note = notes.get(i);
            String lat = note.getLat();
            String lng = note.getLng();
            Double lat_double = Double.parseDouble(lat);
            Double lng_double = Double.parseDouble(lng);
            LatLng pinPoint = new LatLng(lat_double,lng_double);
            mMap.addMarker(new MarkerOptions().position(pinPoint).title(note.getTopic()));
        }



       // mMap.moveCamera(CameraUpdateFactory.newLatLng(pinPoint));

    }

}
