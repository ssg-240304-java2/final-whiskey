package com.whiskey.rvcom.review;

import com.whiskey.libs.rest.request.*;
import com.whiskey.rvcom.ImageFile.ImageFileService;
import com.whiskey.rvcom.entity.receipt.ReceiptData;
import com.whiskey.rvcom.review.receipt.ReceiptMapper;
import com.whiskey.rvcom.util.ocr.model.receipt.ReceiptResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReceiptOcrRequestTest {
    private final ImageFileService imageFileService;

    @Autowired
    public ReceiptOcrRequestTest(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    @Test
    @DisplayName("OCR 요청 테스트")
    public void uploadFileAndRequestOcrTest() throws Exception {
        var invoker = RestInvoker.create("http://web.dokalab.site:8082/ocr/receipt", ReceiptResponse.class);

        String fileName = "2df35998-f930-4db0-af7a-3c00d54b8e58.jpg";
        OcrRequestBody requestBody = new OcrRequestBody();
        requestBody.fileName = fileName;

        ReceiptResponse request = invoker.request(requestBody, OcrRequestBody.class, RequestMethod.POST);
        System.out.println(request);

        System.out.println("Migration Test");
        ReceiptMapper receiptMapper = new ReceiptMapper();

        ReceiptData entity = receiptMapper.toEntity(request);
        System.out.println(entity);
        /*
            ReceiptData(
                id=null,
                storeName=순자포차,
                storeAddress=서울 강남구 강남대로120길 11 (논현동) 1 층(논현동),
                paidAt=2024-08-01T22:35:14,
                paidItems=[
                    PaidItem(id=null, itemName=제6두부김치, price=17000, quantity=1),
                    PaidItem(id=null, itemName=쫀듸기 품은 피카츄, price=10000, quantity=1),
                    PaidItem(id=null, itemName=테라 500, price=18000, quantity=4)
                ],
                totalPrice=45000
            )
         */
        Assertions.assertNotNull(entity);
    }
}

class OcrRequestBody {
    String fileName;
}
