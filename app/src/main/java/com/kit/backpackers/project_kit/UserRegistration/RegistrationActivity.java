package com.kit.backpackers.project_kit.UserRegistration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kit.backpackers.project_kit.R;

public class RegistrationActivity extends AppCompatActivity {
private RadioGroup radioGroup;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        radioGroup=(RadioGroup)findViewById(R.id.radioGender);



    }
}
