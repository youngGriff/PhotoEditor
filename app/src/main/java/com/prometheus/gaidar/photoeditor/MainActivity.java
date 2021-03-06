package com.prometheus.gaidar.photoeditor;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;

public class MainActivity extends AppCompatActivity implements CropImageView.OnSetCropOverlayMovedListener, CropImageView.OnCropImageCompleteListener {
    private CropImageView ivMainView;
    private GPUImageBulgeDistortionFilter mBulgeFilter;
    private GPUImage mGPUImage;
    private ImageView ivPreview;
    private SeekBar sbScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivMainView = findViewById(R.id.image);
        ivPreview = findViewById(R.id.iv_cropImage);

        sbScale = findViewById(R.id.sb_scale);

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.test)).getBitmap();

        ivMainView.setImageBitmap(bitmap);
        ivMainView.setOnSetCropOverlayMovedListener(this);
        ivMainView.setOnCropImageCompleteListener(this);

        mBulgeFilter = new GPUImageBulgeDistortionFilter();
        mGPUImage = new GPUImage(this);
        mGPUImage.setImage(bitmap);

    }


    public void applyEffect(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please wait");
        progressDialog.show();

        Rect wholeImageRect = ivMainView.getWholeImageRect();
        Rect areaRect = ivMainView.getCropRect();

        float i = sbScale.getProgress();
        float scale = ((i - 10.F)) / 10;

        float radius = calcInnerRadius(areaRect) / calcInnerRadius(wholeImageRect);
        PointF center = new PointF(areaRect.exactCenterX() / wholeImageRect.width(),
                areaRect.exactCenterY() / wholeImageRect.height());
        Log.i("parameters", "scale is" + scale + "\n radius is " + radius
                + "\n center: " + center.toString());
        mBulgeFilter.setScale(scale);
        mBulgeFilter.setRadius(radius);
        mBulgeFilter.setCenter(center);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGPUImage.setFilter(mBulgeFilter);
                final Bitmap bitmap = mGPUImage.getBitmapWithFilterApplied();
                mGPUImage.setImage(bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivMainView.setImageBitmap(bitmap);
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        Log.i("crop", "crop is finished");
        ivPreview.setImageBitmap(result.getBitmap());
    }

    public void saveTo(View view) {
        Toast.makeText(this, "Feature is not realized yet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCropOverlayMoved(Rect rect) {
        Log.i("crop", "We are moving. Rect " + rect);
        ivMainView.getCroppedImageAsync();
    }

    private static float calcInnerRadius(Rect rect) {
        return rect.right - rect.exactCenterX();
    }
}
