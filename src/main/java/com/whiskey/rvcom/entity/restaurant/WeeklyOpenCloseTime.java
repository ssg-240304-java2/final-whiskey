package com.whiskey.rvcom.entity.restaurant;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "tbl_weekly_open_close_time")
@Entity
public class WeeklyOpenCloseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "monday_id")
    private OpenCloseTime monday;

    @OneToOne
    @JoinColumn(name = "tuesday_id")
    private OpenCloseTime tuesday;

    @OneToOne
    @JoinColumn(name = "wednesday_id")
    private OpenCloseTime wednesday;

    @OneToOne
    @JoinColumn(name = "thursday_id")
    private OpenCloseTime thursday;

    @OneToOne
    @JoinColumn(name = "friday_id")
    private OpenCloseTime friday;

    @OneToOne
    @JoinColumn(name = "saturday_id")
    private OpenCloseTime saturday;

    @OneToOne
    @JoinColumn(name = "sunday_id")
    private OpenCloseTime sunday;
}