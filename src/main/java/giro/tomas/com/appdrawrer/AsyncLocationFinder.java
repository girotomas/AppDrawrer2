package giro.tomas.com.appdrawrer;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class AsyncLocationFinder extends AsyncTask<String, Void,Address> {
    Geocoder geocoder;
    private static final String TAG = "AsyncLocationFinder";
    TextView addressTextView;
    public AsyncLocationFinder(Geocoder geocoder, TextView addressTextView) {
        this.geocoder= geocoder;
        this.addressTextView= addressTextView;
    }

    @Override
    protected Address doInBackground(String... strings) {
        try {
            List<Address> adresses =geocoder.getFromLocationName(String.valueOf(strings[0]),1);
            if(adresses.size()>=1){
                Address address= adresses.get(0);
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }

    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);
        Log.i(TAG, "onPostExecute: "+address);
        if(address!=null && address.getMaxAddressLineIndex()>=0){
            addressTextView.setText(address.getAddressLine(0));
        }
        else{
            addressTextView.setText("");
        }

    }
}
