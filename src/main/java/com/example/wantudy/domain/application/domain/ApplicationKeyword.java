package com.example.wantudy.domain.application.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="applicationKeyword")
public class ApplicationKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationKeywordId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Application.class)
    @JoinColumn(name="applicationId")
    @JsonBackReference
    @NotNull
    private Application application;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Keyword.class)
    @JoinColumn(name="keywordId")
    @JsonBackReference
    @NotNull
    private Keyword keyword;

    public static ApplicationKeyword toEntity(Application application, Keyword keyword){
        return ApplicationKeyword
                .builder()
                .application(application)
                .keyword(keyword)
                .build();
    }
}
