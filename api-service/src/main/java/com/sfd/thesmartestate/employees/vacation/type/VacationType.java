package com.sfd.thesmartestate.employees.vacation.type;

import com.sfd.thesmartestate.employees.vacation.exceptions.VacationException;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_vacation_type")
@Data
public class VacationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private boolean isActive;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void validate() {
        if(Objects.isNull(name) || "".equals(name.trim())) {
            throw new VacationException("Vacation type name can't be null or empty");
        }
    }
}
