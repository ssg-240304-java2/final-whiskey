package com.whiskey.rvcom.util.ocr;

import com.whiskey.libs.rest.request.*;
import com.whiskey.rvcom.util.ocr.model.receipt.*;

public class OcrRequestService {
    /**
     * OCR 처리 RestAPI로 요청 전달
     * @param fileName 파일명
     * @return ReceiptResponse OCR 처리 결과 객체
     */
    public ReceiptResponse invoke(String fileName) throws Exception {
        String requestUrl = "";
        RestInvoker<ReceiptResponse> invoker = RestInvoker.create(requestUrl, ReceiptResponse.class);

        OcrRequestHead requestHead = new OcrRequestHead();
        requestHead.setFileName(fileName);

        ReceiptResponse response = invoker.request(requestHead, OcrRequestHead.class, RequestMethod.POST);
        return response;
    }
}