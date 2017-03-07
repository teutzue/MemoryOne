package com.example.yoyo.databaseapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import java.util.ArrayList;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationClient;//depricated

public class MainActivity extends AppCompatActivity  {


    //LOcation staff
    GoogleApiClient mGoogleApiClient;
    ListView theMainList;
    Location mLastLocation;
    TextView text;
    // GridView gridview;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private TextView hiddenText;
    private ListView obj;
    private ListCustomAdapter adapter;
    public static final int CHECK_RIGHT_INTENT = 1;  // The request code
    public static final int CHECK_LOCATION_INTENT = 2;
    //   private TextView subject_display;
    private DBHelper mydb;
    ImageView view;
    private ArrayList<Notes> array_list;

    //   private static final String TAG = "THE OBJECT IS ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newview);


        mydb = new DBHelper(this);
        text  = (TextView)findViewById(R.id.text);
        theMainList =(ListView)findViewById(R.id.mainListView);

        obj = (ListView) findViewById(R.id.mainListView);
        array_list = mydb.getAllNotes();

        adapter = new ListCustomAdapter(this, array_list, mydb);

        obj.setAdapter(adapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_Search = position + 1;//because of the way they are stored into the database
                hiddenText = (TextView) theMainList.getChildAt(position).findViewById(R.id.hiddentext);
                String index = (String) hiddenText.getText();
                int real_index = Integer.parseInt(index);

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", real_index);
                Intent intent = new Intent(getApplicationContext(), Display_Info.class);

                intent.putExtras(dataBundle);
                startActivityForResult(intent, CHECK_RIGHT_INTENT);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECK_RIGHT_INTENT )
        {

            array_list.clear();
            array_list = mydb.getAllNotes();
            adapter.updateReceiptsList(array_list);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add:
                Bundle data = new Bundle();
                data.putInt("id", 0);
                Intent intent = new Intent(getApplicationContext(), Display_Info.class);
                intent.putExtras(data);
                //startActivity(intent);
                startActivityForResult(intent, CHECK_RIGHT_INTENT);
                return true;

            case R.id.itemLocation:
                Intent intentLoc = new Intent(getApplicationContext(), MapsActivity.class);
                intentLoc.putParcelableArrayListExtra("notes",array_list);
                startActivityForResult(intentLoc, CHECK_LOCATION_INTENT);
                return true;


            case(R.id.action_settings):
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
