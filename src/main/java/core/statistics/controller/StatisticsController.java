package core.statistics.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonProperty;

import core.statistics.dto.StatisticsCondDto;
import core.statistics.dto.StatisticsDto;
import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.CategoryDto;
import core.statistics.dto.PositionDto;
import core.statistics.service.StatisticsService;

@Controller
public class StatisticsController {
	
	@Autowired
	private StatisticsService statisticsService;
	
	@GetMapping("/main")
	public String showMain()throws Exception {
		return "/statistics/summary";
	}
	
	@ResponseBody
	@GetMapping("/loadStatisticsGraph")
	public StatisticsDto loadStatisticsGraph(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) throws Exception{
		StatisticsDto container = new StatisticsDto();
		List<AmountUsedDto> amountList = new ArrayList<>();
		List<CategoryDto> categoryList = new ArrayList<>();
		List<PositionDto> positionList = new ArrayList<>();
		
		StatisticsCondDto cond = new StatisticsCondDto();
		cond.setFromDate(fromDate);
		cond.setToDate(toDate);
		amountList = statisticsService.searchUsageAmountStatistics(cond);
		categoryList = statisticsService.searchCategoryStatistics(cond);
		positionList = statisticsService.searchLocationCoordinate(cond);
		container.setUsageAmountList(amountList);
		container.setCategoryList(categoryList);
		container.setPositionList(positionList);
		return container;
	}
}
