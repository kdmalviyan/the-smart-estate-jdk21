package com.sfd.thesmartestate.lms.controllers;

import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.services.CommentService;
import com.sfd.thesmartestate.lms.services.LeadUpdateService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "lead/comment")
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@CrossOrigin("*")
public class CommentController {
    private final CommentService commentService;
    private final LeadUpdateService leadUpdateService;

    @PostMapping("/{leadId}")
    public ResponseEntity<Comment> create(@RequestBody Comment comment, @PathVariable("leadId") Long leadId) {
        comment.setCommentType("Remark");
        Comment commentAdded = commentService.create(comment);
        leadUpdateService.updateLeadComment(leadId, commentAdded);
        return ResponseEntity.ok(commentAdded);
    }
}
