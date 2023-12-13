package com.sfd.thesmartestate.lms.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_budget")
@Data
public class Budget implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_amount")
    private Double startAmount;

    @Column(name = "start_amount_unit")
    private String startUnit;

    @Column(name = "end_amount")
    private Double endAmount;

    @Column(name = "end_amount_unit")
    private String endUnit;

    @Column(name = "absolute_start_amount")
    private Double absoluteStartAmount;

    @Column(name = "absolute_end_amount")
    private Double absoluteEndAmount;
}
