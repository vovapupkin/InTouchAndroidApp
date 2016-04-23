package intouchteam.intouch;


import com.vk.sdk.VKSdk;

public class Application extends android.app.Application {

    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        VKSdk.initialize(this);
    }

    public static synchronized Application getInstance() {
        return mInstance;
    }
}