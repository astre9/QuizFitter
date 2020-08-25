package com.luceafarul.quizfitter.view.classification;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.google.android.material.card.MaterialCardView;
import com.luceafarul.quizfitter.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.luceafarul.quizfitter.repositories.api.ClassifyExercise;
import com.luceafarul.quizfitter.view.HomeActivity;


import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CameraActivity extends AppCompatActivity {
    private Camera mCamera;
    private Button btnClassify;
    private Button btnHome;
    private Button btnCancel;
    private FrameLayout preview;
    private CameraPreview mPreview;
    private String filepath;
    private String filename;
    private static String ACCESS_KEY = "?";
    private static String SECRET_KEY = "?";
    private TransferListener transferListener;

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("Error", "Error creating media file, check storage permissions");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("Error", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Error", "Error accessing file: " + e.getMessage());
            }
        }
    };

    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MVPLogin2");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MVPLogin2", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        filename = "IMG_" + timeStamp + ".jpg";
        filepath = mediaStorageDir.getPath() + File.separator + filename;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(filepath);
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        btnClassify = findViewById(R.id.btnClassify);
        btnClassify.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                        upload();
                        startLoading();
                    }
                }
        );

        btnHome = findViewById(R.id.btnHome);
        btnCancel = findViewById(R.id.btnCancel);

        btnHome.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CameraActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
        );
        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CameraActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        transferListener = new TransferListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.equals(TransferState.COMPLETED)) {
                    //Success
                    Log.d("Succes aws upload", "Miau");
                    new ClassifyExercise() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            try {
                                if (s != null) {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String exerciseName = jsonObject.getString("exercise");
                                    String imagePath = jsonObject.getString("image_name");
                                    Intent intent = new Intent(CameraActivity.this, ClassificationResultsActivity.class);
                                    intent.putExtra("exercise", exerciseName);
                                    intent.putExtra("imageName", imagePath);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(CameraActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(CameraActivity.this, "Couldn't connect to classification server!", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(filename);
                } else if (state.equals(TransferState.FAILED)) {
                    //Failed
                    Log.d("Failed aws upload", "Failed");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {

            }

        };
    }

    public void upload() {
        java.util.logging.Logger.getLogger("com.amazonaws").setLevel(java.util.logging.Level.FINEST);
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "?", // Identity pool ID
                Regions.EU_CENTRAL_1 // Region
        );
        AmazonS3 s3 = new AmazonS3Client(credentials);

        final Handler handler = new Handler();
        (new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = new File(filepath);
                            TransferNetworkLossHandler.getInstance(CameraActivity.this);
                            TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());
                            TransferObserver observer = transferUtility.upload(
                                    "luceafarul-quizfitter-app",
                                    "classification/" + filename,
                                    file
                            );

                            observer.setTransferListener(transferListener);
                        } catch (Exception e) {
                            //Log.d("Image save error", e.getMessage());
                        }
                    }
                });
            }
        })).start();
    }

    private void startLoading() {
        MaterialCardView llButtons = findViewById(R.id.llButtons);
        LinearLayout llLoading = findViewById(R.id.llLoading);
        FrameLayout flCamera = findViewById(R.id.camera_preview);
        llLoading.setVisibility(View.VISIBLE);
        llButtons.setVisibility(View.GONE);
        flCamera.setVisibility(View.GONE);
    }
}
