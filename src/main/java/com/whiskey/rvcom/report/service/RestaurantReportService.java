package com.whiskey.rvcom.report.service;


import com.whiskey.rvcom.entity.report.RestaurantReport;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.report.model.dto.RestaurantDTO;
import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.repository.RestaurantReportRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantReportService {

    private final RestaurantReportRepository restaurantReportRepository;

    private final RestaurantRepository restaurantRepository;

    private final ModelMapper modelMapper;


    // 식당 신고 전체 조회
    public List<RestaurantReportDTO> getAllRestaurantReports() {
        List<RestaurantReport> reports = restaurantReportRepository.findAll();

        // 리스트로 반환해야 하므로 빈 리스트 추가
        List<RestaurantReportDTO> restaurantReportDTOList = new ArrayList<>();

        for (RestaurantReport report : reports) {

            // RestaurantReport -> RestaurantReportDTO
            RestaurantDTO restaurantDTO = modelMapper.map(report.getRestaurant(), RestaurantDTO.class);

            // RestaurantReport -> RestaurantReportDTO로 변환할 때 RestaurantDTO도 변환해줘야함
            RestaurantReportDTO restaurantReportDTO = modelMapper.map(report, RestaurantReportDTO.class);
            restaurantReportDTO.setRestaurantDTO(restaurantDTO);

            restaurantReportDTOList.add(restaurantReportDTO);
        }
        return restaurantReportDTOList;
    }


    // 식당 신고 세부 조회
    public RestaurantReportDTO getRestaurantReport(Long id) {

        // 가져온 id 값으로 데이터베이스에서 조회
        RestaurantReport restaurantReport = restaurantReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Report not found with ID: " + id));

        // 결과 값 DTO로 변환 후 리턴
        RestaurantReportDTO result = modelMapper.map(restaurantReport, RestaurantReportDTO.class);
        result.setRestaurantDTO(modelMapper.map(restaurantReport.getRestaurant(), RestaurantDTO.class));

        return result;
    }


    // 식당 신고 상태값 변경
    @Transactional
    public void restaurantReportPunish(Long id, boolean isPunished) {

        // 가져온 id 값으로 데이터베이스에서 조회
        RestaurantReport restaurantReport = restaurantReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Report not found with ID: " + id));

        // 상태값 (확인여부) 변경
        restaurantReport.setChecked(true);

        if (isPunished) {
            restaurantReport.setVisible(false);
        }

        // 변경된 상태값 저장
        restaurantReportRepository.save(restaurantReport);

        // 메일 발송 API 추후 추가 예정
    }


    // 식당 신고 등록
    @Transactional
    public void saveRestaurantReport(RestaurantReportDTO report) {

        // RestaurantReportDTO -> RestaurantReport로 변환
        RestaurantReport restaurantReport = modelMapper.map(report, RestaurantReport.class);
        // RestaurantReportDTO 안에는 RestaurantDTO가 있음 따라서
        // RestaurantReportDTO -> RestaurantReport로 변환할 때 RestaurantDTO도 변환해줘야함
        RestaurantDTO restaurantDTO = report.getRestaurantDTO();

        // RestaurantDTO에서 ID를 가져옴
        Long restaurantId = restaurantDTO.getId();

        // 데이터베이스에서 해당 ID를 가진 Restaurant를 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with ID: " + restaurantId));

        // 조회한 Restaurant 엔티티를 설정
        restaurantReport.setRestaurant(restaurant);

        restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
        restaurantReport.setRestaurant(restaurant);

        restaurantReportRepository.save(restaurantReport);
    }
}
