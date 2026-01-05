package com.example.bigfood.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bigfood.entity.User;
import com.example.bigfood.entity.Role;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.UserMapper;
import com.example.bigfood.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.example.bigfood.repository.RoleRepository;
import com.example.bigfood.dto.request.UserCreateRequest;
import com.example.bigfood.dto.request.UserUpdateRequest;
import com.example.bigfood.dto.response.PageResponse;
import com.example.bigfood.dto.response.SummaryResponse;
import com.example.bigfood.dto.response.UserResponse;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;


    protected User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    boolean existById(String id) {
        return userRepository.existsById(id);
    }

    public UserResponse createUser(UserCreateRequest request) {
         if(!verifyEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById("USER").ifPresent(roles::add);
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }



    public Boolean verifyEmail(String emailRequest) {
        Optional<User> user = userRepository.findByEmailAndDeletedFalse(emailRequest);
        return !user.isPresent();
    }

    public UserResponse updateUser(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        userMapper.toUpdate(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public PageResponse<UserResponse> getAllUser(Integer page) {
        int limit = 2 ;
        int pageCurrent = (page > 0 && page != null) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageCurrent, limit , Sort.by(Sort.Direction.DESC , "name"));
        List<String> roles = List.of(com.example.bigfood.enums.Role.USER.name(),
                                     com.example.bigfood.enums.Role.ADMIN.name());
        Page<User> users = userRepository.findByRolesIn(roles , pageable);

        return PageResponse.<UserResponse>builder()
        .items(users.getContent().stream().map(userMapper::toUserResponse).toList())
        .total(users.getTotalElements())
        .page(pageCurrent + 1)
        .pageSize(limit)
        .totalPages(users.getTotalPages())
        .build();
    }

    public void deleteUser(String id) {
        User user = userRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        user.setDeleted(!user.isDeleted());
        userRepository.save(user);
    }

    public UserResponse addAdminRole(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        HashSet<Role> roles = new HashSet<>(user.getRoles());

        Role adminRole = roleRepository.findById("ADMIN")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        roles.add(adminRole);
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    /**
     * @return UserSummaryResponse chứa thông tin tổng kết người dùng, bao gồm tổng số và
     * % thay đổi số lượng người dùng mới so với cùng kì tháng trước.
     * 
     * hàm lấy ngày tháng hiện tại, tính số người dùng mới trong một chu kì
     * với một chu kì được tính từ ngày 1 tháng này đến ngày hiện tại
     */
    public SummaryResponse getUserSummary() {
        
        // Lấy tổng số người dùng
        long totalUsers = userRepository.count();

        // Tính toán các mốc thời gian
        LocalDateTime now = LocalDateTime.now();
        
        // --- Kì hiện tại
        LocalDateTime startTimeCurrent = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime endTimeCurrent = now.toLocalDate().atTime(LocalTime.MAX); // 23:59:59.999...

        // --- Cùng kì tháng trước 
        LocalDateTime previousPeriod = now.minusMonths(1);
        LocalDateTime startTimePrevious = previousPeriod.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime endTimePrevious = previousPeriod.toLocalDate().atTime(LocalTime.MAX);

        
        // Lấy số lượng của 2 kì
        long currentPeriodCount = userRepository.countByCreatedAtBetweenAndDeletedFalse(startTimeCurrent, endTimeCurrent);
        long previousPeriodCount = userRepository.countByCreatedAtBetweenAndDeletedFalse(startTimePrevious, endTimePrevious);

        
        // Tính toán phần trăm thay đổi (Rất quan trọng: Xử lý chia cho 0)
        double changePercentage = 0.0;
        String direction = "neutral";

        if (previousPeriodCount > 0) {
            // Trường hợp thông thường (ví dụ: tháng trước 100, tháng này 110)
            changePercentage = ((double) (currentPeriodCount - previousPeriodCount) / previousPeriodCount) * 100.0;
        } else if (previousPeriodCount == 0 && currentPeriodCount > 0) {
            // Tháng trước 0, tháng này > 0 (tăng 100%)
            changePercentage = 100.0;
        }
        // Trường hợp (previousPeriodCount == 0 && currentPeriodCount == 0) -> changePercentage = 0.0 (giữ nguyên)

        
        //  Xác định hướng (tăng/giảm)
        if (changePercentage > 0) {
            direction = "increase";
        } else if (changePercentage < 0) {
            direction = "decrease";
        }
        
        // Làm tròn 2 chữ số thập phân
        double roundedPercentage = Math.round(changePercentage * 100.0) / 100.0;

        // Trả về kết quả
        return SummaryResponse.builder()
                .total(totalUsers) // Tên trường nên là long thay vì int
                .changePercentage(roundedPercentage)
                .direction(direction)
                .build();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    public UserResponse getUserResponseById(String id) {
        if(id == null || id.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }
}
