package com.prometheus.gaidar.photoeditor;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.theartofdev.edmodo.cropper.CropImageView;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;

public class MainActivity extends AppCompatActivity implements ImageWithCircleView.BitmapCropListener {
    CropImageView mainImageView;
    private GPUImageBulgeDistortionFilter bulgeFilter;
    private GPUImage gpuImage;
    private ImageView imageView;
    SeekBar sbRadius;
    SeekBar sbScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_image_with_circle_view);
        mainImageView = findViewById(R.id.image);
        imageView = findViewById(R.id.iv_cropImage);
        sbRadius = findViewById(R.id.sb_radius);
        sbScale = findViewById(R.id.sb_scale);

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.main)).getBitmap();

        mainImageView.setImageBitmap(bitmap);
        bulgeFilter = new GPUImageBulgeDistortionFilter();
        gpuImage = new GPUImage(this);
        gpuImage.setImage(bitmap);


        sbScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void showImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void hideImage() {
        imageView.setImageBitmap(null);
    }

    public void applyEffect(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please wait");
        progressDialog.show();

        Rect wholeImageRect = mainImageView.getWholeImageRect();
        Rect areaRect = mainImageView.getCropRect();
        float i = sbScale.getProgress();
        float scale = ((i - 10.F)) / 10;
        Log.i("scale", scale + "");
        bulgeFilter.setScale(scale);
        bulgeFilter.setCenter((new PointF(areaRect.exactCenterX() / wholeImageRect.width(),
                areaRect.exactCenterY() / wholeImageRect.height())));
        new Thread(new Runnable() {
            @Override
            public void run() {
                gpuImage.setFilter(bulgeFilter);
                final Bitmap bitmap = gpuImage.getBitmapWithFilterApplied();
                gpuImage.setImage(bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainImageView.setImageBitmap(bitmap);
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
}
