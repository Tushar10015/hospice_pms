package com.hospicebangladesh.pms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

public class ServiceListActivity extends AppCompatActivity {

    private static final String TAG = "ServiceListActivity";

    @Bind(R.id.buttonDoctorVisit)
    Button _buttonDoctorVisit;

    @Bind(R.id.buttonNursingSupport)
    Button _buttonNursingSupport;

    @Bind(R.id.buttonInstrumentRent)
    Button _buttonInstrumentRent;

    @Bind(R.id.buttonPhysiotherapy)
    Button _buttonPhysiotherapy;

    @Bind(R.id.buttonNutritionist)
    Button _buttonNutritionist;


    @Bind(R.id.buttonAmbulance)
    Button _buttonAmbulance;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        _buttonDoctorVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",0);
                startActivity(intent);
            }
        });

        _buttonNursingSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",1);
                startActivity(intent);
            }
        });

        _buttonInstrumentRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",2);
                startActivity(intent);
            }
        });

        _buttonPhysiotherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",3);
                startActivity(intent);
            }
        });


        _buttonNutritionist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",4);
                startActivity(intent);
            }
        });


        _buttonAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",5);
                startActivity(intent);
            }
        });





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
