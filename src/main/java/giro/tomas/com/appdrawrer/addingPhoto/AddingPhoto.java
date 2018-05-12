package giro.tomas.com.appdrawrer.addingPhoto;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Random;

import giro.tomas.com.appdrawrer.AsyncLocationFinder;
import giro.tomas.com.appdrawrer.IdentificationAsyncTask;
import giro.tomas.com.appdrawrer.R;

public class AddingPhoto extends AppCompatActivity {
    private GPSTracker.GPSTracker gpsTracker;
    private static final int RC_PHOTO_PICKER = 487;
    private StorageReference photoReference;
    private Random random;
    private DatabaseReference databaseReference;
    private Button selectionImage;
    private EditText descriptionPhoto;
    private Button boutonLocalisation;
    private TextView textePhotoPrivee;
    private Button fairePublique;
    private boolean estPublique= false;
    ProgressBar progressBar;
    private ImageView imageView;
    private EditText localisationEditText;
    private static final String TAG = "AddingPhoto";
    private Geocoder geocoder;
    private TextView addressTextView;
    private AsyncLocationFinder asyncLocationFinder;
    private ListView recognizedSpeciesListView;
    EspecesReconnuesAdapter especesReconnuesAdapter;
    private Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_photo2);
        selectionImage= (Button) findViewById(R.id.selection_image);
        descriptionPhoto=(EditText) findViewById(R.id.description_photo);
        boutonLocalisation= (Button) findViewById(R.id.bouton_localisation);
        textePhotoPrivee= (TextView) findViewById(R.id.text_prive);
        fairePublique= (Button) findViewById(R.id.faire_publique);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        imageView= (ImageView) findViewById(R.id.preview_photo);
        localisationEditText=(EditText) findViewById(R.id.localisation_edit_text);
        geocoder= new Geocoder(this);
        addressTextView=(TextView)findViewById(R.id.adress_text_view);
        recognizedSpeciesListView= (ListView) findViewById(R.id.list_view_especes_identifiees);
        especesReconnuesAdapter= new EspecesReconnuesAdapter(this,R.layout.especes_identifiees_list_view_item);
        recognizedSpeciesListView.setAdapter(especesReconnuesAdapter);
        Toolbar myToolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        localisationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(asyncLocationFinder!=null) asyncLocationFinder.cancel(true);
                asyncLocationFinder= new AsyncLocationFinder(geocoder,addressTextView);
                asyncLocationFinder.execute(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gpsTracker= new GPSTracker.GPSTracker(this);
        gpsTracker.getLatitude();

        photoReference= FirebaseStorage.getInstance().getReference("photos");
        random= new Random();

        databaseReference= FirebaseDatabase.getInstance().getReference("photos");

    }
    public void selectionerImageOnClick(View v){
        imageView.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(intent,RC_PHOTO_PICKER);

    }

    public void fairePubliqueOnClick(View v){
        if(estPublique){
            estPublique=false;
            fairePublique.setText("Faire la photo publique");
            textePhotoPrivee.setText("La photo ne sera visible que par vous");
        }
        else {
            estPublique=true;
            fairePublique.setText("Faire la photo privée");
            textePhotoPrivee.setText("La photo ne sera visible que par tout le monde");
        }
    }

    public void definirLocalisationOnClick(View v){

    }

    public void utiliserMaLocalisationOnClick(View v){
        try{
            Location location= gpsTracker.getLocation();
            addressTextView.setText(geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0).getAddressLine(0));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"erreur de localisation",Toast.LENGTH_LONG);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RC_PHOTO_PICKER){

            if(resultCode== RESULT_CANCELED){
                return;
            }else if(resultCode== RESULT_OK){
                progressBar.setVisibility(View.VISIBLE);

                IdentificationAsyncTask identificationAsyncTask= new IdentificationAsyncTask(addressTextView.getContext(),especesReconnuesAdapter);
                identificationAsyncTask.execute(data);


                final Uri selectedImageUri=data.getData();

                StorageReference newReference= photoReference.child(String.valueOf(random.nextInt()));
                newReference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUrl = taskSnapshot.getDownloadUrl();

                        Picasso.get().load(downloadUrl).into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                });
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adding_photo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.send:
                send();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void send() {
    }
}
