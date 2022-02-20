package core.excel.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import core.common.dto.ConditionDto;
import core.excel.dto.ComboDto;
import core.excel.dto.ExcelData;
import core.excel.dto.LocationDto;
import core.statistics.dto.StatisticsCondDto;

public interface ExcelService {
	List<ExcelData> showExcelData(MultipartFile file)throws IOException;
	int saveExcelData(List<ExcelData> data)throws Exception;
	List<LocationDto> returnMapInfo(String keyword,String browser)throws Exception;
	List<LocationDto> searchStoreList(LocationDto data)throws Exception;
	int getStoreListCnt(LocationDto data) throws Exception;
	List<ComboDto> comboCategory(ConditionDto cond)throws Exception;
	List<LocationDto> updateLocation(LocationDto data) throws Exception;
	List<ExcelData> searchTransactionHistory(ConditionDto cond) throws Exception;
}
