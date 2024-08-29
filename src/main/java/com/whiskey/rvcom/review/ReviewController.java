package com.whiskey.rvcom.review;

import com.whiskey.libs.file.FileNameGroup;
import com.whiskey.rvcom.ImageFile.ImageFileService;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.resource.ImageFile;
import com.whiskey.rvcom.entity.review.*;
import com.whiskey.rvcom.repository.MemberRepository;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.review.dto.ReviewDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final RestaurantService restaurantService;
//    private final RestaurantRepository restaurantRepository;    // need. 서비스 모듈로 교체 필요(업요전달)

    private final ReviewService reviewService;
    private final ReviewCommentService reviewCommentService;
    private final ReviewLikeService reviewLikeService;
    private final ReviewImageService reviewImageService;
    private final MemberRepository memberRepository;
    private final ReceiptService receiptService;

    @Autowired
    private ImageFileService imageFileService;

    // use path variable
    // 리뷰 목록 조회 요청
//    @GetMapping("/{restaurantNo}")
//    public String getReviewsByRestaurantId(@PathVariable Long restaurantNo, Model model) {
//        Restaurant restaurant = restaurantRepository.findById(restaurantNo).orElseThrow();
//        List<Review> reviews = reviewService.getReviewsByRestaurant(restaurant);
//
//        model.addAttribute("reviews", reviews);     // desc. 리뷰 목록 바인딩
//
//        return "restaurantDetail";  // need. 뷰 분할 후 리뷰 페이지에 대한 뷰로 변경
//    }

    //    @PostMapping("/list/{restaurantNo}")
    public String getReviewCommentsByReviewId(@PathVariable Long reviewNo, Model model) {
        // todo. 리뷰 아이디로 리뷰 댓글 목록 조회
        Review dest = reviewService.getReviewById(reviewNo);
        List<ReviewComment> comments = reviewCommentService.getCommentsForReview(dest);

        model.addAttribute("comments", comments);     // desc. 리뷰 댓글 목록 바인딩
        return "restaurantDetail";
    }

    // 리뷰 댓글 작성 요청
    @PostMapping("/comment")
    public String saveReviewComment(Long reviewNo, String content) { // need. 리뷰 작성자 세션 정보 추가 필요
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setCommenter(null);   // block. 로그인한 사용자 정보로 대체
        reviewComment.setContent(content);
        reviewComment.setReview(reviewService.getReviewById(reviewNo));

        return "redirect:/";
    }

    // 리뷰 댓글 삭제 요청
    @PostMapping("/comment/remove")
    public String removeReviewComment(Long commentNo) {
        ReviewComment dest = reviewCommentService.getCommentById(commentNo);
        reviewCommentService.removeComment(dest);

        return "redirect:/";
    }

    // 리뷰 좋아요 추가 요청
    @PostMapping("/reviewlike/add")
    @ResponseBody
    public ResponseEntity<Long> addLikeToReview(@RequestParam("reviewId") Long reviewNo, HttpSession session) { // need. 좋아요 처리할 사용자 정보 추가 필요
        Review dest = reviewService.getReviewById(reviewNo);
        Member member = (Member) session.getAttribute("member");

        ReviewLike reviewLike = reviewLikeService.getReviewLikeByReviewAndMember(dest, member);

        // 이미 해당 리뷰에 좋아요를 누른 경우 좋아요 취소(토글처리)
        if (reviewLike == null) {
            reviewLike = new ReviewLike();
            reviewLike.setReview(dest);
            reviewLike.setMember(member);
            reviewLikeService.addReviewLike(reviewLike);
        } else {
            reviewLikeService.removeReviewLike(reviewLike);
        }

        return ResponseEntity.ok(reviewLikeService.getReviewLikeCount(dest));
    }

    // let submitFormData = new FormData();
    //            submitFormData.append('title', document.getElementById('title').value);
    //            submitFormData.append('content', document.getElementById('content').value);
    //            submitFormData.append('rating', rating);
    //            submitFormData.append('images', imageFileIdsAsString);
    //
    //            $.ajax({
    //                url: "review/saveReview",
    //                type: "POST",
    //                data: submitFormData,
    //                processData: false,
    //                contentType: false,
    //                success: function (data) {
    //                    alert("리뷰가 성공적으로 등록되었습니다.");
    //                    window.location.href = data.redirectUrl;
    //                },
    //                error: function (error) {
    //                    console.log("리뷰 등록 오류 : ", error);
    //                    alert("리뷰 등록 중 오류가 발생했습니다.");
    //                },
    //            })
    @PostMapping("/saveReview")
    public ResponseEntity<Void> saveReview(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("rating") int rating,
            @RequestParam("images") String imageFileIds,
            @RequestParam("restaurantId") Long restaurantId,
            @RequestParam("receiptDataId") Long receiptDataId,
            HttpSession session
    ) {
        System.out.println("리뷰 저장 요청 받음");

        System.out.println("title: " + title);
        System.out.println("content: " + content);
        System.out.println("rating: " + rating);
        System.out.println("images: " + imageFileIds);
        System.out.println("restaurantId: " + restaurantId);
        System.out.println("receiptDataId: " + receiptDataId);

        List<Long> imageFileIdList = Arrays.stream(imageFileIds.split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        System.out.println("imageFileIdList: " + imageFileIdList);

        Review destReview = new Review();
        destReview.setTitle(title);
        destReview.setContent(content);
//        destReview.setRating(rating); int -> Rating Enum
//        destReview.setRating(Rating.valueOf(rating));
        destReview.setReviewer((Member) session.getAttribute("member"));
        destReview.setRestaurant(restaurantService.getRestaurantById(restaurantId));
        destReview.setReceiptData(receiptService.getReceipt(receiptDataId));
//        destReview.setRating(Rating.valueOf(rating));
        // rating은 반드시 1~5사이의 정수이며 Rating ENum타입으로 변환되어야 함
        switch(rating) {
            case 1:
                destReview.setRating(Rating.ONE_STAR);
                break;
            case 2:
                destReview.setRating(Rating.TWO_STAR);
                break;
            case 3:
                destReview.setRating(Rating.THREE_STAR);
                break;
            case 4:
                destReview.setRating(Rating.FOUR_STAR);
                break;
            case 5:
                destReview.setRating(Rating.FIVE_STAR);
                break;
            default:    // edge-case(Exception)
                destReview.setRating(Rating.ONE_STAR);
        }

//        destReview.getReviewImages().add()
//        reviewImageService.getReviewImageById(1L);
//        imageFileIdList.forEach(imageFileId -> {
//            ImageFile imageFile = imageFileService.getImageFile(imageFileId);
//            ReviewImage reviewImage = new ReviewImage();
//            destReview.getReviewImages().add(reviewImage);
//        });

        System.out.println("1차 저장 시도");
        reviewService.saveReview(destReview);
        System.out.println("1차 저장 완료");
        System.out.println("배당받은 리뷰 ID: " + destReview.getId());

        System.out.println("이미지 파일 처리 시작");
//        List<ReviewImage> reviewImages = new ArrayList<>();
        destReview.setReviewImages(new ArrayList<>());
        for (Long l : imageFileIdList) {
            System.out.println("처리할 이미지 파일 ID: " + l);
            ImageFile imageFile = imageFileService.getImageFile(l);
            System.out.println("가져온 이미지 엔티티 데이터: " + imageFile.getId());
            System.out.println("가져온 이미지 엔티티의 originalFileName: " + imageFile.getOriginalFileName());
            System.out.println("가져온 이미지 엔티티의 uuidFileName: " + imageFile.getUuidFileName());

            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setImageFile(imageFile);
            reviewImage.setReview(destReview);

            // 리뷰이미지 엔티티 저장하여 영속화
            System.out.println("ReviewImage 객체 생성 및 영속화");
            reviewImageService.addReviewImage(reviewImage);

            destReview.getReviewImages().add(reviewImage);

            System.out.println("ReviewImage 객체 생성 및 저장 완료");
        }

        System.out.println("2차 저장 시도");
        reviewService.saveReview(destReview);
        System.out.println("2차 저장 완료");



//        ReviewImage reviewImage = new ReviewImage();
//        reviewImage.setReview(destReview);
//        System.out.println("이미지 파일 처리 시작");
//        List<ReviewImage> reviewImages = new ArrayList<>();
//        System.out.println("ReviewImage 리스트 생성");
//        for(var imageId : imageFileIdList) {
//            System.out.println("처리할 이미지 파일 ID: " + imageId);
//            ImageFile imageFile = imageFileService.getImageFile(imageId);
//            System.out.println("가져온 이미지 엔티티 데이터: " + imageFile.getId());
//
//            System.out.println("ReviewImage 객체 생성");
//            ReviewImage reviewImage = new ReviewImage();
//            reviewImage.setImageFile(imageFile);
//            reviewImage.setReview(destReview);
//            System.out.println("ReviewImage 객체 생성 완료 : " + reviewImage);
//
//            reviewImages.add(reviewImage);
//            System.out.println("ReviewImage 리스트에 추가 완료");
//        }
//        System.out.println("이미지 파일 처리 완료");
//
//        destReview.setReviewImages(reviewImages);
//        System.out.println("생성된 ReviewImage 리스트를 Review 객체에 추가 완료");
//
//        System.out.println("2차 리뷰 저장 시도");
//        reviewService.saveReview(destReview);
//        System.out.println("2차 리뷰 저장 완료");

        return ResponseEntity.ok().build();
    }

//    @PostMapping("/save")
//    @ResponseBody
//    public ResponseEntity<?> saveReview(@ModelAttribute ReviewDTO reviewDTO,
//                                        @RequestParam("images") List<MultipartFile> images,
//                                        HttpSession session) {
//        log.info("Received request to save review: {}", reviewDTO);
//        try {
//            logger.info("Received reviewDTO: {}", reviewDTO);
//            // receiptDataId 확인
//            if (reviewDTO.getReceiptDataId() == 0) {
//                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "영수증 데이터 ID가 유효하지 않습니다."));
//            }
//            // 세션에서 사용자 정보 가져오기
//            Member member = (Member) session.getAttribute("member");
//            if (member == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(Map.of("success", false, "message", "로그인이 필요합니다."));
//            }
//
//            // images에서 항목을 하나씩 꺼내와 파일을 ncp에 업로드 수행
//            List<FileNameGroup> uploadedFiles = new ArrayList<>();
//            for (MultipartFile image : images) {
//                FileNameGroup fileNameGroup = fileUploader.upload(image);
//                uploadedFiles.add(fileNameGroup);
//            }
//
//            // FileUpload 클래스로 업로드를 하면 FileNameGroup이 하나씩 반환된다.
//            // 1. 업로드 수행 후 파일 이름 객체를 가져와 saveFileName에 저장
//            List<String> savedFileNames = uploadedFiles.stream()
//                    .map(FileNameGroup::getSaveFileName)
//                    .collect(Collectors.toList());
//
//            // 2. 업로드 수행 후 파일 이름 객체를 가지고 ImageFile 타입의 엔티티를 각각 생성하여 데이터베이스에 저장
//            List<ImageFile> imageFiles = new ArrayList<>();
//            for (FileNameGroup fileNameGroup : uploadedFiles) {
//                ImageFile imageFile = new ImageFile();
//                imageFile.setOriginalFileName(fileNameGroup.getOriginalFileName());
//                imageFile.setUuidFileName(fileNameGroup.getUuidFileName());
//                imageFiles.add(imageFileService.saveImageFile(imageFile));
//            }
//
//            // 3. 저장된 ImageFile 엔티티를 ReviewImage 엔티티 리스트로만 만듦
//            List<ReviewImage> reviewImages = imageFiles.stream()
//                    .map(imageFile -> {
//                        ReviewImage reviewImage = new ReviewImage();
//                        reviewImage.setImageFile(imageFile);
//                        return reviewImage;
//                    })
//                    .collect(Collectors.toList());
//
//            // 리뷰 엔티티 객체 생성
//            Review review = new Review();
//            review.setRating(reviewDTO.getRating());
//            review.setContent(reviewDTO.getContent());
//            review.setReviewer(member);
//            review.setRestaurant(restaurantService.getRestaurantById(reviewDTO.getRestaurantId()));
//            review.setReceiptData(receiptService.getReceipt(reviewDTO.getReceiptDataId()));
//            review.setReviewImages(reviewImages);
//
//            // 리뷰 저장 로직 호출
//            // Review savedReview = reviewService.saveReview(reviewDTO, images, member);
//            Review savedReview = reviewService.saveReview(new Review());
//
//            // 리다이렉트 URL 생성
//            String redirectUrl = "/restaurant/" + savedReview.getRestaurant().getId() + "#reviews";
//
//            return ResponseEntity.ok(Map.of("success", true, "redirectUrl", redirectUrl));
//        } catch (Exception e) {
//            logger.error("리뷰 저장 중 오류 발생", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("success", false, "message", e.getMessage()));
//        }
//    }

}
