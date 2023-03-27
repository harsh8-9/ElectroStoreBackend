package com.harry.electro.store.controllers;

import com.harry.electro.store.dtos.ApiResponse;
import com.harry.electro.store.dtos.ImageResponse;
import com.harry.electro.store.dtos.PageableResponse;
import com.harry.electro.store.dtos.UserDto;
import com.harry.electro.store.services.FileService;
import com.harry.electro.store.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/*
 *  @author :-
 *       Harshal Bafna
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${user.profile.image.path}")
    private String userImageUploadPath;

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
        ApiResponse response = ApiResponse.builder().message("User is deleted successfully!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    get all
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<UserDto> allUsers = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
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

    //    upload image
    @PostMapping("image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image,
                                                         @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(image, userImageUploadPath);
        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);
        userService.updateUser(user, userId);
        ImageResponse response = ImageResponse.builder()
                .fileName(imageName)
                .success(true)
                .status(HttpStatus.CREATED)
                .message("Image uploaded Successfully!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //    serve image
    @GetMapping("image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        logger.info("User Image Name : {}", user.getImageName());
        InputStream inputStream = fileService.getResource(userImageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
}
