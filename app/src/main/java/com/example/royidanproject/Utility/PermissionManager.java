package com.example.royidanproject.Utility;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.royidanproject.R;

public final class PermissionManager implements ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {
    private static final int LAYOUT_ID = R.layout.custom_request_permissions;

    private static final int SMS = 1;
    private static final int CAMERA = 2;
    private static final int READ = 3;
    private static final int WRITE = 4;

    private static final String[] PERMISSION_NAMES = new String[]{SEND_SMS,
            Manifest.permission.CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    private AppCompatActivity activity;
    private Dialog dialog;

    private boolean[] grantedPermissions;

    private LinearLayout llPrmSmsDenied, llPrmSmsGranted,
            llPrmCameraDenied, llPrmCameraGranted,
            llPrmReadDenied, llPrmReadGranted,
            llPrmWriteDenied, llPrmWriteGranted;

    private Button btnCheck, btnClose;

    private static String getPermissionName(int permission) {
        return PERMISSION_NAMES[permission - 1];
    }

//    private boolean checkAllPermissions() {
//        return checkPermission(SMS) &&
//                checkPermission(CAMERA) &&
//                checkPermission(READ) &&
//                checkPermission(WRITE);
//    }


    private void setViewPointers() {
        llPrmSmsDenied = dialog.findViewById(R.id.llPrmSmsDenied);
        llPrmSmsGranted = dialog.findViewById(R.id.llPrmSmsGranted);

        llPrmCameraDenied = dialog.findViewById(R.id.llPrmCameraDenied);
        llPrmCameraGranted = dialog.findViewById(R.id.llPrmCameraGranted);

        llPrmReadDenied = dialog.findViewById(R.id.llPrmReadDenied);
        llPrmReadGranted = dialog.findViewById(R.id.llPrmReadGranted);

        llPrmWriteDenied = dialog.findViewById(R.id.llPrmWriteDenied);
        llPrmWriteGranted = dialog.findViewById(R.id.llPrmWriteGranted);

        btnCheck = dialog.findViewById(R.id.btnCheck);
        btnClose = dialog.findViewById(R.id.btnClose);
    }

    private void setButtonsListener() {
        dialog.findViewById(R.id.btnPrmSms).setOnClickListener(this);
        dialog.findViewById(R.id.btnPrmCamera).setOnClickListener(this);
        dialog.findViewById(R.id.btnPrmRead).setOnClickListener(this);
        dialog.findViewById(R.id.btnPrmWrite).setOnClickListener(this);
        dialog.findViewById(R.id.btnClose)
                .setOnClickListener(v -> dialog.dismiss());
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixArray();
                fixLayout();
            }
        });
    }


    public PermissionManager(AppCompatActivity activity) {
        this.activity = activity;

        grantedPermissions = new boolean[]{false, false, false, false};

        fixArray();

        if (allGranted()) {
            return;
        }

        dialog = buildDialog();

        setViewPointers();

        setButtonsListener();

        fixLayout();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixArray();
                fixLayout();
            }
        });
    }

    private void fixArray() {
        grantedPermissions[0] = checkPermission(SMS);
        grantedPermissions[1] = checkPermission(CAMERA);
        grantedPermissions[2] = checkPermission(READ);
        grantedPermissions[3] = checkPermission(WRITE);
    }

    private void fixLayout() {
        changeLayout(SMS, grantedPermissions[0]);
        changeLayout(CAMERA, grantedPermissions[1]);
        changeLayout(READ, grantedPermissions[2]);
        changeLayout(WRITE, grantedPermissions[3]);
        btnClose.setEnabled(allGranted());
    }

    private boolean allGranted() {
        return grantedPermissions[0] &&
                grantedPermissions[1] &&
                grantedPermissions[2] &&
                grantedPermissions[3];
    }

    private void changeLayout(int requestCode, boolean isGranted) {
        if (isGranted) {
            switch (requestCode) {
                case SMS:
                    llPrmSmsDenied.setVisibility(View.GONE);
                    llPrmSmsGranted.setVisibility(View.VISIBLE);
                    break;
                case CAMERA:
                    llPrmCameraDenied.setVisibility(View.GONE);
                    llPrmCameraGranted.setVisibility(View.VISIBLE);
                    break;
                case READ:
                    llPrmReadDenied.setVisibility(View.GONE);
                    llPrmReadGranted.setVisibility(View.VISIBLE);
                    break;
                case WRITE:
                    llPrmWriteDenied.setVisibility(View.GONE);
                    llPrmWriteGranted.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            switch (requestCode) {
                case SMS:
                    llPrmSmsGranted.setVisibility(View.GONE);
                    llPrmSmsDenied.setVisibility(View.VISIBLE);
                    break;
                case CAMERA:
                    llPrmCameraGranted.setVisibility(View.GONE);
                    llPrmCameraDenied.setVisibility(View.VISIBLE);
                    break;
                case READ:
                    llPrmReadGranted.setVisibility(View.GONE);
                    llPrmReadDenied.setVisibility(View.VISIBLE);
                    break;
                case WRITE:
                    llPrmWriteGranted.setVisibility(View.GONE);
                    llPrmWriteDenied.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void onPermissionGranted(int requestCode) {
        grantedPermissions[requestCode - 1] = true;
        changeLayout(requestCode, true);
        btnClose.setEnabled(allGranted());
    }

    private void onPermissionDenied(int requestCode) {
        toast("לא אישרת את ההרשאה: " + getPermissionName(requestCode));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Request Codes:
        // 1 - SMS ; 2 - CAMERA ; 3 - READ ; 4 - WRITE
        if (!(permissions.length > 0 && grantResults.length > 0)) {
            onPermissionDenied(requestCode);
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(requestCode);
        } else {
            onPermissionDenied(requestCode);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPrmSms) {
            getPermission(SMS);
        } else if (v.getId() == R.id.btnPrmCamera) {
            getPermission(CAMERA);
        } else if (v.getId() == R.id.btnPrmRead) {
            getPermission(READ);
        } else if (v.getId() == R.id.btnPrmWrite) {
            getPermission(WRITE);
        }
    }

    private void getPermission(int permission) {
        String perm = getPermissionName(permission);
        ActivityCompat.requestPermissions(activity, new String[]{
                perm
        }, permission);
    }

    private boolean checkPermission(int permission) {
        String perm = getPermissionName(permission);
        int result = ContextCompat.checkSelfPermission(activity, perm);

        return result == PERMISSION_GRANTED;
    }

    private Dialog buildDialog() {
        View promptDialog = LayoutInflater.from(activity).inflate(LAYOUT_ID, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    private void toast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

}
