package com.example.bigfood.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
  ACCOUNT_NOT_FIND(1000, "No account yet !"),
  AUTHENTICATION_FAILED(1001, "Your password is incorrect !"),
  UNAUTHENTICATED(1004, "Bạn không có quyền truy cập"),
  PERMISSION_EXITED(1009, "Permission exited"),
  ROLE_ALREADY_EXISTS(1009, "Role exited"),
  ROLE_NOT_FOUND(1009, "Role exited"),
  RESTAURANT_CATEGORY_NOT_EXISTS(1008, "Restaurant category not exists"),
  RESTAURANT_CATEGORY_ALREADY_EXISTS(1005, "Restaurant category already exists"),
  RESTAURANT_NOT_EXISTS(1006, "Restaurant category not exists"),
  RESTAURANT_ALREADY_EXISTS(1007, "Restaurant already exists"),
  ;

  private int code;
  private String message;
}