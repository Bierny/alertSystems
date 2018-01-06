import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.net.UnknownHostException;

/**
 * Created by damian.biernat on 13.08.2017.
 */
public class MainClassJava {

  private final static String QUEUE_NAME = "alertSystem";

  public static void main(String[] argv)
      throws UnknownHostException,java.io.IOException,
      java.lang.InterruptedException, java.util.concurrent.TimeoutException {



    Notifier notifier = new Notifier();
    notifier.setId(1L);
    notifier.setName("john");
    notifier.setSurname("Blaskowicz");
    notifier.setPhone("656565135135");

    Incident incident = new Incident();
    incident.setHowManyVictims(2);
    incident.setIsNotifierVictim(false);
    incident.setKind("wypadek");
    incident.setLifeDanger(true);
    incident.setLocation("123123x123123123");
    incident.setNotifier(notifier);
    incident.setOtherCircumstances("nic ciekawego");
    incident.setSufferer(false);
    ObjectMapper mapper = new ObjectMapper();

  for(int i = 0; i <5; i++) {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    String message = mapper.writeValueAsString(incident);
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }
  }
}
