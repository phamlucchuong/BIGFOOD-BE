package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.SearchRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.SearchResponse;
import com.example.bigfood.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {
    SearchService searchService;

    @PostMapping
    public ApiResponse<Void> postMethodName(@RequestBody SearchRequest request) {
        searchService.addSearch(request);
        return ApiResponse.<Void>builder()
            .message("Search history logged successfully.")
            .build();
    }
    

    @GetMapping
    public ApiResponse<List<SearchResponse>> getHotSearch() {
        return ApiResponse.<List<SearchResponse>>builder()
            .results(searchService.getHotSearch())
            .build();
    }
    
}
