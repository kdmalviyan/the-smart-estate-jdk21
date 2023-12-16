package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.repositories.CommentRepository;
import com.sfd.thesmartestate.employee.entities.Employee;
import com.sfd.thesmartestate.employee.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EmployeeService employeeService;

    @Override
    public List<Comment> findAll(Long leadId) {
        return commentRepository.findAll();
    }

    @Override
    public Comment create(Comment comment) {
        Employee loggedImEmployee = employeeService.findLoggedInEmployee();
        comment.setCreatedBy(loggedImEmployee);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public Comment update(Comment comment) {
        if (Objects.nonNull(comment)) {
            return commentRepository.saveAndFlush(comment);
        }
        return null;
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
