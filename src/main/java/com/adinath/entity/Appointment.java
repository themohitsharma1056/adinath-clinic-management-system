package com.adinath.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String department;

    @Column(nullable = false, length = 100)
    private String doctor;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false, length = 30)
    private String timeSlot;

    @Column(nullable = false, length = 100)
    private String patientName;

    @Column(nullable = false, length = 10)
    private String mobileNumber;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String reason;

    @Column(length = 500)
    private String additionalDetails;

    @Column(nullable = false, length = 20)
    private String status;

}