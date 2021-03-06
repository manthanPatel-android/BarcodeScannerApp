package com.vasyERP.example.barcodescannerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



                scannerView = new ZXingScannerView(MainActivity.this);
                setContentView(scannerView);
                int currentApiVersion = Build.VERSION.SDK_INT;

                if(currentApiVersion >=  Build.VERSION_CODES.M)
                {
                    if(checkPermission())
                    {
                        Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        requestPermission();
                    }
                }




    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

        final String barcode= result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent  = new Intent(MainActivity.this,PruductDetails.class);

                Map<String,String> barcoderange = new HashMap<>();
                barcoderange.put("barcode",barcode);
                barcoderange.put("formatted","y");
                barcoderange.put("key","p8gpjo70sh50cml5eeep4qwa6syq5b");
                Call<Barcodescan_response> barcodescan_responseCall = WebService.users().barcodescanAPicall(barcoderange);
                barcodescan_responseCall.enqueue(new Callback<Barcodescan_response>() {
                    @Override
                    public void onResponse(Call<Barcodescan_response> call, Response<Barcodescan_response> response) {

                        for (int i=0;i<response.body().getProducts().size();i++)
                        {
                            String name = response.body().getProducts().get(i).getBrand();
                            String qty = "1";
                            for (int k=0;k<i;k++)
                            {
                                String price =response.body().getProducts().get(i).getStores().get(k).getPrice();
                                intent.putExtra("name",name);
                                intent.putExtra("gty",qty);
                                intent.putExtra("price",price);
                            }
                        }

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Barcodescan_response> call, Throwable t) {

                    }
                });
//                scannerView.resumeCameraPreview(MainActivity.this);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
}