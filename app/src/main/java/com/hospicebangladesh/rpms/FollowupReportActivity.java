package com.hospicebangladesh.rpms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.hospicebangladesh.rpms.http.HttpRequest;
import com.hospicebangladesh.rpms.http.HttpRequestCallBack;
import com.hospicebangladesh.rpms.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class FollowupReportActivity extends AppCompatActivity {

    private static final String TAG = "FollowupReportActivity";

    public String showFollowupPostUrl = "get_followup.php";


    @Bind(R.id.followup_report)
    TableLayout _followup_report;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_report);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            initialize();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void initialize() throws JSONException{

        final ProgressDialog progressDialog = new ProgressDialog(FollowupReportActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        JSONObject postBody = new JSONObject();

        String user_id=   Session.getPreference(getApplicationContext(),Session.user_id);
        postBody.put("user_id", user_id);

        try {

            HttpRequest.postRequest(showFollowupPostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    FollowupReportActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayFollowups = json.getJSONArray("followups");

                                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.topMargin = 2;
                                    layoutParams.rightMargin = 2;

                                    TableRow tableRowHeader = new TableRow(getApplicationContext());

                                    TextView textViewHeader1 = new TextView(getApplicationContext());
                                    textViewHeader1.setLayoutParams(layoutParams);
                                    textViewHeader1.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader1.setPadding(20, 20, 20, 20);
                                    textViewHeader1.setText("Date");

                                    TextView textViewHeader2 = new TextView(getApplicationContext());
                                    textViewHeader2.setLayoutParams(layoutParams);
                                    textViewHeader2.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader2.setPadding(20, 20, 20, 20);
                                    textViewHeader2.setText("Time");

                                    TextView textViewHeader3 = new TextView(getApplicationContext());
                                    textViewHeader3.setLayoutParams(layoutParams);
                                    textViewHeader3.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader3.setPadding(20, 20, 20, 20);
                                    textViewHeader3.setText("Bp mmHg");

                                    TextView textViewHeader4 = new TextView(getApplicationContext());
                                    textViewHeader4.setLayoutParams(layoutParams);
                                    textViewHeader4.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader4.setPadding(20, 20, 20, 20);
                                    textViewHeader4.setText("Pulse-/Min");

                                    TextView textViewHeader5 = new TextView(getApplicationContext());
                                    textViewHeader5.setLayoutParams(layoutParams);
                                    textViewHeader5.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader5.setPadding(20, 20, 20, 20);
                                    textViewHeader5.setText("O2 Saturation %");

                                    TextView textViewHeader6 = new TextView(getApplicationContext());
                                    textViewHeader6.setLayoutParams(layoutParams);
                                    textViewHeader6.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader6.setPadding(20, 20, 20, 20);
                                    textViewHeader6.setText("Temperature °F");

                                    TextView textViewHeader7 = new TextView(getApplicationContext());
                                    textViewHeader7.setLayoutParams(layoutParams);
                                    textViewHeader7.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader7.setPadding(20, 20, 20, 20);
                                    textViewHeader7.setText("Blood Sugar");

                                    TextView textViewHeader8 = new TextView(getApplicationContext());
                                    textViewHeader8.setLayoutParams(layoutParams);
                                    textViewHeader8.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader8.setPadding(20, 20, 20, 20);
                                    textViewHeader8.setText("Insulin");


                                    TextView textViewHeader9 = new TextView(getApplicationContext());
                                    textViewHeader9.setLayoutParams(layoutParams);
                                    textViewHeader9.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader9.setPadding(20, 20, 20, 20);
                                    textViewHeader9.setText("Bowel Movement");

                                    TextView textViewHeader10 = new TextView(getApplicationContext());
                                    textViewHeader10.setLayoutParams(layoutParams);
                                    textViewHeader10.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader10.setPadding(20, 20, 20, 20);
                                    textViewHeader10.setText("Intake Ouput");


                                    TextView textViewHeader16 = new TextView(getApplicationContext());
                                    textViewHeader16.setLayoutParams(layoutParams);
                                    textViewHeader16.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader16.setPadding(20, 20, 20, 20);
                                    textViewHeader16.setText("Pain");


                                    TextView textViewHeader12 = new TextView(getApplicationContext());
                                    textViewHeader12.setLayoutParams(layoutParams);
                                    textViewHeader12.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader12.setPadding(20, 20, 20, 20);
                                    textViewHeader12.setText("Shortness of breath");


                                    TextView textViewHeader13 = new TextView(getApplicationContext());
                                    textViewHeader13.setLayoutParams(layoutParams);
                                    textViewHeader13.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader13.setPadding(20, 20, 20, 20);
                                    textViewHeader13.setText("Nausea");

                                    TextView textViewHeader14 = new TextView(getApplicationContext());
                                    textViewHeader14.setLayoutParams(layoutParams);
                                    textViewHeader14.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader14.setPadding(20, 20, 20, 20);
                                    textViewHeader14.setText("Weakness");

                                    TextView textViewHeader15 = new TextView(getApplicationContext());
                                    textViewHeader15.setLayoutParams(layoutParams);
                                    textViewHeader15.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader15.setPadding(20, 20, 20, 20);
                                    textViewHeader15.setText("Poor appetite");


                                    TextView textViewHeader11 = new TextView(getApplicationContext());
                                    textViewHeader11.setLayoutParams(layoutParams);
                                    textViewHeader11.setBackgroundColor(Color.LTGRAY);
                                    textViewHeader11.setPadding(20, 20, 20, 20);
                                    textViewHeader11.setText("Special Note");



                                    tableRowHeader.addView(textViewHeader1);
                                    tableRowHeader.addView(textViewHeader2);
                                    tableRowHeader.addView(textViewHeader3);
                                    tableRowHeader.addView(textViewHeader4);
                                    tableRowHeader.addView(textViewHeader5);
                                    tableRowHeader.addView(textViewHeader6);
                                    tableRowHeader.addView(textViewHeader7);
                                    tableRowHeader.addView(textViewHeader8);
                                    tableRowHeader.addView(textViewHeader9);
                                    tableRowHeader.addView(textViewHeader10);
                                    tableRowHeader.addView(textViewHeader16);
                                    tableRowHeader.addView(textViewHeader12);
                                    tableRowHeader.addView(textViewHeader13);
                                    tableRowHeader.addView(textViewHeader14);
                                    tableRowHeader.addView(textViewHeader15);
                                    tableRowHeader.addView(textViewHeader11);

                                    _followup_report.addView(tableRowHeader);

                                    for (int i = 0; i < jsonArrayFollowups.length(); i++) {

                                        JSONObject objPrescriptions = jsonArrayFollowups.getJSONObject(i);

                                        String fdate = objPrescriptions.getString("fdate");
                                        String ftime = objPrescriptions.getString("ftime");
                                        String bp = objPrescriptions.getString("bp");
                                        String pulse = objPrescriptions.getString("pulse");
                                        String o2_saturation = objPrescriptions.getString("o2_saturation");
                                        String temp = objPrescriptions.getString("temp");
                                        String blood_sugar = objPrescriptions.getString("blood_sugar");
                                        String insulin = objPrescriptions.getString("insulin");
                                        String bowel_movement = objPrescriptions.getString("bowel_movement");
                                        String intake_ouput = objPrescriptions.getString("intake_ouput");
                                        String further_complication = objPrescriptions.getString("further_complication");
                                        String shortness_breath = objPrescriptions.getString("shortness_breath");
                                        String nausea = objPrescriptions.getString("nausea");
                                        String weakness = objPrescriptions.getString("weakness");
                                        String poor_appetite = objPrescriptions.getString("poor_appetite");
                                        String pain = objPrescriptions.getString("pain");

                                        // CREATE TABLE ROW
                                        TableRow tableRow = new TableRow(getApplicationContext());

                                        // CREATE PARAM FOR MARGINING


                                        // CREATE TEXTVIEW

                                        TextView textViewfdate = new TextView(getApplicationContext());
                                        TextView textViewftime = new TextView(getApplicationContext());
                                        TextView textViewbp = new TextView(getApplicationContext());
                                        TextView textViewpulse = new TextView(getApplicationContext());
                                        TextView textViewo2_saturation = new TextView(getApplicationContext());
                                        TextView textViewtemp = new TextView(getApplicationContext());
                                        TextView textViewblood_sugar = new TextView(getApplicationContext());
                                        TextView textViewinsulin = new TextView(getApplicationContext());
                                        TextView textViewbowel_movement = new TextView(getApplicationContext());
                                        TextView textViewintake_ouput = new TextView(getApplicationContext());
                                        TextView textViewfurther_complication = new TextView(getApplicationContext());
                                        TextView textViewshortness_breath = new TextView(getApplicationContext());
                                        TextView textViewnausea = new TextView(getApplicationContext());
                                        TextView textViewweakness = new TextView(getApplicationContext());
                                        TextView textViewpoor_appetite = new TextView(getApplicationContext());
                                        TextView textView_pain = new TextView(getApplicationContext());
                                        // SET PARAMS

                                        textViewfdate.setLayoutParams(layoutParams);
                                        textViewftime.setLayoutParams(layoutParams);
                                        textViewbp.setLayoutParams(layoutParams);
                                        textViewpulse.setLayoutParams(layoutParams);
                                        textViewo2_saturation.setLayoutParams(layoutParams);
                                        textViewtemp.setLayoutParams(layoutParams);
                                        textViewblood_sugar.setLayoutParams(layoutParams);
                                        textViewinsulin.setLayoutParams(layoutParams);
                                        textViewbowel_movement.setLayoutParams(layoutParams);
                                        textViewintake_ouput.setLayoutParams(layoutParams);
                                        textViewfurther_complication.setLayoutParams(layoutParams);
                                        textViewshortness_breath.setLayoutParams(layoutParams);
                                        textViewnausea.setLayoutParams(layoutParams);
                                        textViewweakness.setLayoutParams(layoutParams);
                                        textViewpoor_appetite.setLayoutParams(layoutParams);
                                        textView_pain.setLayoutParams(layoutParams);
                                        // SET BACKGROUND COLOR


                                        textViewfdate.setBackgroundColor(Color.WHITE);
                                        textViewftime.setBackgroundColor(Color.WHITE);
                                        textViewbp.setBackgroundColor(Color.WHITE);
                                        textViewpulse.setBackgroundColor(Color.WHITE);
                                        textViewo2_saturation.setBackgroundColor(Color.WHITE);
                                        textViewtemp.setBackgroundColor(Color.WHITE);
                                        textViewblood_sugar.setBackgroundColor(Color.WHITE);
                                        textViewinsulin.setBackgroundColor(Color.WHITE);
                                        textViewbowel_movement.setBackgroundColor(Color.WHITE);
                                        textViewintake_ouput.setBackgroundColor(Color.WHITE);
                                        textViewfurther_complication.setBackgroundColor(Color.WHITE);
                                        textViewshortness_breath.setBackgroundColor(Color.WHITE);
                                        textViewnausea.setBackgroundColor(Color.WHITE);
                                        textViewweakness.setBackgroundColor(Color.WHITE);
                                        textViewpoor_appetite.setBackgroundColor(Color.WHITE);
                                        textView_pain.setBackgroundColor(Color.WHITE);

                                        // SET PADDING


                                        textViewfdate.setPadding(20, 20, 20, 20);
                                        textViewftime.setPadding(20, 20, 20, 20);
                                        textViewbp.setPadding(20, 20, 20, 20);
                                        textViewpulse.setPadding(20, 20, 20, 20);
                                        textViewo2_saturation.setPadding(20, 20, 20, 20);
                                        textViewtemp.setPadding(20, 20, 20, 20);
                                        textViewblood_sugar.setPadding(20, 20, 20, 20);
                                        textViewinsulin.setPadding(20, 20, 20, 20);
                                        textViewbowel_movement.setPadding(20, 20, 20, 20);
                                        textViewintake_ouput.setPadding(20, 20, 20, 20);
                                        textViewfurther_complication.setPadding(20, 20, 20, 20);
                                        textViewshortness_breath.setPadding(20, 20, 20, 20);
                                        textViewnausea.setPadding(20, 20, 20, 20);
                                        textViewweakness.setPadding(20, 20, 20, 20);
                                        textViewpoor_appetite.setPadding(20, 20, 20, 20);
                                        textView_pain.setPadding(20, 20, 20, 20);

                                        // SET TEXTVIEW TEXT

                                        textViewfdate.setText(fdate);
                                        textViewftime.setText(ftime);
                                        textViewbp.setText(bp);
                                        textViewpulse.setText(pulse);
                                        textViewo2_saturation.setText(o2_saturation);
                                        textViewtemp.setText(temp);
                                        textViewblood_sugar.setText(blood_sugar);
                                        textViewinsulin.setText(insulin);
                                        textViewbowel_movement.setText(bowel_movement);
                                        textViewintake_ouput.setText(intake_ouput);
                                        textViewfurther_complication.setText(further_complication);

                                        textViewshortness_breath.setText(shortness_breath);
                                        textViewnausea.setText(nausea);
                                        textViewweakness.setText(weakness);
                                        textViewpoor_appetite.setText(poor_appetite);
                                        textView_pain.setText(pain);

                                        // ADD TEXTVIEW TO TABLEROW

                                        tableRow.addView(textViewfdate);
                                        tableRow.addView(textViewftime);
                                        tableRow.addView(textViewbp);
                                        tableRow.addView(textViewpulse);
                                        tableRow.addView(textViewo2_saturation);
                                        tableRow.addView(textViewtemp);
                                        tableRow.addView(textViewblood_sugar);
                                        tableRow.addView(textViewinsulin);
                                        tableRow.addView(textViewbowel_movement);
                                        tableRow.addView(textViewintake_ouput);
                                        tableRow.addView(textView_pain);
                                        tableRow.addView(textViewshortness_breath);
                                        tableRow.addView(textViewnausea);
                                        tableRow.addView(textViewweakness);
                                        tableRow.addView(textViewpoor_appetite);
                                        tableRow.addView(textViewfurther_complication);
                                        // ADD TABLEROW TO TABLELAYOUT

                                        _followup_report.addView(tableRow);

                                    }


                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFail() {
                    FollowupReportActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            Log.d(TAG, " onFail");
                            progressDialog.dismiss();

                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
