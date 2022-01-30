package core.excel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import core.excel.dto.ComboDto;
import core.excel.dto.ExcelData;
import core.excel.dto.LocationDto;
import core.statistics.dto.StatisticsCondDto;
import core.statistics.dto.StatisticsDto;

@Mapper
public interface ExcelMapper {
	
	void saveExcelData(ExcelData data) throws Exception;
	int checkApplyExcelCount() throws Exception;
	List<LocationDto> searchStoreList(LocationDto data) throws Exception;
	void updateLocation(LocationDto data) throws Exception;
	List<ComboDto> comboCategory() throws Exception;
}
