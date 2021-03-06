package com.hospicebangladesh.rpms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.hospicebangladesh.rpms.adapter.MedicineAdapter;
import com.hospicebangladesh.rpms.http.HttpRequest;
import com.hospicebangladesh.rpms.http.HttpRequestCallBack;
import com.hospicebangladesh.rpms.model.Medicine;
import com.hospicebangladesh.rpms.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class PrescriptionActivity extends AppCompatActivity {

    private static final String TAG = "PrescriptionActivity";
    public String showPresPostUrl = "get_prescription.php";
    public String profileGetPostUrl = "get_profile.php";
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.recyclerViewMedicine)
    RecyclerView _recyclerViewMedicine;

    @Bind(R.id.textViewId)
    TextView _textViewId;

    @Bind(R.id.textViewSex)
    TextView _textViewSex;

    @Bind(R.id.textViewPatientName)
    TextView _textViewPatientName;

    @Bind(R.id.textViewAge)
    TextView _textViewAge;

    @Bind(R.id.textViewUpdatedDate)
    TextView _textViewUpdatedDate;

    @Bind(R.id.textViewTime)
    TextView _textViewTime;

    @Bind(R.id.textViewDoc0)
    TextView _textViewDoc0;

    @Bind(R.id.textViewDoc1)
    TextView _textViewDoc1;

    @Bind(R.id.textViewDoc2)
    TextView _textViewDoc2;

    @Bind(R.id.textViewDoc3)
    TextView _textViewDoc3;

    @Bind(R.id.textViewDoc4)
    TextView _textViewDoc4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            getProfile();
            showPrescription();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getProfile() throws JSONException {


        String user_id = Session.getPreference(getApplicationContext(), Session.user_id);

        JSONObject postBody = new JSONObject();
        postBody.put("user_id", user_id);

        try {
            HttpRequest.postRequest(profileGetPostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    PrescriptionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayProfiles = json.getJSONArray("profiles");

                                    for (int i = 0; i < jsonArrayProfiles.length(); i++) {
                                        JSONObject objProfiles = jsonArrayProfiles.getJSONObject(i);

                                        String name = objProfiles.getString("name");
                                        String password = objProfiles.getString("password");
                                        String phone = objProfiles.getString("phone");
                                        String email = objProfiles.getString("email");
                                        String gender = objProfiles.getString("gender");
                                        String age = objProfiles.getString("age");


                                        _textViewPatientName.setText("Name :" + name);
                                        _textViewSex.setText("Sex :" + gender);
                                        _textViewAge.setText("Age :" + age);

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
                    PrescriptionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d(TAG, " onFail");
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void showPrescription() throws JSONException {

        final ProgressDialog progressDialog = new ProgressDialog(PrescriptionActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        JSONObject postBody = new JSONObject();

        String user_id = Session.getPreference(getApplicationContext(), Session.user_id);
        postBody.put("user_id", user_id);

        try {

            HttpRequest.postRequest(showPresPostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    PrescriptionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");

                                JSONArray jsonArrayDoctors = json.getJSONArray("doctors");
                                int dlen = jsonArrayDoctors.length();
                                int dcounter = 0;
                                for (int i = 0; i < dlen; i++) {

                                    JSONObject objDoctors = jsonArrayDoctors.getJSONObject(i);
                                    String name = objDoctors.getString("name");

                                    if (dlen == 1) {
                                        _textViewDoc0.setText("1." + name);
                                    }

                                    if (dlen == 2) {
                                        if (dcounter == 0)
                                            _textViewDoc0.setText("1." + name);
                                        if (dcounter == 1)
                                            _textViewDoc1.setText("2." + name);
                                    }

                                    if (dlen == 3) {
                                        if (dcounter == 0)
                                            _textViewDoc0.setText("1." + name);
                                        if (dcounter == 1)
                                            _textViewDoc1.setText("2." + name);
                                        if (dcounter == 2)
                                            _textViewDoc2.setText("3." + name);
                                    }

                                    if (dlen == 4) {
                                        if (dcounter == 0)
                                            _textViewDoc0.setText("1." + name);
                                        if (dcounter == 1)
                                            _textViewDoc1.setText("2." + name);
                                        if (dcounter == 2)
                                            _textViewDoc2.setText("3." + name);
                                        if (dcounter == 3)
                                            _textViewDoc3.setText("4." + name);
                                    }

                                    if (dlen == 5) {
                                        if (dcounter == 0)
                                            _textViewDoc0.setText("1." + name);
                                        if (dcounter == 1)
                                            _textViewDoc1.setText("2." + name);
                                        if (dcounter == 2)
                                            _textViewDoc2.setText("3." + name);
                                        if (dcounter == 3)
                                            _textViewDoc3.setText("4." + name);
                                        if (dcounter == 4)
                                            _textViewDoc4.setText("5." + name);
                                    }

                                    dcounter++;
                                }


                                if (success == 1) {
                                    JSONArray jsonArrayPrescriptions = json.getJSONArray("prescriptions");

                                    List<Medicine> myDataset = new ArrayList<>();
                                    for (int i = 0; i < jsonArrayPrescriptions.length(); i++) {
                                        JSONObject objPrescriptions = jsonArrayPrescriptions.getJSONObject(i);

                                        String prescription_id = objPrescriptions.getString("prescription_id");
                                        String presciption_id = objPrescriptions.getString("presciption_id");
                                        String prescribe_date = objPrescriptions.getString("prescribe_date");

                                        String[] parts = prescribe_date.split(" ");
                                        String date = parts[0];
                                        String time = parts[1];

                                        _textViewId.setText("Id :" + presciption_id);
                                        _textViewUpdatedDate.setText("Updated Date :" + date);
                                        _textViewTime.setText("Time :" + time);

                                        JSONArray jsonArrayMedicine = objPrescriptions.getJSONArray("medicin_details");

                                        for (int j = 0; j < jsonArrayMedicine.length(); j++) {
                                            JSONObject obj = jsonArrayMedicine.getJSONObject(j);
                                            String medicine = obj.getString("medicine");
                                            String duration = obj.getString("duration");
                                            String times = obj.getString("times");
                                            String instruction = obj.getString("instruction");
                                            String dtime = obj.getString("dtime");

                                            Medicine mObj = new Medicine();
                                            mObj.setMedicine(medicine);
                                            mObj.setDuration(duration);
                                            mObj.setTimes(times);
                                            mObj.setInstruction(instruction);
                                            mObj.setDtime(dtime);

                                            myDataset.add(mObj);
                                            Log.d("JSONArray", medicine + "   " + duration);
                                        }
                                    }


                                    _recyclerViewMedicine.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL));
                                    _recyclerViewMedicine.setHasFixedSize(true);
                                    _recyclerViewMedicine.setItemAnimator(new DefaultItemAnimator());
                                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    _recyclerViewMedicine.setLayoutManager(mLayoutManager);
                                    mAdapter = new MedicineAdapter(myDataset, PrescriptionActivity.this);
                                    _recyclerViewMedicine.setAdapter(mAdapter);
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
                    PrescriptionActivity.this.runOnUiThread(new Runnable() {
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
