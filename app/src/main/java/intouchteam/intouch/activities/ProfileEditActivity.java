package intouchteam.intouch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Transformation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import intouchteam.intouch.R;
import intouchteam.intouch.intouchapi.CloudinaryAPI;
import intouchteam.intouch.adapter.MainActivityEventsAdapter;
import intouchteam.intouch.intouchapi.ImageDownloader;
import intouchteam.intouch.intouchapi.ImageUploader;
import intouchteam.intouch.intouchapi.InTouchApi;
import intouchteam.intouch.intouchapi.InTouchCallback;
import intouchteam.intouch.intouchapi.InTouchServerProfile;
import intouchteam.intouch.intouchapi.model.Profile;

public class ProfileEditActivity extends AppCompatActivity {

    final static int GET_PHOTO_FROM_GALLERY_PROFILE = 0;
    final static int GET_PHOTO_FROM_GALLERY_BACKGROUND = 1;

    ImageView changeBackground;
    ImageView background;
    ImageView profile;
    CloudinaryAPI cloudinaryAPI;
    Cloudinary cloudinary;
    Map cloudinaryResult;
    String image_url;
    String background_url;
    int UPLOADING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(InTouchApi.getProfile().getFirstName() + " " + InTouchApi.getProfile().getLastName());
        setSupportActionBar(toolbar);
        setOnImageViewBackgroundClickListener();
        setOnImageViewProfileLongClickListener();
        setProfileFields();

        cloudinaryAPI = new CloudinaryAPI();
        cloudinary = cloudinaryAPI.getCloudinary();

        if(InTouchApi.getProfile().getUserImageURL() != null) {
            image_url = InTouchApi.getProfile().getUserImageURL();
        }
        if(InTouchApi.getProfile().getBackgroundURL() != null) {
            background_url = InTouchApi.getProfile().getBackgroundURL();
        }
    }

    private void setOnImageViewBackgroundClickListener() {
        changeBackground = (ImageView) findViewById(R.id.change_profile_background);
        changeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GET_PHOTO_FROM_GALLERY_BACKGROUND);
            }
        });
    }

    private void setOnImageViewProfileLongClickListener() {
        profile = (ImageView) findViewById(R.id.profile_image);
        profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GET_PHOTO_FROM_GALLERY_PROFILE);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Uri uri = data.getData();
            switch (requestCode) {
                case GET_PHOTO_FROM_GALLERY_BACKGROUND: {
                    startUpload(getAbsolutePath(uri), 1);
                    break;
                }
                case GET_PHOTO_FROM_GALLERY_PROFILE: {
                    startUpload(getAbsolutePath(uri), 0);
                    break;
                }
            }
        }
    }

    private String getAbsolutePath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private void startUpload(String filePath, final int type) {
        UPLOADING = 0;
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            protected String doInBackground(String... paths) {
                File file = new File(paths[0]);
                cloudinaryResult = null;
                try {
                    Map options;
                    switch (type) {
                        case 0:
                            options = ObjectUtils.asMap("transformation", new Transformation().radius("max").crop("crop").chain());
                            break;
                        case 1:
                            options = ObjectUtils.emptyMap();
                            break;
                        default:
                            options = ObjectUtils.emptyMap();
                    }
                    cloudinaryResult = cloudinary.uploader().upload(file, options);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String error) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                UPLOADING = 1;
                if (cloudinaryResult == null) {
                    Toast.makeText(InTouchApi.getContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    switch (type) {
                        case 0:
                            image_url = cloudinaryResult.get("url").toString();
                            new ImageDownloader(profile).execute(image_url);
                            break;
                        case 1:
                            background_url = cloudinaryResult.get("url").toString();
                            new ImageDownloader(background).execute(background_url);
                            break;
                    }
                }
            }
        };
        task.execute(filePath);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            if (UPLOADING == 1)
                UpdateProfile();
            return true;
        } else if(id == R.id.action_clear) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UpdateProfile() {
        TextView phone = ((TextView) findViewById(R.id.profile_phone));
        TextView email = ((TextView) findViewById(R.id.profile_email));
        TextView skype = ((TextView) findViewById(R.id.profile_skype));

        InTouchServerProfile.update(phone.getText().toString(),
                email.getText().toString(),
                skype.getText().toString(),
                image_url,
                background_url,
                new InTouchCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        Profile profile = new Gson().fromJson(result.get("user").getAsString(), Profile.class);
                        InTouchApi.setProfile(profile);
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(InTouchApi.getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_create, menu);
        return true;
    }

    private void setProfileFields() {
        TextView phone = ((TextView) findViewById(R.id.profile_phone));
        TextView email = ((TextView) findViewById(R.id.profile_email));
        TextView skype = ((TextView) findViewById(R.id.profile_skype));

        phone.setText(InTouchApi.getProfile().getPhone());
        email.setText(InTouchApi.getProfile().getEmail());
        skype.setText(InTouchApi.getProfile().getSkype());
        background = (ImageView) findViewById(R.id.profile_image_background);
        if(InTouchApi.getProfile().getUserImageURL() != null) {
            new ImageDownloader(profile).execute(InTouchApi.getProfile().getUserImageURL());
        }
        if(InTouchApi.getProfile().getBackgroundURL() != null) {
            new ImageDownloader(background).execute(InTouchApi.getProfile().getBackgroundURL());
        }
    }
}

