package com.whiskey.rvcom.repository;

import com.whiskey.rvcom.entity.receipt.ReceiptData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<ReceiptData, Long> {
}
