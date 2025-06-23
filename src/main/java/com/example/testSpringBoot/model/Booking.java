package com.example.testSpringBoot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "teacher_id", nullable = false)
    private Integer teacherId;

    @Column(name = "slot_id", nullable = false)
    private Integer slotId;

    @Column(name = "booking_date", nullable = false)
    private java.sql.Date bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private java.sql.Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "teacher_id", insertable = false, updatable = false)
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "slot_id", insertable = false, updatable = false)
    private TimeSlot timeSlot;

    public enum Status {
        PENDING, CONFIRMED, CANCELLED
    }
}