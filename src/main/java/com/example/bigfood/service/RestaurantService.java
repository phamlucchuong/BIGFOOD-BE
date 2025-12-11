package com.example.bigfood.service;

import java.io.IOException;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.response.RestaurantDetailResponse;
import com.example.bigfood.dto.response.GoongResponse.GoongLocation;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.RestaurantCategory;
import com.example.bigfood.entity.User;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.RestaurantMapper;
import com.example.bigfood.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantService {
    RestaurantCategoryService restaurantCategoryService;
    RestaurantRepository restaurantRepository;
    UserService userService;
    RestaurantMapper restaurantMapper;
    GoongService goongService;
    CloudinaryService cloudinaryService;

    public boolean existsByUserId(String userId) {
        return restaurantRepository.existsByUserId(userId);
    }

    public Restaurant getRestaurantByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTS);
        }
        return restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTS));
    }

    public RestaurantResponse createNewRestaurant(String userId, CreateRestaurantRequest request)
            throws AppException, IOException {
        User user = userService.getUserById(userId);

        if (existsByUserId(userId)) {
            throw new AppException(ErrorCode.RESTAURANT_ALREADY_EXISTS);
        }

        Restaurant restaurant = restaurantMapper.toRestaurant(request);
        // chuyển đổi địa chỉ thành tọa độ từ Goong API
        GoongLocation location = goongService.getGeocoding(request.getAddress());
        restaurant.setLatitude(location.getLat());
        restaurant.setLongitude(location.getLng());
        restaurant.setUser(user);

        // tải file đăng ký kinh doanh lên Cloudinary
        String licenseId = cloudinaryService.uploadFile(request.getLicenseFile(), "licenses");
        restaurant.setLicenseId(licenseId);

        Set<RestaurantCategory> categories = restaurantCategoryService.getCategoriesByIds(request.getCategoryIds());
        restaurant.setRestaurantCategories(categories);
        
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantResponse(restaurant);
    }

    public RestaurantResponse getRestaurant(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTS);
        }
        return restaurantRepository.findById(userId)
                .map(restaurantMapper::toRestaurantResponse)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTS));
    }

    public RestaurantDetailResponse getRestaurantDetail(String restaurantId) {
        Restaurant restaurant = getRestaurantByUserId(restaurantId);
        return  RestaurantDetailResponse.builder()
                .id(restaurant.getUserId())
                .restaurantName(restaurant.getRestaurantName())
                .address(restaurant.getAddress())
                .phone(restaurant.getUser().getPhone())
                .email(restaurant.getUser().getEmail())
                .nameBank(restaurant.getNameBank())
                .bankNumber(restaurant.getBankNumber())
                .bankAccountName(restaurant.getBankAccountName())
                .avatar(restaurant.getUser().getImageId())
                .bannerId(restaurant.getBannerId())
                .isApproved(restaurant.getIsApproved())
                .build();
    }
    public void saveRestaurant(Restaurant restaurant) {
        if (restaurant != null)
            restaurantRepository.save(restaurant);
    }


}
