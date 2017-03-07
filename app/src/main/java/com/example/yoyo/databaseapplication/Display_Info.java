package com.example.yoyo.databaseapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class Display_Info extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int CHECK_LOCATION_INTENT_Display_Info = 1;
    static final String Show_Single_Location = "Show_Single_Location";
    private TextToSpeech textToSpeech;
    private Button checkLocation;
    private Button ListenToMemo;
    private Bitmap imageBitmap;
    private TextView textTopic;
    private ImageView imgPreview;
    private TextView textDescription;
    private ImageView image;
    private Button button;
    private Button take_a_pic_button;
    private DBHelper mydb;
    private int value;// kak taka raboti
    private int id_to_delete_update;
    PackageManager packageManager; // determines if a phone got a camera
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    TextView addressLabel;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__info);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status)
            {
                if(status != TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });



        checkLocation =(Button) findViewById(R.id.buttonCheckLocation);

        textDescription = (TextView) findViewById((R.id.editTextDescription));
        ListenToMemo =(Button)findViewById(R.id.ListenToMemo);
        ListenToMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = textDescription.getText().toString();
                //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        addressLabel =(TextView) findViewById(R.id.addressLabel);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        take_a_pic_button = (Button) findViewById(R.id.buttonTakeAPhoto);
        textTopic = (TextView) findViewById(R.id.editTextTopic);
        image = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        packageManager = this.getPackageManager();



        buildGoogleApiClient();

        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        } else
        {
           // Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();
        }

        mydb = new DBHelper(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getInt("id");
            //Toast.makeText(Display_Info.this, "The vallues passed is " + value, Toast.LENGTH_SHORT).show();
            if (value > 0)
            {



                checkLocation.setVisibility(View.VISIBLE);
                id_to_delete_update = value;
                ListenToMemo.setVisibility(View.VISIBLE);
                //   Toast.makeText(Display_Info.this, "The value to be updated is "+id_to_delete_update, Toast.LENGTH_SHORT).show();
                Log.d("My Custom Tag", Integer.toString(id_to_delete_update));
                button.setVisibility(View.INVISIBLE);
                take_a_pic_button.setVisibility(View.INVISIBLE);
               // ListenToMemo.setVisibility(View.INVISIBLE);
                // Toast.makeText(Display_Info.this,id_to_delete_update, Toast.LENGTH_LONG).show();
                Notes note = mydb.getData(id_to_delete_update);//java advanced
                if (note != null)
                {

//                    checkLocation.setOnClickListener(new View.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(View v)
//                        {
//                            Notes note = mydb.getData(id_to_delete_update);
//                           String lat = note.getLat();
//                           String lng = note.getLng();
//                            String topic = note.getTopic();
//                            String transporter_info = lat+","+lng+","+topic;
//                            Toast.makeText(Display_Info.this, " lat lng "+lat+" "+lng, Toast.LENGTH_SHORT).show();
//                            Bundle bundle_location_one = new Bundle();
//                            bundle_location_one.putString(Show_Single_Location,transporter_info);
//                            Toast.makeText(Display_Info.this, "", Toast.LENGTH_SHORT).show();
//
//                            Intent intentLoc = new Intent(getApplicationContext(), MapsActivity.class);
//                            intentLoc.putExtras(bundle_location_one);
//                            startActivityForResult(intentLoc, CHECK_LOCATION_INTENT_Display_Info);
//                        }
//                    });

                    textTopic.setText(note.getTopic());
                    textTopic.setFocusable(false);
                    textTopic.setClickable(false);
                    textDescription.setText(note.getDescription());
                    textDescription.setFocusable(false);
                    textDescription.setClickable(false);
                    Bitmap bit = mydb.getNotesPicture(note.getImage());
                    image.setImageBitmap(bit);
                } else {
                    Toast.makeText(Display_Info.this, "Returned null from databse", Toast.LENGTH_SHORT).show();
                }

            }else
            {
                checkLocation.setVisibility(View.INVISIBLE);
            }
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (id_to_delete_update > 0)
        {
            getMenuInflater().inflate(R.menu.display_note, menu);

        } else {

        }
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Edit_Contact:
                button.setVisibility(View.VISIBLE);
                take_a_pic_button.setVisibility(View.VISIBLE);
                checkLocation.setVisibility(View.INVISIBLE);
                textTopic.setEnabled(true);
                textTopic.setFocusableInTouchMode(true);
                textTopic.setClickable(true);

                textDescription.setEnabled(true);
                textDescription.setFocusableInTouchMode(true);
                textDescription.setClickable(true);
                return true;

            case R.id.Delete_Contact:

                checkLocation.setVisibility(View.INVISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteById(id_to_delete_update);
                               // Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public void run(View view) {
        if (value > 0)
        {


            if (textTopic.getText().toString().length() > 0 && textDescription.getText().toString().length() > 0) {
                String topic = textTopic.getText().toString();
                String description = (String) textDescription.getText().toString();

                Notes note = mydb.getData(id_to_delete_update);
                if (imageBitmap != null) {
                    note.setTopic(topic);
                    note.setDescription(description);
                    Bitmap map_to_be_saved = imageBitmap;
                    mydb.updateContactwithBitmap(note, map_to_be_saved);
                } else {
                    note.setTopic(topic);
                    note.setDescription(description);
                    mydb.updateNote(note);
                }

                Toast.makeText(Display_Info.this, " Updated " + topic, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Display_Info.this, "Please provide all values!!! ", Toast.LENGTH_SHORT).show();
            }
            //only visible button if we are in edit option

        } else {
            //should be saved
            if (textTopic.getText().toString().length() > 0 && textDescription.getText().toString().length() > 0) {
                String topic = textTopic.getText().toString();
                String description = (String) textDescription.getText().toString();
                Notes note = new Notes();
                note.setTopic(topic);
                note.setDescription(description);
                //here Camera Intent wjich takes photos


                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

                    //yes
                    take_a_pic_button.setVisibility(View.VISIBLE);
                    if (imageBitmap != null) {
                        Location loc  =    TakeALocationHa();
                        String lat = String.valueOf(loc.getLatitude());
                        String lng = String.valueOf(loc.getLongitude());
                        note.setLat(lat);
                        note.setLng(lng);
                        mydb.insertNote(note,imageBitmap);

                    } else
                    {
                        Location loc  =       TakeALocationHa();
                        String lat = String.valueOf(loc.getLatitude());
                        String lng = String.valueOf(loc.getLongitude());
                        note.setLat(lat);
                        note.setLng(lng);
                        mydb.insertNoteWithoutBitmap(note);
                    }

                    Log.i("camera", "This device has camera!");
                } else
                {
                    //no
                    Location loc  =     TakeALocationHa();
                    String lat = String.valueOf(loc.getLatitude());
                    String lng = String.valueOf(loc.getLongitude());
                    note.setLat(lat);
                    note.setLng(lng);

                    take_a_pic_button.setVisibility(View.INVISIBLE);
                    mydb.insertNoteWithoutBitmap(note);// for now I insert only without a picture
                  //  Toast.makeText(Display_Info.this, " Note added " + topic, Toast.LENGTH_SHORT).show();
                    Log.i("camera", "This device has no camera!");
                }
                finish();
            } else {

                Toast.makeText(Display_Info.this, "Please provide all values!!! ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        setResult(MainActivity.CHECK_RIGHT_INTENT);
        super.finish();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void TakeAPhoto(View v) {
        dispatchTakePictureIntent();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imgPreview.setImageBitmap(imageBitmap);

//            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
//
//            // CALL THIS METHOD TO GET THE ACTUAL PATH
//
//            File finalFile = new File(getRealPathFromURI(tempUri));
//
//
//            if (finalFile.exists()) {
//                if (finalFile.delete()) {
//                   // Toast.makeText(Display_Info.this, "Gallery photo deleted", Toast.LENGTH_SHORT).show();
//                } else {
//                  //  Toast.makeText(Display_Info.this, "Gallery photo NOT deleted", Toast.LENGTH_SHORT).show();
//                }
//            }
//            //     System.out.println(mImageCaptureUri);


        }
        if ( requestCode==CHECK_LOCATION_INTENT_Display_Info)
        {


        }
    }



    public Location TakeALocationHa()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null)
        {
        //    Toast.makeText(this, "lat " + String.valueOf(mLastLocation.getLatitude()) + " Long " + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
            return mLastLocation;
            //save the loc to the database
        }
        return null;
        // to display the current address in the ui
        // (new GetAddressTask(this)).execute(mLastLocation);
    }
    public void TakeALocation(View view)
    {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Display_Info Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.yoyo.databaseapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Display_Info Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.yoyo.databaseapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {
     //   Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    @Override
    protected void onPause()
    {

        if(textToSpeech !=null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
}
