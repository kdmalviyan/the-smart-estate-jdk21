package com.sfd.thesmartestate.lms.repositories;

import com.sfd.thesmartestate.lms.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
