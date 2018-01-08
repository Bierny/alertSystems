package alert.bierny.com.alertapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    ConnectionFactory factory = new ConnectionFactory();
    private final static String QUEUE_NAME = "alertSystem";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupConnectionFactory();
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        new PublishMessage().execute();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textView.append("\n"+ location.getLatitude() + " "+ location.getLongitude());
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               requestPermissions(new String[]{
                       Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                       Manifest.permission.INTERNET
               },10);
                return;
            }
        }else{
            configureButton();
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        button.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view){
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
    private class PublishMessage extends AsyncTask  {


    @Override
    protected Object doInBackground(Object[] objects) {
        String message = "Yolki polki";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.99.1");
        factory.setUsername("alert");
        factory.setPassword("alert");
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
        publishThread.interrupt();
    }
}
