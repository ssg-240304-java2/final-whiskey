package com.whiskey.rvcom.notice;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.RestaurantNotice;
import com.whiskey.rvcom.notice.dto.RestaurantNoticeRequestDTO;
import com.whiskey.rvcom.notice.dto.RestaurantNoticeResponseDTO;
import com.whiskey.rvcom.repository.RestaurantNoticeRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantNoticeService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantNoticeRepository noticeRepository;

    // TODO: 해당 음식점의 공지사항 전체 조회
    public List<RestaurantNoticeResponseDTO> findNoticeByRestaurantId(Long restaurantId) {
        return noticeRepository.findByRestaurantId(restaurantId).stream()
                .sorted(Comparator.comparing(RestaurantNotice::getCreatedAt).reversed())
                .map(it -> new RestaurantNoticeResponseDTO(
                        it.getTitle(),
                        it.getContent(),
                        it.getCreatedAt()
                )).toList();
    }

    // TODO: 해당 음식점의 점주가 공지사항 작성
    @Transactional
    public void save(RestaurantNoticeRequestDTO request, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        noticeRepository.save(new RestaurantNotice(request.title(), request.content(), restaurant));
    }

    // TODO: 해당 음식점의 점주가 공지사항 삭제
    @Transactional
    public void delete(Long noticeId) {
        RestaurantNotice restaurantNotice = noticeRepository.findById(noticeId).orElseThrow();
        restaurantNotice.delete();
    }
}
