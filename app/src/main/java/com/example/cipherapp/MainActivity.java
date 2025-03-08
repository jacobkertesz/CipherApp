package com.example.cipherapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    private String textIntake;
    private TextView passcodeBox;
    private TextView inBox;
    private TextView outBox;
    private Character[] characters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','1','2','3','4','5','6','7','8','9','0',' ','-'};
    private String passcode = "-";
    private Switch settingToggle;
    private Boolean setting = Boolean.FALSE;

    TextWatcher passcodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            passcode = passcodeBox.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textIntake = inBox.getText().toString();
            String modifiedText = modifyText(textIntake, setting);
            outBox.setText(modifiedText);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String modifyText(String text, Boolean setting)
    {
        String result =  "";

        for (int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == '\n')
            {
                result += text.charAt(i);
            }
            else if (setting == Boolean.FALSE) // encryption
            {
                Character c = Character.toLowerCase(text.charAt(i));

                int baseI = findIndex(c);
                int settingI = findIndex(passcode.charAt(i % passcode.length())) + 1;

                int encryptI = (baseI + settingI) % characters.length;
                result += characters[encryptI];
            }
            else if (setting == Boolean.TRUE) // decryption
            {
                int baseI = findIndex(text.charAt(i));
                int passcodeI = findIndex(passcode.charAt(i % passcode.length())) + 1;

                int decryptI = (baseI - passcodeI + characters.length) % characters.length;
                result += characters[decryptI];
            }
        }
        return result;
    }
    private int findIndex(Character target)
    {
        int output = 37;
        for (int i = 0; i < characters.length; i++)
        {
            if (target == characters[i])
            {
                output = i;
            }
        }
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        passcodeBox = findViewById(R.id.editTextText);
        inBox = findViewById(R.id.editTextTextMultiLine);
        outBox = findViewById(R.id.editTextTextMultiLine2);
        settingToggle = findViewById(R.id.switch1);

        passcodeBox.addTextChangedListener(passcodeWatcher);
        inBox.addTextChangedListener(inputWatcher);

        settingToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setting = isChecked ? Boolean.TRUE : Boolean.FALSE;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}