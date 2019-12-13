package com.example.photos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private int albumIndex;
    private ArrayList<Photo> photos;
    private Context context;

    public PhotosAdapter(Context context, ArrayList<Photo> photos, int albumIndex) {
        this.context = context;
        this.photos = photos;
        this.albumIndex = albumIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View photoView = inflater.inflate(R.layout.item_photo_thumbnail, parent, false);
        return new ViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.ViewHolder holder, int position) {
        Uri imageUri = Uri.parse(photos.get(position).filepath);
        Cursor returnCursor = context.getContentResolver().query(imageUri, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        holder.img.setImageURI(imageUri);
        holder.title.setText(returnCursor.getString(nameIndex));

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                User user = User.getInstance(context.getApplicationContext());
                Intent showPhotoDetailsActivity = new Intent(v.getContext(), PhotoDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("photoIndex", position);
                bundle.putInt("albumIndex", albumIndex);
                showPhotoDetailsActivity.putExtra("bundle", bundle);
                v.getContext().startActivity(showPhotoDetailsActivity);

//////////////////////////////////////////////////////////////////////////////////
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private ImageView img;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.filename);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            MenuItem deletePhoto = menu.add(Menu.NONE, 1, 1, "Delete Photo");
            MenuItem movePhoto = menu.add(Menu.NONE, 2, 2, "Move Photo");

            deletePhoto.setOnMenuItemClickListener(listener);
            movePhoto.setOnMenuItemClickListener(listener);
        }

        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                User user = User.getInstance(context.getApplicationContext());
                Photo p = photos.get(getAdapterPosition());
                if(item.getItemId() == 1){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Photo");
                    builder.setMessage("Are you sure you want to delete this photo from this album?");
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            photos.remove(p);
                            user.albums.get(albumIndex).photos.remove(p);
                            user.saveInstance(context.getApplicationContext());
                            notifyItemRemoved(getAdapterPosition());
                            Toast.makeText(context, "Photo Successfully Removed", Toast.LENGTH_SHORT);
                        }
                    });
                    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    return true;
                } else if(item.getItemId() == 2) {
                    List<String> albumNamesList = user.albums.stream().map(a -> a.toString()).collect(Collectors.toList());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Select an album to move this image to");
                    builder.setSingleChoiceItems(albumNamesList.toArray(new String[0]), 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            photos.remove(p);
                            user.albums.get(albumIndex).photos.remove(p);
                            user.albums.get(which).photos.add(p);
                            user.saveInstance(context.getApplicationContext());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();
                            Toast.makeText(context, "Photo Successfully Moved", Toast.LENGTH_SHORT);
                        }
                    });
                    builder.show();
                    return true;
                }
                return false;
            }
        };
    }
}
