package com.whiskey.rvcom.Member.controller;

import com.whiskey.rvcom.ImageFile.ImageFileService;
import com.whiskey.rvcom.Member.service.MemberManagementService;
import com.whiskey.rvcom.Member.service.SocialLoginService;
import com.whiskey.rvcom.entity.favorite.Favorite;
import com.whiskey.rvcom.entity.member.LoginType;
import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.entity.member.Role;
import com.whiskey.rvcom.entity.resource.ImageFile;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.review.Review;
import com.whiskey.rvcom.favorite.FavoriteService;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.review.ReviewImageService;
import com.whiskey.rvcom.review.ReviewService;
import com.whiskey.rvcom.util.ImagePathParser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class MemberController {

    private final ReviewService reviewService;
    private final SocialLoginService socialLoginService;
    private final MemberManagementService memberManagementService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final ImageFileService imageFileService;
    private final ReviewImageService reviewImageService;
    private final FavoriteService favoriteService;
    private final RestaurantService restaurantService;

    @Autowired
    public MemberController(ReviewService reviewService, SocialLoginService socialLoginService,
                            MemberManagementService memberManagementService, PasswordEncoder passwordEncoder,
                            RestTemplate restTemplate, ImageFileService imageFileService, ReviewImageService reviewImageService, FavoriteService favoriteService, RestaurantService restaurantService) {
        this.reviewService = reviewService;
        this.socialLoginService = socialLoginService;
        this.memberManagementService = memberManagementService;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
        this.imageFileService = imageFileService;
        this.reviewImageService = reviewImageService;
        this.favoriteService = favoriteService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/admin/user-management")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listMembersData(@RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 각각의 로그인 타입에 따라 데이터를 가져옵니다.
        Page<Member> basicMembers = memberManagementService.getMembers(pageable);
        Page<Member> naverMembers = socialLoginService.getMembers(pageable, LoginType.NAVER);
        Page<Member> googleMembers = socialLoginService.getMembers(pageable, LoginType.GOOGLE);

        // 모든 데이터를 합칩니다.
        List<Member> combinedMembers = new ArrayList<>();
        combinedMembers.addAll(basicMembers.getContent());
        combinedMembers.addAll(naverMembers.getContent());
        combinedMembers.addAll(googleMembers.getContent());

        // 전체 페이지 수와 아이템 수를 계산합니다.
        int totalElements = (int) (basicMembers.getTotalElements() + naverMembers.getTotalElements() + googleMembers.getTotalElements());
        Page<Member> combinedPage = new PageImpl<>(combinedMembers, pageable, totalElements);

        // 응답을 준비합니다.
        Map<String, Object> response = new HashMap<>();
        response.put("content", combinedPage.getContent());
        response.put("totalPages", combinedPage.getTotalPages());
        response.put("totalElements", combinedPage.getTotalElements());
        response.put("number", combinedPage.getNumber());
        response.put("size", combinedPage.getSize());

        return ResponseEntity.ok(response);
    }




    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String userRole = (String) session.getAttribute("userRole");

        if (Boolean.TRUE.equals(isAuthenticated) && userRole != null) {
            return "redirect:/mainPage";
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId, @RequestParam String password, HttpSession session, Model model) {
        Member member = memberManagementService.findByLoginId(loginId);
        if (member == null) {
            model.addAttribute("error", "존재하지 않는 회원입니다.");
            return "login";
        }

        if (!member.isActive()) {
            model.addAttribute("alertMessage", "해당 계정은 비활성화되어 있습니다. 관리자에게 문의하세요.");
            return "login";
        }

        if (passwordEncoder.matches(password, member.getPassword())) {
            setSessionAttributes(session, member);
            return "redirect:/mainPage";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login";
        }
    }

    @GetMapping("/mainPage")
    public String mainPage(HttpSession session, Model model) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String userRole = (String) session.getAttribute("userRole");

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("userRole", userRole);

        return "mainPage";
    }

    @PostMapping("/mypage/remove-favorite")
    public String removeFavorite(@RequestParam("restaurantId") Long restaurantId,
                                 @RequestParam(value = "favoritePage", defaultValue = "0") int favoritePage,
                                 @RequestParam(value = "favoriteSize", defaultValue = "10") int favoriteSize,
                                 HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            return "redirect:/login";
        }

        try {
            // restaurantId를 사용하여 Restaurant 객체를 조회
            Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

            // 조회된 Restaurant 객체를 removeFavorite 메서드에 전달
            favoriteService.removeFavorite(member, restaurant);

            // 현재 페이지의 즐겨찾기 아이템이 모두 제거된 경우
            List<Favorite> favorites = favoriteService.getFavoritesByMember(member);
            int totalPages = (int) Math.ceil((double) favorites.size() / favoriteSize);

            // 삭제 후 페이지가 없어지는 경우 이전 페이지로 이동
            if (favoritePage >= totalPages && favoritePage > 0) {
                model.addAttribute("message", "즐겨찾기가 해제되었습니다.");
                return "redirect:/mypage?favoritePage=" + (favoritePage - 1) + "&favoriteSize=" + favoriteSize + "&activeTab=favorite";
            }

            // 삭제 후 현재 페이지로 리다이렉트
            model.addAttribute("message", "즐겨찾기가 해제되었습니다.");
            return "redirect:/mypage?favoritePage=" + favoritePage + "&favoriteSize=" + favoriteSize + "&activeTab=favorite";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "해당 레스토랑을 찾을 수 없습니다.");
            return "mypage";
        } catch (Exception e) {
            model.addAttribute("error", "즐겨찾기 해제 중 오류가 발생했습니다.");
            return "mypage";
        }
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         @RequestParam(value = "favoritePage", defaultValue = "0") int favoritePage,
                         @RequestParam(value = "favoriteSize", defaultValue = "10") int favoriteSize,
                         @RequestParam(value = "activeTab", defaultValue = "profile") String activeTab) {

        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            return "redirect:/login";
        }

        List<Favorite> favorites = favoriteService.getFavoritesByMember(member);

        // 즐겨찾기 페이지네이션을 위한 하위 리스트 생성
        int favoriteStart = Math.min(favoritePage * favoriteSize, favorites.size());
        int favoriteEnd = Math.min((favoritePage + 1) * favoriteSize, favorites.size());
        List<Favorite> paginatedFavorites = favorites.subList(favoriteStart, favoriteEnd);

        // 프로필 이미지 URL 설정
        String profileImageUrl = "https://via.placeholder.com/150";
        if (member.getProfileImage() != null) {
            profileImageUrl = ImagePathParser.parse(member.getProfileImage().getUuidFileName());
        }

        // 회원의 리뷰를 가져옴
        List<Review> reviews = reviewService.getReviewsByMember(member);

        // 페이지네이션을 위한 하위 리스트 생성
        int start = Math.min(page * size, reviews.size());
        int end = Math.min((page + 1) * size, reviews.size());
        List<Review> paginatedReviews = reviews.subList(start, end);

        // 리뷰 이미지 리스트를 가져와서 URL로 변환
        Map<Long, List<String>> reviewImagesMap = new HashMap<>();
        for (Review review : paginatedReviews) {
            List<String> imageUrls = reviewImageService.getReviewImagesByReview(review).stream()
                    .map(image -> ImagePathParser.parse(image.getImageFile().getUuidFileName()))
                    .collect(Collectors.toList());
            reviewImagesMap.put(review.getId(), imageUrls);
        }

        // 즐겨찾기된 레스토랑의 평균 평점을 계산하여 가져옴
        Map<Long, Double> restaurantRatingsMap = new HashMap<>();
        for (Favorite favorite : paginatedFavorites) {
            double averageRating = reviewService.getAverageRatingForRestaurant(reviewService.getReviewsByRestaurantAsList(favorite.getRestaurant()));
            restaurantRatingsMap.put(favorite.getRestaurant().getId(), averageRating);
        }

        int totalPages = (int) Math.ceil((double) reviews.size() / size);
        int favoriteTotalPages = (int) Math.ceil((double) favorites.size() / favoriteSize);

        model.addAttribute("member", member);
        model.addAttribute("profileImageUrl", profileImageUrl);
        model.addAttribute("isSocialLogin", member.getLoginType() != LoginType.BASIC);
        model.addAttribute("reviews", paginatedReviews); // 페이지네이션 된 리뷰 목록
        model.addAttribute("reviewImagesMap", reviewImagesMap); // 리뷰 이미지 URL 맵
        model.addAttribute("restaurantRatingsMap", restaurantRatingsMap); // 레스토랑 평점 맵
        model.addAttribute("favorites", paginatedFavorites); // 즐겨찾기된 레스토랑 목록 추가
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("favoriteCurrentPage", favoritePage);
        model.addAttribute("favoriteTotalPages", favoriteTotalPages > 0 ? favoriteTotalPages : 1); // 기본값 설정
        model.addAttribute("favoriteSize", favoriteSize);
        model.addAttribute("activeTab", activeTab); // 활성화된 탭 정보 전달

        return "mypage";
    }

    @PostMapping("/checkLoginId")
    public ResponseEntity<Map<String, Boolean>> checkLoginId(@RequestParam String loginId) {
        boolean exists = memberManagementService.existsByLoginId(loginId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register_basic")
    public String registerBasic(@RequestParam Map<String, String> allParams, Model model) {
        String loginId = allParams.get("loginId");
        String password = allParams.get("password");
        String name = allParams.get("name");
        String email = allParams.get("email");
        String nickname = allParams.get("nickname");
        String verificationCode = allParams.get("emailVerificationCode");

        // 로그인 ID 중복 확인
        if (memberManagementService.existsByLoginId(loginId)) {
            model.addAttribute("error", "이미 존재하는 로그인 ID입니다. 다른 ID를 사용해주세요.");
            return "register_basic";
        }

        // 이메일 인증 코드 검증
        String url = String.format("https://web.dokalab.site:8081/api/redis/get?key=%s", email);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() != HttpStatus.OK || !response.getBody().equals(verificationCode)) {
            model.addAttribute("error", "이메일 인증에 실패했습니다. 올바른 인증 코드를 입력해주세요.");
            return "register_basic";
        }

        String encodedPassword = passwordEncoder.encode(password);

        try {
            memberManagementService.registerMember(name, nickname, loginId, email, encodedPassword, LoginType.BASIC);
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "register_basic";
        }
    }

    @PostMapping("/register_social")
    public String registerSocial(@RequestParam String nickname, HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        String loginTypeStr = (String) session.getAttribute("loginType");
        Map<String, Object> userAttributes = (Map<String, Object>) session.getAttribute("userAttributes");

        if (loginId == null || loginTypeStr == null || userAttributes == null) {
            model.addAttribute("error", "세션 정보가 없습니다. 다시 로그인해 주세요.");
            return "redirect:/login";
        }

        LoginType loginType = LoginType.valueOf(loginTypeStr);

        try {
            Member existingMember = socialLoginService.findMemberByLoginIdAndLoginType(loginId, loginType);
            if (existingMember != null) {
                model.addAttribute("error", "이미 존재하는 회원입니다.");
                return "register_social";
            }

            Member member = new Member();
            member.setLoginId(loginId);
            member.setNickname(nickname);
            member.setName((String) userAttributes.get("name"));
            member.setEmail((String) userAttributes.get("email"));
            member.setLoginType(loginType); // LoginType 설정
            member.setRole(Role.USER);
            member.setActive(true);
            member.setPassword(null);

            // 사용자에게 환영 메시지를 설정
            member.setIntroduction("안녕하세요 " + nickname + "입니다!! 만나서 반갑습니다");

            socialLoginService.save(member);
            setSessionAttributes(session, member);

            return "redirect:/mainPage";

        } catch (Exception ex) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "register_social";
        }
    }

    @GetMapping("/register_basic")
    public String showBasicRegisterForm(Model model) {
        model.addAttribute("isSocialLogin", false);
        return "register_basic";
    }

    @GetMapping("/register_social")
    public String showSocialRegisterForm(HttpSession session, Model model) {
        String loginType = (String) session.getAttribute("loginType");
        Map<String, Object> userAttributes = (Map<String, Object>) session.getAttribute("userAttributes");

        if (loginType != null && userAttributes != null) {
            model.addAttribute("userAttributes", userAttributes);
        } else {
            model.addAttribute("userAttributes", new HashMap<>());
        }
        model.addAttribute("isSocialLogin", true);
        return "register_social";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/mypage";
    }

    @PostMapping("/updateProfileBasic")
    public String updateProfileBasic(@RequestParam Map<String, String> params,
                                     @RequestParam("profileImage") MultipartFile profileImage,
                                     HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            log.warn("No member found in session, redirecting to login.");
            return "redirect:/login";
        }

        String newLoginId = params.get("loginId");
        if (!newLoginId.equals(member.getLoginId()) && memberManagementService.existsByLoginId(newLoginId)) {
            model.addAttribute("error", "이미 존재하는 로그인 ID입니다. 다른 ID를 사용해주세요.");
            model.addAttribute("member", member);
            model.addAttribute("profileImageUrl", member.getProfileImage() != null ? ImagePathParser.parse(member.getProfileImage().getUuidFileName()) : "/default/image/path/default-profile.jpg");
            return "mypage"; // 에러 메시지와 함께 마이페이지로 리턴
        }

        // 로그인 ID 업데이트
        member.setLoginId(newLoginId);

        // 이름, 닉네임, 이메일, 비밀번호 및 자기소개 업데이트
        member.setName(params.get("name"));
        member.setNickname(params.get("nickname"));
        member.setEmail(params.get("email"));

        String password = params.get("password");
        if (password != null && !password.isEmpty()) {
            member.setPassword(passwordEncoder.encode(password));
        }

        member.setIntroduction(params.get("introduction"));

        // 프로필 이미지 업데이트 처리
        if (!profileImage.isEmpty()) {
            try {
                ImageFile imageFile = imageFileService.uploadFile(profileImage);
                member.setProfileImage(imageFile);
            } catch (Exception e) {
                log.error("Error uploading profile image", e);
                model.addAttribute("error", "프로필 이미지 업로드 중 오류가 발생했습니다.");
                return "redirect:/mypage";
            }
        }

        memberManagementService.updateMember(member);

        log.info("Successfully updated member: {}", member);

        session.setAttribute("member", member);
        model.addAttribute("member", member);

        return "redirect:/mypage";
    }

    @PostMapping("/updateProfileSocial")
    public String updateProfileSocial(
            @RequestParam Map<String, String> params,
            @RequestParam("profileImage") MultipartFile profileImage,
            HttpSession session,
            Model model) {

        log.info("Received request to update social profile with params: {}", params);

        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            log.warn("No member found in session, redirecting to login.");
            return "redirect:/login";
        }

        member.setNickname(params.get("nickname"));
        member.setIntroduction(params.get("introduction"));

        // 프로필 이미지가 업로드된 경우
        if (!profileImage.isEmpty()) {
            try {
                ImageFile imageFile = imageFileService.uploadFile(profileImage);
                member.setProfileImage(imageFile);
            } catch (Exception e) {
                log.error("Error uploading profile image", e);
                model.addAttribute("error", "프로필 이미지 업로드 중 오류가 발생했습니다.");
                return "redirect:/mypage";
            }
        }

        // 변경 사항을 저장하는 코드
        socialLoginService.updateMember(member);

        log.info("Successfully updated social member: {}", member);

        session.setAttribute("member", member);
        model.addAttribute("member", member);

        return "redirect:/mypage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/mainPage";
    }

    @PostMapping("/deactivateAccount")
    public String deactivateAccount(HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            log.warn("No member found in session, redirecting to login.");
            return "redirect:/login";
        }

        // 회원의 로그인 타입에 따라 적절한 서비스를 호출
        if (member.getLoginType() == LoginType.BASIC) {
            // 일반 로그인 회원의 비활성화 처리
            memberManagementService.deactivateMember(member);
        } else {
            // 소셜 로그인 회원의 비활성화 처리
            socialLoginService.deactivateMember(member);
        }

        // 세션 무효화
        session.invalidate();

        log.info("Member {} has deactivated their account.", member.getLoginId());

        return "redirect:/mainPage";
    }

    private void setSessionAttributes(HttpSession session, Member member) {
        session.setAttribute("member", member);
        session.setAttribute("isAuthenticated", true);
        session.setAttribute("userRole", member.getRole().toString());
        session.setAttribute("loginType", member.getLoginType().toString());
        session.setAttribute("memberId", member.getId());
        log.info("Session loginType set to: {}", member.getLoginType().toString());
        log.info("Session Id set to: {}", member.getId());
    }
}
