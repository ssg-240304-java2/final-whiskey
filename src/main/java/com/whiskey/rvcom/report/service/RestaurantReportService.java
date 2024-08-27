package com.whiskey.rvcom.report.service;


import com.whiskey.rvcom.entity.report.RestaurantReport;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.repository.RestaurantReportRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
public class RestaurantReportService {

    private final RestaurantReportRepository restaurantReportRepository;

    private final RestaurantRepository restaurantRepository;

    private final ModelMapper modelMapper;


    // 식당 신고 전체 조회
    public Page<RestaurantReport> getAllRestaurantReports(int page, String sortOrder) {

        Sort sort = Sort.by("reportedAt");

        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, 10, sort);
        return restaurantReportRepository.findAll(pageable);
    }


    // 식당 신고 세부 조회
    public RestaurantReport getRestaurantReport(Long id) {

        return restaurantReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Report not found with ID: " + id));

    }


    // 식당 신고 상태값 변경
    @Transactional
    public String restaurantReportPunish(Long id, boolean isPunish) {

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

        return restaurantReport.getRestaurant().getOwner().getEmail();

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

    public String getMailText(Long reportId) {

        RestaurantReport report = getRestaurantReport(reportId);

        return "다음과 같은 내용이 확인되었으니 정보 변경바랍니다. \n" +
                "신고 내용 : " + report.getContent();
    }
}
