package giro.tomas.com.appdrawrer.PictureInfoView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import giro.tomas.com.appdrawrer.Comment;
import giro.tomas.com.appdrawrer.R;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> implements ChildEventListener{
    List<Comment> comments;
    DatabaseReference commentsDatabaseReference;
    private static final String TAG = "CommentsAdapter";


    public CommentsAdapter(DatabaseReference commentsDatabaseReference) {
        this.commentsDatabaseReference= commentsDatabaseReference;
        this.comments= new ArrayList<>();
        commentsDatabaseReference.addChildEventListener(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_view,parent,false);
        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment= comments.get(position);
        holder.textComment.setText(comment.getText());
        holder.userName.setText(comment.getUserName());
        holder.date.setText(comment.getDate());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG, "onChildAdded: adding child");
        Comment comment=dataSnapshot.getValue(Comment.class);
        comments.add(comment);
        notifyDataSetChanged();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView date;
        TextView textComment;

        public ViewHolder(View itemView) {
            super(itemView);
            userName=(TextView)itemView.findViewById(R.id.username);
            date=(TextView)itemView.findViewById(R.id.date);
            textComment=(TextView)itemView.findViewById(R.id.text_comment);
        }
    }
}
