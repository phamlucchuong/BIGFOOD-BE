package com.example.bigfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoongService {

    @Value("${goong.api.key}")
    private String GOONG_API_KEY;

    @Value("${goong.api.base-url}")
    private String GOONG_BASE_URL;

    private final WebClient webClient;

    public GoongService(WebClient webClient) {
        this.webClient = webClient;
    }

    public GoongLocation getGeocoding(String address) {
        String geocodingPath = "geocode";

        String url = UriComponentsBuilder.fromHttpUrl(GOONG_BASE_URL)
                .path(geocodingPath)
                .queryParam("address", address)
                .queryParam("api_key", GOONG_API_KEY)
                .build()
                .toUriString();

        GeocodingResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GeocodingResponse.class)
                .block();

        // 3. Xử lý kết quả
        if (response != null && response.getStatus().equals("OK") && !response.getResults().isEmpty()) {
            return response.getResults().get(0).getGeometry().getLocation();
        }

        // Xử lý khi không tìm thấy hoặc lỗi
        throw new RuntimeException("Không tìm thấy tọa độ cho địa chỉ: " + address);
    }

    // Ví dụ về cấu trúc JSON từ Goong Geocoding
    public static class GoongGeometry {
        private GoongLocation location;

        public GoongLocation getLocation() {
            return location;
        }

        public void setLocation(GoongLocation location) {
            this.location = location;
        }
    }

    public static class GoongLocation {
        private double lat; // Latitude
        private double lng; // Longitude

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    public static class GoongResult {
        private GoongGeometry geometry;

        public GoongGeometry getGeometry() {
            return geometry;
        }

        public void setGeometry(GoongGeometry geometry) {
            this.geometry = geometry;
        }
    }

    public static class GeocodingResponse {
        private List<GoongResult> results;
        private String status;

        public List<GoongResult> getResults() {
            return results;
        }

        public void setResults(List<GoongResult> results) {
            this.results = results;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public Object getReverseGeocoding(GoongLocation geocoding) {
        String geocodingPath = "geocode";

        String url = UriComponentsBuilder.fromHttpUrl(GOONG_BASE_URL)
                .path(geocodingPath)
                .queryParam("latlng", geocoding.getLat() + "," + geocoding.getLng())
                .queryParam("api_key", GOONG_API_KEY)
                .build()
                .toUriString();

        Object response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        // 3. Xử lý kết quả
        // if (response != null && response.getStatus().equals("OK") && !response.getResults().isEmpty()) {
            return response;
        // }

        // Xử lý khi không tìm thấy hoặc lỗi
        // throw new RuntimeException("Không tìm thấy tọa độ cho địa chỉ: " + geocoding.getLat() + "," + geocoding.getLng());
    }
}
