package com.example.photos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AlbumsActivity extends AppCompatActivity {

    private RecyclerView albumsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_albums);

        User u = User.getInstance(this.getApplicationContext());

        albumsListView = findViewById(R.id.AlbumsListView);
        albumsListView.setLayoutManager(new LinearLayoutManager(this));
        albumsListView.setAdapter(new AlbumsAdapter(u.albums, this));


        FloatingActionButton fab = findViewById(R.id.addAlbum);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlbumsActivity.this);
                builder.setTitle("Add Album");

                final EditText input = new EditText(AlbumsActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                );

                input.setLayoutParams(layoutParams);
                builder.setView(input);

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User u = User.getInstance(AlbumsActivity.this.getApplicationContext());

                        Album toAdd = new Album(input.getText().toString());
                        if(!u.albums.contains(toAdd)) {
                            u.albums.add(new Album(input.getText().toString()));
                            u.saveInstance(AlbumsActivity.this.getApplicationContext());
                            Toast.makeText(AlbumsActivity.this, "Album added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AlbumsActivity.this, "An Album with this name already exists. Please use a different name.", Toast.LENGTH_LONG).show();
                        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //TODO: change this to a single search icon and bring up the search view
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.app_bar_search) {
            Intent showSearchResultsActivity = new Intent(this, SearchResultsActivity.class);
            this.startActivity(showSearchResultsActivity);


        }

        return super.onOptionsItemSelected(item);
    }
}
