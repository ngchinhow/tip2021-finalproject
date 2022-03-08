package com.tfip2021.module4.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "event")
public class DatabaseEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private DatabaseUser owner;

    private String provider;

    @Column(name = "providereventid", length = 500)
    private String providerEventId;

    private String agenda;
    private String location;

    @Column(name = "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @CreationTimestamp
    @Column(name = "createdat", updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updatedat", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
