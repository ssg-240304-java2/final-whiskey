package com.whiskey.rvcom.entity.receipt;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_paid_item")
@Entity
public class PaidItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;   // 수량
}