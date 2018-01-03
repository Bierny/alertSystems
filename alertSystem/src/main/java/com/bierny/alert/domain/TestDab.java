package com.bierny.alert.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TestDab.
 */
@Entity
@Table(name = "test_dab")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TestDab implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "second")
    private LocalDate second;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TestDab name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getSecond() {
        return second;
    }

    public TestDab second(LocalDate second) {
        this.second = second;
        return this;
    }

    public void setSecond(LocalDate second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestDab testDab = (TestDab) o;
        if (testDab.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testDab.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestDab{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", second='" + getSecond() + "'" +
            "}";
    }
}
