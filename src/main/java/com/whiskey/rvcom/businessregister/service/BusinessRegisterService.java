package com.whiskey.rvcom.businessregister.service;


import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.restaurant.Address;
import com.whiskey.rvcom.entity.restaurant.registration.RegistrationStatus;
import com.whiskey.rvcom.entity.restaurant.registration.RestaurantRegistration;
import com.whiskey.rvcom.repository.AddressRepository;
import com.whiskey.rvcom.repository.MemberRepository;
import com.whiskey.rvcom.repository.RestaurantRegistrationRepository;
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
}
