package giro.tomas.com.appdrawrer.addingPhoto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.net.Uri;

import java.util.List;

import giro.tomas.com.appdrawrer.Espece;

public class AddingPhotoViewModel extends ViewModel {
    public LiveData<String> description;
    public LiveData<Location> location;
    public LiveData<Espece> especeSelectionee;
    public LiveData<Boolean> isPublic;
    public LiveData<Uri> pictureDownloadUri;
    public LiveData<List<Espece>> especesProposees;
    public LiveData<String> titre;
    public LiveData<Boolean> imageSeCharge;
    public LiveData<Boolean> propositionsSeChargent;
    public LiveData<Boolean> isImageLoaded;

}
