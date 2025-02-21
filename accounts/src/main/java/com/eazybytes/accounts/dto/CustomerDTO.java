package com.eazybytes.accounts.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "des"
)
public class CustomerDTO {
    @Schema(description = "Name",example = "ducanh")
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 5, max = 30, message = "The length of name must between 5 and 30")
    private String name;
    @Schema(description = "Email here",example = "ducanh@gmail.com")
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email must be a valid email")
    private String email;

    @Schema(description = "Phone Number here",example = "09414141934")
    @NotEmpty(message = "Phone number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Phone number must 10 digits")
    private String mobileNumber;

    private AccountsDto accountsDTO;

}
