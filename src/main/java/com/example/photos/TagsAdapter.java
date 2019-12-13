package com.example.photos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private List<String> tagList;
    private Context context;
    private int albumIndex;
    private int photoIndex;
    private boolean isPeopleList;

    public TagsAdapter(List<String> tagList, int albumIndex, int photoIndex, boolean isPeopleList, Context context){
        this.tagList = tagList;
        this.albumIndex = albumIndex;
        this.photoIndex = photoIndex;
        this.isPeopleList = isPeopleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View tagView = inflater.inflate(R.layout.item_album, parent, false);

        TagsAdapter.ViewHolder viewHolder = new TagsAdapter.ViewHolder(tagView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String tag = tagList.get(position);

        TextView tv = holder.tagName;
        tv.setText(tag);
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tagName;
        public ImageView tagDeleteButton;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tagName = (TextView) itemView.findViewById(R.id.album_name);
            tagDeleteButton = (ImageView) itemView.findViewById(R.id.album_delete_button);

            tagDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Tag");
            builder.setMessage("Are you sure you want to delete this tag?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    User user = User.getInstance(context.getApplicationContext());
                    tagList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    user.saveInstance(context.getApplicationContext());
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
}
