package com.example.marco.sosopermission.permission;

/**
 * Created by Marco on 16/11/24.
 */

public interface PermissionListener{

    void onPermissionGranted();

    void onPermissionDenied(String[] deniedPermissions);

    void onPermissionDoNotAskAgain(String[] deniedPermissions);
}
