package giro.tomas.com.appdrawrer;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class SpeciesImageUrlFinder extends AsyncTask<Espece, Void, Uri> {
    private final ImageView imageView;

    public SpeciesImageUrlFinder(ImageView imageView) {
        this.imageView= imageView;
    }

    @Override
    protected Uri doInBackground(Espece... especes) {
        final Espece espece= especes[0];
        final FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final Uri[] theUri = {null};
        StorageReference storageReference= firebaseStorage.getReferenceFromUrl("gs://logindemo-cf739.appspot.com/img_"+espece.getNom()+" <"+espece.getNomLatin()+">0_.jpg");
        Task task= storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
           @Override
           public void onSuccess(Uri uri) {
               theUri[0] = uri;
           }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                StorageReference storageReference= firebaseStorage.getReferenceFromUrl("gs://logindemo-cf739.appspot.com/img_"+espece.getNom()+" <"+espece.getNomLatin()+">0_.png");
                Task task2=storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        theUri[0]=uri;
                    }
                });
                try {
                    Tasks.await(task2);
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        try {
            Tasks.await(task);
            if(theUri!=null){
               return theUri[0];
            }else {
               return null;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Uri uri) {
        if(imageView == null){
            return;
        }else{
            Picasso.get().load(uri).into(imageView);
        }
    }
}
