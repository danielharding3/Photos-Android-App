package com.example.photos;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    Context context;
    User user;
    ArrayList<Photo> photos;

    private RecyclerView photoSearchResults;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = User.getInstance(this.getApplicationContext());



        //photoSearchResults.setLayoutManager(new GridLayoutManager(this, 2));
        //photoSearchResults.setAdapter(new PhotosAdapter(this, album.photos, bundle.getInt("albumIndex")));

        EditText personTagText = (EditText)findViewById(R.id.personTagText);
        String personTag = personTagText.getText().toString();

        EditText locationTagText = (EditText)findViewById(R.id.locationTagText);
        String locationTag = locationTagText.getText().toString();

        Switch switch1 = (Switch)findViewById(R.id.switch1);
        boolean andOr = !switch1.isChecked();


        //photos = search(personTag, andOr, locationTag);

        //photoSearchResults = findViewById(R.id.photoSearchResults);
        //photoSearchResults.setAdapter(new SearchRecyclerViewAdapter(this, photos));

        Button searchBtn = (Button)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<Photo> photosToShow = search(personTag, andOr, locationTag);

                if (!photosToShow.isEmpty()) {
                    photoSearchResults = findViewById(R.id.photoSearchResults);
                    photoSearchResults.setAdapter(new SearchRecyclerViewAdapter(context, photosToShow));

                } else {
                    //this is if there are no photos that match the search tags
                    //Toast.makeText(context, "No photos match the given tag types.", Toast.LENGTH_SHORT).show();


                }

            }
        });
    }

    public ArrayList<Photo> search(String peopleTag, boolean andOr, String locationTag) {
        //user = User.getInstance(context.getApplicationContext());
        ArrayList<Photo> photosToDisplay = new ArrayList<>();

        for (Photo photo : user.photos) {

            //this is if AND is selected
            if (andOr) {

                if (photo.peopleTagged.contains(peopleTag) && photo.locationsTagged.contains(locationTag)) {
                    photosToDisplay.add(photo);
                }

            //this is if OR is selected
            } else {

                if (photo.peopleTagged.contains(peopleTag) || photo.locationsTagged.contains(locationTag)) {
                    photosToDisplay.add(photo);
                }

            }
        }

        return photosToDisplay;

    }


}
