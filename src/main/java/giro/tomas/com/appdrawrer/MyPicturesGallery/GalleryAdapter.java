package giro.tomas.com.appdrawrer.MyPicturesGallery;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import giro.tomas.com.appdrawrer.Picture;
import giro.tomas.com.appdrawrer.PictureInfoView.PictureInfos;
import giro.tomas.com.appdrawrer.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements ChildEventListener, View.OnClickListener {

    List<Picture> pictures;

    public GalleryAdapter() {
        pictures= new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout frameLayout= (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_image,parent,false);
        ViewHolder viewHolder= new ViewHolder(frameLayout);
        frameLayout.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picture picture= pictures.get(position);
        holder.frameLayout.setTag(position);
        Picasso.get().load(picture.getStorageUri()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        pictures.add(dataSnapshot.getValue(Picture.class));
        super.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onClick(View v) {
        int postition= (int) v.getTag();
        Intent intent= new Intent(v.getContext(),PictureInfos.class);
        intent.putExtra("Picture",pictures.get(postition));
        v.getContext().startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        FrameLayout frameLayout;
        ImageView image;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            frameLayout= (FrameLayout)itemView;
            image = (ImageView) itemView.findViewById(R.id.imageView2);
            textView = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}
