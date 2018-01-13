package com.bierny.alert;

import com.bierny.alert.config.ApplicationProperties;
import com.bierny.alert.config.DefaultProfileUtil;

import com.bierny.alert.domain.Incident;
import com.bierny.alert.domain.IncidentServiceEntity;
import com.bierny.alert.domain.Notifier;
import com.bierny.alert.repository.IncidentRepository;
import com.bierny.alert.repository.IncidentServiceRepository;
import com.bierny.alert.service.QueueIncidentsSingleton;
import com.bierny.alert.service.SaveAlertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.github.jhipster.config.JHipsterConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@ComponentScan
@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
@EnableDiscoveryClient
public class AlertSystemApp {
    @Autowired
    private IncidentRepository incidentRepository;
    @Autowired
    private IncidentServiceRepository incidentServiceRepository;
    @Autowired
    private SaveAlertService saveAlertService;

    private static final Logger log = LoggerFactory.getLogger(AlertSystemApp.class);

    private final Environment env;

    public AlertSystemApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes alertSystem.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://jhipster.github.io/profiles/">https://jhipster.github.io/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() throws IOException, TimeoutException {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("alertSystem", false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        makeTestData();
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException{
                String message = new String(body, "UTF-8");
                saveAlertService.saveAlertToDatabase(message);
            }
        };
        channel.basicConsume("alertSystem", true, consumer);
    }

    private void makeTestData() {
        Notifier notifier = new Notifier();
        notifier.setId(1L);
        notifier.setName("john");
        notifier.setSurname("Blaskowicz");
        notifier.setPhone("656565135135");
        for(int i = 0; i <15; i++) {
            Incident incident = new Incident();
            if(i % 2 == 1){
            incident.setIsNotifierVictim(false);
            }else{
                incident.setIsNotifierVictim(true);

            }
            incident.setKind("wypadek");
            if(i % 2 == 1){
                incident.setLifeDanger(false);
            }else{
                incident.setLifeDanger(true);

            }
            incident.setLocation("-45.363882,131.044922");
            incident.setNotifier(notifier);
            incident.setOtherCircumstances("nic ciekawego");
            if(i % 2 == 1){
                incident.setSufferer(false);
            }else{
                incident.setSufferer(true);

            }
            incident.setHowManyVictims(i);
            incident.setFillingDate(new Date());
            incidentRepository.save(incident);

        }

       Queue queue = QueueIncidentsSingleton.getInstance().getQueue();
        queue.addAll(incidentRepository.findAll().stream().collect(Collectors.toList()));


    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException,java.io.IOException,
        java.lang.InterruptedException, java.util.concurrent.TimeoutException {
        SpringApplication app = new SpringApplication(AlertSystemApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}\n\t" +
                "External: \t{}://{}:{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            env.getProperty("server.port"),
            protocol,
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            env.getActiveProfiles());




    }
}
