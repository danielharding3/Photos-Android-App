package com.example.photos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View AlbumView = inflater.inflate(R.layout.item_album, parent, false);

        ViewHolder viewHolder = new ViewHolder(AlbumView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Album album = mAlbums.get(position);

        TextView textView = holder.albumNameTextView;
        ImageView imageView = holder.albumDeleteButton;

        textView.setText(album.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = User.getInstance(context.getApplicationContext());
                Intent intent = new Intent(v.getContext(), AlbumContentsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("albumIndex", position);
                intent.putExtra("bundle", bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

        public TextView albumNameTextView;
        public ImageView albumDeleteButton;

        public ViewHolder (final View itemView){
            super(itemView);

            albumNameTextView = (TextView) itemView.findViewById(R.id.album_name);
            albumDeleteButton = (ImageView) itemView.findViewById(R.id.album_delete_button);

            albumDeleteButton.setOnClickListener(this);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v){
            if(v.getId() == albumDeleteButton.getId()){
                Album album = mAlbums.get(getAdapterPosition());
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Album");
                builder.setMessage("Are you sure you want to delete " + album.name + " from your gallery?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User user = User.getInstance(context.getApplicationContext());
                        user.albums.remove(getAdapterPosition());
                        user.saveInstance(context.getApplicationContext());
                        notifyItemRemoved(getAdapterPosition());
                        Toast.makeText(context, "Album deleted successfully", Toast.LENGTH_SHORT).show();
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
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Rename = menu.add(Menu.NONE, 1, 1, "Rename Album");

            Rename.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == 1){
                        Album album = mAlbums.get(getAdapterPosition());
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Rename Album");
                        builder.setMessage("Edit Album name");

                        final EditText input = new EditText(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                        );

                        input.setLayoutParams(layoutParams);
                        builder.setView(input);

                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User user = User.getInstance(context.getApplicationContext());
                                final String inputText = input.getText().toString();
                                Album temp = new Album(inputText);
                                if(!user.albums.contains(temp)){
                                    user.albums.get(getAdapterPosition()).name = input.getText().toString();
                                    user.saveInstance(context.getApplicationContext());
                                    notifyItemChanged(getAdapterPosition());
                                    Toast.makeText(context, "Album renamed successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "An Album with this name already exists. Please use a different name.", Toast.LENGTH_LONG).show();
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
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private List<Album> mAlbums;
    private Context context;

    public AlbumsAdapter(List<Album> albums, Context context){
        this.mAlbums = albums;
        this.context = context;
    }
}
