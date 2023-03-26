package com.harry.electro.store.services.impl;

import com.harry.electro.store.dtos.PageableResponse;
import com.harry.electro.store.dtos.UserDto;
import com.harry.electro.store.entities.User;
import com.harry.electro.store.exceptions.ResourceNotFoundException;
import com.harry.electro.store.repositories.UserRepo;
import com.harry.electro.store.services.UserService;
import com.harry.electro.store.utils.PagingAndSorting;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Value("${user.profile.image.path}")
    private String imagePath;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

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
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found with id - " + userId));
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
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found with id - " + userId));
        // delete user image
        String fullImagePath = imagePath + user.getImageName();
        try {
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Image doesnt exists for user " + userId);
        } catch (IOException e) {
            logger.info("Image doesnt exists for user " + userId);
        }
        userRepo.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = ("desc".equalsIgnoreCase(sortDir)) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<User> page = userRepo.findAll(pageable);
        PageableResponse<UserDto> response = PagingAndSorting.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found with id - " + userId));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User Not found with email - " + email));
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
