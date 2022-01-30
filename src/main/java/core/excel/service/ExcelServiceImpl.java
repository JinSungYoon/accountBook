package core.excel.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import core.api.MapApi;
import core.excel.dto.ComboDto;
import core.excel.dto.ExcelData;
import core.excel.dto.LocationDto;
import core.excel.mapper.ExcelMapper;

@Service
public class ExcelServiceImpl implements ExcelService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ExcelMapper excelMapper;
	
	@Autowired
	private MapApi mapApi;
	
	@Override
	public List<ExcelData> showExcelData(MultipartFile file) throws IOException {

		List<ExcelData> dataList = new ArrayList<>();
		
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		
		if(!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}
		
		Workbook workbook = null;
		
		
		if(extension.equals("xlsx")) {
			workbook = new XSSFWorkbook(file.getInputStream());
		}else if(extension.equals("xls")) {
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		
		Sheet worksheet = workbook.getSheetAt(0);
		
		// 이용일에 연도 붙히기
		String[] boundary = null;
		Row header = worksheet.getRow(0);
		boundary = header.getCell(6).getStringCellValue().split("~");
		boundary[0] = boundary[0].substring(1,8);
		boundary[1] = boundary[1].substring(1,8);
		
		ArrayList<Integer> year = new ArrayList<>();
		for(int idx = Integer.parseInt(boundary[1].substring(0,4)); idx>=Integer.parseInt(boundary[0].substring(0,4));idx--) {
			year.add(idx);
		}
		int yidx = 0;
		String pMidx = boundary[1].substring(boundary[1].length()-2,boundary[1].length());
		String cMidx = boundary[1].substring(boundary[1].length()-2,boundary[1].length());
		
		for(int i =0; i<worksheet.getPhysicalNumberOfRows();i++) {
			Row row = worksheet.getRow(i);
			ExcelData data = new ExcelData();
			
			if(row != null) {
				if(row.getCell(0) != null) {
					if(!row.getCell(0).getStringCellValue().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
						pMidx = worksheet.getRow(i-1).getCell(0).getStringCellValue().substring(0,2);
						cMidx = worksheet.getRow(i).getCell(0).getStringCellValue().substring(0,2);
						if(pMidx=="01" && cMidx=="12") {
							yidx++;
						}
						// 이용일 처리
						data.setPaymentDate(Integer.toString(year.get(yidx))+"-"+row.getCell(0).getStringCellValue().replace(".", "-"));
						data.setApprovalNumber(row.getCell(4).getStringCellValue());
						data.setUsedCardNumber(row.getCell(8).getStringCellValue());
						data.setStoreName(row.getCell(9).getStringCellValue());
						data.setClassificationOfUse(row.getCell(13).getStringCellValue());
						if(row.getCell(15).getStringCellValue()!="") {
							data.setInstallmentMonth(Long.parseLong(row.getCell(15).getStringCellValue()));
						}
						if(row.getCell(16).getStringCellValue()!="") {
							data.setAmountOfPayment(Long.parseLong(row.getCell(16).getStringCellValue().replaceAll(",","")));
						}
						if(row.getCell(17).getStringCellValue()!="") {
							data.setAmountOfPayment(Long.parseLong(row.getCell(17).getStringCellValue().replaceAll(",","")));
						}
						dataList.add(data);
					}
				}
			}
			
		}
		return dataList;
	}

	@Override
	public int saveExcelData(List<ExcelData> data) throws Exception {
		for(ExcelData item : data) {
			if(item.getCancellation().equals("Y")) {
				item.setAmountOfPayment(item.getAmountOfPayment()*-1); 
			}
			excelMapper.saveExcelData(item);
		}
		int cnt = excelMapper.checkApplyExcelCount();
		return cnt;
	}

	@Override
	public List<LocationDto> returnMapInfo(String keyword, String browser) throws Exception {
		
		List<LocationDto> list = new ArrayList<>();
		
		if(browser.equals("kakao")) {
			list = mapApi.searchKakaoLocation(keyword);
		}else if(browser.equals("naver")) {
			list = mapApi.searchNaverLocation(keyword);
		}

		return list;
	}

	@Override
	public List<LocationDto> searchStoreList(LocationDto data) throws Exception {
		List<LocationDto> list = excelMapper.searchStoreList(data);
		return list;
	}

	@Override
	public List<LocationDto> updateLocation(LocationDto data) throws Exception {
		excelMapper.updateLocation(data);
		List<LocationDto> list = excelMapper.searchStoreList(data);
		return list;
	}

	@Override
	public List<ComboDto> comboCategory() throws Exception {
		List<ComboDto> list = excelMapper.comboCategory();
		return list;
	}

}
