package alert.bierny.com.alertapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupConnectionFactory();
        new PublishMessage().execute();
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
