package com.example.marco.sosopermission;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.marco.sosopermission.permission.PermissionFragment;
import com.example.marco.sosopermission.permission.PermissionListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btLocation;
    private PermissionFragment mPermissionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLocation = (Button)findViewById(R.id.bt1);
        btLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                checkLocationPermission();
                break;
        }
    }

    private void checkLocationPermission(){
        if (mPermissionFragment == null){
            mPermissionFragment = PermissionFragment.getPermissionFragment(this);
        }
        mPermissionFragment.setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showToast("Granted");
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {
                showToast("Denied");
            }

            @Override
            public void onPermissionDoNotAskAgain(String[] deniedPermissions) {
                showToast("DoNotAskAgain");
            }
        });
        mPermissionFragment.checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void showToast(String text){

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

}
