package com.harry.electro.store.repositories;

import com.harry.electro.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 *  @author :-
 *       Harshal Bafna
 */
@Repository
public interface CategoryRepo extends JpaRepository<Category, String> {
}
