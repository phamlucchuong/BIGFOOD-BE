package com.example.BIGFOOD;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.bigfood.dto.response.GoongResponse.GoongLocation;
import com.example.bigfood.service.GoongService;

@SpringBootTest
class BigfoodBeApplicationTests {
	@Autowired
	GoongService goongService;
	@Test
	void contextLoads() {
		double distances = goongService.getDrivingDistance(10.762622, 106.660172, 10.762895, 106.682301);
		System.out.println("Distance: " + distances);

		// GoongLocation location = goongService.getGeocoding("450-451 Lê Văn Việt, Phường Tăng Nhơn Phú, Tp. Hồ Chí Minh");
		// System.out.println("Location: " + location.getLat() + ", " + location.getLng());
	}

}
