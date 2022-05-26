package me.alin.passtore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focus = getCurrentFocus();
        if (focus != null) {
            if (focus.equals(masterPassInput) || focus.equals(passwordLayout)) {
                passwordLayout.setError(null);
            }

            if (!focus.equals(passwordLayout) && !focus.equals(masterPassInput)) {
                passwordLayout.clearFocus();
            }

            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }

        return super.dispatchTouchEvent(ev);
    }

    InputMethodManager imm;
    static final String MASTER_PASS_ENTRY_NAME = "MASTER";
    DBHandler dbHandler;
    EditText masterPassInput;
    Button authButton;
    TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        dbHandler = new DBHandler(this);
        masterPassInput = findViewById(R.id.masterPassInput);
        authButton = findViewById(R.id.authButton);
        passwordLayout = findViewById(R.id.masterPassInputContainer);
        authButton.setOnClickListener(view ->
        {
            String masterPassAttempt = masterPassInput.getText().toString().trim();
            if (!validateMasterPass(masterPassAttempt)) {
                if (masterPassAttempt.length() < 8) {
                    passwordLayout.setError("Password must be at least 8 characters long");
                    return;
                }
                if (!masterPassAttempt.matches(".*[A-Z]+.*")) {
                    passwordLayout.setError("Password must contain at least 1 uppercase character");
                    return;

                }
                if (!masterPassAttempt.matches(".*[a-z]+.*")) {
                    passwordLayout.setError("Password must contain at least 1 lowercase character");
                    return;

                }
                if (!masterPassAttempt.matches(".*[0-9]+.*")) {
                    passwordLayout.setError("Password must contain at least 1 number");
                    return;

                }
                if (!masterPassAttempt.matches(".*[a-z]+.*")) {
                    passwordLayout.setError("Password must contain at least 1 lowercase character");
                    return;

                }
                if (!masterPassAttempt.matches("[~`!@#$%^&*()_+\\-={\\[\\]}|;:'\",.<>/?]")) {
                    passwordLayout.setError("Password must contain at least 1 special character, eg: !, @, %, etc");
                    return;

                }
                return;
            }
            AccountModel masterPass = dbHandler.getAccount(0);
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

            if (masterPass == null) {
                dbHandler.addAccount(new AccountModel(MASTER_PASS_ENTRY_NAME, MASTER_PASS_ENTRY_NAME, masterPassAttempt));
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());

            } else if (masterPass.password.equals(masterPassAttempt)) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            } else {
                passwordLayout.setError("Wrong password");
            }
        });
    }

    boolean validateMasterPass(String attempt) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~`!@#$%^&*()_+\\-={\\[\\]}|;:'\",.<>/?]).{8,512}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(attempt);

        return matcher.matches();
    }
}
