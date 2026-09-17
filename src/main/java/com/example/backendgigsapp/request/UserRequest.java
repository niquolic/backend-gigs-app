package com.example.backendgigsapp.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
  @NotNull(message="Login is required")
  private String login;

  @NotNull(message="Password is required")
  private String password;
}
