package com.hospicebangladesh.pms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hospicebangladesh.pms.adapter.MedicineAdapter;
import com.hospicebangladesh.pms.http.HttpRequest;
import com.hospicebangladesh.pms.http.HttpRequestCallBack;
import com.hospicebangladesh.pms.model.Medicine;
import com.hospicebangladesh.pms.utils.Session;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;


public class ServicesActivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "ServicesActivity";
    public String serviceUpdatePostUrl = "service.php";
    public String showServiceGetUrl = "get_services.php";

    final static String DATEPICKER = "datePicker";

    HashMap<String, Integer> spinnerMap;

    @Bind(R.id.input_mobile)
    EditText _mobileText;

    @Bind(R.id.input_address)
    EditText _addressText;

    @Bind(R.id.input_note)
    EditText _noteText;


    @Bind(R.id.input_type)
    LabelledSpinner _input_type;

    @Bind(R.id.input_time_spinner)
    LabelledSpinner _input_time_spinner;

    @Bind(R.id.textViewStoreCare)
    TextView _textViewStoreCare;


    @Bind(R.id.btn_service)
    Button _serviceButton;

    static EditText _input_date;
    static EditText _input_time;

    static EditText _input_from;
    static EditText _input_to;


    int serviceIndex,input_type_text=0;
    String  input_time_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinnerMap=new HashMap<String, Integer>();
        _input_date = (EditText) findViewById(R.id.input_date);
        _input_time = (EditText) findViewById(R.id.input_time);


        _input_from = (EditText) findViewById(R.id.input_from);
        _input_to = (EditText) findViewById(R.id.input_to);


        String[] serviceList = new String[]{"Palliative Doctor Visit", "Nursing Support", "Medical Instrument Rent", "Medical Procedure", "Allied Helth Support", "Lab Support", "Ambulance", "Patient Catering", "Hospice e-store", "Telecare"};

        serviceIndex = getIntent().getExtras().getInt("index");
        getSupportActionBar().setTitle(serviceList[serviceIndex]);

        String[] defaultList = new String[]{"Please select from dropdown list"};
        _input_type.setItemsArray(defaultList);
        spinnerMap.put(defaultList[0], 0);

        _input_date.setHint("Date");
        _input_time.setHint("Time");

        if (serviceIndex == 0) {

            _input_type.setVisibility(View.GONE);  //View.VISIBLE
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);

        } else if (serviceIndex == 1) {

            try {
                getService();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _input_time.setVisibility(View.GONE);

        } else if (serviceIndex == 2) {

            try {
                getService();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);

        } else if (serviceIndex == 3) {

            _input_type.setVisibility(View.GONE);
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);

        } else if (serviceIndex == 4) {

            try {
                getService();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);

        } else if (serviceIndex == 5) {
            try {
                getService();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);

        } else if (serviceIndex == 6) {

            _input_type.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);

            _input_date.setHint("From Time");
            _input_time.setHint("To Time");

        } else if (serviceIndex == 7) {


            try {
                getService();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);

        } else if (serviceIndex == 8) {


            _textViewStoreCare.setText("Hospice-e-Store\n\nwww.hospicebangladesh.com/hospice-e-store/");
            _input_type.setVisibility(View.GONE);
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _noteText.setVisibility(View.GONE);
            _addressText.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);
            _input_date.setVisibility(View.GONE);
            _input_time.setVisibility(View.GONE);
            _mobileText.setVisibility(View.GONE);

            _serviceButton.setVisibility(View.GONE);

            _textViewStoreCare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://hospicebangladesh.com/hospice-e-store/"));
                    startActivity(browserIntent);
                }
            });


        } else if (serviceIndex == 9) {


            _textViewStoreCare.setText("For Palliative Care Call us on 09666-788887 anywhere in Bangladesh\n" +
                    "(9am to 5pm, except Friday & Govt. Holiday)\n" +
                    "Access to Telecare firstly you have to registration by a minimum charge 50 BDT weekly/150 BDT monthly according to your need through bKash on Merchant account 01840486007.\n" +
                    "\nDetails on http://hospicebangladesh.com/telecare/ \n");
            _input_type.setVisibility(View.GONE);
            _input_from.setVisibility(View.GONE);
            _input_to.setVisibility(View.GONE);
            _noteText.setVisibility(View.GONE);
            _addressText.setVisibility(View.GONE);
            _input_time_spinner.setVisibility(View.GONE);
            _input_date.setVisibility(View.GONE);
            _input_time.setVisibility(View.GONE);
            _mobileText.setVisibility(View.GONE);

            _serviceButton.setVisibility(View.GONE);


            _textViewStoreCare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://hospicebangladesh.com/telecare/"));
                    startActivity(browserIntent);
                }
            });

        }

        String[] timeList = new String[]{"12 hour", "24 hour"};
        _input_time_spinner.setItemsArray(timeList);
        _input_time_spinner.setOnItemChosenListener(this);

        _input_type.setOnItemChosenListener(this);


        _serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    service();
                } catch (JSONException e) {

                }
            }
        });


        _input_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogFrom(v);
            }
        });

        _input_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogTo(v);
            }
        });

        _input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviceIndex==6){
                    showTimePickerDialogFrom(v);
                }else{
                    showDatePickerDialog(v);
                }

            }
        });


        _input_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });


    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        switch (labelledSpinner.getId()) {


            case R.id.input_time_spinner:
                input_time_text = (String) adapterView.getItemAtPosition(position);
                break;

            case R.id.input_type:

               input_type_text =spinnerMap.get((String)adapterView.getItemAtPosition(position)) ;
                break;

        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new ServicesActivity.TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    public void showTimePickerDialogFrom(View v) {
        DialogFragment newFragment = new ServicesActivity.TimePickerFragmentFrom();
        newFragment.show(getFragmentManager(), "timePicker");
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new ServicesActivity.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogFrom(View v) {
        DialogFragment newFragment = new ServicesActivity.DatePickerFragmentFrom();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogTo(View v) {
        DialogFragment newFragment = new ServicesActivity.DatePickerFragmentTo();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            _input_time.setText(hourOfDay + ":" + minute);
        }
    }


    public static class TimePickerFragmentFrom extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            _input_date.setText(hourOfDay + ":" + minute);
        }
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public static final int FLAG_START_DATE = 0;
        public static final int FLAG_END_DATE = 1;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            _input_date.setText(year + "-" + month + "-" + day);

        }
    }


    public static class DatePickerFragmentFrom extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public static final int FLAG_START_DATE = 0;
        public static final int FLAG_END_DATE = 1;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            _input_from.setText(year + "-" + month + "-" + day);

        }
    }

    public static class DatePickerFragmentTo extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public static final int FLAG_START_DATE = 0;
        public static final int FLAG_END_DATE = 1;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            _input_to.setText(year + "-" + month + "-" + day);

        }
    }

    public void service() throws JSONException {
        Log.d(TAG, "service");

        if (!validate()) {
            onSignupFailed("Validation Failed");
            return;
        }

        _serviceButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ServicesActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");
        progressDialog.show();


        String mobile = _mobileText.getText().toString();
        String address = _addressText.getText().toString();
        String note = _noteText.getText().toString();
        String date = _input_date.getText().toString();
        String time = _input_time.getText().toString();

        String date_from = _input_from.getText().toString();
        String date_to = _input_to.getText().toString();

        if (time.equals("")) {
            if (input_time_text != null)
                time = input_time_text;
        }

        JSONObject postBody = new JSONObject();

        String user_id = Session.getPreference(getApplicationContext(), Session.user_id);

        postBody.put("user_id", user_id);
        postBody.put("date", date);
        postBody.put("time", time);
        postBody.put("mobile", mobile);
        postBody.put("service_id", serviceIndex + 1);
        postBody.put("address", address);
        postBody.put("note", note);


        postBody.put("date_from", date_from);
        postBody.put("date_to", date_to);

            postBody.put("input_type_text", input_type_text);



        try {
            HttpRequest.postRequest(serviceUpdatePostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    ServicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    onSignupSuccess(message);
                                    progressDialog.dismiss();
                                } else {
                                    onSignupFailed(message);
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
                    ServicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            _serviceButton.setEnabled(true);
                            progressDialog.dismiss();
                            Log.d(TAG, " onFail");
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void getService() throws JSONException {

        final ProgressDialog progressDialog = new ProgressDialog(ServicesActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sync...");
        progressDialog.show();

        JSONObject postBody = new JSONObject();
        postBody.put("service_id", serviceIndex + 1);

        try {
            HttpRequest.postRequest(showServiceGetUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    ServicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {

                                    JSONArray jsonArrayServices = json.getJSONArray("services");
                                    String[] typeList = new String[jsonArrayServices.length()];
                                    for (int i = 0; i < jsonArrayServices.length(); i++) {

                                        JSONObject objPrescriptions = jsonArrayServices.getJSONObject(i);

                                        int ssid = objPrescriptions.getInt("ssid");
                                        String sub_service = objPrescriptions.getString("sub_service");
                                        spinnerMap.put(sub_service, ssid);

                                        typeList[i] = sub_service;
                                    }

                                    _input_type.setItemsArray(typeList);


                                    progressDialog.dismiss();
                                } else {

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
                    ServicesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            _serviceButton.setEnabled(true);
                            progressDialog.dismiss();
                            Log.d(TAG, " onFail");
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void onSignupSuccess(String message) {
        _serviceButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _serviceButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String mobile = _mobileText.getText().toString();


        String date = _input_date.getText().toString();
        String time = _input_time.getText().toString();

        if (date.isEmpty()) {
            _input_date.setError("Enter Valid Date");
            valid = false;
        } else {
            _input_date.setError(null);
        }

        if (time.isEmpty()) {
            _input_time.setError("Enter Valid Time");
            valid = false;
        } else {
            _input_time.setError(null);
        }

       /* if (username.isEmpty()) {
            _usernameText.setError("Enter Valid Username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }


        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid Email Address");
            valid = false;
        } else {
            _emailText.setError(null);
        }*/

        if (mobile.isEmpty() || mobile.length() != 11) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }


        return valid;
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
