package com.phoenix.activitytracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StudentDTO {

    @NotBlank(message = "First Name must not be blank")
    private String firstName;

    @NotBlank(message = "Last Name must not be blank")
    private String lastName;
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank(message = "Password must not Be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    public StudentDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
