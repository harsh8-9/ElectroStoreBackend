package com.harry.electro.store.controllers;

import com.harry.electro.store.dtos.ApiResponse;
import com.harry.electro.store.dtos.UserDto;
import com.harry.electro.store.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
@author :-
        Harshal Bafna
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //    create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        logger.info("USER : User created successfully!");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //    update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
        UserDto updatedUser = userService.updateUser(userDto, userId);
        logger.info("USER : User updated successfully!");
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //    delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        logger.info("USER : User deleted successfully!");
        ApiResponse response = ApiResponse.builder()
                .message("User is deleted successfully!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    get all
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    //    get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    //    get by email
    @GetMapping("email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    //    search user
    @GetMapping("search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords) {
        List<UserDto> userDtos = userService.searchUser(keywords);
        return ResponseEntity.ok(userDtos);
    }
//    other feature
}
