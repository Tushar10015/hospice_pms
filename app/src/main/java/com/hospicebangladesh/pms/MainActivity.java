package com.hospicebangladesh.pms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.hospicebangladesh.pms.http.HttpRequestCallBack;
import com.hospicebangladesh.pms.utils.Session;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;

    private static final String TAG = "MainActivity";
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

    @Bind(R.id.buttonFollowupReport)
    Button _buttonFollowupReport;

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
            payment.checkPayment(getApplicationContext(),new HttpRequestCallBack(){
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

        _buttonFollowupReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FollowupReportActivity.class));
            }
        });


        _buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));
            }
        });


    }


    private void startSlider() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();

        file_maps.put("We connect you with professionals in home care – Knowledge and skills to your doorstep with a warm touch", R.drawable.e);
        file_maps.put("Team Member", R.drawable.a);
        file_maps.put("Before I Die I want", R.drawable.b);
        file_maps.put("Before I Die", R.drawable.c);
        file_maps.put("Hospice and Palliative care is most appropriate when a patient no longer benefits from curative treatment", R.drawable.d);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

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
        mDemoSlider.addOnPageChangeListener(this);

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
        }

        else if (id == R.id.nav_share) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://2aitbd.com/"));
            startActivity(browserIntent);
        }

        else if (id == R.id.nav_send) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://2aitbd.com/"));
            startActivity(browserIntent);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
