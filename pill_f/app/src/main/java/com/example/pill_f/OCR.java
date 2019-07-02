package com.example.pill_f;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Base64;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.TextAnnotation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class OCR extends AsyncTask {
    Vision.Builder visionBuilder;
    Vision vision;
    Image inputImage;
    String encoded;

    public OCR(){
        visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyD5-J3sE1WxIUsA-WNdulc30YpCNHEtBjA"));

        vision = visionBuilder.build();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Do();
        return null;
    }

    public void setImage(Bitmap res){
        res=changeBitmapContrastBrightness(res,2.0f,1.0f);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        res.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] currentData = stream.toByteArray();

        inputImage = new Image();
        inputImage.encodeContent(currentData);
    }

    public void Do(){
        Feature desiredFeature = new Feature();
        desiredFeature.setType("TEXT_DETECTION");
        AnnotateImageRequest request = new AnnotateImageRequest();
        request.setImage(inputImage);
        request.setFeatures(Arrays.asList(desiredFeature));

        BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();

        batchRequest.setRequests(Arrays.asList(request));
        BatchAnnotateImagesResponse batchResponse=null;
        try {
            batchResponse = vision.images().annotate(batchRequest).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextAnnotation text = batchResponse.getResponses().get(0).getFullTextAnnotation();
        if(text!=null)
            encoded=text.getText().split("\n")[0];

        else
            encoded="";
    }
    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }



}
