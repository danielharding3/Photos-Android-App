package com.example.photos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

public class AlbumContentsActivity extends AppCompatActivity {

    User user;
    Bundle bundle;
    Album album;

    int PICKIMG_RESULT_CODE = 41;

    private RecyclerView photosGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        setContentView(R.layout.activity_album_contents);

        user = User.getInstance(this.getApplicationContext());

        bundle = intent.getBundleExtra("bundle");

        album = user.albums.get(bundle.getInt("albumIndex"));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(album.name);

        photosGridView = findViewById(R.id.photosGridView);
        photosGridView.setLayoutManager(new GridLayoutManager(this, 2));
        photosGridView.setAdapter(new PhotosAdapter(this, album.photos, bundle.getInt("albumIndex")));

        FloatingActionButton fab = findViewById(R.id.addPhoto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseImage = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                chooseImage.setType("image/*");
                chooseImage = Intent.createChooser(chooseImage, "Choose an Image");
                startActivityForResult(chooseImage, PICKIMG_RESULT_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == PICKIMG_RESULT_CODE && resultCode == Activity.RESULT_OK){
            Uri filepath = intent.getData();
            if(filepath != null){
                User user = User.getInstance(this.getApplicationContext());
                Photo p = new Photo(filepath.toString());

                if(!album.photos.contains(p)){
                    album.photos.add(p);
                    if(!user.photos.contains(p)){
                        user.photos.add(p);
                    }
                    user.saveInstance(this.getApplicationContext());
                    photosGridView.setAdapter(new PhotosAdapter(this, album.photos, bundle.getInt("albumIndex")));
                    Toast.makeText(this, "image successfully added to " + album.name, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, album.name + " already contains this image.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
