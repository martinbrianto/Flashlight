package com.example.flashlight;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private ImageButton btn;
    private boolean FlashOn;

    private CameraManager mCameraManager;
    private String mCameraId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.imageSwitch);

        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!isFlashAvailable) {
            showNoFlashError();

        }

        //getting the camera manager and camera id
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        toggleButtonImage();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FlashOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });
    }



    private void turnOnFlash() {
        if (!FlashOn) {
            try {
                mCameraManager.setTorchMode(mCameraId,true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            FlashOn = true;

            // changing button/switch image
            toggleButtonImage();
        }

    }

    private void turnOffFlash() {
        if (FlashOn) {
            try {
                mCameraManager.setTorchMode(mCameraId,false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            FlashOn = false;

            // changing button/switch image
            toggleButtonImage();
        }
    }

    private void toggleButtonImage() {
        if (FlashOn) {
            btn.setImageResource(R.drawable.btn_on);
        } else {
            btn.setImageResource(R.drawable.btn_off);
        }
    }

    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }
}
