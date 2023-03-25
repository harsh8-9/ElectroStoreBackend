package com.harry.electro.store.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
@author :-
        Harshal Bafna
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    @Column(name = "user_name", length = 50, nullable = false)
    private String name;
    @Column(name = "user_email", length = 100, unique = true, nullable = false)
    private String email;
    @Column(name = "user_password", nullable = false)
    private String password;
    private String gender;
    @Column(length = 1000)
    private String about;
    @Column(name = "user_image_name")
    private String imageName;

}
