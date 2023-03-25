package com.harry.electro.store.repositories;

import com.harry.electro.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
@author :-
        Harshal Bafna
 */
@Repository
public interface UserRepo extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findByNameContaining(String keywords);
}
