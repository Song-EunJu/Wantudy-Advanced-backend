package com.example.wantudy.domain.study.dto;

import com.example.wantudy.domain.study.domain.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel
public class CommentRequestDto {

    @ApiModelProperty(value = "댓글 내용", required = true, example = "댓글 입니다.")
    private String content;

    public Comment toEntity(CommentRequestDto commentRequestDto){
        return Comment.builder()
                .content(content)
                .build();
    }

}
