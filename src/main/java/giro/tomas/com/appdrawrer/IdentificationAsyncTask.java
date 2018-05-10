package giro.tomas.com.appdrawrer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IdentificationAsyncTask extends AsyncTask<Intent,Void,List<Espece>> {
    private Context context;
    private static final String TAG = "IdentificationAsyncTask";
    private RequestQueue queue;
    private EspecesReconnuesAdapter especesReconnuesAdapter;

    public IdentificationAsyncTask(Context context, EspecesReconnuesAdapter especesReconnuesAdapter) {
        this.context= context;
        this.especesReconnuesAdapter= especesReconnuesAdapter;
    }

    @Override
    protected List<Espece> doInBackground(Intent... intents) {


        Bitmap bitmap = null;
        MediaStore.Images.Media i = new MediaStore.Images.Media();
        try {
            bitmap=i.getBitmap(context.getContentResolver(),intents[0].getData());
            Log.i(TAG, "onActivityResult: "+bitmap.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap==null){return null;}


        OkHttpClient client = new OkHttpClient.Builder().readTimeout(1, TimeUnit.HOURS)
                .connectTimeout(3, TimeUnit.MINUTES)
                .build();


        //Drawable drawable = getResources().getDrawable(R.drawable.lol);
        // Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();



        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] bitmapdata = stream.toByteArray();


        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "logo-square.jpeg",
                        RequestBody.create(MEDIA_TYPE_PNG, bitmapdata))
                .build();

        Request request = new Request.Builder()
                .url("http://ramp.studio:24000/predict_from_image")
                .post(requestBody)

                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            final String text = response.body().string();

            Log.i("hi", text);
            return Espece.parser(text);

            //"http://ramp.studio:24000/predict_from_url?url=


        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Espece> especes) {
        super.onPostExecute(especes);
        if(especes!=null){
            for(Espece espece : especes){
                especesReconnuesAdapter.add(espece);
            }
            especesReconnuesAdapter.notifyDataSetChanged();

        }
    }
}