package com.hospicebangladesh.pms;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadPictureActivity extends AppCompatActivity {
    private static final String TAG = "UploadPictureActivity";
    static final int PICK_IMAGE_REQUEST = 1;
    String filePath;
    File file;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    @Bind(R.id.imageView)
    ImageView _imageView;

    @Bind(R.id.button_choose)
    Button _button_choose;

    @Bind(R.id.button_upload)
    Button _button_upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        _button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });

        _button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    try {
                        Random rand = new Random();
                        int name = rand.nextInt(9999999) + 1000000;
                        uploadImage(file, "pms_" + name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri picUri = data.getData();
                filePath = getPath(picUri);
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picUri));
                    file = convertBitmapToFile(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                Log.d("picUri", picUri.toString());
                Log.d("filePath", filePath);

                _imageView.setImageURI(picUri);

            }

        }

    }


    public void uploadImage(File image, String imageName) throws IOException {

        final ProgressDialog progressDialog = new ProgressDialog(UploadPictureActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", imageName, RequestBody.create(MEDIA_TYPE_PNG, image))
                .build();

        Request request = new Request.Builder().url("http://2aitbd.com/pms/api/upload_picture.php")
                .post(requestBody).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.d(TAG, " onFailure" + e.toString());
                UploadPictureActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Server is not responding properly", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, " onResponse");

                final String serverResponse = response.body().string();
                UploadPictureActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(serverResponse);
                            int success = json.getInt("success");
                            String message = json.getString("message");
                            if (success == 1) {
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                   progressDialog.dismiss();
                            } else {
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                   progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });


    }


    private File convertBitmapToFile(@NonNull Bitmap bitmap) {
        try {
            // use getCacheDir to temporarily store the file
            File file = new File(getCacheDir(), "image.png");
            file.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
