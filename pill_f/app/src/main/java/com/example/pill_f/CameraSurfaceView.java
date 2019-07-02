package com.example.pill_f;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final float STROKE_WIDTH = 5f;
    private SurfaceHolder mHolder;
    private static Camera mCamera = null;
    private Camera.CameraInfo mCameraInfo;
    private int mDisplayOrientation;
    private int mCameraID;
    private Activity mActivity;
    private MainActivity mainActivity;

    // 필수 생성자
    public CameraSurfaceView(Context context) {
        super(context);
        init(context);
    }

    // 필수 생성자
    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    // 초기화를 위한 메서드
    private void init(Context context) {


        mHolder = getHolder(); // 서피스뷰 내에 있는 SurfaceHolder 라고 하는 객체를 참조할 수 있다.
        mHolder.addCallback(this); // holder
    }
    static Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0){
                mCamera.cancelAutoFocus();
            }
        }
    };
    public static void doTouchFocus(Rect tfocusRect) {
        try {
            List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters param = mCamera.getParameters();
            param.setFocusAreas(focusList);
            param.setMeteringAreas(focusList);
            mCamera.setParameters(param);

            mCamera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            Rect touchRect = new Rect(
                    (int) (x - 100),
                    (int) (y - 100),
                    (int) (x + 100),
                    (int) (y + 100));


            final Rect targetFocusRect = new Rect(
                    touchRect.left * 2000 / this.getWidth() - 1000,
                    touchRect.top * 2000 / this.getHeight() - 1000,
                    touchRect.right * 2000 / this.getWidth() - 1000,
                    touchRect.bottom * 2000 / this.getHeight() - 1000);

            CameraSurfaceView.doTouchFocus(targetFocusRect);

        }
        return false;
    }

    public static int calculatePreviewOrientation(Camera.CameraInfo info, int rotation) {
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public void setDisplayInfo(Activity act,MainActivity mm){
        mDisplayOrientation = act.getWindowManager().getDefaultDisplay().getRotation();
        mActivity=act;
        mainActivity=mm;
    }


    private void findFrontFacingCameraID() {
        mCameraID = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mCameraID = i;
                break;
            }
        }
    }

    // 서피스뷰가 메모리에 만들어지는 시점에 호출됨
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCamera = Camera.open(); // 카메라 객체를 참조하여 변수에 할당
        mCamera.setDisplayOrientation(90); // 이게 없으면 미리보기 화면이 회전되어 나온다.

        findFrontFacingCameraID();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);

        mCameraInfo = cameraInfo;


        try {
            mCamera.setPreviewDisplay(mHolder); // Camera 객체에 이 서피스뷰를 미리보기로 하도록 설정
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 서피스뷰가 크기와 같은 것이 변경되는 시점에 호출
     * 화면에 보여지기 전 크기가 결정되는 시점 */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // 미리보기 화면에 픽셀로 뿌리기 시작! 렌즈로부터 들어온 영상을 뿌려줌.
        mCamera.startPreview();
    }

    // 없어질 때 호출
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview(); // 미리보기 중지. 많은 리소스를 사용하기 때문에
        // 여러 프로그램에서 동시에 쓸 때 한쪽에서 lock 을 걸어 사용할 수 없는 상태가 될 수 있기 때문에, release 를 꼭 해주어야함
        mCamera.release(); // 리소스 해제
        mCamera = null;
    }

    Camera.PictureCallback jpegcallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //TextRecognizer txtRecognizer = new TextRecognizer.Builder(mActivity.getApplicationContext()).build();
            int w = camera.getParameters().getPictureSize().width;
            int h = camera.getParameters().getPictureSize().height;
            int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
            //byte array를 bitmap으로 변환
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray( data, 0, data.length, options);
            //이미지를 디바이스 방향으로 회전
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            bitmap =  Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
            matrix.postScale(0.03125f,0.03125f);
            matrix.postRotate(270);
            Bitmap res = Bitmap.createBitmap(bitmap,bitmap.getWidth()/2-1024,bitmap.getHeight()/2-1024,2048,2048,matrix,true);
            //bitmap을 byte array로 변환
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            res.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] currentData = stream.toByteArray();
/*
            OCR ocr = new OCR();
            ocr.setImage(ocrbitmap);
            try {
                ocr.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            EditText editText = (EditText)mActivity.findViewById(R.id.editText);
            String name = editText.getText().toString();
            if(name==null){
                name="";
            }
            //String name=ocr.encoded;

            Httpconnect httpconnect = new Httpconnect(currentData,name);
            try {
                httpconnect.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            GotoWeb gtw=new GotoWeb(mActivity);
            gtw.GotoServer();
        }
    };

    // 서피스뷰에서 사진을 찍도록 하는 메서드
    public boolean capture(){
        if (mCamera != null){
            mCamera.takePicture(null, null,jpegcallback);
            return true;
        } else {
            return false;
        }
    }




}