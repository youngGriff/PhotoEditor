package com.prometheus.gaidar.photoeditor;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.widget.ImageView;
import android.widget.SeekBar;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;

public class MainActivity extends AppCompatActivity implements ImageWithCircleView.BitmapCropListener {
    ImageWithCircleView mainImageView;
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

        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.main)).getBitmap();
        mainImageView.setImageBitmap(bitmap);
        bulgeFilter = new GPUImageBulgeDistortionFilter();
        gpuImage = new GPUImage(this);
        gpuImage.setImage(bitmap);


        mainImageView.setBitmapCropListener(this);
        sbScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float scale = (i - 10)/10;
                bulgeFilter.setScale(scale);
                gpuImage.setFilter(bulgeFilter);
                mainImageView.setImageBitmap(gpuImage.getBitmapWithFilterApplied());

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
                float radius = i/100;
                mainImageView.setRadius(radius);
                bulgeFilter.setRadius(radius);

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
}
