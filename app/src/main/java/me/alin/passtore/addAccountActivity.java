package me.alin.passtore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class addAccountActivity extends AppCompatActivity {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focus = getCurrentFocus();
        if (focus != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
            if (!focus.equals(serviceNameInputContainer) || !focus.equals(serviceNameInput)) {
                serviceNameInputContainer.clearFocus();
            }
            if (!focus.equals(usernameInputContainer) || !focus.equals(usernameInput)) {
                usernameInputContainer.clearFocus();
            }
            if (!focus.equals(passwordInputContainer) || !focus.equals(passwordInput)) {
                passwordInputContainer.clearFocus();
            }

        }

        return super.dispatchTouchEvent(ev);
    }

    SwitchMaterial genPassSwitch;
    TextInputLayout serviceNameInputContainer;
    TextInputEditText serviceNameInput;
    TextInputLayout usernameInputContainer;
    TextInputEditText usernameInput;
    TextInputLayout passwordInputContainer;
    TextInputEditText passwordInput;
    Slider passwordLengthInput;
    MaterialCheckBox checkBoxLower;
    MaterialCheckBox checkBoxUpper;
    MaterialCheckBox checkBoxSpecial;
    MaterialCheckBox checkBoxNumber;
    Button generatePasswordBtn;
    TextView passwordLengthLabel;
    FloatingActionButton addAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        setupViews();
        passwordLengthInput.setValueFrom(4.0f);
        handlePasswordLengthMin();
        genPassSwitch.setOnCheckedChangeListener((view, isChecked) -> toggleGenPassVisibility(isChecked));
        addAccountBtn.setOnClickListener(view -> addAccountHandler());
        generatePasswordBtn.setOnClickListener(view -> generatePassword());
        setupInputErrorHandling();
    }

    void generatePassword() {
        PasswordGenerator.Builder builder = new PasswordGenerator.Builder();
        if (checkBoxSpecial.isChecked()) {
            builder = builder.special(1);
        }
        if (checkBoxUpper.isChecked()) {
            builder = builder.upper(1);
        }
        if (checkBoxLower.isChecked()) {
            builder = builder.lower(1);
        }
        if (checkBoxNumber.isChecked()) {
            builder = builder.digits(1);
        }
        passwordInput.setText(builder.build().generate((int) passwordLengthInput.getValue()));
    }


    void setMinPasswordLength(boolean isChecked) {
        if (isChecked) {
            passwordLengthInput.setValue(passwordLengthInput.getValueFrom() + 1.0f);
            passwordLengthInput.setValueFrom(passwordLengthInput.getValueFrom() + 1.0f);
        } else {
            passwordLengthInput.setValueFrom(passwordLengthInput.getValueFrom() - 1.0f);
        }
    }

    void handlePasswordLengthMin() {
        checkBoxLower.setOnCheckedChangeListener((view, isChecked) -> setMinPasswordLength(isChecked));
        checkBoxUpper.setOnCheckedChangeListener((view, isChecked) -> setMinPasswordLength(isChecked));
        checkBoxSpecial.setOnCheckedChangeListener((view, isChecked) -> setMinPasswordLength(isChecked));
        checkBoxNumber.setOnCheckedChangeListener((view, isChecked) -> setMinPasswordLength(isChecked));
    }

    void addAccountHandler() {
        if (serviceNameInput.getText() == null) {
            serviceNameInputContainer.setError("Service name needs to be set!");
            return;
        }
        if (usernameInput.getText() == null) {
            usernameInputContainer.setError("Username needs to be set!");
            return;
        }
        if (passwordInput.getText() == null) {
            passwordInputContainer.setError("Password needs to be set!");
            return;
        }
        if (serviceNameInput.getText().toString().trim().length() == 0) {
            serviceNameInputContainer.setError("Service name needs to be set!");
            return;
        }
        if (usernameInput.getText().toString().trim().length() == 0) {
            usernameInputContainer.setError("Username needs to be set!");
            return;
        }
        if (passwordInput.getText().toString().trim().length() == 0) {
            passwordInputContainer.setError("Password needs to be set!");
            return;
        }
        String service = serviceNameInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra("service", service);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }

    void toggleGenPassVisibility(boolean visibility) {
        checkBoxLower.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        checkBoxUpper.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        checkBoxSpecial.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        checkBoxNumber.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        passwordLengthInput.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        generatePasswordBtn.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        passwordLengthLabel.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    void setupViews() {
        genPassSwitch = findViewById(R.id.genPassSwitch);
        serviceNameInputContainer = findViewById(R.id.serviceNameInputContainer);
        serviceNameInput = findViewById(R.id.serviceNameInput);
        usernameInputContainer = findViewById(R.id.usernameInputContainer);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInputContainer = findViewById(R.id.passwordInputContainer);
        passwordInput = findViewById(R.id.passwordInput);
        checkBoxLower = findViewById(R.id.checkBoxLower);
        checkBoxUpper = findViewById(R.id.checkBoxUpper);
        checkBoxSpecial = findViewById(R.id.checkBoxSpecial);
        checkBoxNumber = findViewById(R.id.checkBoxNumber);
        addAccountBtn = findViewById(R.id.addAccountBtn);
        passwordLengthInput = findViewById(R.id.passwordLengthInput);
        generatePasswordBtn = findViewById(R.id.generatePasswordBtn);
        passwordLengthLabel = findViewById(R.id.passwordLengthLabel);
    }

    void setupInputErrorHandling() {
        serviceNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    serviceNameInputContainer.setError("Service name needs to be set!");
                } else {
                    serviceNameInputContainer.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    usernameInputContainer.setError("Username needs to be set!");
                } else {
                    usernameInputContainer.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    passwordInputContainer.setError("Password needs to be set!");
                } else {
                    passwordInputContainer.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}