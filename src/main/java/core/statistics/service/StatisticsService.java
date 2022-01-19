package core.statistics.service;

import java.util.List;

import core.statistics.dto.StatisticsCondDto;
import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.CategoryDto;
import core.statistics.dto.PositionDto;

public interface StatisticsService {
	List<AmountUsedDto> searchUsageAmountStatistics(StatisticsCondDto cond) throws Exception;
	List<CategoryDto> searchCategoryStatistics(StatisticsCondDto cond) throws Exception;
	List<PositionDto> searchLocationCoordinate(StatisticsCondDto cond) throws Exception;
}
