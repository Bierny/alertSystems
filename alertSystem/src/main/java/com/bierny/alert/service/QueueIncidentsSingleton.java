package com.bierny.alert.service;

import com.bierny.alert.domain.Incident;
import com.bierny.alert.domain.IncidentServiceEntity;
import com.bierny.alert.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Created by damian.biernat on 04.01.2018.
 */
public class QueueIncidentsSingleton {
  private static volatile QueueIncidentsSingleton queueIncidentsSingleton;

  private QueueIncidentsSingleton(){
    queue = new LinkedList<>();
  }
  public static synchronized QueueIncidentsSingleton getInstance(){
    if(queueIncidentsSingleton == null){
      queueIncidentsSingleton = new QueueIncidentsSingleton();
    }
    return queueIncidentsSingleton;
  }


  Queue<Incident> queue = null;


  public Queue<Incident> getQueue() {
    return this.queue;
  }

  public void addIncident(Incident incident) {
    queue.add(incident);
  }

  public Incident getFirstIncident() {
    return queue.poll();
  }
  public Incident peekFirstIncident() {
    return queue.peek();
  }


}
