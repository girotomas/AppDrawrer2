package giro.tomas.com.appdrawrer.addingPhoto;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import giro.tomas.com.appdrawrer.R;
import giro.tomas.com.appdrawrer.databinding.AddingPhotoBinding;

public class AddingPhoto2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddingPhotoViewModel viewModel= ViewModelProviders.of(this).get(AddingPhotoViewModel.class);
        AddingPhotoBinding binding= DataBindingUtil.setContentView(this, R.layout.activity_adding_photo2);
        binding.setModel(viewModel);
    }
}
