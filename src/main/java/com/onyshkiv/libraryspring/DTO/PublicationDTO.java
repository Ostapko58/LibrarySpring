package com.onyshkiv.libraryspring.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicationDTO {
    @NotBlank(message = "Bad author name")
    private String name;
}

