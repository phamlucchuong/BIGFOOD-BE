package com.example.bigfood.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.RestaurantCategory;
import com.example.bigfood.entity.User;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.RestaurantMapper;
import com.example.bigfood.repository.RestaurantCategoryRepository;
import com.example.bigfood.repository.RestaurantRepository;
import com.example.bigfood.service.GoongService.GoongLocation;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantService {
   
    RestaurantCategoryRepository restaurantCategoryRepository; 
    RestaurantRepository restaurantRepository;
    UserService userService;
    RestaurantMapper restaurantMapper;
    GoongService goongService;
    CloudinaryService cloudinaryService;


    public boolean existsByUserId(String userId) {
        return restaurantRepository.existsByUserId(userId);
    }

    public RestaurantResponse createNewRestaurant(String userId, CreateRestaurantRequest request) 
        throws AppException, IOException
        {
        User user = userService.getUserById(userId);

        if(existsByUserId(userId)) {
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

        Set<RestaurantCategory> categories = getCategoriesByIds(request.getCategoryIds());
        restaurant.setRestaurantCategories(categories);
        
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantResponse(restaurant);
    }

    public RestaurantResponse getRestaurant(String userId) {
        return restaurantRepository.findById(userId)
            .map(restaurantMapper::toRestaurantResponse)
            .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTS));
    }

    private Set<RestaurantCategory> getCategoriesByIds(List<String> categoryIds) 
        throws AppException {
        
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_REQUIRED);
        }
        
        List<RestaurantCategory> categories = restaurantCategoryRepository.findAllById(categoryIds);
     
        if (categories.size() != categoryIds.size()) {
            Set<String> foundIds = categories.stream()
                .map(RestaurantCategory::getId)
                .collect(Collectors.toSet());
            List<String> invalidIds = categoryIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toList());
            
            log.error("Invalid category IDs: {}", invalidIds);
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        
        return new HashSet<>(categories);
    }
}
