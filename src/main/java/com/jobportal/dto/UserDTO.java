package com.jobportal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobportal.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private UserRole role;

    @Pattern(regexp = "^\\+?[0-9\\s-]+$", message = "Invalid phone number format")
    private String phone;

    // Job seeker specific fields
    private String skills;
    private String experience;
    private String education;
    private String resume;

    // Employer specific fields
    private String companyName;
    private String industry;
    private String companyDescription;
    private String website;
}
