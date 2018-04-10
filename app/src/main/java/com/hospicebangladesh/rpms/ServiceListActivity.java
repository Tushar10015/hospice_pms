package com.hospicebangladesh.rpms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hospicebangladesh.rpms.utils.Session;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ServiceListActivity extends AppCompatActivity {

    private static final String TAG = "ServiceListActivity";

    @Bind(R.id.buttonDoctorVisit)
    Button _buttonDoctorVisit;

    @Bind(R.id.buttonNursingSupport)
    Button _buttonNursingSupport;

    @Bind(R.id.buttonInstrumentRent)
    Button _buttonInstrumentRent;

    @Bind(R.id.buttonMedicalProcedure)
    Button _buttonMedicalProcedure;

    @Bind(R.id.buttonAlliedHelthSupport)
    Button _buttonAlliedHelthSupport;


    @Bind(R.id.buttonLabSupport)
    Button _buttonLabSupport;

    @Bind(R.id.buttonAmbulance)
    Button _buttonAmbulance;

    @Bind(R.id.buttonPatientCatering)
    Button _buttonPatientCatering;


    @Bind(R.id.buttonHospiceEstore)
    Button _buttonHospiceEstore;

    @Bind(R.id.buttonTelecare)
    Button _buttonTelecare;



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

        _buttonMedicalProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",3);
                startActivity(intent);
            }
        });


        _buttonAlliedHelthSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",4);
                startActivity(intent);
            }
        });


        _buttonLabSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",5);
                startActivity(intent);
            }
        });


        _buttonAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",6);
                startActivity(intent);
            }
        });

        _buttonPatientCatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",7);
                startActivity(intent);
            }
        });


        _buttonHospiceEstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://hospicebangladesh.com/hospice-e-store/"));
                startActivity(browserIntent);

               /* Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",8);
                startActivity(intent);*/
            }
        });


        _buttonTelecare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri number = Uri.parse("tel:09666788887");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);

              /*  Intent intent=new Intent(getApplicationContext(),ServicesActivity.class);
                intent.putExtra("index",9);
                startActivity(intent);*/
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
