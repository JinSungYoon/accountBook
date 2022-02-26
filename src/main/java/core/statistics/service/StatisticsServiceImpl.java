package core.statistics.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.statistics.dto.StatisticsCondDto;
import core.excel.dto.ExcelData;
import core.statistics.domain.repository.TransactionHistoryRepository;
import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.CategoryDto;
import core.statistics.dto.PositionDto;
import core.statistics.mapper.StatisticsMapper;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private StatisticsMapper statisticsMapper;
	
	@Autowired
	private TransactionHistoryRepository transactionHistoryRepository;
	
	@Override
	public List<AmountUsedDto> searchUsageAmountStatistics(StatisticsCondDto cond) throws Exception {
		
		List<AmountUsedDto> usageAmount = statisticsMapper.searchUsageAmountStatistics(cond);
		List<AmountUsedDto> averageDailyUsage = statisticsMapper.searchAverageDailyUsage(cond);
		
		int loop = 0;
		for(int idx=0;idx<averageDailyUsage.size();idx++) {
			if(loop < usageAmount.size()) {
				if(averageDailyUsage.get(idx).getDailyUsage().equals(usageAmount.get(loop).getDailyUsage())) {
					averageDailyUsage.get(idx).setAccumulatedDateAmount(usageAmount.get(loop).getAccumulatedDateAmount());
					averageDailyUsage.get(idx).setSumDateAmount(usageAmount.get(loop).getSumDateAmount());
					loop++;
				}
			}
		}
		
		
		
		return averageDailyUsage;
	}

	@Override
	public List<CategoryDto> searchCategoryStatistics(StatisticsCondDto cond) throws Exception {
		
		List<CategoryDto> categoryList = statisticsMapper.searchStoreCategory(cond);
		
		return categoryList;
	}

	@Override
	public List<PositionDto> searchLocationCoordinate(StatisticsCondDto cond) throws Exception {
		List<PositionDto> locationList = statisticsMapper.searchLocationCoordinate(cond);
		return locationList;
	}

	@Override
	public String getLastDateTimeOfMonth(String date) throws Exception {
		
		String year = date.substring(0,4);
		
		String month = date.substring(4,6);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month)-1,1);
		
		String lastDay = year+month+Integer.toString(cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
		
		lastDay+="235959";
		
		return lastDay;
	}

	@Override
	public String getLastDateTimeOfWeek(String date) throws Exception {
		String year = date.substring(0,4);
		
		String month = date.substring(4,6);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month)-1,1);
		
		
		
		return null;
	}
	
}
