package com.example.bigfood.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.bigfood.dto.request.CreateRestaurantRequest;
import com.example.bigfood.dto.request.SearchRequest;
import com.example.bigfood.dto.request.UpdateRestaurantRequest;
import com.example.bigfood.dto.response.RestaurantProfileResponse;
import com.example.bigfood.dto.response.RestaurantReportResponse;
import com.example.bigfood.dto.response.RestaurantResponse;
import com.example.bigfood.dto.response.RestaurantTagResponse;
import com.example.bigfood.dto.response.GoongResponse.GoongLocation;
import com.example.bigfood.dto.response.PageResponse;
import com.example.bigfood.dto.response.RestaurantActiveResponse;
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
    SearchService searchService;

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
                .banner(cloudinaryService.generateUrl(restaurant.getBannerId()))
                .build();
    }
     
    public void saveRestaurant(Restaurant restaurant) {
        if (restaurant != null)
            restaurantRepository.save(restaurant);
    }

     public RestaurantProfileResponse updateRestaurant(String userId , UpdateRestaurantRequest request)
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

    public RestaurantsResponseSet<RestaurantResponse> getRestaurantNearBy(double longitude, double latitude) {
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

        return RestaurantsResponseSet.<RestaurantResponse>builder()
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
    public RestaurantsResponseSet<RestaurantResponse> getRestaurantByCategory(String categoryId) {
        Pageable pageable = PageRequest.of(0, 2);
        return RestaurantsResponseSet.<RestaurantResponse>builder()
                .restaurants(
                        restaurantRepository.findByCategoryId(categoryId, pageable).stream()
                                .map(restaurantMapper::toRestaurantResponse)
                                .toList())
                .build();
    }


    private String formatSearchText(String searchText) {
    if (searchText == null || searchText.trim().isEmpty()) {
        return null;
    }

    // Bước 1: Xóa khoảng trắng thừa ở đầu đuôi và chuẩn hóa nhiều dấu cách ở giữa thành 1
    // VD: "  yen    tra   " -> "yen tra"
    String cleaned = searchText.trim().replaceAll("\s+", " ");

    // Bước 2: Thay thế khoảng trắng bằng dấu %
    // VD: "yen tra" -> "yen%tra"
    String withWildcards = cleaned.replace(" ", "%");

    // Bước 3: Bao bọc 2 đầu bằng %
    // VD: "yen%tra" -> "%yen%tra%"
    return "%" + withWildcards + "%";
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
    public RestaurantsResponseSet<RestaurantResponse> getRestaurantSet(Double lat, Double lng, String categoryId, String searchText, int page) {
        Page<RestaurantProjection> resultPage;

        // Chuẩn hóa input (tránh null pointer và string rỗng)
        String cleanCategoryId = (categoryId != null && !categoryId.trim().isEmpty()) ? categoryId.trim() : null;
        String cleanSearchText = (searchText != null && !searchText.trim().isEmpty()) ? "%" + searchText.trim() + "%"
                : null;

        if(cleanSearchText != null) {
            cleanSearchText = formatSearchText(searchText);
            searchService.addSearch(
                SearchRequest.builder()
                .content(cleanSearchText.replace("%", " ").trim())
                .build());
        }

        int size = 8; // số kết quả tối đa trả về
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
        return RestaurantsResponseSet.<RestaurantResponse>builder()
                .restaurants(resultPage.getContent().stream()
                        .map(proj -> {
                            var response = restaurantMapper.toRestaurantResponse(proj);
                            response.setBanner(cloudinaryService.generateUrl(proj.getBannerId()));
                            return response;
                        })
                        .toList())
                .total(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
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

    public RestaurantsResponseSet<RestaurantTagResponse> getRestaurantRequestSet(int page) {
        int size = 10; // số kết quả tối đa trả về

        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> pageData = restaurantRepository.findAllByApprovedFalse(pageable);

        return RestaurantsResponseSet.<RestaurantTagResponse>builder()
                .restaurants(
                        pageData.getContent().stream()
                                .map(restaurantMapper::toRestaurantTagResponse)
                                .toList())
                .total(pageData.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
        
    }

    public void approveRestaurantRequest(String restaurantId, boolean approved) {
        Restaurant restaurant = getRestaurantByUserId(restaurantId);
        if(approved) {
            restaurant.setApproved(true);
            restaurantRepository.save(restaurant);
        } else {
            restaurantRepository.delete(restaurant);
        }
    }

    public PageResponse<RestaurantActiveResponse> getRestaurantActiveSet(String categoryId, Integer page) {
        int size = 1; // số kết quả tối đa trả về
        int pageCurrent = (page > 0 && page != null) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageCurrent, size);
        Page<Restaurant> pageData;
        if(categoryId == null || categoryId.isEmpty()) {
            pageData = restaurantRepository.findAllByApprovedTrue(pageable);
        } else {
            pageData = restaurantRepository.findAllByApprovedTrueAndRestaurantCategories_Id(categoryId, pageable);
        }

        return PageResponse.<RestaurantActiveResponse>builder()
                .items(
                        pageData.getContent().stream()
                                .map(restaurantMapper::toRestaurantActiveResponse)
                                .toList())
                .total(pageData.getTotalElements())
                .page(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .build();
    }

    public RestaurantsResponseSet<RestaurantReportResponse> getRestaurantReport(int page) {
        int size = 10; // số kết quả tối đa trả về

        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> pageData = restaurantRepository.findHighNegativeRestaurants(pageable);

        return RestaurantsResponseSet.<RestaurantReportResponse>builder()
                .restaurants(
                        pageData.getContent().stream()
                                .map(restaurantMapper::toRestaurantReportResponse)
                                .toList())
                .total(pageData.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
    }

}
