package giro.tomas.com.appdrawrer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EspecesReconnuesAdapter extends ArrayAdapter<Espece> {

    public EspecesReconnuesAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView== null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.especes_identifiees_list_view_item,parent,false);
        }


        TextView especeFrancais= (TextView) convertView.findViewById(R.id.nom_espece_francais);
        TextView especeLatin= (TextView) convertView.findViewById(R.id.nom_espece_latin);

        especeFrancais.setText(getItem(position).getNom());
        especeLatin.setText(getItem(position).getNomLatin());

        return convertView;
    }
}
