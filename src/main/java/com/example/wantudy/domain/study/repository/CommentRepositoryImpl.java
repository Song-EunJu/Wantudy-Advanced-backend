package com.example.wantudy.domain.study.repository;

import com.example.wantudy.domain.study.domain.Comment;
import com.example.wantudy.domain.study.domain.QComment;
import com.example.wantudy.domain.study.dto.CommentResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDto> findCommentByStudyId(long studyId) {

        List<CommentResponseDto> parentList = new ArrayList<>();
        List<CommentResponseDto> childList = new ArrayList<>();

        List<Comment> comments = queryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.study.studyId.eq(studyId).and(QComment.comment.parent.commentId.isNull()))
                .orderBy(QComment.comment.commentId.asc())
                .fetch();

        for(int i=0; i<comments.size(); i++){
            CommentResponseDto dto = new CommentResponseDto(comments.get(i));
            parentList.add(dto);
        }

        List<Comment> childComments = queryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.study.studyId.eq(studyId).and(QComment.comment.parent.commentId.isNotNull()))
                .orderBy(QComment.comment.commentId.asc())
                .fetch();

        if(!CollectionUtils.isEmpty(childComments)){
            for(int i=0; i<childComments.size(); i++){
                CommentResponseDto dto = new CommentResponseDto(childComments.get(i));
                dto.setParentId(childComments.get(i).getParent().getCommentId());
                childList.add(dto);
            }
            parentList.stream()
                    .forEach(parent -> {
                        parent.setChildren(childList.stream()
                                .filter(child -> child.getParentId().equals(parent.getCommentId()))
                                .collect(Collectors.toList()));
                    });

            return parentList;
        }

        return parentList;
    }
}
