package com.harry.electro.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

/*
@author :-
        Harshal Bafna
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private String fileName;
    private String message;
    private boolean success;
    private HttpStatus status;
}
