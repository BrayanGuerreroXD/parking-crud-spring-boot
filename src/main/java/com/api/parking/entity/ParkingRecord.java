package com.api.parking.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(name = "parkingrecord")
public class ParkingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pare_id")
    private Long id;

    @Column(name = "pare_entrydate", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "pare_exitdate")
    private LocalDateTime exitDate;

    @ManyToOne
    @JoinColumn(name = "vehi_id", referencedColumnName = "vehi_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "park_id",  referencedColumnName = "park_id", nullable = false)
    private Parking parking;
}