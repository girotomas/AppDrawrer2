package giro.tomas.com.appdrawrer;

import android.net.Uri;
import android.os.Parcelable;

import java.io.Serializable;

public class Picture implements Serializable {
    private String storageUri;
    private double latitude;
    private double longitude;
    private String pictureCode;



    public String getStorageUri() {
        return storageUri;
    }

    public Picture(String storageUri, double latitude, double longitude, String pictureCode) {
        this.storageUri = storageUri;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pictureCode = pictureCode;
    }

    public String getPictureCode() {
        return pictureCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Picture() {
    }
}
