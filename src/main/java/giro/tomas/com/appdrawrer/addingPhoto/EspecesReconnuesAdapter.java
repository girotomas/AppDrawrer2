package giro.tomas.com.appdrawrer.addingPhoto;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import giro.tomas.com.appdrawrer.Espece;
import giro.tomas.com.appdrawrer.R;

public class EspecesReconnuesAdapter extends ArrayAdapter<Espece> {

    public EspecesReconnuesAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable  View convertView, @NonNull ViewGroup parent) {
        if(convertView== null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.especes_identifiees_list_view_item,parent,false);
        }

        final ImageView imageView= (ImageView) convertView.findViewById(R.id.species_image_view);
        final TextView especeFrancais= (TextView) convertView.findViewById(R.id.nom_espece_francais);
        final TextView especeLatin= (TextView) convertView.findViewById(R.id.nom_espece_latin);



        final Espece espece= getItem(position);
        final String imageName= "img_"+espece.getNom()+" <"+espece.getNomLatin()+">0_.jpg";
        FirebaseStorage.getInstance().getReference("imagesEspeces").child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseStorage.getInstance().getReference("imagesEspeces").child("img_" + espece.getNom() + " <" + espece.getNomLatin() + ">0_.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                    }
                });
            }
        });

        especeFrancais.setText(getItem(position).getNom());
        especeLatin.setText(getItem(position).getNomLatin());

        return convertView;

    }
}
