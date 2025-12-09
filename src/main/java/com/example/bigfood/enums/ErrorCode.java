package com.example.bigfood.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định"),
ACCOUNT_NOT_FIND(1000, "Không tìm thấy tài khoản!"),
ACCOUNT_DELETED(1003, "Tài khoản đã bị xóa!"),
AUTHENTICATION_FAILED(1001, "Mật khẩu của bạn không đúng!"),
UNAUTHENTICATED(1004, "Bạn không có quyền truy cập"),
PERMISSION_EXITED(1009, "Quyền đã tồn tại"),
ROLE_ALREADY_EXISTS(1009, "Vai trò đã tồn tại"),
ROLE_NOT_FOUND(1009, "Không tìm thấy vai trò"),
CATEGORY_REQUIRED(1002, "Phải chọn ít nhất một lĩnh vực kinh doanh"),
CATEGORY_NOT_FOUND(1003, "Một số lĩnh vực kinh doanh không tồn tại"),
GEOCODING_FAILED(1004, "Không thể chuyển đổi địa chỉ thành tọa độ"),
FILE_UPLOAD_FAILED(1005, "Tải tệp lên thất bại"),

RESTAURANT_CATEGORY_NOT_EXISTS(1008, "Danh mục nhà hàng không tồn tại"),
RESTAURANT_CATEGORY_ALREADY_EXISTS(1005, "Danh mục nhà hàng đã tồn tại"),
RESTAURANT_NOT_EXISTS(1006, "Nhà hàng không tồn tại"),
RESTAURANT_ALREADY_EXISTS(1007, "Nhà hàng đã tồn tại"),
RESTAURANT_UNAUTHENTICATED(1010, "Nhà hàng của bạn đang chờ phê duyệt"), 
EMAIL_EXISTED (1011, "Email đã tồn tại"), 
  ;

  private int code;
  private String message;
}