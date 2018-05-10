package giro.tomas.com.appdrawrer;


import android.util.Log;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Espece {
    private String nom;
    private static final String TAG = "Espece";

    public Espece(String nom, String nomLatin) {
        this.nom = nom;
        this.nomLatin = nomLatin;
    }

    private String nomLatin;

    public String getNom() {
        return nom;
    }

    public Espece() {
    }

    public String getNomLatin() {
        return nomLatin;
    }

    @Override
    public String toString() {

        return "Espece{" +
                "nom='" + nom + '\'' +
                ", nomLatin='" + nomLatin + '\'' +
                '}';
    }

    /**
     * {
     "classes": [
     "L'Abeille mellif\u00e8re <Apis mellifera>",
     "Les Andr\u00e8nes ray\u00e9es difficiles \u00e0 d\u00e9terminer <Andrenidae et autres>",
     "Taxon inconnu de la cl\u00e9",
     "Les Halictes (femelles) <Halictus, Lasioglossum et autres>",
     "Les Coll\u00e8tes (autres) <Colletidae>"
     ],
     "probas": [
     0.999777997154646,
     6.7241666403702e-05,
     2.1688924023350415e-05,
     1.6147020828148064e-05,
     1.5582575645163543e-05
     ]
     }
     * @return
     */
    public static List<Espece> parser(String stringResult){
        try{
            List<Espece> especes= new ArrayList<>();
            JSONObject result= new JSONObject(stringResult);
            JSONArray classes = result.getJSONArray("classes");
            JSONArray probabilites = result.getJSONArray("probas");

            Pattern pattern= Pattern.compile("(.*) <(.*)>");
            for(int i=0;i<5;i++){
                Matcher matcher= pattern.matcher(classes.getString(i));
                if(matcher.matches()){
                    Espece espece= new Espece(matcher.group(1),matcher.group(2));
                    especes.add(espece);
                    Log.i(TAG, "parser: matcher matched:"+espece.toString());
                }

            }
            return especes;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    return  null;
    }
}
