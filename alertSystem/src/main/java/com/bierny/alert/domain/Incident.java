package com.bierny.alert.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
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

    @Column(name = "service_kind")
    private String serviceKind;

    @Column(name = "life_danger")
    private Boolean lifeDanger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public Incident kind(String kind) {
        this.kind = kind;
        return this;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLocation() {
        return location;
    }

    public Incident location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServiceKind() {
        return serviceKind;
    }

    public Incident serviceKind(String serviceKind) {
        this.serviceKind = serviceKind;
        return this;
    }

    public void setServiceKind(String serviceKind) {
        this.serviceKind = serviceKind;
    }

    public Boolean isLifeDanger() {
        return lifeDanger;
    }

    public Incident lifeDanger(Boolean lifeDanger) {
        this.lifeDanger = lifeDanger;
        return this;
    }

    public void setLifeDanger(Boolean lifeDanger) {
        this.lifeDanger = lifeDanger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Incident incident = (Incident) o;
        if (incident.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), incident.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Incident{" +
            "id=" + getId() +
            ", kind='" + getKind() + "'" +
            ", location='" + getLocation() + "'" +
            ", serviceKind='" + getServiceKind() + "'" +
            ", lifeDanger='" + isLifeDanger() + "'" +
            "}";
    }
}
