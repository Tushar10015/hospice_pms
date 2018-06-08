package com.hospicebangladesh.rpms;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hospicebangladesh.rpms.http.HttpRequest;
import com.hospicebangladesh.rpms.http.HttpRequestCallBack;
import com.hospicebangladesh.rpms.model.Profile;
import com.hospicebangladesh.rpms.utils.Session;
import com.hospicebangladesh.rpms.utils.SessionManager;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {

    private static final String TAG = "SignupActivity";
    public String signupPostUrl = "signup.php";

    String gender = null, age = null;


    @Bind(R.id.input_name)
    EditText _nameText;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_mobile)
    EditText _mobileText;
    @Bind(R.id.input_age)
    LabelledSpinner _ageSpinner;
    @Bind(R.id.input_gender)
    LabelledSpinner _genderSpinner;

    @Bind(R.id.chk_terms)
    CheckBox _chk_terms;
    @Bind(R.id.input_address)
    EditText _addressText;

    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;

    @Bind(R.id.input_nid)
    EditText _nidText;


    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        initializeSpinner();

     //   setSignup();


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (JSONException e) {

                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });




        _chk_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {

                    AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
                    alertDialog.setTitle("সেবা প্রদানের শর্তাবলী");
                    alertDialog.setMessage("সেবা ব্যবহার করার পূর্বে অনুগ্রহ করে এইসব শর্তাবলী মনযোগ সহকারে পড়ুন।\n" +
                            "\n" +
                            " শর্তাবলী এবং নিয়মাবলী\n" +
                            "১. আমাদের সেবাতে প্রবেশ করে বা ব্যবহার করে আপনি এইসব শর্তাবলী মেনে চলতে বাধ্য থাকতে সম্মত হচ্ছেন এবং এখানে বর্নিত শর্ত ও নিয়মাবলী অনুযায়ী আমাদের সেবা ব্যবহার করতে সম্মত হয়েছেন। যদি আপনি এইসব শর্তাবলীতে সম্মত না হন তাহলে আপনি সেবা ব্যবহার থেকে বিরত থাকুন।\n" +
                            "২. যদি আমাদের এইসব শর্তাবলী পরিবর্তন করার প্রয়োজন হয় তাহলে আমরা সংশোধিত শর্তাবলী আমাদের সেবার মাধ্যমে পোষ্ট করবো এবং সর্বশেষ সংশোধিত তারিখ হালনাগাদ করবো যাতে করে এসএমএস, ই-মেইল বা একটি নোটিশ পোষ্টিং করার মাধ্যমে ওয়েবসাইটের উপরে বা অন্যকোন লিখিত যোগাযোগের মাধ্যমে তারিখটি প্রতিফলিত হবে। আপনি নিজেই এইসব শর্তাবলীতে কোন পরিবর্তন সম্পর্কে সচেতন থাকার জন্য দায়ী।\n" +
                            "৩. আমাদের সেবার জন্য রেজিস্ট্রেশন করার মাধ্যমে আপনি এটা গ্রহন এবং স্বীকার করেন যে, সেবাসমূহ যা আমরা প্রদান করতে পারি সে সম্পর্কে আমরা আপনার সাথে বিভিন্ন সময়ে যোগাযোগ করতে পারি। \n" +
                            "৪. আমরা শুধুমাত্র প্যালিয়েটিভ রোগীদের চিকিৎসা সংক্রান্ত পরামর্শ প্রদান করে থাকি।  \n" +
                            "৫. আপনি সম্মত হচ্ছেন যে সেবাসমুহ লাভ করা সম্পর্কে আপনার প্রদত্ত সকল তথ্য সত্য, নির্ভূল, আপডেট এবং পরিপূর্ণ এবং আপনি সম্মত হচ্ছেন যে এজাতীয় তথ্য যখন যেভাবে প্রয়োজন হয় তখন দ্রুততার সাথে এটা আপডেট রাখার জন্য আমাদেরকে দ্রুততার সাথে জানাবেন। যদি আমাদের যৌক্তিকভাবে বিশ্বাস করার কারন থাকে যে, এজাতীয় তথ্য অসত্য, ভুল বা অসম্পুর্ন তাহলে সেবা সাময়িকভাবে বন্ধ আমাদের অধিকার আছে।\n" +
                            "৬. আমাদের সেবাটি সম্পূর্ণই প্যালিয়াটিভ সেবা যা রোগকে সারাতে কোন পদক্ষেপ গ্রহণ করবে না কিন্তু রোগের উপসর্গগুলোকে প্রশমিত করে আপানার সর্বোচ্চ প্রশান্তি নিশ্চিত করার চেষ্টা করবে। এজন্য প্রয়োজনীয় টেলিমেডিসিন’ সেবা সহ নার্সিং সেবা ও নিয়মিত ডাক্তার হোম ভিজিট প্রদান করা হবে। \n" +
                            "৭. যেহেতু প্যালিয়াটিভ সেবা রোগকে সারানো, রোগকে ক্ষণস্থায়ী করা বা দীর্ঘায়িত করেনা তাই  সেবা প্রদানের সময় রোগীর শারীরিক অবস্থার অবনতি বা রোগীর মৃত্যুর জন্য কর্তৃপক্ষ তথা সংশ্লিষ্ট ডাক্তার দায়ী থাকবেনা।  \n" +
                            "৮. হসপিস বাংলাদেশ হতে প্রদানকৃত নার্স/ নার্সিং এইড চুক্তির মেয়াদকালে তার নির্দিষ্ট পরিমাণ কার্যবিবরনীর বাহিরে কোন প্রকার গৃহস্থালি কাজে ব্যবহার করা যাবেনা এবং কোনপ্রকার বকশিস প্রদান করা যাবে না। হসপিস বাংলাদেশ নার্সেরা প্রত্যেকেই প্যালিয়াটিভ ট্রেনিং প্রাপ্ত। সুতরাং তাদেরকে তাদের প্রাপ্য সম্মান প্রদর্শন করতে হবে।  \n" +
                            "৯. হসপিস বাংলাদেশের সাথে চুক্তি কালীন সময়ে প্রাইমারী চিকিৎসকবৃন্দ ও হসপিস ডাক্তারের সম্মিলিত প্রচেষ্টাতেই রোগীর রোগের উপসর্গের সেবা নিশ্চিত হবে। \n" +
                            "১০. সেবা সমূহের চার্জ সম্পর্কে আপনি সম্পূর্ণ অবগত আছেন। \n" +
                            "১১. আপনার স্বাস্থ্যসেবার প্রয়োজনে কর্তৃপক্ষ কর্তৃক প্রদানকৃত যেকোনো প্রয়োজনীয় সেবা আপনি সম্পূর্ণ স্বেচ্ছায় গ্রহণ করতে সম্মত। \n\n" +
                            "উপরোক্ত তথ্যগুলো মেনে নিয়েই আমি হসপিস বাংলাদেশের সেবা গ্রহনের চুক্তি স্বাক্ষর করলাম। \n");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                    _chk_terms.setError(null);
                }
            }
        });



    }






    public void initializeSpinner() {

        String[] genderList = new String[]{"Select Gender", "Male", "Female"};

        String[] ageList = new String[100];

        ageList[0]="Select Age";
        for (int i = 1; i < 100; i++) {
            ageList[i] = (i+1) + "";
        }

        _genderSpinner.setItemsArray(genderList);
        _genderSpinner.setOnItemChosenListener(this);

        _ageSpinner.setItemsArray(ageList);
        _ageSpinner.setOnItemChosenListener(this);

    }

    private void setSignup() {

        _nameText.setText("Md. Developer");
        _emailText.setText("saniultushar@gmail.com");
        _mobileText.setText("01748702672");
        _genderSpinner.setSelection(1);
        _ageSpinner.setSelection(1);
        _passwordText.setText("123456");
        _reEnterPasswordText.setText("123456");
        _addressText.setText("test");

    }


    public void signup() throws JSONException {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();

        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String address = _addressText.getText().toString();
        String nid = _nidText.getText().toString();


        Profile profile = new Profile();
        profile.setName(name);
        profile.setEmail(email);
        profile.setMobile(mobile);
        profile.setPassword(password);
        profile.setGender(gender);
        profile.setAge(age);
        profile.setAddress(address);
        profile.setNid(nid);

        final List<Profile> list = new ArrayList<>();
        list.add(profile);

        JSONObject postBody = new JSONObject();
        postBody.put("name", name);
        postBody.put("email", email);
        postBody.put("mobile", mobile);
        postBody.put("password", password);
        postBody.put("gender", gender);
        postBody.put("age", age);
        postBody.put("address", address);
        postBody.put("nid", nid);

        try {
            HttpRequest.postRequest(signupPostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    SignupActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");

                                if (success == 1) {
                                //    ProfileRepository.insert(getApplicationContext(), list);
                                    int insertid = json.getInt("insertid");
                                    String status=json.getString("status");
                                    String message=json.getString("message");
                                    onSignupSuccess(insertid,status,message);
                                    Session.savePreference(getApplicationContext(),Session.name,name);
                                    progressDialog.dismiss();
                                } else {
                                    onSignupFailed();
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
                    SignupActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                            _signupButton.setEnabled(true);
                            progressDialog.dismiss();
                            Log.d(TAG, " onFail");
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


 /*new android.os.Handler().postDelayed(
                new Runnable() {
        public void run() {
            // On complete call either onSignupSuccess or onSignupFailed
            // depending on success
            onSignupSuccess();
            // onSignupFailed();
            progressDialog.dismiss();
        }
    }, 3000);*/


    }


    public void onSignupSuccess(int insertid,String status,String message) {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        SessionManager sessionManager=new SessionManager(insertid,mobile,password);
        Session.savePreference(getApplicationContext(),sessionManager);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("At Least 3 Characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (gender.equalsIgnoreCase("Select Gender")) {
            TextView errorText = (TextView) this._genderSpinner.getSpinner().getSelectedView();
            errorText.setError("Enter Valid Gender");
            valid = false;
        } else {
            TextView errorText = (TextView) this._genderSpinner.getSpinner().getSelectedView();
            errorText.setError(null);
        }

        if (age.equalsIgnoreCase("Select Age")) {
            TextView errorText = (TextView) this._ageSpinner.getSpinner().getSelectedView();
            errorText.setError("Enter Valid Age");
            valid = false;
        } else {
            TextView errorText = (TextView) this._ageSpinner.getSpinner().getSelectedView();
            errorText.setError(null);
        }

       /* if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 Alphanumeric Characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do Not Match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        if (!_chk_terms.isChecked()) {
            _chk_terms.setError("Please check terms and condition");
            valid = false;
        } else {
            _chk_terms.setError(null);
        }


        return valid;
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        switch (labelledSpinner.getId()) {
            case R.id.input_gender:
                gender = (String) adapterView.getItemAtPosition(position);
                break;
            case R.id.input_age:
                age = (String) adapterView.getItemAtPosition(position);
                break;

        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

    }
}
