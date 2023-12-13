package com.sfd.thesmartestate.employees.attendance;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_attendance")
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDateTime punchIn;
    private LocalDateTime punchOut;
    private Double punchInLat;
    private Double punchInLong;
    private Double punchOutLat;
    private Double punchOutLang;
    private Long userId;
}
