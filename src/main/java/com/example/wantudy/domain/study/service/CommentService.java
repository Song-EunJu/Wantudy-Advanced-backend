package com.example.wantudy.domain.study.service;

import com.example.wantudy.domain.study.domain.Study;
import com.example.wantudy.domain.study.domain.Comment;
import com.example.wantudy.domain.study.dto.CommentRequestDto;
import com.example.wantudy.domain.study.repository.CommentRepository;
import com.example.wantudy.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final StudyRepository studyRepository;

    public Optional<Comment> findCommentById(long commentId){
        return commentRepository.findById(commentId);
    }

    public long saveCommentParent(long studyId, CommentRequestDto commentRequestDto) {

        Comment comment = commentRequestDto.toEntity(commentRequestDto);
        Optional<Study> study = studyRepository.findById(studyId);

        commentRepository.save(comment);
        comment.setStudy(study.get());
        comment.setParent(null);

        return comment.getCommentId();
    }


    public long saveCommentChild(long studyId, long parentId, CommentRequestDto commentRequestDto) {

        Optional<Study> study = studyRepository.findById(studyId);
        Optional<Comment> parent = commentRepository.findById(parentId);

        //부모 댓글이 삭제 됐거나, 부모 댓글이 아니면 id 그대로 리턴
        if (parent.isEmpty() || parent.get().getParent() != null) {
            return parentId;
        }
        Comment comment = commentRequestDto.toEntity(commentRequestDto);

        commentRepository.save(comment);
        comment.setStudy(study.get());
        comment.setParent(parent.get());

        parent.get().addChildComment(comment);

        return comment.getCommentId();
    }
}
