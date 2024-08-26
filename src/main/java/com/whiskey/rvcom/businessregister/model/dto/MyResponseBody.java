package com.whiskey.rvcom.businessregister.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class MyResponseBody {
    private int request_cnt;
    private int valid_cnt;
    private String status_code;
    private List<Data> data;


    @Getter
    @Setter
    @ToString
    public class Data {
        private String b_no;
        private String valid;
        private RequestParam request_param;
        private Status status;


        @Getter
        @Setter
        @ToString
        public class RequestParam {
            private String b_no;
            private String start_dt;
            private String p_nm;
        }


        @Getter
        @Setter
        @ToString
        public class Status {
            private String b_no;
            private String b_stt;
            private String b_stt_cd;
            private String tax_type;
            private String tax_type_cd;
            private String end_dt;
            private String utcc_yn;
            private String tax_type_change_dt;
            private String invoice_apply_dt;
            private String rbf_tax_type;
            private String rbf_tax_type_cd;
        }
    }
}
