package me.alin.passtore;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class AccountAdapter extends
        RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private DBHandler dbHandler;
    private final List<AccountModel> mAccounts;
    private final List<AccountModel> mAccountsCopy;

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String text) {
        mAccounts.clear();
        if (text.isEmpty()) {
            mAccounts.addAll(mAccountsCopy);
        } else {
            text = text.toLowerCase(Locale.ROOT);
            for (AccountModel account : mAccountsCopy) {
                if (account.service.toLowerCase(Locale.ROOT).contains(text) || account.username.toLowerCase(Locale.ROOT).contains(text)) {
                    mAccounts.add(account);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Pass in the contact array into the constructor
    public AccountAdapter(List<AccountModel> accounts) {
        mAccounts = accounts;
        mAccountsCopy = new ArrayList<>();
        mAccountsCopy.addAll(accounts);
    }

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        dbHandler = new DBHandler(context);
        View courseView = inflater.inflate(R.layout.account_view, parent, false);

        return new ViewHolder(courseView, context);
    }


    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccountModel account = mAccounts.get(position);
        TextView serviceNameView = holder.serviceNameView;
        TextView usernameView = holder.usernameView;
        TextView passwordView = holder.passwordView;
        Button removeAccountBtn = holder.removeAccountBtn;


        serviceNameView.setText(account.service);
        usernameView.setText(account.username);
        passwordView.setText(account.password);
        removeAccountBtn.setOnClickListener(view -> {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
            dialogBuilder.setTitle("Permanently remove?").setNeutralButton("CANCEL", ((dialogInterface, i) ->
                    dialogInterface.cancel())).setPositiveButton("OKAY", (((dialogInterface, i) -> {
                dbHandler.removeAccount(account.getId());
                this.notifyItemRemoved(mAccounts.indexOf(account));
                mAccounts.remove(account);

            }))).show();


        });
    }

    @Override
    public int getItemCount() {
        return mAccounts.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ConstraintLayout container;
        Button removeAccountBtn;
        TextView serviceNameView;
        TextView usernameView;
        TextView passwordView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, Context context) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            container = itemView.findViewById(R.id.account_view_container);
            serviceNameView = itemView.findViewById(R.id.serviceNameView);
            usernameView = itemView.findViewById(R.id.usernameView);
            passwordView = itemView.findViewById(R.id.passwordView);
            removeAccountBtn = itemView.findViewById(R.id.removeAccountBtn);
            passwordView.setOnClickListener(view -> {
                ClipboardManager clipboard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", passwordView.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Password copied into clipboard!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}