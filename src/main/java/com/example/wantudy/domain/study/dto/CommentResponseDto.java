package com.example.wantudy.domain.study.dto;

import com.example.wantudy.domain.study.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    @ApiModelProperty(value="댓글 id")
    private Long commentId;

    @ApiModelProperty(value="부모 댓글 id")
    private Long parentId;

    @ApiModelProperty(value="등록된 스터디 게시글 id")
    private Long studyId;

    @ApiModelProperty(value="댓글 내용")
    private String content;

    @ApiModelProperty(value="자식 댓글")
    private List<CommentResponseDto> children = new ArrayList<>();

    @ApiModelProperty(value = "댓글 등록 시간", example = "2022-05-22 07:30:55")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    /* Entity -> Dto*/
    public CommentResponseDto(Comment comment) {
        if(comment.getParent() != null){
            this.parentId = comment.getParent().getCommentId();
        }
        this.parentId = null;
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createAt = comment.getCreateAt();
        this.studyId = comment.getStudy().getStudyId();
    }
}
