package com.kit.backpackers.project_kit.UserRegistration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kit.backpackers.project_kit.R;

public class RegistrationActivity extends AppCompatActivity {
private RadioGroup radioGroup;
    TextView poi_name,poi_lat,poi_long;
    private RadioButton radioButton;
    Button add_poi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        poi_name=(TextView)findViewById(R.id.poi_name);
        poi_lat=(TextView)findViewById(R.id.poi_lat);
        poi_long=(TextView)findViewById(R.id.poi_long);
        add_poi=(Button)findViewById(R.id.btn_add_poi);
        addListenerOnButton();
    }
    public void addListenerOnButton() {




    }
}
