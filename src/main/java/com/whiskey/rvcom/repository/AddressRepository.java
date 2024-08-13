package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.restaurant.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // todo 주소 검색 기능
    // todo 특정 지역의 레스토랑 조회
    // todo 주소 정보 업데이트
}
