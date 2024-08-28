package com.whiskey.rvcom.notice;

import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.entity.restaurant.RestaurantNotice;
import com.whiskey.rvcom.notice.dto.RestaurantNoticeRequestDTO;
import com.whiskey.rvcom.notice.dto.RestaurantNoticeResponseDTO;
import com.whiskey.rvcom.repository.RestaurantNoticeRepository;
import com.whiskey.rvcom.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantNoticeService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantNoticeRepository noticeRepository;

    public List<RestaurantNoticeResponseDTO> findNoticeByRestaurantId(Long restaurantId) {
        return noticeRepository.findByRestaurantId(restaurantId).stream()
                .sorted(Comparator.comparing(RestaurantNotice::getCreatedAt).reversed())
                .map(it -> new RestaurantNoticeResponseDTO(
                        it.getId(),
                        it.getTitle(),
                        it.getContent(),
                        it.isDeleted(),
                        it.getCreatedAt()
                )).toList();
    }

    public Page<RestaurantNoticeResponseDTO> getPagedRestaurantNotices(Long restaurantId, Pageable pageable) {
        return noticeRepository.getPagedRestaurantNotices(restaurantId, pageable);
    }

    @Transactional
    public void save(RestaurantNoticeRequestDTO request, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        noticeRepository.save(new RestaurantNotice(request.title(), request.content(), restaurant));
    }

    @Transactional
    public void delete(Long noticeId) {
        RestaurantNotice restaurantNotice = noticeRepository.findById(noticeId).orElseThrow();
        restaurantNotice.delete();
    }
}
