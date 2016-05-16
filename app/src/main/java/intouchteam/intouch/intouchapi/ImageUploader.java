package intouchteam.intouch.intouchapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ImageUploader extends AsyncTask<String, Void, String> {
    //ProgressDialog dialog;
    Map cloudinaryResult;
    Context context;
    Cloudinary cloudinary;
    ImageView imageView;
    String url;
    ImageDownloader imageDownloader;
    int type;

    public ImageUploader(Context context, ImageView imageView, Cloudinary cloudinary, int type) {
        this.cloudinary = cloudinary;
        this.context = context;
        this.imageView = imageView;
        this.type = type;
    }

    public void setCloudinary(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrl(String... params) {
        this.execute(params);

        return cloudinaryResult.get("url").toString();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
//        dialog = new ProgressDialog(context);
//        dialog.setCancelable(false);
//        dialog.show(context, "Uploading photo", "Please wait...", true, false);
//        dialog.dismiss();
    }

    @Override
    protected String doInBackground(String... paths) {
        File file = new File(paths[0]);
        cloudinaryResult = null;
        try {
            Map options;
            switch (type) {
                case 0:
                    options = ObjectUtils.asMap("transformation", new Transformation().radius("max"));
                    break;
                case 1:
                    options = ObjectUtils.emptyMap();
                    break;
                default: options = null;
            }
            cloudinaryResult = cloudinary.uploader().upload(file, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (cloudinaryResult == null) {
            Toast.makeText(InTouchApi.getContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(InTouchApi.getContext(), cloudinaryResult.get("url").toString(), Toast.LENGTH_SHORT).show();
            imageDownloader = new ImageDownloader(imageView);
            imageDownloader.execute(url);
        }
    }
}
