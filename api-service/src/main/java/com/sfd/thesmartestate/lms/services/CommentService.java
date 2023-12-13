package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAll(Long leadId);

    Comment create(Comment comment);

    Comment update(Comment comment);

    Comment findById(Long id);

    void delete(Comment comment);
}
