package com.sfd.thesmartestate.common.responsemapper;

import com.sfd.thesmartestate.lms.dto.CommentDto;
import com.sfd.thesmartestate.lms.entities.Comment;

public class CommentsMapper {
    private CommentsMapper() {
        throw new IllegalStateException("Constructor can't be initialized");
    }

    public static CommentDto mapToComments(Comment comment) {
        CommentDto response = new CommentDto();
        if (comment != null) {
            response.setId(comment.getId());
            response.setCommentType(comment.getCommentType());
            response.setCreatedAt(comment.getCreatedAt());
            response.setMessage(comment.getMessage());
            response.setCreatedBy(UserResponseMapper.mapToUserResponse(comment.getCreatedBy()));
        }
        return response;

    }
}
