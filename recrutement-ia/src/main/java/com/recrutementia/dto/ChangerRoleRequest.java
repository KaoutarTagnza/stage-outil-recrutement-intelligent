package com.recrutementia.dto;

import com.recrutementia.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangerRoleRequest {

    @NotNull
    private Role role;
}