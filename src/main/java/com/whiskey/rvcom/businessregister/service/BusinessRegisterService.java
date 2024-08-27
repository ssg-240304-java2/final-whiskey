package com.whiskey.rvcom.businessregister.service;


import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.report.RestaurantReport;
import com.whiskey.rvcom.entity.restaurant.Address;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import com.whiskey.rvcom.entity.restaurant.registration.RegistrationStatus;
import com.whiskey.rvcom.entity.restaurant.registration.RestaurantRegistration;
import com.whiskey.rvcom.repository.AddressRepository;
import com.whiskey.rvcom.repository.MemberRepository;
import com.whiskey.rvcom.repository.RestaurantRegistrationRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BusinessRegisterService {

    private final MemberRepository memberRepository;
    private final RestaurantRegistrationRepository restaurantRegistrationRepository;
    private final AddressRepository addressRepository;


    public Member getMember(Long memberId) {

        return memberRepository.findById(memberId).orElse(null);
    }

    public void saveRestaurantRegistration(RestaurantRegistration registration) {

        restaurantRegistrationRepository.save(registration);
    }

    public Address setAddress(String address) {
        Address restaurantAddress = new Address();
        restaurantAddress.setName(address);
        restaurantAddress.setLongitude(0);
        restaurantAddress.setLatitude(0);

        return addressRepository.save(restaurantAddress);
    }

    public Page<RestaurantRegistration> getAllBusinessRegister(int page, String sortOrder) {
        Sort sort = Sort.by("createdAt");

        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, 10, sort);
        return restaurantRegistrationRepository.findAll(pageable);
    }

    public RestaurantRegistration getBusinessRegister(Long id) {

        return restaurantRegistrationRepository.findById(id).orElse(null);
    }

    public String processBusinessRegist(Long registerId, boolean isApprove) {

        RestaurantRegistration registration = restaurantRegistrationRepository.findById(registerId).orElse(null);

        if (registration != null) {
            if (isApprove) {
                registration.setRegistrationStatus(RegistrationStatus.APPROVED);
                restaurantRegistrationRepository.save(registration);
            } else {
                registration.setRegistrationStatus(RegistrationStatus.REJECTED);
                restaurantRegistrationRepository.save(registration);
            }
        }

        return registration.getMember().getEmail();
    }

    public String getApproveMailText(Long registerId) {

        RestaurantRegistration registration = restaurantRegistrationRepository.findById(registerId).orElse(null);

        return "회원님의 입점 신청이 승인되었습니다.\n" +
                "다음과 같은 정보로 FoodFolio에 등록됩니다.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "음식점명 : " + registration.getRestaurantName() + "\n" +
                "음식점 카테고리 : " + registration.getRestaurantCategory() + "\n" +
                "음식점 전화번호 : " + registration.getRestaurantNumber() + "\n" +
                "음식점 주소 : " + registration.getRestaurantAddress().getName();
    }

    public String getRejectMailText(Long registerId) {

        RestaurantRegistration registration = restaurantRegistrationRepository.findById(registerId).orElse(null);

        return "회원님의 입점 신청이 반려되었습니다.\n" +
                "음식점 정보를 확인후 다시 신청해주세요.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "음식점명 : " + registration.getRestaurantName() + "\n" +
                "음식점 카테고리 : " + registration.getRestaurantCategory() + "\n" +
                "음식점 전화번호 : " + registration.getRestaurantNumber() + "\n" +
                "음식점 주소 : " + registration.getRestaurantAddress().getName();
    }

    public Restaurant getRestairantInfo(Long registerId) {

        RestaurantRegistration registration = restaurantRegistrationRepository.findById(registerId).orElse(null);

        Member owner = registration.getMember();
        String restaurantName = registration.getRestaurantName();
        String restaurantNumber = registration.getRestaurantNumber();
        Address restaurantAddress = registration.getRestaurantAddress();
        RestaurantCategory restaurantCategory = registration.getRestaurantCategory();

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);
        restaurant.setName(restaurantName);
        restaurant.setNumber(restaurantNumber);
        restaurant.setAddress(restaurantAddress);
        restaurant.setCategory(restaurantCategory);
        restaurant.setVisible(true);

        return restaurant;
    }
}
