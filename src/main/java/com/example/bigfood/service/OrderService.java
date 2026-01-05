package com.example.bigfood.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bigfood.dto.request.CreateOrderRequest;
import com.example.bigfood.dto.request.UpdateOrderStatusRequest;
import com.example.bigfood.dto.response.GoongResponse.GoongLocation;
import com.example.bigfood.dto.response.InfoUserOrderResponse;
import com.example.bigfood.dto.response.OrderDetailResponse;
import com.example.bigfood.dto.response.OrderFoodDetailResponse;
import com.example.bigfood.dto.response.OrderFullResponse;
import com.example.bigfood.dto.response.OrderResponse;
import com.example.bigfood.dto.response.OrderShortPageResponse;
import com.example.bigfood.dto.response.OrderShortResponse;
import com.example.bigfood.dto.response.RestaurantStatisticalResponse;
import com.example.bigfood.dto.response.SummaryResponse;
import com.example.bigfood.entity.Order;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.Review;
import com.example.bigfood.entity.User;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.enums.OrderStatus;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.OrderMapper;
import com.example.bigfood.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    RestaurantService restaurantService;
    UserService userService;
    OrderRepository orderRepository;
    GoongService goongService;
    OrderDetailService orderDetailService;
    OrderMapper orderMapper;
    CloudinaryService cloudinaryService;
    EntityManager entityManager;
    FinanceService financeService;

    @Transactional
    public OrderFullResponse createOrder(String userId, CreateOrderRequest request) {
        // Validate and fetch related entities
        Restaurant restaurant = restaurantService.getRestaurantByUserId(request.getRestaurantId());
        User user = userService.getUserById(userId);

        // Get delivery location coordinates
        GoongLocation location = goongService.getGeocoding(request.getDeliveryAddress());

        // Calculate delivery distance and fee
        double deliveryDistance = goongService.getDrivingDistance(
                restaurant.getLatitude(), restaurant.getLongitude(),
                location.getLat(), location.getLng());

        // Build Order entity (parent)
        Order order = Order.builder()
                .user(user)
                .restaurant(restaurant)
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryLatitude(location.getLat())
                .deliveryLongitude(location.getLng())
                .deliveryDistance(deliveryDistance)
                .deliveryFee(deliveryDistance * 3000)
                .paymentMethod(request.getPaymentMethod())
                .notes(request.getNotes())
                .build();

        double totalPrice = orderDetailService.createSetOrderDetails(order, request.getOrderDetails());
        order.setTotalAmount(totalPrice + order.getDeliveryFee());

        orderRepository.save(order);
        entityManager.flush(); // Ensure immediate persistence
        entityManager.refresh(order); // Refresh to get any DB-generated values

        return orderMapper.toFullResponse(order);
    }

    public OrderShortPageResponse getAllOrdersByUserId(String userId, boolean status, Integer page) {
        List<OrderStatus> statusList = null;

        if (!status) {
            // Nhóm trạng thái đang xử lý
            statusList = List.of(
                    OrderStatus.PENDING,
                    OrderStatus.CONFIRMED,
                    OrderStatus.PREPARING,
                    OrderStatus.DELIVERING);
        } else {
            // Nhóm trạng thái đã hoàn thành hoặc hủy
            statusList = List.of(
                    OrderStatus.COMPLETED,
                    OrderStatus.REJECTED,
                    OrderStatus.CANCELLED);
        }

        public OrderShortPageResponse<OrderShortResponse> getAllOrdersByUserId(String userId, boolean status, Integer page) {
                List<OrderStatus> statusList = null;

                if (!status) {
                        // Nhóm trạng thái đang xử lý
                        statusList = List.of(
                                        OrderStatus.PENDING,
                                        OrderStatus.CONFIRMED,
                                        OrderStatus.PREPARING,
                                        OrderStatus.DELIVERING);
                } else {
                        // Nhóm trạng thái đã hoàn thành hoặc hủy
                        statusList = List.of(
                                        OrderStatus.COMPLETED,
                                        OrderStatus.REJECTED,
                                        OrderStatus.CANCELLED);
                }

                int size = 5;
                int safePage = page != null && page >= 0 ? page : 0; // fallback tránh NullPointer và page âm
                Pageable pageable = PageRequest.of(safePage, size);

                var pageData = orderRepository.findByUser_Id(userId, statusList, pageable);

                return OrderShortPageResponse.<OrderShortResponse>builder()
                                .orders(pageData.getContent().stream()
                                                .map(orderMapper::toShortResponse)
                                                .toList())
                                .total(pageData.getTotalElements())
                                .page(safePage)
                                .pageSize(size)
                                .totalPages(pageData.getTotalPages())
                                .build();

        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        public OrderShortPageResponse<OrderResponse> getAllOrders(Integer page) {
                int size = 2 ;
                Integer pageCurrent = page > 0 ? page : 0;

                Pageable pageable = PageRequest.of(pageCurrent, size , Sort.by(Sort.Direction.DESC , "createdAt"));
                var pageData = orderRepository.findAll(pageable);
                  return OrderShortPageResponse.<OrderResponse>builder()
                                .orders(pageData.getContent().stream()
                                                .map(orderMapper::toResponse)
                                                .toList())
                                .total(pageData.getTotalElements())
                                .page(pageCurrent)
                                .pageSize(size)
                                .totalPages(pageData.getTotalPages())
                                .build();
        }
        order.setStatus(request.getStatus());

        if (request.getStatus().equals(OrderStatus.CANCELLED)) {
            order.setCancellReason(request.getReason());
        }
        if (request.getStatus().equals(OrderStatus.REJECTED)) {
            order.setRejectReason(request.getReason());
        }
        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

        public OrderShortPageResponse<OrderResponse> getAllOrdersByRestaurantId(String restaurantId , Integer page) {
                int size = 2 ;
               int pageCurrent = (page != null && page > 0) ? page - 1 : 0;

                Pageable pageable = PageRequest.of(pageCurrent, size , Sort.by(Sort.Direction.DESC , "createdAt"));
                var pageData = orderRepository.findByRestaurant_UserId(restaurantId , pageable);
               return OrderShortPageResponse.<OrderResponse>builder()
                                .orders(pageData.getContent().stream()
                                                .map(orderMapper::toResponse)
                                                .toList())
                                .total(pageData.getTotalElements())
                                .page(pageCurrent + 1)
                                .pageSize(size)
                                .totalPages(pageData.getTotalPages())
                                .build();
        }

        if (order.getStatus().equals(request.getStatus())) {
            throw new AppException(ErrorCode.STATUS_SAME_AS_BEFORE);
        }

        public OrderShortPageResponse<OrderResponse> getLoadStatusFilter(String restaurantId, String filter , Integer page) {
                OrderStatus statusEnum;
                try {
                        statusEnum = OrderStatus.valueOf(filter);
                } catch (Exception e) {
                        throw new IllegalArgumentException("Invalid status: " + filter);
                }

                int size = 2 ;
                int pageCurrent = (page != null && page > 0) ? page - 1 : 0;

                Pageable pageable = PageRequest.of(pageCurrent, size , Sort.by(Sort.Direction.DESC , "createdAt"));

                var pageData =  orderRepository.findByStatus(restaurantId, statusEnum , pageable);
                 return OrderShortPageResponse.<OrderResponse>builder()
                                .orders(pageData.getContent().stream()
                                                .map(orderMapper::toResponse)
                                                .toList())
                                .total(pageData.getTotalElements())
                                .page(pageCurrent + 1)
                                .pageSize(size)
                                .totalPages(pageData.getTotalPages())
                                .build();
        }

        List<Order> listOrder = orderRepository.findByStatus(restaurantId, statusEnum);
        return orderMapper.toResponseList(listOrder);
    }

    protected Order getOrderById(String orderId) {
        if (orderId == null || orderId.isEmpty()) {

            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

        public RestaurantStatisticalResponse restaurantStatistical(String restaurantId , Integer page) {
                int size = 2 ;
                Integer pageCurrent = page > 0 ? page : 0;

                Pageable pageable = PageRequest.of(pageCurrent, size , Sort.by(Sort.Direction.DESC , "createdAt"));
                var pageData= orderRepository.findByRestaurant_UserId(restaurantId , pageable);
                List<Order> allOrders = pageData.getContent();

                if (allOrders.isEmpty()) {
                        return RestaurantStatisticalResponse.builder()
                                        .totalPrice(0)
                                        .percentagePrice(0)
                                        .averageUnitRevenuePrice(0)
                                        .numberOfOrder(0)
                                        .percentageOrder(0)
                                        .numberOrderCompleted(0)
                                        .numberOrderRejected(0)
                                        .averageStars(0)
                                        .percentageStart(0)
                                        .percentagePositive(0)
                                        .percentNegative(0)
                                        .build();
                }

                double totalRevenue = allOrders.stream()
                                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                                .mapToDouble(Order::getTotalAmount)
                                .sum();

                int totalOrders = allOrders.size();
                long completedOrders = allOrders.stream()
                                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                                .count();
                long rejectedOrders = allOrders.stream()
                                .filter(order -> order.getStatus() == OrderStatus.REJECTED
                                                || order.getStatus() == OrderStatus.CANCELLED)
                                .count();

                double averageRevenuePerOrder = completedOrders > 0
                                ? totalRevenue / completedOrders
                                : 0;
                LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
                LocalDateTime currentPeriodStart = LocalDateTime.now().minusYears(1);

                List<Order> previousPeriodOrders = orderRepository.findByRestaurant_UserIdAndCreatedAtBetween(
                                restaurantId, currentPeriodStart, oneYearAgo);

                double previousRevenue = previousPeriodOrders.stream()
                                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                                .mapToDouble(Order::getTotalAmount)
                                .sum();

                int previousTotalOrders = previousPeriodOrders.size();
                double percentagePrice = previousRevenue > 0
                                ? ((totalRevenue - previousRevenue) / previousRevenue) * 100
                                : 0;

                double percentageOrder = previousTotalOrders > 0
                                ? ((totalOrders - previousTotalOrders) / (double) previousTotalOrders) * 100
                                : 0;

                percentagePrice = Math.round(percentagePrice * 10.0) / 10.0;
                percentageOrder = Math.round(percentageOrder * 10.0) / 10.0;

                // Calculate review statistics
                List<Review> allReviews = allOrders.stream()
                                .map(Order::getReview)
                                .filter(review -> review != null)
                                .collect(Collectors.toList());

                double averageStars = 0;
                double percentagePositive = 0; // 4-5 stars
                double percentNegative = 0; // 1-2 stars
                double percentageStart = 0;

                if (!allReviews.isEmpty()) {
                        // Calculate average rating
                        averageStars = allReviews.stream()
                                        .mapToInt(Review::getRating)
                                        .average()
                                        .orElse(0);
                        averageStars = Math.round(averageStars * 10.0) / 10.0;

                        // Count rating distribution
                        long positiveCount = allReviews.stream()
                                        .filter(review -> review.getRating() >= 4)
                                        .count();
                        long negativeCount = allReviews.stream()
                                        .filter(review -> review.getRating() <= 2)
                                        .count();

                        percentagePositive = (positiveCount * 100.0) / allReviews.size();
                        percentNegative = (negativeCount * 100.0) / allReviews.size();

                        // Calculate percentage change in average rating from previous period
                        List<Review> previousReviews = previousPeriodOrders.stream()
                                        .map(Order::getReview)
                                        .filter(review -> review != null)
                                        .collect(Collectors.toList());

                        if (!previousReviews.isEmpty()) {
                                double previousAverageStars = previousReviews.stream()
                                                .mapToInt(Review::getRating)
                                                .average()
                                                .orElse(0);
                                percentageStart = Math.round((averageStars - previousAverageStars) * 10.0) / 10.0;
                        }

                        percentagePositive = Math.round(percentagePositive * 10.0) / 10.0;
                        percentNegative = Math.round(percentNegative * 10.0) / 10.0;
                }

                return RestaurantStatisticalResponse.builder()
                                .totalPrice(totalRevenue)
                                .percentagePrice(percentagePrice)
                                .averageUnitRevenuePrice(averageRevenuePerOrder)
                                .numberOfOrder(totalOrders)
                                .percentageOrder(percentageOrder)
                                .numberOrderCompleted((int) completedOrders)
                                .numberOrderRejected((int) rejectedOrders)
                                .averageStars(averageStars)
                                .percentageStart(percentageStart)
                                .percentagePositive(percentagePositive)
                                .percentNegative(percentNegative)
                                .build();
        }

        double totalRevenue = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        int totalOrders = allOrders.size();
        long completedOrders = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .count();
        long rejectedOrders = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.REJECTED
                        || order.getStatus() == OrderStatus.CANCELLED)
                .count();

        double averageRevenuePerOrder = completedOrders > 0
                ? totalRevenue / completedOrders
                : 0;
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        LocalDateTime currentPeriodStart = LocalDateTime.now().minusYears(1);

        List<Order> previousPeriodOrders = orderRepository.findByRestaurant_UserIdAndCreatedAtBetween(
                restaurantId, currentPeriodStart, oneYearAgo);

        double previousRevenue = previousPeriodOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        int previousTotalOrders = previousPeriodOrders.size();
        double percentagePrice = previousRevenue > 0
                ? ((totalRevenue - previousRevenue) / previousRevenue) * 100
                : 0;

        double percentageOrder = previousTotalOrders > 0
                ? ((totalOrders - previousTotalOrders) / (double) previousTotalOrders) * 100
                : 0;

        percentagePrice = Math.round(percentagePrice * 10.0) / 10.0;
        percentageOrder = Math.round(percentageOrder * 10.0) / 10.0;

        // Calculate review statistics
        List<Review> allReviews = allOrders.stream()
                .map(Order::getReview)
                .filter(review -> review != null)
                .collect(Collectors.toList());

        double averageStars = 0;
        double percentagePositive = 0; // 4-5 stars
        double percentNegative = 0; // 1-2 stars
        double percentageStart = 0;

        if (!allReviews.isEmpty()) {
            // Calculate average rating
            averageStars = allReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0);
            averageStars = Math.round(averageStars * 10.0) / 10.0;

            // Count rating distribution
            long positiveCount = allReviews.stream()
                    .filter(review -> review.getRating() >= 4)
                    .count();
            long negativeCount = allReviews.stream()
                    .filter(review -> review.getRating() <= 2)
                    .count();

            percentagePositive = (positiveCount * 100.0) / allReviews.size();
            percentNegative = (negativeCount * 100.0) / allReviews.size();

            // Calculate percentage change in average rating from previous period
            List<Review> previousReviews = previousPeriodOrders.stream()
                    .map(Order::getReview)
                    .filter(review -> review != null)
                    .collect(Collectors.toList());

            if (!previousReviews.isEmpty()) {
                double previousAverageStars = previousReviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0);
                percentageStart = Math.round((averageStars - previousAverageStars) * 10.0) / 10.0;
            }

            percentagePositive = Math.round(percentagePositive * 10.0) / 10.0;
            percentNegative = Math.round(percentNegative * 10.0) / 10.0;
        }

        return RestaurantStatisticalResponse.builder()
                .totalPrice(totalRevenue)
                .percentagePrice(percentagePrice)
                .averageUnitRevenuePrice(averageRevenuePerOrder)
                .numberOfOrder(totalOrders)
                .percentageOrder(percentageOrder)
                .numberOrderCompleted((int) completedOrders)
                .numberOrderRejected((int) rejectedOrders)
                .averageStars(averageStars)
                .percentageStart(percentageStart)
                .percentagePositive(percentagePositive)
                .percentNegative(percentNegative)
                .build();
    }

    public OrderFullResponse getOrderByOrderId(String id) {
        if (id == null || id.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orderMapper.toFullResponse(orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));
    }

    /**
     * Get restaurant statistics filtered by time range
     * 
     * @param restaurantId Restaurant user ID
     * @param timeRange    Filter: "day" (hôm nay), "week" (tuần này),
     *                     "month" (tháng này), "year" (năm này)
     * @return RestaurantStatisticalResponse with filtered statistics
     */
    public RestaurantStatisticalResponse restaurantStatisticalByTimeRange(String restaurantId, String timeRange) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime;

        // Determine start date based on time range
        switch (timeRange != null ? timeRange.toLowerCase() : "day") {
            case "day":
                // Hôm nay - từ 00:00:00 hôm nay
                startDateTime = now.toLocalDate().atStartOfDay();
                break;
            case "week":
                // Tuần này - từ thứ Hai tuần này
                startDateTime = now.toLocalDate()
                        .minusDays(now.getDayOfWeek().getValue() - 1)
                        .atStartOfDay();
                break;
            case "month":
                // Tháng này - từ ngày 1 tháng
                startDateTime = now.toLocalDate()
                        .withDayOfMonth(1)
                        .atStartOfDay();
                break;
            case "year":
                // Năm này - từ ngày 1/1
                startDateTime = now.toLocalDate()
                        .withDayOfYear(1)
                        .atStartOfDay();
                break;
            default:
                startDateTime = now.toLocalDate().atStartOfDay();
        }

        // Get orders within time range
        List<Order> allOrders = orderRepository.findByRestaurant_UserIdAndCreatedAtBetween(
                restaurantId, startDateTime, now);

        if (allOrders.isEmpty()) {
            return RestaurantStatisticalResponse.builder()
                    .totalPrice(0)
                    .percentagePrice(0)
                    .averageUnitRevenuePrice(0)
                    .numberOfOrder(0)
                    .percentageOrder(0)
                    .numberOrderCompleted(0)
                    .numberOrderRejected(0)
                    .averageStars(0)
                    .percentageStart(0)
                    .percentagePositive(0)
                    .percentNegative(0)
                    .build();
        }

        // Calculate revenue statistics
        double totalRevenue = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        int totalOrders = allOrders.size();
        long completedOrders = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .count();
        long rejectedOrders = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.REJECTED
                        || order.getStatus() == OrderStatus.CANCELLED)
                .count();

        double averageRevenuePerOrder = completedOrders > 0
                ? totalRevenue / completedOrders
                : 0;

        // Get previous period for comparison
        LocalDateTime previousPeriodEnd = startDateTime;
        LocalDateTime previousPeriodStart;

        switch (timeRange != null ? timeRange.toLowerCase() : "day") {
            case "day":
                previousPeriodStart = previousPeriodEnd.minusDays(1);
                break;
            case "week":
                previousPeriodStart = previousPeriodEnd.minusWeeks(1);
                break;
            case "month":
                previousPeriodStart = previousPeriodEnd.minusMonths(1);
                break;
            case "year":
                previousPeriodStart = previousPeriodEnd.minusYears(1);
                break;
            default:
                previousPeriodStart = previousPeriodEnd.minusDays(1);
        }

        List<Order> previousPeriodOrders = orderRepository.findByRestaurant_UserIdAndCreatedAtBetween(
                restaurantId, previousPeriodStart, previousPeriodEnd);

        double previousRevenue = previousPeriodOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        int previousTotalOrders = previousPeriodOrders.size();
        double percentagePrice = previousRevenue > 0
                ? ((totalRevenue - previousRevenue) / previousRevenue) * 100
                : 0;

        double percentageOrder = previousTotalOrders > 0
                ? ((totalOrders - previousTotalOrders) / (double) previousTotalOrders) * 100
                : 0;

        percentagePrice = Math.round(percentagePrice * 10.0) / 10.0;
        percentageOrder = Math.round(percentageOrder * 10.0) / 10.0;

        // Calculate review statistics
        List<Review> allReviews = allOrders.stream()
                .map(Order::getReview)
                .filter(review -> review != null)
                .collect(Collectors.toList());

        double averageStars = 0;
        double percentagePositive = 0; // 4-5 stars
        double percentNegative = 0; // 1-2 stars
        double percentageStart = 0;

        if (!allReviews.isEmpty()) {
            // Calculate average rating
            averageStars = allReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0);
            averageStars = Math.round(averageStars * 10.0) / 10.0;

            // Count rating distribution
            long positiveCount = allReviews.stream()
                    .filter(review -> review.getRating() >= 4)
                    .count();
            long negativeCount = allReviews.stream()
                    .filter(review -> review.getRating() <= 2)
                    .count();

            percentagePositive = (positiveCount * 100.0) / allReviews.size();
            percentNegative = (negativeCount * 100.0) / allReviews.size();

            // Calculate percentage change in average rating from previous period
            List<Review> previousReviews = previousPeriodOrders.stream()
                    .map(Order::getReview)
                    .filter(review -> review != null)
                    .collect(Collectors.toList());

            if (!previousReviews.isEmpty()) {
                double previousAverageStars = previousReviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0);
                percentageStart = Math.round((averageStars - previousAverageStars) * 10.0) / 10.0;
            }

            percentagePositive = Math.round(percentagePositive * 10.0) / 10.0;
            percentNegative = Math.round(percentNegative * 10.0) / 10.0;
        }

        return RestaurantStatisticalResponse.builder()
                .totalPrice(totalRevenue)
                .percentagePrice(percentagePrice)
                .averageUnitRevenuePrice(averageRevenuePerOrder)
                .numberOfOrder(totalOrders)
                .percentageOrder(percentageOrder)
                .numberOrderCompleted((int) completedOrders)
                .numberOrderRejected((int) rejectedOrders)
                .averageStars(averageStars)
                .percentageStart(percentageStart)
                .percentagePositive(percentagePositive)
                .percentNegative(percentNegative)
                .build();
    }

    public SummaryResponse getOrderSummary() {

        // Lấy tổng số người dùng
        long totalUsers = orderRepository.count();

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
        long currentPeriodCount = orderRepository.countByCreatedAtBetween(startTimeCurrent,
                endTimeCurrent);
        long previousPeriodCount = orderRepository.countByCreatedAtBetween(startTimePrevious,
                endTimePrevious);

        // Tính toán phần trăm thay đổi (Rất quan trọng: Xử lý chia cho 0)
        double changePercentage = 0.0;
        String direction = "neutral";

        if (previousPeriodCount > 0) {
            // Trường hợp thông thường (ví dụ: tháng trước 100, tháng này 110)
            changePercentage = ((double) (currentPeriodCount - previousPeriodCount) / previousPeriodCount)
                    * 100.0;
        } else if (previousPeriodCount == 0 && currentPeriodCount > 0) {
            // Tháng trước 0, tháng này > 0 (tăng 100%)
            changePercentage = 100.0;
        }
        // Trường hợp (previousPeriodCount == 0 && currentPeriodCount == 0) ->
        // changePercentage = 0.0 (giữ nguyên)

        // Xác định hướng (tăng/giảm)
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

    public List<Integer> getOrderChart() {
        List<Object[]> monthlyCounts = orderRepository.countOrdersByMonthInCurrentYear();
        Map<Integer, Integer> monthToCountMap = new HashMap<>();
        for (Object[] moc : monthlyCounts) {
            monthToCountMap.put((Integer) moc[0], ((Number) moc[1]).intValue());
        }

        List<Integer> orderCountsByMonth = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            orderCountsByMonth.add(monthToCountMap.getOrDefault(month, 0));
        }

        return orderCountsByMonth;
    }

    @Transactional
    public OrderFullResponse completeOrder(String orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != OrderStatus.DELIVERING) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_COMPLETED);
        }
        order.setStatus(OrderStatus.COMPLETED);
        financeService.createFinance(order);
        return orderMapper.toFullResponse(order);
    }
}
