package com.cse14kuet.rksazid.mathcam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.github.kexanie.library.MathView;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{


    private static int RESULT_LOAD_IMG = 1;

    String imgDecodableString,reformattedString="";

    //Bitmap
    Bitmap cameraImageBitmap,image,bitmap,showImage;

    //Mat declaration
    private Mat mGrey;
    private Mat mRgba;
    private Mat mRgbaT;

    //camera
    private JavaCameraView mOpenCvCameraView;

    //UI declaration
    Button processButton,importButton,cameraButton,mathcamButton,detailsButton;
    TextView resultTextView, additionalTextView,equationView;
    ImageView importedImageView;
    private TessBaseAPI mTess;
    String datapath = "";

    //Equation solver class
    EquationSolver equationSolver;

    //Callback for Camera
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);


      //  additionalTextView  = (TextView) findViewById(R.id.additionalTextView);

        //Animation
        final Animation myAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2,20);
        myAnimation.setInterpolator(interpolator);

        //all UI initialization

        //camera
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.my_java_camera);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        //Buttons
        processButton = (Button) findViewById(R.id.buttonprocess);
        importButton  = (Button) findViewById(R.id.buttonimport);
        cameraButton = (Button) findViewById(R.id.buttoncamera);
        mathcamButton = (Button) findViewById(R.id.buttonmathcam);

        //ImageView
        importedImageView = (ImageView) findViewById(R.id.importedImageView);

        //TextView
        equationView = (TextView) findViewById(R.id.textviewequation);
        resultTextView = (TextView) findViewById(R.id.textviewresult);


        myAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        cameraButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                       // cameraButton.startAnimation(myAnimation);
                        cameraButton.setAlpha((float) 0.5);
                        break;
                    case MotionEvent.ACTION_UP:
                        cameraButton.setAlpha((float) 1.0);
                        if(mOpenCvCameraView.getVisibility() == SurfaceView.VISIBLE){
                            captureImage();
                            cameraOff();
                        }else {
                            cameraOn();
                            importedImageViewOff();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return false;
            }
        });

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraOff();
                importedImageViewOn();
                loadImagefromGallery(view);
            }
        });

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processButton.startAnimation(myAnimation);
                try {
                    processImage(view);

                }catch (Exception e){
                    Log.e( "RESULT", ""+e );
                    Toast.makeText(MainActivity.this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //init image
        image = BitmapFactory.decodeResource(getResources(), R.drawable.example1);

        //initialize Tesseract API
        String language = "eng";
        datapath = getFilesDir()+ "/tesseract/";
        mTess = new TessBaseAPI();

        checkFile(new File(datapath + "tessdata/"));

        mTess.init(datapath, language);

       // importedImageViewOff();


    }

    private void captureImage() {
        importedImageViewOn();
        Mat temp2 = mRgbaT;
        showImage = Bitmap.createBitmap(temp2.cols(), temp2.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(temp2,showImage);
        importedImageView.setImageBitmap(showImage);

        Core.flip(mGrey.t(), mGrey, 1);
        Size szResized = new Size(720,1280);
        Imgproc.resize( mGrey, mGrey, szResized);
        Mat temp = mGrey;
        bitmap = Bitmap.createBitmap(temp.cols(),temp.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(temp,bitmap);
    }




    private void importedImageViewOff() {
        importedImageView.setVisibility(View.INVISIBLE);
    }
    private void importedImageViewOn() {
        importedImageView.setVisibility(View.VISIBLE);
    }

    private void cameraOff() {
        mOpenCvCameraView.setVisibility(SurfaceView.INVISIBLE);
    }

    private  void cameraOn(){
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

            // Get the Image from data
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();


            bitmap = BitmapFactory.decodeFile(imgDecodableString);

            // Set the Image in ImageView after decoding the String
            importedImageView.setImageBitmap(bitmap);

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public void loadImagefromGallery(View view) {

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void processImage(View view){
        String OCRresult = null;
        mTess.setImage(bitmap);
        OCRresult = mTess.getUTF8Text();

        //Show logcat
        Log.e( "RESULT","From Main Activity"+ OCRresult );
        for(int i = 0;i< OCRresult.length();i++){
            if(OCRresult.charAt(i) == '\n'){
                Log.e( "RESULT", "NEW LINE" );
            }
        }
        reformatTheText(OCRresult);
        Log.e( "RESULT", "  " +reformattedString );

        equationSolver = new EquationSolver(OCRresult);
        equationView.setText(reformattedString);
        reformattedString = "";
        resultTextView.setText(equationSolver.getResult());
    }

    private void reformatTheText(String s) {
        char c;
        for(int i = 0;i<s.length()-1;i++){
            if(s.charAt(i) == 'x' || s.charAt(i) == 'y' || s.charAt(i) == 'X' || s.charAt(i) == 'Y'){
                c = s.charAt(i+1);
                int a = c;
                if(a>=48 && a<=56){
                    reformattedString+=s.charAt(i);
                    reformattedString+='^';
                   // reformattedString+=c;
                }else reformattedString+=s.charAt(i);
            }else if(s.charAt(i) == ' '){
                if(i == s.length()-2){
                    reformattedString+=s.charAt(i+1);
                }
            }
            else if(i == s.length()-2){
                reformattedString+=s.charAt(i);
                reformattedString+=s.charAt(i+1);
            }
            else reformattedString+=s.charAt(i);
        }
    }

    private void checkFile(File dir) {
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }


            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mGrey = inputFrame.gray();
        mRgba = inputFrame.rgba();
       /*
        temp = mGrey;
        bitmap = Bitmap.createBitmap(temp.cols(),temp.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(temp,bitmap);
        img.setImageBitmap(bitmap);
        */

        mRgbaT = mRgba.t();
        Core.flip(mRgba.t(), mRgbaT, 1);
        Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
        return mRgbaT;

//        detectText();


        //  return mRgba;
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        Mat mIntermediateMat = new Mat();
        mGrey = new Mat(height, width, CvType.CV_8UC4);
        mRgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }

}
