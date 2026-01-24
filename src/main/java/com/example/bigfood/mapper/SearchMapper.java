
package com.example.bigfood.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import com.example.bigfood.dto.response.SearchResponse;
import com.example.bigfood.entity.HistorySearch;


@Mapper(componentModel = "spring")
public interface SearchMapper {
    List<SearchResponse> toListResponse(List<HistorySearch> historySearch);
}
