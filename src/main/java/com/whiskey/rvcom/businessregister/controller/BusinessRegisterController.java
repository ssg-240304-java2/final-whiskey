package com.whiskey.rvcom.businessregister.controller;

import com.whiskey.libs.rest.request.RequestMethod;
import com.whiskey.libs.rest.request.RestInvoker;
import com.whiskey.rvcom.businessregister.model.dto.BusinessRequestHead;
import com.whiskey.rvcom.businessregister.model.dto.RegistInfo;
import com.whiskey.rvcom.businessregister.model.dto.MyResponseBody;
import com.whiskey.rvcom.businessregister.service.BusinessRegisterService;
import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import com.whiskey.rvcom.entity.restaurant.registration.RegistrationStatus;
import com.whiskey.rvcom.entity.restaurant.registration.RestaurantRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/businessregister")
@RequiredArgsConstructor
public class BusinessRegisterController {

    @Value("${register.api}")
    private String KEY;

    private final String REQUEST_URL = "https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=";

    private final BusinessRegisterService businessRegisterService;

    @PostMapping("/valid")
    @ResponseBody
    public String validBusinessRegister(@RequestBody BusinessRequestHead requestBody) throws Exception {

        RestInvoker<MyResponseBody> invoker = RestInvoker.create(REQUEST_URL + KEY, MyResponseBody.class);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        MyResponseBody request = invoker.request(requestBody, BusinessRequestHead.class, headers, RequestMethod.POST);

        String result;


        if(request.getData().get(0).getValid().equals("01")) {
            result = "success";
        } else {
            result = "fail";
        }

        return result;
    }


    @PostMapping("/regist")
    @ResponseBody
    public void insertRestaurantRegistration(@RequestBody RegistInfo registInfo) {

        RestaurantRegistration registration = new RestaurantRegistration();

        registration.setRestaurantName(registInfo.getName());
        registration.setRestaurantNumber(registInfo.getPhone());
        registration.setRegistrationStatus(RegistrationStatus.SUBMITTED);


        // Category
        registration.setRestaurantCategory(RestaurantCategory.valueOf(registInfo.getCategory().toUpperCase()));
        // Address
        registration.setRestaurantAddress(businessRegisterService.setAddress(registInfo.getAddress()));
        // Member
        registration.setMember(businessRegisterService.getMember(registInfo.getMemberId()));

        businessRegisterService.saveRestaurantRegistration(registration);
    }

    @GetMapping("/list")
    @ResponseBody
    public Page<RestaurantRegistration> getRegistrations(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "asc") String sortOrder) {
        return businessRegisterService.getAllBusinessRegister(page, sortOrder);
    }

    @GetMapping("/detail/{registerId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRegistrationDetail(@PathVariable("registerId") Long id) {
        System.out.println("id = " + id + " / 동작 확인");

        Map<String, Object> response = new HashMap<>();

        RestaurantRegistration register = businessRegisterService.getBusinessRegister(id);

        if(register != null) {
            response.put("register", register);
        } else {
            response.put("register", "register not found");
        }

        return ResponseEntity.ok(response);
    }

    // 임시 페이지 이동용
    @GetMapping("/registrestaurant")
    private String moveToPage() {
        return "admin/registrestaurant";
    }

    // 임시 페이지 이동용
    @GetMapping("/regist-detail")
    private String moveToDetailPage() {
        return "admin/regist-detail";
    }
}
