package com.rest.paidparkingapi.services;

import com.rest.paidparkingapi.dtos.SaleRequest;
import com.rest.paidparkingapi.entities.Sale;
import java.util.List;

public interface SaleService {

   String getSalesReportByDateRange(SaleRequest saleRequest);

   List<Sale> getSales();

   String getRevenueByMonth(SaleRequest saleRequest);
}
