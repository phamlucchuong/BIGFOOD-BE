package com.example.bigfood.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.RestaurantCategoryCreateRequest;
import com.example.bigfood.dto.response.RestaurantCategoryResponse;
import com.example.bigfood.entity.RestaurantCategory;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.RestaurantCategoryMapper;
import com.example.bigfood.repository.RestaurantCategoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestaurantCategoryService {
    @Autowired
    RestaurantCategoryRepository restaurantCategoryRepository;
    @Autowired
    RestaurantCategoryMapper restaurantCategoryMapper;

    public RestaurantCategoryResponse create(RestaurantCategoryCreateRequest request) {
        if(restaurantCategoryRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.RESTAURANT_CATEGORY_ALREADY_EXISTS);
        }
        return restaurantCategoryMapper.toRestaurantCategoryResponse(
            restaurantCategoryRepository.save(restaurantCategoryMapper.toRestaurantCategory(request))
        );
    }
    public RestaurantCategoryResponse getById(String id){
        var restaurantCategory = restaurantCategoryRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_CATEGORY_NOT_EXISTS));
        return restaurantCategoryMapper.toRestaurantCategoryResponse(restaurantCategory);
    }

    public List<RestaurantCategoryResponse> getAll(){
        List<RestaurantCategoryResponse> listRCResponse = restaurantCategoryRepository
            .findAll()
            .stream()
            .map(restaurantCategoryMapper ::toRestaurantCategoryResponse)
            .toList();
        return listRCResponse;
    }

    public void deleteById(String id){
        try {
            restaurantCategoryRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_CATEGORY_NOT_EXISTS));
        } catch (EmptyResultDataAccessException  e) {
            throw e;
        }
        restaurantCategoryRepository.deleteById(id);
    }

    public Set<RestaurantCategory> getCategoriesByIds(List<String> categoryIds) 
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
