package com.whiskey.rvcom.report.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportData { // web 연동 용 DTO

    private String title;
    private String content;
    private LocalDateTime reportedAt;
    private boolean isChecked;
    private boolean isVisible;
    private Long id;
}
