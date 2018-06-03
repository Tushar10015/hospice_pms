package com.hospicebangladesh.rpms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class NoPaidActivity extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    public static String getSliderGetUrl = "get_slider.php";
    @Bind(R.id.buttonServices)
    Button _buttonServices;

    @Bind(R.id.textViewLabel)
    TextView _textViewLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nopaid);
        ButterKnife.bind(this);


        startSlider();

        String messge = getIntent().getExtras().getString("message");
        _textViewLabel.setText(messge);

        _buttonServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ServiceListActivity.class));
            }
        });


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

                    NoPaidActivity.this.runOnUiThread(new Runnable() {
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
                                        TextSliderView textSliderView = new TextSliderView(NoPaidActivity.this);
                                        // initialize a SliderLayout
                                        textSliderView
                                                .description(name)
                                                .image(url_maps.get(name))
                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                .setOnSliderClickListener(NoPaidActivity.this);

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
                                    mDemoSlider.addOnPageChangeListener(NoPaidActivity.this);





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
                    NoPaidActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
                            file_maps.put("We connect you with professionals in home care – Knowledge and skills to your doorstep with a warm touch", R.drawable.e);
                            file_maps.put("Team Member", R.drawable.a);
                            file_maps.put("Before I Die I want to", R.drawable.b);
                            file_maps.put("Before I Die", R.drawable.c);
                            file_maps.put("Hospice and Palliative care is most appropriate when a patient no longer benefits from curative treatment", R.drawable.d);



                            for (String name : file_maps.keySet()) {
                                TextSliderView textSliderView = new TextSliderView(NoPaidActivity.this);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(file_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(NoPaidActivity.this);

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
                            mDemoSlider.addOnPageChangeListener(NoPaidActivity.this);


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
       startActivity(new Intent(this,MainActivity.class));
       finish();
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

}
