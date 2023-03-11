package com.rest.paidparkingapi.services;

import com.rest.paidparkingapi.dtos.SaleRequest;
import com.rest.paidparkingapi.entities.Sale;
import com.rest.paidparkingapi.enums.VehicleType;
import com.rest.paidparkingapi.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    public SaleServiceImpl() {
    }

    @Override
    public String getSalesReportByDateRange(SaleRequest saleRequest) {
        List<Sale> carSalesForDateRange = saleRepository.findByParkingIdAndSaleDateBetweenAndVehicleType(
                saleRequest.getParkingId(), saleRequest.getFrom(), saleRequest.getTo(), VehicleType.CAR);
        List<Sale> busSalesForDateRange = saleRepository.findByParkingIdAndSaleDateBetweenAndVehicleType(
                saleRequest.getParkingId(), saleRequest.getFrom(), saleRequest.getTo(), VehicleType.BUS);

        return populateReport(carSalesForDateRange, busSalesForDateRange);
    }

    @Override
    public String getRevenueByMonth(SaleRequest saleRequest) {

        LocalDateTime beginLocalDateTime = LocalDateTime.of(saleRequest.getYear(), saleRequest.getMonth(), 1, 0, 0, 0);
        ChronoField chronoField = ChronoField.DAY_OF_MONTH;
        int daysFromMonth = Math.toIntExact(beginLocalDateTime.range(chronoField).getMaximum());
        LocalDateTime endLocalDateTime = LocalDateTime.of(saleRequest.getYear(), saleRequest.getMonth(), daysFromMonth, 23, 59, 59);

        List<Sale> salesForGivenMonth = saleRepository.findByParkingIdAndSaleDateBetween(
                saleRequest.getParkingId(), beginLocalDateTime, endLocalDateTime);

        return populateRevenueReport(daysFromMonth, salesForGivenMonth);
    }

    @Override
    public List<Sale> getSales() {
        return saleRepository.findAll();
    }

    private String populateReport(List<Sale> carSalesForDateRange, List<Sale> busSalesForDateRange) {
        BigDecimal carSalesAmountSumForPeriod = carSalesForDateRange.stream()
                .map(Sale::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal busSalesAmountSumForPeriod = busSalesForDateRange.stream()
                .map(Sale::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> salesAmountReport = new HashMap<>();
        salesAmountReport.put(VehicleType.CAR.name(), carSalesAmountSumForPeriod);
        salesAmountReport.put(VehicleType.BUS.name(), busSalesAmountSumForPeriod);

        return String.valueOf(salesAmountReport);
    }

    private String populateRevenueReport(int daysFromMonth, List<Sale> salesForGivenMonth) {
        Map<Integer, BigDecimal> revenueReport = new HashMap<>();
        for (int i = 1; i <= daysFromMonth; i++) {
            int dayOfMonth = i;
            BigDecimal salesAmountSumForDay = salesForGivenMonth.stream()
                    .filter(sale -> sale.getSaleDate().getDayOfMonth() == dayOfMonth)
                    .map(Sale::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            revenueReport.put(dayOfMonth, salesAmountSumForDay);
        }
        return String.valueOf(revenueReport);
    }
}
