package com.apps.rkanaje.otprelay;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.rkanaje.otprelay.service.RelayUser;
import com.apps.rkanaje.otprelay.service.RelayUserService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SMS = 123;

    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private TableLayout usersTableLayout;
    private RelayUserService relayUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        relayUserService = new RelayUserService(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get consent
        consent(getApplicationContext());
        // Draw UI
        draw();
    }

    private void draw() {
        phoneNumberEditText = findViewById(R.id.phNumTxt);
        emailEditText = findViewById(R.id.emlTxt);
        usersTableLayout = findViewById(R.id.userTable);
        list(null);
    }

    private void consent(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_MMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_WAP_PUSH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_MMS,
                            Manifest.permission.RECEIVE_WAP_PUSH},
                    MY_PERMISSIONS_REQUEST_SMS);
        } else {
            //accept();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        final String grant;
        if (checkPermissionGranted(requestCode, permissions, grantResults)) {
            grant = "permission granted";
        } else {
            grant = "permission not granted";
        }
        //Toast.makeText(this, grant, Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermissionGranted(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (MY_PERMISSIONS_REQUEST_SMS == requestCode) {
            // If request is cancelled, the result arrays are empty.
            return (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }

    public void saveRelayUser(View view) {
        // Get phone number and email
        final String phNum = phoneNumberEditText.getText().toString();
        final String email = emailEditText.getText().toString();

        // TODO: validate entries
        if (null == phNum || null == email || phNum.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to database
        long id = relayUserService.create(new RelayUser(phNum, email));
        Log.i("SAVE", "id: " + id);
        emailEditText.getText().clear();
        phoneNumberEditText.getText().clear();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        list(view);
    }

    public void list(View view) {
        usersTableLayout.removeAllViews();
        final List<RelayUser> relayUsers = relayUserService.getAllUsers();
        // Add header
        usersTableLayout.addView(newRow("Phone", "Email"));
        // Add rows
        int i = 1;
        for (RelayUser relayUser : relayUsers) {
            final TableRow row = newRow(relayUser.getPhone(), relayUser.getEmail());
            usersTableLayout.addView(row, i++);
        }
    }

    private TableRow newRow(String value1, String value2) {
        final TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        lp.setMargins(200, 200, 200, 200);
        row.setLayoutParams(lp);
        newCell(value1, row);
        newCell(value2, row);
        return row;
    }

    private void newCell(String value1, TableRow row) {
        final TextView phoneTxt = new TextView(this);
        phoneTxt.setText(value1);
        phoneTxt.setGravity(Gravity.CENTER);
        setCellLayout(phoneTxt);
        row.addView(phoneTxt);
    }

    private void setCellLayout(TextView view) {
        final TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;
        view.setLayoutParams(params);
    }

    public void deleteAll(View view) {
        int deleted = relayUserService.deleteAll();
        list(view);
    }
}
