package me.alin.passtore;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            if (!getCurrentFocus().equals(searchInputContainer)) {
                searchInputContainer.clearFocus();
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DBHandler dbHandler;
    TextInputLayout searchInputContainer;
    TextInputEditText searchInput;
    List<AccountModel> accounts;
    AccountAdapter adapter;

    public void openAddAccountActivity() {
        Intent intent = new Intent(HomeActivity.this, addAccountActivity.class);
        ActivityResultLauncher.launch(intent /*, ActivityOptionsCompat.makeSceneTransitionAnimation(this)*/);
//        startActivityForResult(intent, Activity.RESULT_OK, ActivityOptionsCompat.makeSceneTransitionAnimation(this, searchInput, "serviceNameInputContainer").toBundle());
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    androidx.activity.result.ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d("MYDEBUG", "result code: " + result.getResultCode());
                    Intent data = result.getData();
                    assert data != null;
                    AccountModel account = new AccountModel(
                            data.getStringExtra("service"),
                            data.getStringExtra("username"),
                            data.getStringExtra("password")
                    );
                    Log.d("MYDEBUG", "account added: " + account);
                    accounts.add(account);
                    dbHandler.addAccount(account);
                    adapter.notifyItemInserted(accounts.indexOf(account));
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floating_action_button);
        dbHandler = new DBHandler(this);
        searchInputContainer = findViewById(R.id.searchInputContainer);
        searchInput = findViewById(R.id.searchInput);
        accounts = dbHandler.getAllAccounts();
        adapter = new AccountAdapter(accounts);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        floatingActionButton.setOnClickListener(view -> openAddAccountActivity());

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.filter(editable.toString());
            }
        });
    }


}