package com.example.farmaser.service;

import com.example.farmaser.model.dto.reportDto.DailySalesPointDto;
import com.example.farmaser.model.dto.reportDto.SalesSummaryDto;
import com.example.farmaser.model.dto.reportDto.SellerPerformanceDto;
import com.example.farmaser.model.dto.reportDto.TopProductDto;

import java.util.Date;
import java.util.List;

public interface IReport {

    SalesSummaryDto getSalesSummary(Date start, Date end);

    List<TopProductDto> getTopProducts(Date start, Date end, int limit);

    List<SellerPerformanceDto> getSellerPerformance(Date start, Date end);

    List<DailySalesPointDto> getDailySalesSeries(Date start, Date end);
}


