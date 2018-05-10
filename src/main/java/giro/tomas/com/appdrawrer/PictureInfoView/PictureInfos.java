package giro.tomas.com.appdrawrer.PictureInfoView;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import giro.tomas.com.appdrawrer.Comment;
import giro.tomas.com.appdrawrer.Picture;
import giro.tomas.com.appdrawrer.R;

public class PictureInfos extends AppCompatActivity implements OnMapReadyCallback {
    Picture picture;
    ImageView imageView;
    LatLng location;
    private static final String TAG = "PictureInfos";
    RecyclerView commentRecyclerView;
    ImageButton sendCommentButton;
    DatabaseReference commentSectionReference;
    EditText editText;
    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_infos);
        picture=(Picture) getIntent().getSerializableExtra("Picture");
        Log.i(TAG, "onCreate: "+picture.getStorageUri());
        imageView=(ImageView) findViewById(R.id.image_view_big);
        Picasso.get().load(picture.getStorageUri()).into(imageView);
        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        location = new LatLng(picture.getLatitude(),picture.getLongitude());
        commentRecyclerView=(RecyclerView) findViewById(R.id.comments_recycle_view);
        sendCommentButton=(ImageButton) findViewById(R.id.comment_send_button);
        commentSectionReference= FirebaseDatabase.getInstance().getReference("comments").child(picture.getPictureCode());
        editText= (EditText) findViewById(R.id.comment_edit_text);
        commentsAdapter = new CommentsAdapter(commentSectionReference);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(commentsAdapter);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.addMarker(new MarkerOptions()
        .position(location)
                .title("position approchée"));
    }


    public void commentSendOnClick(View v){
        DatabaseReference pushedPostRef = commentSectionReference.push();
        String commentPostId = pushedPostRef.getKey();
        String text= editText.getText().toString();
        pushedPostRef.setValue(new Comment(text,"some day",commentPostId,"some username"));
        editText.setText("");
    }
}
