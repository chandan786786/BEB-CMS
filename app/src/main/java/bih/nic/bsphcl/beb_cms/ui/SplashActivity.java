package bih.nic.bsphcl.beb_cms.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bih.nic.bsphcl.beb_cms.R;
import bih.nic.bsphcl.beb_cms.database.DataBaseHelper;
import bih.nic.bsphcl.beb_cms.entities.ActivityGroup;
import bih.nic.bsphcl.beb_cms.servises.ActivityGroupLoader;
import bih.nic.bsphcl.beb_cms.utilities.CommonPref;

public class SplashActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 10;
    TextView text_head_sp;
    LinearLayout ll_sp;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        text_head_sp = (TextView) findViewById(R.id.splash_text);
        ll_sp = (LinearLayout) findViewById(R.id.ll_sp);
        //text_ver = (TextView) findViewById(R.id.text_imei);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/header_font.ttf");
        text_head_sp.setTypeface(face);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Call some material design APIs here
            if (checkAndRequestPermissions()) {
                init2();
            }

        } else {
            // Implement this feature without material design
            init2();
        }
    }

    private boolean checkAndRequestPermissions() {

        int read_phone = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int read_external = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (read_external != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (read_phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //init();
                    init2();
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(this, "Please Enable All permissions !", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void init2() {
        DataBaseHelper db = new DataBaseHelper(this);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;

        }
        if (CommonPref.getCheckUpdate(SplashActivity.this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }, 2800);
        }else{
            ll_sp.setVisibility(View.GONE);
            new ActivityGroupLoader(SplashActivity.this).execute();
        }
    }
}
