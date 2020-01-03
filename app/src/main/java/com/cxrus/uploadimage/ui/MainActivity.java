package com.cxrus.uploadimage.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cxrus.uploadimage.R;
import com.cxrus.uploadimage.adapter.ImageAdapter;
import com.cxrus.uploadimage.model.Upload;
import com.cxrus.uploadimage.util.ActivityUtils;
import com.cxrus.uploadimage.util.ImageUtils;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    LinearLayout layout;
    Button btnImg;
    RecyclerView mRecyclerViewFeedback;

    final int KEY_IMAGE_PICKER = 55;
    List<String> feedbackImages = new ArrayList<>();
    List<Upload> uploadList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private int MAX_IMAGE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.mButtonAdd);
        layout = findViewById(R.id.layoutImage);
        btnImg = findViewById(R.id.mFeedbackImage);
        mRecyclerViewFeedback = findViewById(R.id.mRecyclerViewFeedback);

        imageAdapter = new ImageAdapter();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecyclerViewFeedback.setLayoutManager(manager);
        mRecyclerViewFeedback.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewFeedback.setNestedScrollingEnabled(true);
        mRecyclerViewFeedback.setAdapter(imageAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(MainActivity.this)
                        .showCamera(true)
                        .folderMode(true)
                        .toolbarImageTitle("Select image")
                        .single()
                        .start(KEY_IMAGE_PICKER);
            }
        });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(MainActivity.this)
                        .showCamera(true)
                        .toolbarImageTitle("Select an Image")
                        .single()
                        .start(KEY_IMAGE_PICKER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == KEY_IMAGE_PICKER && data != null) {
            List<Image> image = ImagePicker.getImages(data);
            if (!image.isEmpty()) {
                String feedbackImagePath = image.get(0).getPath();
                generateBase64(feedbackImagePath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ImageView addImageView(String path) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams((int) ActivityUtils.convertDpToPixel(120, this),
                (int) ActivityUtils.convertDpToPixel(120, this)));
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
        margin.bottomMargin = (int) ActivityUtils.convertDpToPixel(4, this);
        margin.leftMargin = (int) ActivityUtils.convertDpToPixel(4, this);
        margin.rightMargin = (int) ActivityUtils.convertDpToPixel(4, this);
        margin.topMargin = (int) ActivityUtils.convertDpToPixel(4, this);
        imageView.requestLayout();
        Glide.with(this).load(path).into(imageView);

        return imageView;
    }

    @SuppressLint("StaticFieldLeak")
    private void generateBase64(final String feedbackImagePath) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return ImageUtils.encodeToBase64(feedbackImagePath);
            }

            @Override
            protected void onPostExecute(String value) {
                super.onPostExecute(value);
                feedbackImages.add(value);
                if (feedbackImages.size() >= MAX_IMAGE) btnAdd.setVisibility(View.GONE);
            }
        }.execute();
    }

}
