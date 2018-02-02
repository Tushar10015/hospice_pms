package com.hospicebangladesh.pms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hospicebangladesh.pms.adapter.MedicineAdapter;
import com.hospicebangladesh.pms.http.HttpRequest;
import com.hospicebangladesh.pms.http.HttpRequestCallBack;
import com.hospicebangladesh.pms.model.Medicine;
import com.hospicebangladesh.pms.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class InvestigationActivity extends AppCompatActivity {

    private static final String TAG = "InvestigationActivity";
    public String showInvestigationGetUrl = "http://2aitbd.com/pms/api/get_investigation.php";
    public String investigationUpdatePostUrl = "http://2aitbd.com/pms/api/investigation.php";

    final ArrayList<EditText> editTextArrayList = new ArrayList<>();
    @Bind(R.id.mainInvestigationLayout)
    LinearLayout _mainInvestigationLayout;

    @Bind(R.id.btn_investigation)
    Button _btn_investigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigation);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showInvestigation();

        _btn_investigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    investigation();
                } catch (JSONException e) {

                }
            }
        });
    }


    private void showInvestigation() {

        final ProgressDialog progressDialog = new ProgressDialog(InvestigationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        try {

            HttpRequest.getRequest(showInvestigationGetUrl, new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();

                    final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                    Log.d(TAG, serverResponse);
                    InvestigationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayinvestigations = json.getJSONArray("investigations");


                                    for (int i = 0; i < jsonArrayinvestigations.length(); i++) {
                                        JSONObject objPrescriptions = jsonArrayinvestigations.getJSONObject(i);
                                        int i_id = objPrescriptions.getInt("i_id");
                                        String invest_name = objPrescriptions.getString("invest_name");

                                        TextView category = new TextView(getApplicationContext());
                                        category.setText(invest_name + i);
                                        _mainInvestigationLayout.addView(category);


                                        JSONArray jsonArraySubCategory = objPrescriptions.getJSONArray("sub_category");



                                        for (int j = 0; j < jsonArraySubCategory.length(); j++) {
                                            JSONObject obj = jsonArraySubCategory.getJSONObject(j);

                                            int i_sid = obj.getInt("i_sid");
                                            String inc_sub = obj.getString("inc_sub");

                                            LinearLayout ll = new LinearLayout(getApplicationContext());
                                            ll.setOrientation(LinearLayout.HORIZONTAL);

                                            TextView subcategory = new TextView(getApplicationContext());
                                            subcategory.setText(inc_sub);
                                            ll.addView(subcategory);

                                            final EditText inputText = new EditText(getApplicationContext());
                                            inputText.setId(i_sid);
                                            inputText.setLayoutParams(params);
                                            editTextArrayList.add(inputText);
                                            ll.addView(inputText);

                                            _mainInvestigationLayout.addView(ll);

                                        }
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
                    InvestigationActivity.this.runOnUiThread(new Runnable() {
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


    public void investigation() throws JSONException {
        Log.d(TAG, "Followup");


        _btn_investigation.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(InvestigationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        StringBuilder stringBuilder = new StringBuilder();
        JSONObject jObjectData = new JSONObject();
        for (EditText editText : editTextArrayList) {
            if((!editText.getText().toString().equals(""))) {

                jObjectData.put(editText.getId()+"", editText.getText().toString());
                stringBuilder.append(editText.getText().toString() +":"+ editText.getId()+"_");

            }
        }


        JSONObject postBody = new JSONObject();
        postBody.put("user_id", Session.getPreference(getApplicationContext(), Session.user_id));
        postBody.put("investigations", jObjectData);


        try {
            HttpRequest.postRequest(investigationUpdatePostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    InvestigationActivity.this.runOnUiThread(new Runnable() {
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
                    InvestigationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            _btn_investigation.setEnabled(true);
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
        _btn_investigation.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _btn_investigation.setEnabled(true);
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
