package core.statistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import core.statistics.dto.StatisticsCondDto;
import core.excel.dto.ExcelData;
import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.CategoryDto;
import core.statistics.dto.PositionDto;
import core.statistics.dto.StoreDto;

@Mapper
public interface StatisticsMapper {
	List<AmountUsedDto> searchUsageAmountStatistics(StatisticsCondDto cond) throws Exception;
	List<AmountUsedDto> searchAverageDailyUsage(StatisticsCondDto cond) throws Exception;
	List<CategoryDto> searchStoreCategory(StatisticsCondDto cond) throws Exception;
	List<PositionDto> searchLocationCoordinate(StatisticsCondDto cond) throws Exception;
	void insertStoreInformation(StoreDto info) throws Exception;
}
