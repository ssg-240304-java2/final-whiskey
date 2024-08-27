package com.whiskey.rvcom.report.controller;

import com.whiskey.rvcom.report.model.dto.MailInfo;
import com.whiskey.libs.rest.request.RequestMethod;
import com.whiskey.libs.rest.request.RestInvoker;
import com.whiskey.rvcom.entity.report.RestaurantReport;
import com.whiskey.rvcom.report.model.dto.ReportData;
import com.whiskey.rvcom.report.service.RestaurantReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.whiskey.rvcom.report.model.dto.MailConst.MAIL_URL;
import static com.whiskey.rvcom.report.model.dto.MailConst.RESTAURANT_SUBJECT;


@Controller
@RequestMapping("/restaurantreport")
@RequiredArgsConstructor
public class RestaurantReportController {

    private final RestaurantReportService restaurantReportService;



    @PostMapping("/regist")
    public ResponseEntity<Void> registRestaurantReport(@RequestBody ReportData report) {

        RestaurantReport restaurantReport = new RestaurantReport();
        restaurantReport.setTitle(report.getTitle());
        restaurantReport.setContent(report.getContent());
        restaurantReport.setReportedAt(LocalDateTime.now());
        restaurantReport.setChecked(false);
        restaurantReport.setVisible(true);

        restaurantReport.setRestaurant(restaurantReportService.returnRestaurant(report.getId()));

        restaurantReportService.saveRestaurantReport(restaurantReport);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    @ResponseBody
    public Page<RestaurantReport> getReports(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "asc") String sortOrder) {
        return restaurantReportService.getAllRestaurantReports(page, sortOrder);
    }

    @GetMapping("/detail/{reportId}")
    public ResponseEntity<Map<String, Object>> getReportDetail(@PathVariable("reportId") Long id) {

        Map<String, Object> response = new HashMap<>();

        RestaurantReport report = restaurantReportService.getRestaurantReport(id);

        if (report != null) {
            response.put("report", report);
        } else {
            response.put("report", "report not found");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{reportId}")
    public ResponseEntity<Void> updateReport(@PathVariable Long reportId, @RequestParam String btnId) {

        boolean isPunish = btnId.equals("punish");

        String onwerEmail = restaurantReportService.restaurantReportPunish(reportId, isPunish);

        if (isPunish){
            // 메일 발송
            MailInfo mailInfo =
                    new MailInfo(onwerEmail, RESTAURANT_SUBJECT, restaurantReportService.getMailText(reportId));

            var invoker = RestInvoker.create(MAIL_URL, null);

            try {
                invoker.request(mailInfo, MailInfo.class, RequestMethod.POST);
            } catch (Exception e) {
                System.out.println("메일 발송 응답이 없어 NullPointException 발생~!");
            }
        }
        return ResponseEntity.ok().build(); // 명시적으로 상태 코드 200 OK를 반환
    }
}
