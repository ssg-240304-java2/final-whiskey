package com.whiskey.rvcom.report.service;


import com.whiskey.rvcom.entity.report.RestaurantReport;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.repository.RestaurantReportRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantReportService {

    private final RestaurantReportRepository restaurantReportRepository;

    private final RestaurantRepository restaurantRepository;

    private final ModelMapper modelMapper;


    // 식당 신고 전체 조회
    public List<RestaurantReport> getAllRestaurantReports() {
        List<RestaurantReport> reports = restaurantReportRepository.findAll();

        for (RestaurantReport report : reports) {

            // RestaurantReport -> RestaurantReportDTO
            Restaurant restaurant = returnRestaurant(report.getRestaurant().getId());

            // RestaurantReport -> RestaurantReportDTO로 변환할 때 RestaurantDTO도 변환해줘야함
            report.setRestaurant(restaurant);
        }
        return reports;
    }


    // 식당 신고 세부 조회
    public RestaurantReport getRestaurantReport(Long id) {

        return restaurantReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Report not found with ID: " + id));
    }


    // 식당 신고 상태값 변경
    @Transactional
    public void restaurantReportPunish(Long id, boolean isPunish) {

        // 가져온 id 값으로 데이터베이스에서 조회
        RestaurantReport restaurantReport = restaurantReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Report not found with ID: " + id));

        // 상태값 (확인여부) 변경
        restaurantReport.setChecked(true);

        if (isPunish) {
            restaurantReport.setVisible(false);
        }

        // 변경된 상태값 저장
        restaurantReportRepository.save(restaurantReport);

        // 메일 발송 API 추후 추가 예정
    }


    // 식당 신고 등록
    @Transactional
    public void saveRestaurantReport(RestaurantReport report) {


        RestaurantReport restaurantReport = modelMapper.map(report, RestaurantReport.class);

        Restaurant restaurant = report.getRestaurant();

        Long restaurantId = restaurant.getId();

        // 데이터베이스에서 해당 ID를 가진 Restaurant를 조회
        restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with ID: " + restaurantId));

        // 조회한 Restaurant 엔티티를 설정

        restaurantReport.setRestaurant(restaurant);

        restaurantReportRepository.save(restaurantReport);
    }

    public Restaurant returnRestaurant(Long id) {

        return restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with ID: " + id));

    }
}
