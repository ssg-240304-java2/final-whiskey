package com.whiskey.rvcom.entity.review;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum Rating {
    ONE_STAR(1),
    TWO_STAR(2),
    THREE_STAR(3),
    FOUR_STAR(4),
    FIVE_STAR(5);

    private final int value;
}
