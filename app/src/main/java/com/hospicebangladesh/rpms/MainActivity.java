package com.hospicebangladesh.rpms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.hospicebangladesh.rpms.http.HttpRequest;
import com.hospicebangladesh.rpms.http.HttpRequestCallBack;
import com.hospicebangladesh.rpms.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;

    private static final String TAG = "MainActivity";
    public static String getSliderGetUrl = "get_slider.php";
    public static String getMobileGetUrl = "get_mobile.php";


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 100;
    @Bind(R.id.buttonProfile)
    Button _buttonProfile;

    @Bind(R.id.buttonPrescription)
    Button _buttonPrescription;

    @Bind(R.id.buttonFollowup)
    Button _buttonFollowup;

    @Bind(R.id.buttonUploadPicture)
    Button _buttonUploadPicture;

    @Bind(R.id.buttonInvestigation)
    Button _buttonInvestigation;


    @Bind(R.id.buttonServices)
    Button _buttonServices;

    @Bind(R.id.buttonSOS)
    Button _buttonSOS;

    @Bind(R.id.buttonChat)
    Button _buttonChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView headerName = (TextView) headerView.findViewById(R.id.textViewNavName);
        headerName.setText(Session.getPreference(getApplicationContext(), Session.name));


        TextView headerMobile = (TextView) headerView.findViewById(R.id.textViewNavMobile);
        headerMobile.setText(Session.getPreference(getApplicationContext(), Session.mobile));

        startSlider();

        try {
            CheckPayment payment = new CheckPayment();
            payment.checkPayment(getApplicationContext(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {
                    String status = Session.getPreference(getApplicationContext(), "status");
                    String message = Session.getPreference(getApplicationContext(), "message");
                    if (status.equals("1")) {
                        //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), NoPaidActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFail() {
                    String status = Session.getPreference(getApplicationContext(), "status");
                    String message = Session.getPreference(getApplicationContext(), "message");
                    if (status.equals("1")) {
                        //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), NoPaidActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                    }

                    Log.d(TAG, " onFail");
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


        _buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        _buttonPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PrescriptionActivity.class));
            }
        });

        _buttonFollowup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FollowupActivity.class));
            }
        });

        _buttonUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UploadPictureActivity.class));
            }
        });


        _buttonInvestigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), InvestigationActivity.class));
            }
        });


        _buttonServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ServiceListActivity.class));
            }
        });

        _buttonSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.SEND_SMS)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    sendSms();
                }


            }
        });


        _buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    sendSms();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void sendSms() {


        try {

            HttpRequest.getRequest(getMobileGetUrl, new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HashMap<String, String> mob_maps = new HashMap<String, String>();
                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayProfiles = json.getJSONArray("mobiles");
                                    String msg="";
                                    for (int i = 0; i < jsonArrayProfiles.length(); i++) {
                                        JSONObject objProfiles = jsonArrayProfiles.getJSONObject(i);

                                        String mob1 = objProfiles.getString("mob1");
                                        String mob2 = objProfiles.getString("mob2");
                                        String mob3 = objProfiles.getString("mob3");
                                        msg = objProfiles.getString("content");

                                        mob_maps.put("mob1",mob1);
                                        mob_maps.put("mob2",mob2);
                                        mob_maps.put("mob3",mob3);

                                    }

                                    for (String name : mob_maps.keySet()) {
                                     String no=   mob_maps.get(name);

                                        //Getting intent and PendingIntent instance
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                                        //Get the SmsManager instance and call the sendTextMessage method to send message
                                        SmsManager sms = SmsManager.getDefault();
                                        sms.sendTextMessage(no, null, msg, pi, null);

                                        Toast.makeText(getApplicationContext(), "Message Sent successfully.",
                                                Toast.LENGTH_LONG).show();

                                    }




                                } else {

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFail() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly.Please check your internet connection.", Toast.LENGTH_LONG).show();
                            Log.d(TAG, " onFail");
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }






    }

    private void startSlider() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);


//         HashMap<String, String> url_maps = new HashMap<String, String>();
//        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
//        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
//        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
//        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");


        try {
            HttpRequest.getRequest(getSliderGetUrl, new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayProfiles = json.getJSONArray("sliders");
                                    for (int i = 0; i < jsonArrayProfiles.length(); i++) {
                                        JSONObject objProfiles = jsonArrayProfiles.getJSONObject(i);

                                        String img = objProfiles.getString("img");
                                        String content = objProfiles.getString("content");

                                        url_maps.put(content, img);

                                    }


                                    for (String name : url_maps.keySet()) {
                                        TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                                        // initialize a SliderLayout
                                        textSliderView
                                                .description(name)
                                                .image(url_maps.get(name))
                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                .setOnSliderClickListener(MainActivity.this);

                                        //add your extra information
                                        textSliderView.bundle(new Bundle());
                                        textSliderView.getBundle()
                                                .putString("extra", name);

                                        mDemoSlider.addSlider(textSliderView);
                                    }
                                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                                    mDemoSlider.setDuration(4000);
                                    mDemoSlider.addOnPageChangeListener(MainActivity.this);





                                } else {

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFail() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d(TAG, " onFail");

                            HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
                            file_maps.put("We connect you with professionals in home care – Knowledge and skills to your doorstep with a warm touch", R.drawable.e);
                            file_maps.put("Team Member", R.drawable.a);
                            file_maps.put("Before I Die I want to", R.drawable.b);
                            file_maps.put("Before I Die", R.drawable.c);
                            file_maps.put("Hospice and Palliative care is most appropriate when a patient no longer benefits from curative treatment", R.drawable.d);



                            for (String name : file_maps.keySet()) {
                                TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(file_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(MainActivity.this);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                            mDemoSlider.setDuration(4000);
                            mDemoSlider.addOnPageChangeListener(MainActivity.this);


                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        //  mDemoSlider.setPresetTransformer("Default");
        //    Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Session.clearPreference(getApplicationContext());
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_patient_profile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        } else if (id == R.id.nav_prescription) {
            startActivity(new Intent(getApplicationContext(), PrescriptionActivity.class));
        } else if (id == R.id.nav_investigation) {
            startActivity(new Intent(getApplicationContext(), InvestigationActivity.class));
        } else if (id == R.id.nav_followup) {
            startActivity(new Intent(getApplicationContext(), FollowupActivity.class));
        } else if (id == R.id.nav_services) {
            startActivity(new Intent(getApplicationContext(), ServiceListActivity.class));
        } else if (id == R.id.nav_upload_picture) {
            startActivity(new Intent(getApplicationContext(), UploadPictureActivity.class));
        } else if (id == R.id.nav_followup_report) {
            startActivity(new Intent(getApplicationContext(), FollowupReportActivity.class));
        } else if (id == R.id.nav_chat) {
            startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));
        } else if (id == R.id.nav_share) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://2aitbd.com/"));
            startActivity(browserIntent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
