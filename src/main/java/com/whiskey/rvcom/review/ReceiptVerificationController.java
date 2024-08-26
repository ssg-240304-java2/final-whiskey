package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.receipt.ReceiptData;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/receipt")
@RequiredArgsConstructor
public class ReceiptVerificationController {
//    @GetMapping("/receipt-verification")
//    // 영수증 인증 페이지로 이동
//    public String getReceiptVerification() {
//        return "receiptVerification";
//    }

    private final ReceiptService receiptService;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("verify/{restaurantId}")
    public String verifyReceipt(@PathVariable Long restaurantId, Model model) {
        // 영수증 인증 페이지로 이동
        model.addAttribute("restaurantId", restaurantId);
        return "receiptVerification";
    }

    @PostMapping("/verificationUrl")
    public Long verifyReceipt(@RequestBody ReceiptData receiptData) {
        System.out.println("================ next-step 요청 들어옴");
        System.out.println("=====================================");
        System.out.println("================ receiptData: " + receiptData);

        ReceiptData receiptData1 = new ReceiptData();
        receiptData1.setTotalPrice(receiptData.getTotalPrice());
        receiptData1.setPaidAt(receiptData.getPaidAt());
        receiptData1.setStoreName(receiptData.getStoreName());
        receiptData1.setStoreAddress(receiptData.getStoreAddress());
        receiptData1.setPaidItems(receiptData.getPaidItems());

        receiptService.addReceipt(receiptData1);

        ReceiptData uploadedReceipt = receiptService.getReceipt(receiptData1.getId());
        System.out.println("================ uploadedReceipt: " + uploadedReceipt);

        return uploadedReceipt.getId();
    }

    @GetMapping("/review/{restaurantId}/{receiptId}")
    public String gotoNextStep(@PathVariable Long restaurantId, @PathVariable Long receiptId, Model model) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
//        ReceiptData receiptData = receiptService.getReceipt(receiptId);
        ReceiptData receiptData = receiptService.getReceipt(receiptId);

        System.out.println("================ restaurant: " + restaurant);

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("receiptId", receiptId);

        return "writeReview";
    }
}
