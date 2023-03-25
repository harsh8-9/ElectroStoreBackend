package com.harry.electro.store.dtos;

import com.harry.electro.store.utils.ImageNameValid;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/*
@author :-
        Harshal Bafna
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class UserDto {
    private String id;
    @Size(min = 3, max = 50, message = "Invalid name!")
    private String name;
    @NotBlank(message = "Email is required!")
//    @Email(message = "Invalid Email!")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email!")
    private String email;
    @NotBlank(message = "Password is required!")
    private String password;
    private String gender;
    @Size(max = 1000)
    private String about;
    @ImageNameValid
    private String imageName;

    // pattern
    // custom validator
}
