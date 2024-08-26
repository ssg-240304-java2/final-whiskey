package com.whiskey.rvcom.review;

import com.whiskey.rvcom.entity.receipt.ReceiptData;
import com.whiskey.rvcom.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    @Transactional
    public void addReceipt(ReceiptData receipt) {
        receiptRepository.save(receipt);
    }

    public ReceiptData getReceipt(Long id) {
        return receiptRepository.findById(id).orElse(null);
    }
}
