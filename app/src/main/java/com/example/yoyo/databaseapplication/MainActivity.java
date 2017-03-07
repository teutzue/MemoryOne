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


//        Drawable myDrawable = getResources().getDrawable(R.drawable.me);
//        Bitmap anImage  = ((BitmapDrawable) myDrawable).getBitmap();
//        Notes note  = new Notes();
//        note.setTopic("Yoanas first entry");
//        note.setDescription(" Dano se polu4i slava tebe gospodi ");
//        mydb.insertNote(note,anImage);

        theMainList =(ListView)findViewById(R.id.mainListView);

      //  hiddenText = (TextView) findViewById(R.id.hiddentext);
        obj = (ListView) findViewById(R.id.mainListView);
        array_list = mydb.getAllNotes();

        adapter = new ListCustomAdapter(this, array_list, mydb);

        obj.setAdapter(adapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(MainActivity.this, "The position pressed is "+position, Toast.LENGTH_SHORT).show();
                int id_Search = position + 1;//because of the way they are stored into the database
                hiddenText = (TextView) theMainList.getChildAt(position).findViewById(R.id.hiddentext);
                String index = (String) hiddenText.getText();
                int real_index = Integer.parseInt(index);

               // Toast.makeText(MainActivity.this, "The position pressed is " + id_Search, Toast.LENGTH_SHORT).show();

                Bundle dataBundle = new Bundle();
                //   LinearLayout lay = obj.
//                String text_id  =  (String) hiddenText.getText();
//                int _id = Integer.parseInt(text_id);
//
//
//                View v = getViewByPosition(position, obj);
//                if (v != null) {
//
//                }


                // dataBundle.putInt("id",id_Search);
              //  dataBundle.putInt("id", id_Search);

                dataBundle.putInt("id", real_index);
                Intent intent = new Intent(getApplicationContext(), Display_Info.class);

                intent.putExtras(dataBundle);
                startActivityForResult(intent, CHECK_RIGHT_INTENT);
            }
        });








    }


    // from byte array to Bitmap
//    public  Bitmap getPhoto(byte[] image)
//    {
//        return BitmapFactory.decodeByteArray(image, 0, image.length);
//    }
//
//    public  byte[] getBytes(Bitmap bitmap) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
//        return stream.toByteArray();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECK_RIGHT_INTENT )
        {

            array_list.clear();
            array_list = mydb.getAllNotes();
            //Toast.makeText(MainActivity.this, " onActivityResult called  and the size of the list is " + array_list, Toast.LENGTH_SHORT).show();

            adapter.updateReceiptsList(array_list);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        }


        if ( requestCode==CHECK_LOCATION_INTENT)
        {


        }



        // }
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
               // Bundle dataLoc = new Bundle();
              //  dataLoc.
                // dataLoc.putInt("id", 0);
                Intent intentLoc = new Intent(getApplicationContext(), MapsActivity.class);
               // dataLoc.putParcelableArrayList("notes",array_list);
                intentLoc.putParcelableArrayListExtra("notes",array_list);
                //intentLoc.putExtras(dataLoc);
                //startActivity(intent);
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



    /////////////////////////////GoogleApiClient.ConnectionCallbacks methods





}
