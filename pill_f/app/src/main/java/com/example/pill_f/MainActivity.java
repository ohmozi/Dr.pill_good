package com.example.pill_f;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    CameraSurfaceView cameraSurfaceView;
    ImageView imageView;
    ImageView ii;
    public Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    //Prev prev;
    Context mContext=MainActivity.this;
    Button button;
    Button captureButton;

    Activity mainActivity = this;
    MainActivity mm=this;

    private static final String TAG = "MAINACTIVITY";

    static final int REQUEST_CAMERA = 1;//후면 카메라


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cameraSurfaceView.setVisibility(View.VISIBLE);
                setContentView(R.layout.camera_main);
                onDraw();

                cameraSurfaceView = (CameraSurfaceView)findViewById(R.id.cameraSurfaceView);
                cameraSurfaceView.setDisplayInfo(mainActivity,mm);
                captureButton=(Button)findViewById(R.id.captureButton);
                captureButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){

                        cameraSurfaceView.capture();

                    }
                });
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {//요건 그냥 권한받기
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            //mCameraTextureView = (TextureView) findViewById(R.id.cameraTextureView);
                            //mPreview = new Preview(mainActivity, mCameraTextureView);
                        } else {
                            Toast.makeText(this,"Should have camera permission to run", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mPreview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mPreview.onPause();
    }
    private void onDraw(){//사각형 그리기
        imageView = (ImageView)findViewById(R.id.imageView);
        DisplayMetrics dm = getApplication().getResources().getDisplayMetrics();
        Bitmap bitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_4444);
        paint = new Paint();
        canvas = new Canvas(bitmap);

        paint.setColor(Color.parseColor("#4bd27d"));
        paint.setAlpha(255);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(150,dm.heightPixels/2-dm.widthPixels/2+150,dm.widthPixels-150,dm.heightPixels/2+dm.widthPixels/2-150,paint);

        canvas.drawRect(dm.widthPixels/2-50,dm.heightPixels/2,dm.widthPixels/2,dm.heightPixels/2,paint);
        canvas.drawRect(dm.widthPixels/2,dm.heightPixels/2,dm.widthPixels/2+50,dm.heightPixels/2,paint);
        canvas.drawRect(dm.widthPixels/2,dm.heightPixels/2-50,dm.widthPixels/2,dm.heightPixels/2,paint);
        canvas.drawRect(dm.widthPixels/2,dm.heightPixels/2,dm.widthPixels/2,dm.heightPixels/2+50,paint);

        paint.setColor(Color.parseColor("#000000"));
        paint.setAlpha(150);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(0,0,dm.widthPixels,dm.heightPixels/2-dm.widthPixels/2+145,paint);
        canvas.drawRect(0,dm.heightPixels/2+dm.widthPixels/2-145,dm.widthPixels,dm.heightPixels,paint);
        canvas.drawRect(0,dm.heightPixels/2-dm.widthPixels/2+145,145,dm.heightPixels/2+dm.widthPixels/2-145,paint);
        canvas.drawRect(dm.widthPixels-145,dm.heightPixels/2-dm.widthPixels/2+145,dm.widthPixels,dm.heightPixels/2+dm.widthPixels/2-145,paint);

        imageView.setImageDrawable(new BitmapDrawable(getResources(),bitmap));
    }



}
