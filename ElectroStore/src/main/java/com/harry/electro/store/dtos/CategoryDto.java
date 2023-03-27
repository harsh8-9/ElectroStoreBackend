package com.harry.electro.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private String categoryId;
    @NotBlank(message = "Title is required!")
    @Size(min = 3, max = 60, message = "Title must be more than 3 characters long!")
    private String title;
    @NotBlank(message = "Description is required!")
    @Size(max = 250, message = "Description should not be more than 250 characters long!")
    private String description;
    private String coverImage;
}
