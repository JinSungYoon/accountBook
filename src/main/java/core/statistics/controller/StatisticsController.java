package core.statistics.controller;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import core.statistics.dto.StatisticsCondDto;
import core.statistics.dto.StatisticsDto;

import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.CategoryDto;
import core.statistics.dto.PositionDto;
import core.statistics.service.StatisticsService;

@RestController
public class StatisticsController {
	
	private Logger logger = LoggerFactory.getLogger(StatisticsController.class);	
	
	@Autowired
	private StatisticsService statisticsService;
	
	
	@GetMapping("/statistics/month")
	public ModelAndView showMonthDashboard()throws Exception {
		ModelAndView mv = new ModelAndView("/statistics/dashboardMonth");
		return mv;
	}
	
	@GetMapping("/statistics/year")
	public ModelAndView showYearDashboard()throws Exception {
		ModelAndView mv = new ModelAndView("/statistics/dashboardYear");
		return mv;
	}
	
	@GetMapping("/loadStatisticsGraph")
	public StatisticsDto loadStatisticsGraph(@RequestParam("fromDate") String fromDate,@RequestParam("unit") String unit) throws Exception{
		StatisticsDto container = new StatisticsDto();
		List<AmountUsedDto> amountList = new ArrayList<>();
		List<CategoryDto> categoryList = new ArrayList<>();
		List<PositionDto> positionList = new ArrayList<>();
		if(unit.equals("month")) {
			String lastDate = statisticsService.getLastDateTimeOfMonth(fromDate);
			
			StatisticsCondDto cond = new StatisticsCondDto();
			cond.setFromDate(fromDate);
			cond.setToDate(lastDate);
			amountList = statisticsService.searchUsageAmountStatistics(cond);
			categoryList = statisticsService.searchCategoryStatistics(cond);
			positionList = statisticsService.searchLocationCoordinate(cond);
			container.setUsageAmountList(amountList);
			container.setCategoryList(categoryList);
			container.setPositionList(positionList);
		}else if(unit.equals("year")) {
			int div = Integer.valueOf(fromDate.substring(4,6))/12;
			String changeDt = fromDate.substring(0,4)+String.valueOf((div*12)+12)+fromDate.substring(6);
			String lastDate = statisticsService.getLastDateTimeOfMonth(changeDt);
			StatisticsCondDto cond = new StatisticsCondDto();
			cond.setFromDate(fromDate);
			cond.setToDate(lastDate);
			container = statisticsService.searchStatistics(cond);
		}
		
		
		return container;
	}
	
	@GetMapping("/realTimeStatisticsGraph")
	public SseEmitter realTimeStatisticsGraph(@RequestParam("fromDate") String fromDate) throws Exception{
		
		SseEmitter emitter = new SseEmitter();
		
		StatisticsDto container = new StatisticsDto();
		List<AmountUsedDto> amountList = new ArrayList<>();
		List<CategoryDto> categoryList = new ArrayList<>();
		List<PositionDto> positionList = new ArrayList<>();
		
		String lastDate = statisticsService.getLastDateTimeOfMonth(fromDate);
		
		StatisticsCondDto cond = new StatisticsCondDto();
		cond.setFromDate(fromDate);
		cond.setToDate(lastDate);
		amountList = statisticsService.searchUsageAmountStatistics(cond);
		categoryList = statisticsService.searchCategoryStatistics(cond);
		positionList = statisticsService.searchLocationCoordinate(cond);
		container.setUsageAmountList(amountList);
		container.setCategoryList(categoryList);
		container.setPositionList(positionList);
		
		emitter.send(container);
		
		return emitter;
	}
}

