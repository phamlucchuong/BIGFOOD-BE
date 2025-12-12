package com.example.bigfood.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.request.UpdateRestaurantRequest;
import com.example.bigfood.dto.response.RestaurantProfileResponse;
import com.example.bigfood.dto.response.GoongResponse.GoongLocation;
import com.example.bigfood.dto.response.RestaurantDetailResponse;
import com.example.bigfood.dto.response.RestaurantsResponseSet;
import com.example.bigfood.dto.response.RestaurantFullResponse;
import com.example.bigfood.dto.RestaurantProjection;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.RestaurantCategory;
import com.example.bigfood.entity.User;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.RestaurantMapper;
import com.example.bigfood.repository.RestaurantRepository;
import com.example.bigfood.repository.UserRepository;

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
    UserRepository userRepository;
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

    public RestaurantFullResponse createNewRestaurant(String userId, CreateRestaurantRequest request)
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

    public RestaurantFullResponse getRestaurant(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTS);
        }
        return restaurantRepository.findById(userId)
                .map(restaurantMapper::toRestaurantResponse)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTS));
    }

    public RestaurantProfileResponse getRestaurantDetail(String restaurantId) {
        Restaurant restaurant = getRestaurantByUserId(restaurantId);
        return  RestaurantProfileResponse.builder()
                .id(restaurant.getUserId())
                .restaurantName(restaurant.getRestaurantName())
                .address(restaurant.getAddress())
                .phone(restaurant.getUser().getPhone())
                .email(restaurant.getUser().getEmail())
                .nameBank(restaurant.getNameBank())
                .bankNumber(restaurant.getBankNumber())
                .bankAccountName(restaurant.getBankAccountName())
                .avatar(cloudinaryService.generateUrl(restaurant.getLicenseId()))
                .bannerId(cloudinaryService.generateUrl(restaurant.getBannerId()))
                .isApproved(restaurant.getIsApproved())
                .build();
    }
     
    public void saveRestaurant(Restaurant restaurant) {
        if (restaurant != null)
            restaurantRepository.save(restaurant);
    }

     public RestaurantProfileResponse updateRestaurant(String userId ,UpdateRestaurantRequest request)
            throws AppException, IOException {

        User user = userService.getUserById(userId);
           user.setPhone(request.getPhone());
           user.setEmail(request.getEmail());

        Restaurant restaurant = getRestaurantByUserId(userId);

                restaurant.setRestaurantName(request.getRestaurantName());
                restaurant.setAddress(request.getAddress());
                restaurant.setNameBank(request.getNameBank());
                restaurant.setBankAccountName(request.getBankAccountName());
                restaurant.setBankNumber(request.getBankNumber());

        GoongLocation location = goongService.getGeocoding(request.getAddress());
        restaurant.setLatitude(location.getLat());
        restaurant.setLongitude(location.getLng());
        if(request.getAvatar()!=null && !request.getAvatar().isEmpty()){
            String licenseId = cloudinaryService.uploadFile(request.getAvatar(), "licenses");
            restaurant.setLicenseId(licenseId);
        }
        if(request.getBanner()!=null && !request.getBanner().isEmpty()){
            String bannerId = cloudinaryService.uploadFile(request.getBanner(), "licenses");
             restaurant.setBannerId(bannerId);
        }
        restaurant = restaurantRepository.save(restaurant);
        userRepository.save(user);
        return getRestaurantDetail(restaurant.getUserId());
    }

    public RestaurantsResponseSet getRestaurantNearBy(double longitude, double latitude) {
        if (longitude == 0 || latitude == 0) {
            throw new IllegalArgumentException("Location must be [lat, lng]");
        }

        double lat = latitude;
        double lng = longitude;
        double radiusMeters = 20_000; // 5km
        int page = 0;
        int size = 2; // số kết quả tối đa trả về

        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantProjection> pageData = restaurantRepository.findNearby(lat, lng, radiusMeters, pageable);

        return RestaurantsResponseSet.builder()
                .restaurants(
                        pageData.getContent().stream()
                                .map(restaurantMapper::toRestaurantResponse)
                                .toList())
                .total(pageData.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
    }

    /**
     * hàm lấy danh sách nhà hàng theo danh mục category
     *
     * @param categoryid id của danh mục
     * @return danh sách nhà hàng thuộc danh mục category theo số phân trang
     */
    public RestaurantsResponseSet getRestaurantByCategory(String categoryId) {
        Pageable pageable = PageRequest.of(0, 2);
        return RestaurantsResponseSet.builder()
                .restaurants(
                        restaurantRepository.findByCategoryId(categoryId, pageable).stream()
                                .map(restaurantMapper::toRestaurantResponse)
                                .toList())
                .build();
    }

    /**
     * hàm lấy danh sách nhà hàng theo điều kiện
     *
     * @param longitude,latitude nếu khác null sẽ ưu tiên lấy nhà hàng gần với tọa
     *                           dộ của user
     * @param categoryId         nếu khác null hoặc rỗng sẽ lấy nhà hàng theo loại
     *                           danh mục
     * @param searchText         nếu khác null hoặc rỗng sẽ lấy nhà hàng yêu cầu của
     *                           tìm kiếm
     * @return danh sách nhà hàng theo điều kiện
     */
    public RestaurantsResponseSet getRestaurantSet(Double lat, Double lng, String categoryId, String searchText, int page) {
        Page<RestaurantProjection> resultPage;

        // Chuẩn hóa input (tránh null pointer và string rỗng)
        String cleanCategoryId = (categoryId != null && !categoryId.trim().isEmpty()) ? categoryId.trim() : null;
        String cleanSearchText = (searchText != null && !searchText.trim().isEmpty()) ? "%" + searchText.trim() + "%"
                : null;
        int size = 1; // số kết quả tối đa trả về
        Double radius = 20000.0; // Bán kính mặc định 5km

        Pageable pageable = PageRequest.of(page, size);

        if (lat != null && lng != null) {
            // NHÓM 1: CÓ TỌA ĐỘ -> Gọi hàm tìm gần (kèm logic lọc Cat HOẶC Search)
            resultPage = restaurantRepository.findNearbyWithFilter(lat, lng, radius, cleanCategoryId, cleanSearchText,
                    pageable);
        } else {
            // NHÓM 2: KHÔNG TỌA ĐỘ -> Gọi hàm gợi ý ngẫu nhiên (kèm logic lọc Cat HOẶC
            // Search)
            resultPage = restaurantRepository.findAllWithFilter(cleanCategoryId, cleanSearchText, pageable);
        }

        // Map từ Projection sang Response DTO (Builder)
        return RestaurantsResponseSet.builder()
                .restaurants(resultPage.getContent().stream()
                        .map(proj -> {
                            var response = restaurantMapper.toRestaurantResponse(proj);
                            response.setBanner(cloudinaryService.generateUrl(proj.getBannerId()));
                            return response;
                        })
                        .toList())
                .total(resultPage.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
    }

    public RestaurantDetailResponse getRestaurantByRestaurantId(String restaurantId) {
        if (restaurantId == null || restaurantId.isEmpty()) {
            throw new AppException(ErrorCode.ID_INVALID);
        }
        return restaurantRepository.findById(restaurantId)
                .map(res -> {
                    var response = restaurantMapper.toRestaurantDetailResponse(res);
                    response.setBanner(cloudinaryService.generateUrl(res.getBannerId()));
                    return response;
                })
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTS));
    }

}
