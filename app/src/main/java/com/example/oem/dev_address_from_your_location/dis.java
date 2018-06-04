package com.example.oem.dev_address_from_your_location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class dis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis);
        TextView tv1,tv2,tv3,tv4;
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv1.setText(MapsActivity.postalcode);
        tv2.setText(MapsActivity.phonecode);
        tv3.setText(MapsActivity.Countrycode);
        tv4.setText(MapsActivity.daddress);
}
}
