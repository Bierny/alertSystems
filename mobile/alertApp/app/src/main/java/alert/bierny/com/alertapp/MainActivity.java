package alert.bierny.com.alertapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {
    ConnectionFactory factory = new ConnectionFactory();
    private static final String PREF_NAME = "BiernyPref";
    // Shared pref mode
    int PRIVATE_MODE = 0;
    SharedPreferences pref;

    private final static String QUEUE_NAME = "alertSystem";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button button;
    private TextView textView;
    private LoginButton fbButton;
    private TextView fbView;
    private CallbackManager callbackManager;
    private String locaLoca = "";
    private String firstName = "";
    private String lastName = "";
    private Uri pictureUri;
    private ImageView imageView;
    private ProfileTracker mProfileTracker;
    private AccessTokenTracker accessTokenTracker;
    private TextView helloText;

    private void setFacebookData(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (Profile.getCurrentProfile() == null) {
                            accessTokenTracker.startTracking();
                            mProfileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                    getDataFromFb();
                                    mProfileTracker.stopTracking();
                                }
                            };
                        } else {
                            getDataFromFb();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void getDataFromFb() {
        Profile profile = Profile.getCurrentProfile();
        String id = profile.getId();
        String link = profile.getLinkUri().toString();
        Log.i("Link", link);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("nameKey", profile.getFirstName());
        editor.commit();

        editor.putString("surnameKey", profile.getLastName());
        editor.commit();
        editor.putString("picUriKey", profile.getId());
        editor.commit();
        editor.putBoolean("logged", true);
        editor.commit();
        try {
            showLoggedPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        setupConnectionFactory();
        callbackManager = CallbackManager.Factory.create();
        //pref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        fbButton = findViewById(R.id.fb_login_btn);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        imageView = findViewById(R.id.image_id);
        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {
                System.out.print("");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    pref.edit().clear().apply();
                    showLoginPage();
                }
            }
        };


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locaLoca = location.getLatitude() + "," + location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        } else {
            configureButton();
        }
        locationManager.requestLocationUpdates("gps", 500, 0, locationListener);

        if (!pref.getBoolean("logged", false)) {
            showLoginPage();

        } else {
            try {
                showLoggedPage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private void showLoginPage() {
        setContentView(R.layout.nolog_layout);

    }

    private void showLoggedPage() throws IOException {
        setContentView(R.layout.activity_main);

        AQuery aq = new AQuery(this);
        boolean memCache = true;
        boolean fileCache = true;
        aq.id(R.id.image_id).image("https://graph.facebook.com/v2.2/" + pref.getString("picUriKey", "") + "/picture?height=120&type=normal", memCache, fileCache);
        helloText = findViewById(R.id.hello);

        helloText.setText("Witaj " + pref.getString("nameKey", "") + " " + pref.getString("surnameKey", ""));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

            }
        });

    }

    Thread publishThread;
    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();

    private void setupConnectionFactory() {
        String uri = "localhost";
        factory.setHost(uri);
    }

    public void okAgree(View view) {
        setContentView(R.layout.activity_main);
        helloText = findViewById(R.id.hello);
        helloText.setText("Witaj " + pref.getString("nameKey", "") + " " + pref.getString("surnameKey", ""));
        AQuery aq = new AQuery(this);
        boolean memCache = true;
        boolean fileCache = true;
        aq.id(R.id.image_id).image("https://graph.facebook.com/v2.2/" + pref.getString("picUriKey", "") + "/picture?height=120&type=normal", memCache, fileCache);


    }


    private class PublishMessage extends AsyncTask {
        private ConnectionFactory setConnectionFactory(){
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.43.49");
            factory.setUsername("alert");
            factory.setPassword("alert");
            return factory;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            String message = "0";
            ConnectionFactory factory = setConnectionFactory();
            ObjectMapper mapper = new ObjectMapper();
            try {
                message = mapper.writeValueAsString(inc);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Connection connection = null;
            try {
                connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        publishThread.interrupt();
    }


    public void start(View view) {
        Intent getScreenIntent = new Intent(this,
                SecondScreen.class);
        Notifier notifier = new Notifier();
        SharedPreferences myPrefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        notifier.setName(pref.getString("nameKey", ""));
        notifier.setSurname(pref.getString("surnameKey", ""));
        notifier.setPhone("564658791");
        Incident incident = new Incident();
        incident.setNotifier(notifier);
        incident.setLocation(locaLoca);

        final int result = 1;
        getScreenIntent.putExtra("data", incident);
        startActivityForResult(getScreenIntent, result);
    }

    Incident inc;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            super.onActivityResult(requestCode, resultCode, data);
            TextView view = (TextView) findViewById(R.id.textView);
            inc = (Incident) data.getSerializableExtra("data");
            new PublishMessage().execute();
            setContentView(R.layout.activity_send);


        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
