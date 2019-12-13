package com.example.photos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoDetailsActivity extends AppCompatActivity {

    User user;
    Photo photo;
    Bundle bundle;
    int photoIndex;
    int albumIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        Intent intent = this.getIntent();

        user = User.getInstance(this.getApplicationContext());
        bundle = intent.getBundleExtra("bundle");
        photoIndex = bundle.getInt("photoIndex");
        albumIndex = bundle.getInt("albumIndex");
        photo = user.albums.get(albumIndex).photos.get(photoIndex);

        ImageView image = (ImageView)findViewById(R.id.imageView);
        Uri imageUri = Uri.parse(photo.filepath);
        image.setImageURI(imageUri);

        //this sets text under photo to initial photo's filename
        TextView tagsTextView = (TextView)findViewById(R.id.tagsTextView);
        Cursor returnCursor = this.getContentResolver().query(imageUri, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        tagsTextView.setText(returnCursor.getString(nameIndex));

        RecyclerView rv_people = (RecyclerView)findViewById(R.id.rv_people);
        rv_people.setLayoutManager(new LinearLayoutManager(this));
        rv_people.setAdapter(new TagsAdapter(user.albums.get(albumIndex).photos.get(photoIndex).peopleTagged, albumIndex, photoIndex, true, this));

        RecyclerView rv_places = (RecyclerView)findViewById(R.id.rv_places);
        rv_places.setLayoutManager(new LinearLayoutManager(this));
        rv_places.setAdapter(new TagsAdapter(user.albums.get(albumIndex).photos.get(photoIndex).locationsTagged, albumIndex, photoIndex, false, this));

        Button leftBtn = (Button)findViewById(R.id.leftBtn);
        Button rightBtn = (Button)findViewById(R.id.rightBtn);
        Button addTagBtn = (Button)findViewById(R.id.addTag);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoIndex - 1 >= 0) {
                    photoIndex--;
                    user = User.getInstance(v.getContext().getApplicationContext());
                    photo = user.albums.get(albumIndex).photos.get(photoIndex);
                    ImageView image = (ImageView)findViewById(R.id.imageView);
                    Uri imageUri = Uri.parse(photo.filepath);
                    image.setImageURI(imageUri);

                } else {
                    photoIndex = user.albums.get(albumIndex).photos.size() - 1;
                    photo = user.albums.get(albumIndex).photos.get(photoIndex);
                    ImageView image = (ImageView)findViewById(R.id.imageView);
                    Uri imageUri = Uri.parse(photo.filepath);
                    image.setImageURI(imageUri);
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoIndex + 1 <= user.albums.get(albumIndex).photos.size() - 1) {
                    photoIndex++;
                    user = User.getInstance(v.getContext().getApplicationContext());
                    photo = user.albums.get(albumIndex).photos.get(photoIndex);
                    ImageView image = (ImageView)findViewById(R.id.imageView);
                    Uri imageUri = Uri.parse(photo.filepath);
                    image.setImageURI(imageUri);
                } else {
                    photoIndex = 0;
                    photo = user.albums.get(albumIndex).photos.get(photoIndex);
                    ImageView image = (ImageView)findViewById(R.id.imageView);
                    Uri imageUri = Uri.parse(photo.filepath);
                    image.setImageURI(imageUri);
                }
            }
        });

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] types = {"people", "locations"};
                int checked = -1;
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoDetailsActivity.this);
                builder.setTitle("Enter text for the tag and choose what type of tag to add.");

                final EditText input = new EditText(PhotoDetailsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);

                builder.setSingleChoiceItems(types, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!input.getText().toString().equals("")){
                            if(which == 0){
                                user.albums.get(albumIndex).photos.get(photoIndex).peopleTagged.add(input.getText().toString());
                            } else if (which == 1){
                                user.albums.get(albumIndex).photos.get(photoIndex).locationsTagged.add(input.getText().toString());
                            }
                            user.saveInstance(PhotoDetailsActivity.this.getApplicationContext());
                            dialog.dismiss();
                        }
                        builder.setMessage("Please Enter a valid tag value");
                    }
                });

                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


    }
}
