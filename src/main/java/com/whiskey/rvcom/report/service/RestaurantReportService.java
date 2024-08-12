package com.whiskey.rvcom.report.service;


import com.whiskey.rvcom.entity.report.RestaurantReport;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.report.model.dto.RestaurantDTO;
import com.whiskey.rvcom.report.model.dto.RestaurantReportDTO;
import com.whiskey.rvcom.repository.RestaurantReportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantReportService {

    @Autowired
    private RestaurantReportRepository restaurantReportRepository;

    @Autowired
    private ModelMapper modelMapper;

    public RestaurantReportService(RestaurantReportRepository restaurantReportRepository, ModelMapper modelMapper) {
        this.restaurantReportRepository = restaurantReportRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void createRestaurantReport(RestaurantReportDTO tempRestaurantReport) {

        RestaurantReport restaurantReport = modelMapper.map(tempRestaurantReport, RestaurantReport.class);

        RestaurantDTO restaurantDTO = tempRestaurantReport.getRestaurantDTO();
        Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);

        restaurantReport.setRestaurant(restaurant);

        restaurantReportRepository.save(restaurantReport);

    }



    public List<RestaurantReportDTO> getAllRestaurantReports() {
        List<RestaurantReport> reports = restaurantReportRepository.findAll();

//        for (RestaurantReport report : reports) {
//            System.out.println("!@#!@#!@#!@#!@# : "+ report.getId());
//        }
//        return reports.stream()
//                .map(report -> modelMapper.map(report, RestaurantReportDTO.class))
//                .collect(Collectors.toList());

        // 리스트로 반환해야 하므로 빈 리스트 추가
        List<RestaurantReportDTO> restaurantReportDTOList = new ArrayList<>();

        for (RestaurantReport report : reports) {
            // RestaurantReport -> RestaurantReportDTO로 변환
            // RestaurantReportDto 안에는 RestaurantDTO가 있음 따라서
            // RestaurantReport -> RestaurantReportDTO로 변환할 때 RestaurantDTO도 변환해줘야함

            RestaurantDTO restaurantDTO = modelMapper.map(report.getRestaurant(), RestaurantDTO.class);
            RestaurantReportDTO restaurantReportDTO = modelMapper.map(report, RestaurantReportDTO.class);

            restaurantReportDTO.setRestaurantDTO(restaurantDTO);

            restaurantReportDTOList.add(restaurantReportDTO);
        }

        return restaurantReportDTOList;
    }
}
