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
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class InvestigationActivity extends AppCompatActivity {

    private static final String TAG = "InvestigationActivity";
    public String showInvestigationGetUrl = "get_investigation_report.php";
    public String investigationUpdatePostUrl = "investigation.php";

    final ArrayList<EditText> editTextArrayList = new ArrayList<>();
    final ArrayList<TextView> textViewArrayList = new ArrayList<>();
    @Bind(R.id.mainInvestigationLayout)
    LinearLayout _mainInvestigationLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigation);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            showInvestigation();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void showInvestigation() throws JSONException {

        final ProgressDialog progressDialog = new ProgressDialog(InvestigationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        JSONObject postBody = new JSONObject();
        postBody.put("user_id", Session.getPreference(getApplicationContext(), Session.user_id));

        try {

            HttpRequest.postRequest(showInvestigationGetUrl,postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();



                    final HashMap<String, String> keyvalueHashMap = new HashMap<String, String>();
                    final HashMap<String, String> categoryHashMap = new HashMap<String, String>();
                    final HashMap<String, String> subCategoryHashMap = new HashMap<String, String>();
                    final HashMap<String, String> cat_subHashMap = new HashMap<String, String>();


                    final ArrayList<String> keyArrayList = new ArrayList();
                    final ArrayList<String> valueArrayList = new ArrayList();
                    final ArrayList<String> trackKeyArrayList = new ArrayList();
                    final HashMap<String, String> keyvalueHashMap2 = new HashMap<String, String>();
                    Log.d(TAG, serverResponse);
                    InvestigationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayData = json.getJSONArray("data");
                                    for (int i = 0; i < jsonArrayData.length(); i++) {

                                        JSONObject objPrescriptions = jsonArrayData.getJSONObject(i);

                                        String invdate = objPrescriptions.getString("invdate");
                                        String invvalue = objPrescriptions.getString("invvalues");
                                        String invvalues = invvalue.replaceAll("[{|}|\"]", "");
                                        keyvalueHashMap.put(invdate, invvalues);
                                        keyArrayList.add(invdate);
                                        valueArrayList.add(invvalues);

                                    }


                                    JSONArray jsonArrayCategory = json.getJSONArray("category");
                                    for (int i = 0; i < jsonArrayCategory.length(); i++) {

                                        JSONObject objPrescriptions = jsonArrayCategory.getJSONObject(i);

                                        String i_id = objPrescriptions.getString("i_id");
                                        String invest_name = objPrescriptions.getString("invest_name");

                                        categoryHashMap.put(i_id, invest_name);

                                    }


                                    JSONArray jsonArraySubCategory = json.getJSONArray("sub_category");
                                    for (int i = 0; i < jsonArraySubCategory.length(); i++) {

                                        JSONObject objPrescriptions = jsonArraySubCategory.getJSONObject(i);

                                        String i_sid = objPrescriptions.getString("i_sid");
                                        String i_id = objPrescriptions.getString("i_id");
                                        String inc_sub = objPrescriptions.getString("inc_sub");

                                        subCategoryHashMap.put(i_sid, inc_sub);
                                        cat_subHashMap.put(i_sid, i_id);
                                    }


                                    TableRow tableRowHeader = new TableRow(getApplicationContext());
                                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.topMargin = 2;
                                    layoutParams.rightMargin = 2;

                                    TextView textViewLabel = new TextView(getApplicationContext());
                                    textViewLabel.setLayoutParams(layoutParams);
                                    textViewLabel.setBackgroundColor(Color.WHITE);
                                    textViewLabel.setPadding(20, 20, 20, 20);
                                    textViewLabel.setText("LABEL");
                                    tableRowHeader.addView(textViewLabel);

                                    for (int i = 0; i < keyArrayList.size(); i++) {


                                        TextView textViewinvdate = new TextView(getApplicationContext());
                                        textViewinvdate.setLayoutParams(layoutParams);
                                        textViewinvdate.setBackgroundColor(Color.WHITE);
                                        textViewinvdate.setPadding(20, 20, 20, 20);
                                        textViewinvdate.setText(keyArrayList.get(i));


                                        tableRowHeader.addView(textViewinvdate);
                                    }

                                    _mainInvestigationLayout.addView(tableRowHeader);


                                    for (int i = 0; i < valueArrayList.size(); i++) {

                                        String[] output = valueArrayList.get(i).split(",");
                                        Log.d("output", Arrays.toString(output));

                                        for (int j = 0; j < output.length; j++) {

                                            String[] subIdValue = output[j].split(":");
                                            String subId = subIdValue[0];
                                            String subValue = subIdValue[1];

                                            if (!trackKeyArrayList.contains(subId)) {

                                                trackKeyArrayList.add(subId);
                                                keyvalueHashMap2.put(subId + "." + i, subValue);

                                            } else {
                                                keyvalueHashMap2.put(subId + "." + i, subValue);

                                            }
                                        }

                                    }


                                 /*
                                  for (String name : keyvalueHashMap2.keySet()) {
                                        String key = name.toString();
                                        String value = keyvalueHashMap2.get(name).toString();
                                        Log.d("keyvalueHashMap2", key + " " + value);
                                    }
                                    */


                                    for (int i = 0; i < trackKeyArrayList.size(); i++) {


                                       String cat_id= cat_subHashMap.get(trackKeyArrayList.get(i));

                                        TableRow tableRowCategory = new TableRow(getApplicationContext());

                                        TextView textViewCategory = new TextView(getApplicationContext());
                                        textViewCategory.setLayoutParams(layoutParams);
                                        textViewCategory.setBackgroundColor(Color.LTGRAY);
                                        textViewCategory.setPadding(20, 20, 20, 20);
                                        textViewCategory.setText(categoryHashMap.get(cat_id));
                                        tableRowCategory.addView(textViewCategory);



                                        TableRow tableRowBody = new TableRow(getApplicationContext());
                                        TextView textViewLabelValue = new TextView(getApplicationContext());
                                        textViewLabelValue.setLayoutParams(layoutParams);
                                        textViewLabelValue.setBackgroundColor(Color.WHITE);
                                        textViewLabelValue.setPadding(20, 20, 20, 20);
                                        textViewLabelValue.setText(subCategoryHashMap.get(trackKeyArrayList.get(i)));
                                        tableRowBody.addView(textViewLabelValue);


                                        for (int j = 0; j < trackKeyArrayList.size(); j++) {

                                            TextView textViewCategoryValue = new TextView(getApplicationContext());
                                            textViewCategoryValue.setLayoutParams(layoutParams);
                                            textViewCategoryValue.setBackgroundColor(Color.LTGRAY);
                                            textViewCategoryValue.setPadding(20, 20, 20, 20);
                                            textViewCategoryValue.setText("");
                                            tableRowCategory.addView(textViewCategoryValue);

                                            String test = keyvalueHashMap2.get(trackKeyArrayList.get(i) + "." + j);

                                            TextView textViewinvvalues = new TextView(getApplicationContext());
                                            textViewinvvalues.setLayoutParams(layoutParams);
                                            textViewinvvalues.setBackgroundColor(Color.WHITE);
                                            textViewinvvalues.setPadding(20, 20, 20, 20);
                                            textViewinvvalues.setText(test);
                                            tableRowBody.addView(textViewinvvalues);




                                            Log.d("test", trackKeyArrayList.get(i) + "-" + test);

                                        }

                                        _mainInvestigationLayout.addView(tableRowCategory);

                                        _mainInvestigationLayout.addView(tableRowBody);

                                    }

                                    Log.d("trackKeyArrayList", Arrays.toString(trackKeyArrayList.toArray()));

                                    for (String name : categoryHashMap.keySet()) {
                                        String key = name.toString();
                                        String value = categoryHashMap.get(name).toString();
                                        Log.d("categoryHashMap", key + " " + value);
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


    /*
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
                                        }*/

   /* public void investigation() throws JSONException {
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

    }*/


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
