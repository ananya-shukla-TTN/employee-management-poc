package com.ttn.employeemanagement.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRegisterRequest {

    @NotBlank(message = "Department code must not be blank")
    @Size(max = 32)
    private String code;

    @NotBlank(message = "Department name must not be blank")
    private String name;

    @Size(max = 128)
    private String location;

    private Boolean active;
}
