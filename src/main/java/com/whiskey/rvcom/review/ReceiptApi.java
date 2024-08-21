package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.receipt.ReceiptData;
import com.whiskey.rvcom.review.receipt.ReceiptMapper;
import com.whiskey.rvcom.util.ocr.OcrRequestService;
import com.whiskey.rvcom.util.ocr.model.receipt.ReceiptResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReceiptApi {
    @PostMapping("/receipt/verification")
    public ResponseEntity<ReceiptData> verifyReceipt(@RequestParam String imagePath) throws Exception {
        // todo. 이미지 파일을 받아서 OCR로 텍스트 추출 후, ReceiptMapper를 통해 ReceiptData로 변환하여 반환
//        ReceiptResponseDto result = new ReceiptResponseDto();

        // test. 테스트 목적으로 임의의 이미지 파일로 오버라이드
        imagePath = "2df35998-f930-4db0-af7a-3c00d54b8e58.jpg";

        ReceiptResponse response = new OcrRequestService().invoke(imagePath);
        ReceiptData entity = new ReceiptMapper().toEntity(response);

        return ResponseEntity.ok(entity);
    }
}