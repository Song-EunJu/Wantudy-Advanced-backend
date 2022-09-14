package com.example.wantudy.domain.study.repository;

import com.example.wantudy.domain.study.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>  {
    Category findByCategoryName(String categoryName);
    List<Category> findAllByParentIsNull();
//    Category findByParentId(long parentId);

//    @Query("select c from Category c left join c.parent p order by p.id asc nulls first, c.id asc")
//    List<Category> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
}
