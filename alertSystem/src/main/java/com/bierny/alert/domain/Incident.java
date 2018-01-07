package com.bierny.alert.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A Incident.
 */
@Entity
@Table(name = "incident")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Incident implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kind")
    private String kind;

    @Column(name = "location")
    private String location;


    @Column(name = "life_danger")
    private Boolean lifeDanger;

    @Column(name = "notifier")
    private Notifier notifier;

    @Column (name = "isNotifierVictim")
    private Boolean isNotifierVictim;

    @Column(name="sufferer")
    private Boolean sufferer;

    @Column(name= "howManyVictims" )
    private Integer howManyVictims;

    @Column(name = "otherCircumstances")
    private String otherCircumstances;

    @Column(name = "fillingDate")
    private Date fillingDate;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @Column(name = "serviceDate")
    private Date serviceDate;

    @Column(name = "incidentStatus")
    private IncidentStatus incidentStatus = IncidentStatus.SENT;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getLifeDanger() {
        return lifeDanger;
    }

    public void setLifeDanger(Boolean lifeDanger) {
        this.lifeDanger = lifeDanger;
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    public Boolean getIsNotifierVictim() {
        return isNotifierVictim;
    }

    public void setIsNotifierVictim(Boolean isNotifierVictim) {
        this.isNotifierVictim = isNotifierVictim;
    }

    public Boolean getSufferer() {
        return sufferer;
    }

    public void setSufferer(Boolean sufferer) {
        this.sufferer = sufferer;
    }

    public Integer getHowManyVictims() {
        return howManyVictims;
    }

    public void setHowManyVictims(Integer howManyVictims) {
        this.howManyVictims = howManyVictims;
    }

    public String getOtherCircumstances() {
        return otherCircumstances;
    }

    public void setOtherCircumstances(String otherCircumstances) {
        this.otherCircumstances = otherCircumstances;
    }

    public Date getFillingDate() {
        return fillingDate;
    }

    public void setFillingDate(Date fillingDate) {
        this.fillingDate = fillingDate;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public IncidentStatus getIncidentStatus() {
        return incidentStatus;
    }

    public void setIncidentStatus(IncidentStatus incidentStatus) {
        this.incidentStatus = incidentStatus;
    }
}
