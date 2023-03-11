package com.rest.paidparkingapi.controllers;

import com.rest.paidparkingapi.dtos.SaleRequest;
import com.rest.paidparkingapi.entities.Sale;
import com.rest.paidparkingapi.services.SaleService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/range")
    public ResponseEntity<String> getSalesReportByDateRange(@RequestBody @NonNull SaleRequest saleRequest) {
        return ResponseEntity.ok().body(saleService.getSalesReportByDateRange(saleRequest));
    }

    @PostMapping("/revenue")
    public ResponseEntity<String> getRevenueByMonth(@RequestBody @NonNull SaleRequest saleRequest) {
        return ResponseEntity.ok().body(saleService.getRevenueByMonth(saleRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Sale>> getSales() {
        return ResponseEntity.ok().body(saleService.getSales());
    }
}
