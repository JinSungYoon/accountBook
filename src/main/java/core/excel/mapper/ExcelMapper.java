package core.excel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import core.excel.dto.ExcelData;
import core.statistics.dto.StatisticsCondDto;
import core.statistics.dto.StatisticsDto;

@Mapper
public interface ExcelMapper {
	
	void saveExcelData(ExcelData dat) throws Exception;
	int checkApplyExcelCount() throws Exception;
	List<String> storeName() throws Exception;
}
