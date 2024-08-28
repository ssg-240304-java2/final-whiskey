package com.whiskey.rvcom.review;

import com.whiskey.rvcom.ImageFile.ImageFileService;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.receipt.ReceiptData;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.repository.RestaurantRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/receipt")
@RequiredArgsConstructor
@Slf4j
public class ReceiptVerificationController {

    private final ReceiptService receiptService;
    private final RestaurantRepository restaurantRepository;
    private final ImageFileService imageFileService;
    private final ReceiptApi receiptApi;

    @GetMapping("verify/{restaurantId}")
    public String verifyReceipt(@PathVariable Long restaurantId, Model model, HttpSession session) {
        // 로그인한 사용자의 정보를 가져옴
        Member member = (Member) session.getAttribute("member");

        // 로그인한 사용자의 정보가 없으면 로그인 페이지로 이동
        // todo. 메시지 출력해주도록 변경
        if (member == null) {
            return "redirect:/login";
        }

        // 영수증 인증 페이지로 이동
        model.addAttribute("restaurantId", restaurantId);
        return "receiptVerification";
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어있습니다.");
        }
        try {
            // 이미지 파일 업로드 및 UUID 파일명 반환
            String uuidFileName = imageFileService.uploadFile(file).getUuidFileName();
            return ResponseEntity.ok(uuidFileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패: " + e.getMessage());
        }
    }

    @PostMapping("/verificationUrl")
    @ResponseBody
    public ResponseEntity<Long> verifyReceipt(@RequestBody ReceiptData receiptData) {
        // ReceiptData 저장 및 ID 반환
        ReceiptData savedReceipt = receiptService.addReceipt(receiptData);
        return ResponseEntity.ok(savedReceipt.getId());
    }

    @GetMapping("/review/{restaurantId}/{receiptId}")
    public String gotoNextStep(@PathVariable Long restaurantId, @PathVariable Long receiptId, Model model) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        ReceiptData receiptData = receiptService.getReceipt(receiptId);

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("receiptId", receiptId);

        return "writeReview";
    }
}
