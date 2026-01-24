package com.example.bigfood.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.SearchRequest;
import com.example.bigfood.dto.response.SearchResponse;
import com.example.bigfood.entity.HistorySearch;
import com.example.bigfood.mapper.SearchMapper;
import com.example.bigfood.repository.HistorySearchRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {

    HistorySearchRepository historySearchRepository;
    SearchMapper searchMapper;

    public void addSearch(SearchRequest request) {
        System.out.println("Search content: '" + request.getContent() + "******************************");
        String searchContent = request.getContent().toLowerCase().trim();
        if (searchContent == "")
            return;

        var existingSearch = historySearchRepository.findByContent(searchContent);
        if (existingSearch.isPresent()) {
            HistorySearch historySearch = existingSearch.get();
            historySearch.setCount(historySearch.getCount() + 1);
            historySearchRepository.save(historySearch);
        } else {
            HistorySearch newSearch = HistorySearch.builder()
                    .content(searchContent)
                    .count(1)
                    .build();
            historySearchRepository.save(newSearch);
        }
    }

    public List<SearchResponse> getHotSearch() {
        return searchMapper.toListResponse(historySearchRepository.findTop20ByOrderByCountDesc());
    }
}
