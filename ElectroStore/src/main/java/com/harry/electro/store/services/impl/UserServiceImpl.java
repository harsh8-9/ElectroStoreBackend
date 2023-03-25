package com.harry.electro.store.services.impl;

import com.harry.electro.store.dtos.UserDto;
import com.harry.electro.store.entities.User;
import com.harry.electro.store.exceptions.ResourceNotFoundException;
import com.harry.electro.store.repositories.UserRepo;
import com.harry.electro.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
@author :-
        Harshal Bafna
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
//        random user id
        String userId = UUID.randomUUID().toString();
        userDto.setId(userId);
//        dto -> entity
        User user = dtoToEntity(userDto);
        User savedUser = userRepo.save(user);
//        entity -> dto
        UserDto savedUserDto = entityToDto(savedUser);
        return savedUserDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found with id - " + userId, HttpStatus.NOT_FOUND));
        user.setName(userDto.getName());
        //email update
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());
        User updatedUser = userRepo.save(user);
        UserDto updatedUserDto = entityToDto(updatedUser);
        return updatedUserDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found with id - " + userId, HttpStatus.NOT_FOUND));
        userRepo.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = userRepo.findAll();
        List<UserDto> allUsersDto = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return allUsersDto;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found with id - " + userId, HttpStatus.NOT_FOUND));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User Not found with email - " + email, HttpStatus.NOT_FOUND));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepo.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    private UserDto entityToDto(User user) {
//        UserDto userDto = UserDto.builder()
//                .id(user.getId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .about(user.getAbout())
//                .gender(user.getGender())
//                .imageName(user.getImageName())
//                .build();
        UserDto userDto = mapper.map(user, UserDto.class);
        return userDto;
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .id(userDto.getId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
        User user = mapper.map(userDto, User.class);
        return user;
    }
}
