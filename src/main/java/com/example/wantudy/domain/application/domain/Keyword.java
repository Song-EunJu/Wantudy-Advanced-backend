package com.example.wantudy.domain.application.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="keyword")
public class Keyword {
    @Id
    @Column(name = "keywordId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;

    @NotNull
    private String keyword;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "keyword",
            targetEntity = ApplicationKeyword.class,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private Set<ApplicationKeyword> applicationKeywords = new HashSet<>();

    @Builder
    public Keyword(String keyword) {
        this.keyword = keyword;
    }
}

