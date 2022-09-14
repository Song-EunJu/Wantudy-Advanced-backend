package com.example.wantudy.domain.application.domain;

import com.example.wantudy.domain.study.domain.Category;
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
@Table(name="applicationInterests")
public class ApplicationInterests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationInterestsId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Application.class)
    @JoinColumn(name="applicationId")
    @JsonBackReference
    @NotNull
    private Application application;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Category.class)
    @JoinColumn(name="categoryId")
    @JsonBackReference
    @NotNull
    private Category category;

    public static ApplicationInterests toEntity(Application application, Category category){
        return ApplicationInterests
                .builder()
                .application(application)
                .category(category)
                .build();
    }
}
