package com.hospicebangladesh.pms;

import android.app.ProgressDialog;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.hospicebangladesh.pms.http.HttpRequest;
import com.hospicebangladesh.pms.http.HttpRequestCallBack;
import com.hospicebangladesh.pms.utils.Session;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class FollowupActivity extends AppCompatActivity {

    private static final String TAG = "FollowupActivity";
    public String profileUpdatePostUrl = "http://2aitbd.com/pms/api/followup.php";


    @Bind(R.id.input_bp)
    EditText _input_bp;
    @Bind(R.id.input_pulse)
    EditText _input_pulse;
    @Bind(R.id.input_o2_saturation)
    EditText _input_o2_saturation;
    @Bind(R.id.input_temp)
    EditText _input_temp;
    @Bind(R.id.input_blood_sugar)
    EditText _input_blood_sugar;
    @Bind(R.id.input_insulin)
    EditText _input_insulin;
    @Bind(R.id.input_bowel_movement)
    EditText _input_bowel_movement;
    @Bind(R.id.input_intake_ouput)
    EditText _input_intake_ouput;
    @Bind(R.id.input_further_complication)
    EditText _input_further_complication;



    @Bind(R.id.btn_followup)
    Button _btn_followup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _btn_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    followup();
                } catch (JSONException e) {

                }
            }
        });
    }

    public void followup() throws JSONException {
        Log.d(TAG, "Followup");

        if (!validate()) {
            onSignupFailed("Validation Failed");
            return;
        }

        _btn_followup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(FollowupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        String bp = _input_bp.getText().toString();
        String pulse = _input_pulse.getText().toString();
        String o2_saturation = _input_o2_saturation.getText().toString();
        String temp = _input_temp.getText().toString();
        String blood_sugar = _input_blood_sugar.getText().toString();
        String insulin = _input_insulin.getText().toString();

        String bowel_movement = _input_bowel_movement.getText().toString();
        String intake_ouput = _input_intake_ouput.getText().toString();
        String further_complication = _input_further_complication.getText().toString();



        JSONObject postBody = new JSONObject();
        postBody.put("user_id", Session.getPreference(getApplicationContext(),Session.user_id));
        postBody.put("bp", bp);
        postBody.put("pulse", pulse);
        postBody.put("o2_saturation", o2_saturation);
        postBody.put("temp", temp);
        postBody.put("blood_sugar", blood_sugar);
        postBody.put("insulin", insulin);
        postBody.put("bowel_movement", bowel_movement);
        postBody.put("intake_ouput", intake_ouput);
        postBody.put("further_complication", further_complication);

        try {
            HttpRequest.postRequest(profileUpdatePostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    FollowupActivity.this.runOnUiThread(new Runnable() {
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
                    FollowupActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            _btn_followup.setEnabled(true);
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
        _btn_followup.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _btn_followup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String bp = _input_bp.getText().toString();
        String pulse = _input_pulse.getText().toString();
        String o2_saturation = _input_o2_saturation.getText().toString();
        String temp = _input_temp.getText().toString();
        String blood_sugar = _input_blood_sugar.getText().toString();
        String insulin = _input_insulin.getText().toString();

        String bowel_movement = _input_bowel_movement.getText().toString();
        String intake_ouput = _input_intake_ouput.getText().toString();
        String further_complication = _input_further_complication.getText().toString();


        if (bp.isEmpty()) {
            _input_bp.setError("Enter Valid Bp");
            valid = false;
        } else {
            _input_bp.setError(null);
        }


        if (pulse.isEmpty()) {
            _input_pulse.setError("Enter Valid Pulse");
            valid = false;
        } else {
            _input_pulse.setError(null);
        }

       /* if (o2_saturation.isEmpty()) {
            _input_o2_saturation.setError("Enter Valid O2 Saturation");
            valid = false;
        } else {
            _input_o2_saturation.setError(null);
        }

        if (temp.isEmpty()) {
            _input_temp.setError("Enter Valid Temp");
            valid = false;
        } else {
            _input_temp.setError(null);
        }


        if (blood_sugar.isEmpty()) {
            _input_blood_sugar.setError("Enter Valid Blood Sugar");
            valid = false;
        } else {
            _input_blood_sugar.setError(null);
        }

        if (insulin.isEmpty()) {
            _input_insulin.setError("Enter Valid Insulin");
            valid = false;
        } else {
            _input_insulin.setError(null);
        }


        if (bowel_movement.isEmpty()) {
            _input_bowel_movement.setError("Enter Valid Bowel Movement");
            valid = false;
        } else {
            _input_bowel_movement.setError(null);
        }



        if (intake_ouput.isEmpty()) {
            _input_intake_ouput.setError("Enter Valid Intake Ouput");
            valid = false;
        } else {
            _input_intake_ouput.setError(null);
        }

        if (further_complication.isEmpty()) {
            _input_further_complication.setError("Enter Valid Further Complication");
            valid = false;
        } else {
            _input_further_complication.setError(null);
        }*/


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
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
