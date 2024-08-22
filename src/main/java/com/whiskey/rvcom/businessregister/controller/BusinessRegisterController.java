package com.whiskey.rvcom.businessregister.controller;

import com.whiskey.libs.rest.request.RequestMethod;
import com.whiskey.libs.rest.request.RestInvoker;
import com.whiskey.rvcom.businessregister.model.dto.BusinessRequestHead;
import com.whiskey.rvcom.businessregister.model.dto.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/businessregister")
public class BusinessRegisterController {

    @Value("${register.api}")
    private String KEY;

    private final String REQUEST_URL = "https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=";

    @PostMapping("/register")
    public String validBusinessRegister(@RequestBody BusinessRequestHead requestBody) throws Exception {
        System.out.println("requestBody = " + requestBody.toString());

        RestInvoker<ResponseBody> invoker = RestInvoker.create(REQUEST_URL+ KEY, ResponseBody.class);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        ResponseBody request = invoker.request(requestBody, BusinessRequestHead.class, headers, RequestMethod.POST);

        String result;

        System.out.println(request.getData().get(0).getValid());

        if(request.getData().get(0).getValid().equals("01")) {
            result = "success";
        } else {
            result = "fail";
        }

        System.out.println("result = " + result);
        return result;
    }


}
