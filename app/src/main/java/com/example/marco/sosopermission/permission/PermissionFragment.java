package com.example.marco.sosopermission.permission;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import java.util.ArrayList;

/**
 * Created by Marco on 16/12/5.
 */

public class PermissionFragment extends Fragment {

    public static final String TAG = "PermissionFragment";

    private static final int PERMISSIONS_REQUEST_CODE = 40;

    private PermissionListener mPermissionListener;

    public boolean isStatus;

    public void setPermissionListener(PermissionListener permissionListener){
        mPermissionListener = permissionListener;
    }

    private void requestPermissions(@NonNull String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isStatus = shouldShowRequestPermissionRationale(permissions[0]);
            requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_REQUEST_CODE:
                ArrayList<String> deniedPermissions = new ArrayList<>();
                if (permissions.length > 0){
                    for (int i = 0; i < permissions.length; i ++){
                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED){
                            deniedPermissions.add(permissions[i]);
                        }
                    }
                }
                if (deniedPermissions.size() > 0){
                    String[] string = new String[deniedPermissions.size()];
                    string = deniedPermissions.toArray(string);

                    if (!shouldShowRequestPermissionRationale(permissions[0]) && !isStatus){
                        doNotAskAgain(string);
                    }else {
                        denied(string);
                    }
                }else {
                    granted();
                }
                break;
        }
    }

    public void checkPermissions(String... permissions){
        if (Build.VERSION.SDK_INT < 23){
            granted();
            return;
        }
        ArrayList<String> needPermissions = new ArrayList();
        for (String permission : permissions){
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED){
                needPermissions.add(permission);
            }
        }
        if (needPermissions.isEmpty()){
            granted();
            return;
        }
        String[] string = new String[needPermissions.size()];
        needPermissions.toArray(string);
        requestPermissions(string);
    }

    public static PermissionFragment getPermissionFragment(Activity activity) {
        PermissionFragment permissionFragment = (PermissionFragment) activity.getFragmentManager().findFragmentByTag(PermissionFragment.TAG);
        if (permissionFragment == null) {
            permissionFragment = new PermissionFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(permissionFragment, PermissionFragment.TAG)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
        return permissionFragment;
    }

    private void granted(){
        if (mPermissionListener != null){
            mPermissionListener.onPermissionGranted();
        }
    }

    private void denied(String[] permissions){
        if (mPermissionListener != null){
            mPermissionListener.onPermissionDenied(permissions);
        }
    }

    private void doNotAskAgain(String[] permissions){
        if (mPermissionListener != null){
            mPermissionListener.onPermissionDoNotAskAgain(permissions);
        }
    }

}
