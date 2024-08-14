package com.whiskey.rvcom.report;


import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import com.whiskey.rvcom.report.model.dto.RestaurantDTO;
import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.report.service.RestaurantReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class RestaurantReportTest {

    private final RestaurantReportService restaurantReportService;

    @Autowired
    public RestaurantReportTest(RestaurantReportService restaurantReportService) {
        this.restaurantReportService = restaurantReportService;
    }


    @Test
    @DisplayName("식당신고목록전체조회")
    public void findAll() {

        // given

        // when
        List<RestaurantReportDTO> reports = restaurantReportService.getAllRestaurantReports();

        for (RestaurantReportDTO report : reports) {
            System.out.println(report);
        }

        // then 조회한 신고목록이 null이 아닌지 확인
        Assertions.assertNotNull(reports);
    }


    @Test
    @DisplayName("식당 신고 세부조회")
    public void findById() {

        // given
        Long id = 1L;

        // when
        RestaurantReportDTO result = restaurantReportService.getRestaurantReport(id);

        System.out.println("id = " + id);
        System.out.println("result.getId() = " + result.getId());
        System.out.println("result = " + result);

        // then Null 값 인지 확인 후 조회한 신고와 DB에 있는 신고의 ID가 같은지 확인
        Assertions.assertNotNull(result);

        System.out.println("result.getId() = " + result.getId());
        System.out.println("id = " + id);

        Assertions.assertEquals(id, result.getId());
    }


    @Test
    @DisplayName("식당신고 결정 및 상태값 변경")
    public void restaurantReportPunish() {

        // given
        Long id = 1L;

        boolean isPunish = true; // 신고처리 결정 여부, web에서 누른 버튼에 따라 결정할 예정

        // when
        restaurantReportService.restaurantReportPunish(id, isPunish);

        // then 상태값 변경 후 확인
        RestaurantReportDTO result = restaurantReportService.getRestaurantReport(id);
        System.out.println("isPunish = " + isPunish);
        System.out.println("result.isVisible() = " + result.isVisible());
        System.out.println("result.isChecked() = " + result.isChecked());

        Assertions.assertTrue(result.isChecked());
        Assertions.assertFalse(result.isVisible());
    }


    @Test
    @DisplayName("식당신고등록")
    public void save() {

        // given
        RestaurantDTO restaurantDTO = new RestaurantDTO(2L, "아약스커리", RestaurantCategory.INDIAN, "000-000-0000", true);
        RestaurantReportDTO report = new RestaurantReportDTO(null, "신고 제목4", "신고 내용4", LocalDateTime.now(), false, true, restaurantDTO);

        // where
        restaurantReportService.saveRestaurantReport(report);

        // 조회 성공 후 목록 가져오기
        List<RestaurantReportDTO> reports = restaurantReportService.getAllRestaurantReports();

        // then 현재 테스트 코드로 등록한 신고와 DB에 등록된 신고의 내용이 같은지 비교
        Assertions.assertEquals(reports.get(reports.size()-1).getContent(), report.getContent());
    }
}
