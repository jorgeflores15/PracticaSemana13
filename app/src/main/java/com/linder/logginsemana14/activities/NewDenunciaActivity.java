package com.linder.logginsemana14.activities;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.linder.logginsemana14.R;
import com.linder.logginsemana14.Service.ApiService;
import com.linder.logginsemana14.Service.ApiServiceGenerator;
import com.linder.logginsemana14.Service.ResponseMessage;
import com.linder.logginsemana14.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewDenunciaActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = NewDenunciaActivity.class.getSimpleName();
    private ImageView imagePreview;
    private EditText titulonew;
    private EditText descripcionnew;
    private EditText ubicacionnew;
    private EditText latitudnew;
    private EditText longitudnew;
    protected LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_denuncia);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imagePreview = (ImageView) findViewById(R.id.imagen_preview);
        descripcionnew = (EditText) findViewById(R.id.descripcionNew);
        titulonew = (EditText) findViewById(R.id.titulonew);
        ubicacionnew = (EditText) findViewById(R.id.ubicacionnew);
        latitudnew = (EditText) findViewById(R.id.latitudnew);
        longitudnew = (EditText) findViewById(R.id.longitudnew);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            getLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void getLocation() throws IOException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                latitudnew.setText(String.valueOf(lat));
                longitudnew.setText(String.valueOf(lng));
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                ubicacionnew.setText(address + ", " + city);

            } else {
                latitudnew.setText("error");
                longitudnew.setText("error");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case REQUEST_LOCATION:
                try {
                    getLocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Camera handler
     */

    private static final int CAPTURE_IMAGE_REQUEST = 300;

    private Uri mediaFileUri;

    public void takePicture(View view) {
        try {

            if (!permissionsGranted()) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, PERMISSIONS_REQUEST);
                return;
            }

            // Creando el directorio de imágenes (si no existe)
            File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new Exception("Failed to create directory");
                }
            }

            // Definiendo la ruta destino de la captura (Uri)
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            mediaFileUri = Uri.fromFile(mediaFile);
            Log.d("Foto", String.valueOf(mediaFile));


            // Iniciando la captura
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(this, "Error en captura: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CAPTURE_IMAGE_REQUEST) {
            // Resultado en la captura de la foto
            if (resultCode == RESULT_OK) {
                try {
                    Log.d(TAG, "ResultCode: RESULT_OK");
                    // Toast.makeText(this, "Image saved to: " + mediaFileUri.getPath(), Toast.LENGTH_LONG).show();

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mediaFileUri);

                    // Reducir la imagen a 800px solo si lo supera
                    bitmap = scaleBitmapDown(bitmap, 800);

                    imagePreview.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(this, "Error al procesar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "ResultCode: RESULT_CANCELED");
            } else {
                Log.d(TAG, "ResultCode: " + resultCode);
            }
        }


    }


    public void callRegister(View view) {

        String titulo = titulonew.getText().toString();
        String descripcion = descripcionnew.getText().toString();
        String ubicacion = ubicacionnew.getText().toString();
        String lati = latitudnew.getText().toString();
        String lngo = longitudnew.getText().toString();
        float lat = Float.parseFloat(lati);
        float lng = Float.parseFloat(lngo);
        Log.d("Lat", String.valueOf(lat));
        Log.d("Lng", String.valueOf(lng));
       /* if (nombre.isEmpty() || precio.isEmpty()) {
            Toast.makeText(this, "Nombre y Precio son campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }*/

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<ResponseMessage> call = null;

        if (mediaFileUri == null) {
            // Si no se incluye imagen hacemos un envío POST simple
            call = service.createDenuncia(titulo, descripcion, ubicacion, lat, lng, "1");
        } else {
            // Si se incluye hacemos envió en multiparts

            File file = new File(mediaFileUri.getPath());
            Log.d(TAG, "File: " + file.getPath() + " - exists: " + file.exists());

            // Podemos enviar la imagen con el tamaño original, pero lo mejor será comprimila antes de subir (byteArray)
            // RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

            Bitmap bitmap = BitmapFactory.decodeFile(mediaFileUri.getPath());

            // Reducir la imagen a 800px solo si lo supera
            bitmap = scaleBitmapDown(bitmap, 800);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);

            RequestBody tituloPart = RequestBody.create(MultipartBody.FORM, titulo);
            RequestBody descripcionPart = RequestBody.create(MultipartBody.FORM, descripcion);
            RequestBody ubicacaionPart = RequestBody.create(MultipartBody.FORM, ubicacion);
            RequestBody latPart = RequestBody.create(MultipartBody.FORM, String.valueOf(lat));
            RequestBody lngPart = RequestBody.create(MultipartBody.FORM, String.valueOf(lng));
            RequestBody userIdPart = RequestBody.create(MultipartBody.FORM, "1");

            call = service.createDenunciaWithImage(tituloPart, descripcionPart, ubicacaionPart, latPart, lngPart, userIdPart, imagenPart);
        }

        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        ResponseMessage responseMessage = response.body();
                        Log.d(TAG, "responseMessage: " + responseMessage);

                        Toast.makeText(NewDenunciaActivity.this, responseMessage.getMessage(), Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(NewDenunciaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(NewDenunciaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }


    /**
     * Permissions handler
     */

    private static final int PERMISSIONS_REQUEST = 200;

    private static String[] PERMISSIONS_LIST = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private boolean permissionsGranted() {
        for (String permission : PERMISSIONS_LIST) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }



    // Redimensionar una imagen bitmap
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    @Override
    public void onLocationChanged(Location location) {

    }
}
