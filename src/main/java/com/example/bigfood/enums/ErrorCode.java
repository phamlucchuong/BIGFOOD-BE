package com.example.bigfood.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
  ACCOUNT_NOT_FOUND(1004, "Account not found !"),
  AUTHENTICATION_FAILED(1001, "Your password is incorrect !"),
  UNAUTHENTICATED(1004, "Bạn không có quyền truy cập"),
  PERMISSION_EXITED(1009, "Permission exited"),
  ROLE_ALREADY_EXISTS(1009, "Role exited"),
  ROLE_NOT_FOUND(1009, "Role exited"),
  RESTAURANT_CATEGORY_NOT_EXISTS(1008, "Restaurant category not exists"),
  RESTAURANT_CATEGORY_ALREADY_EXISTS(1005, "Restaurant category already exists"),
  RESTAURANT_NOT_EXISTS(1006, "Restaurant not exists"),
  RESTAURANT_ALREADY_EXISTS(1007, "Restaurant already exists"),
  FOOD_CATEGORY_ALREADY_EXISTS(1008, "Food category already exists"), 
  FOOD_CATEGORY_NOT_EXISTS(1009, "Food category not exists"), 
  FOOD_CATEGORY_HAS_FOODS(1010, "Food category has foods"), 
  FOOD_NOT_EXISTS(1011, "Food not exists"), 
  ORDER_DETAIL_NOT_FOUND(1012, "Order detail not found"), 
  EMAIL_EXISTED(1013, "Email already exists"), 
  ORDER_NOT_FOUND(1014, "Order not found"), 
  STATUS_SAME_AS_BEFORE(1015, "Status is the same as before"), 
  REVIEW_NOT_FOUND(1016, "Review not found"), 
  REVIEW_ALREADY_EXISTS(1017, "Review already exists"), 
  FOOD_CREATION_FAILED(1018, "Food creation failed"), 
  ORDER_DETAIL_ID_EMPTY(1019, "Order detail ID is empty"), 
  ORDER_NOT_COMPLETED(1020, "Order not completed"), 
  REVIEW_ALREADY_REPLIED(1021, "Review already replied"), 
  REVIEW_ALREADY_DELETED(1022, "Review already deleted"), 
  ID_INVALID(1023, "ID is invalid")
  ;

  private int code;
  private String message;
}