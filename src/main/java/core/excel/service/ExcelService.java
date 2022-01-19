package core.excel.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import core.excel.dto.ExcelData;
import core.excel.dto.LocationDto;

public interface ExcelService {
	List<ExcelData> showExcelData(MultipartFile file)throws IOException;
	int saveExcelData(List<ExcelData> data)throws Exception;
	List<LocationDto> returnMapInfo(String keyword,String browser)throws Exception;
}
