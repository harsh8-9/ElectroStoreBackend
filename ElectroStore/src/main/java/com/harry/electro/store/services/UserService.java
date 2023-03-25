package com.harry.electro.store.services;

import com.harry.electro.store.dtos.UserDto;
import com.harry.electro.store.entities.User;

import java.util.List;

/*
@author :-
        Harshal Bafna
 */
public interface UserService {

//    create
    UserDto createUser(UserDto userDto);
//    update
    UserDto updateUser(UserDto userDto, String userId);
//    delete
    void deleteUser(String userId);
//    get all
    List<UserDto> getAllUsers();
//    get one
    UserDto getUserById(String userId);
//    search by email
    UserDto getUserByEmail(String email);
//    Search user
    List<UserDto> searchUser(String keyword);
    // other
}



