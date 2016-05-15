package intouchteam.intouch.intouchapi;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryAPI {
    private Cloudinary cloudinary;

    public CloudinaryAPI() {
        Map config = new HashMap();
        config.put("cloud_name", "intouch");
        config.put("api_key", "782299839685479");
        config.put("api_secret", "qrAenu5H2IiAMUWs0d4EZz7EYco");
        cloudinary = new Cloudinary(config);
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }
}
