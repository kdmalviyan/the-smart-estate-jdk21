package com.sfd.thesmartestate.lms.entities;

import com.sfd.thesmartestate.employee.entities.Employee;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_comments")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")
public class Comment implements Serializable, Comparable<Comment> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "comment_type")
    private String commentType;

    @Override
    public int compareTo(Comment comment) {
        return this.id.compareTo(comment.id);
    }
}
