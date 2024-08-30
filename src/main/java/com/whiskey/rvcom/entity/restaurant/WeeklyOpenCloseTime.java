package com.whiskey.rvcom.entity.restaurant;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_weekly_open_close_time")
@Entity
@Getter
@Setter
public class WeeklyOpenCloseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "monday_id")
    private OpenCloseTime monday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tuesday_id")
    private OpenCloseTime tuesday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wednesday_id")
    private OpenCloseTime wednesday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "thursday_id")
    private OpenCloseTime thursday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "friday_id")
    private OpenCloseTime friday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "saturday_id")
    private OpenCloseTime saturday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sunday_id")
    private OpenCloseTime sunday;
}