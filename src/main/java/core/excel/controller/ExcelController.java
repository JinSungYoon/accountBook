package core.excel.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import core.common.dto.ConditionDto;
import core.common.dto.Pagination;
import core.common.service.CommonService;
import core.excel.dto.ComboDto;
import core.excel.dto.ExcelData;
import core.excel.dto.LocationDto;
import core.excel.dto.storeContainer;
import core.excel.service.ExcelService;


@Controller
public class ExcelController {
	private Logger logger = LoggerFactory.getLogger(ExcelController.class);
	
	
	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private CommonService commonService;
	
	
	@GetMapping("/accountBook")
	public String main() {
	    return "/excel/accountBook";
	}
	
	@ResponseBody
	@PostMapping("/accountBook")
	public ModelAndView readExcel(@RequestParam("file") MultipartFile file) throws IOException {
		ModelAndView mv = new ModelAndView();
		List<ExcelData> dataList = new ArrayList<>(); 
		dataList = excelService.showExcelData(file);
		mv.addObject("datas",dataList);
		return mv;
	}
	
	@GetMapping("/searchLocation")
	public String getSearchLocation() {
	    return "/excel/searchLocation";
	}
	
	@PostMapping("/searchLocation")
	public ModelAndView openSearchLocation(@RequestParam("keyword") String keyword,@RequestParam("rowIndex") String rowIndex) {
		ModelAndView mv = new ModelAndView("/excel/searchLocation");
		mv.addObject("keyword",keyword);
		mv.addObject("rowIndex",rowIndex);
		return mv;
	}
	
	@ResponseBody
	@GetMapping("/searchMapInfo")
	public List<LocationDto> returnMapInfo(@RequestParam("keyword") String keyword,@RequestParam("browser") String browser)throws Exception{
		List<LocationDto> list = new ArrayList<>();
		list = excelService.returnMapInfo(keyword, browser);
		System.out.println(list.toString());
		return list;
	}
	
	// 페이지 전환 없이 데이터만 전달 할 경우 ResponseBody
	@ResponseBody
	@PostMapping("/saveExcelData")
	public int saveExcelData(@RequestBody List<ExcelData> dataArr) throws Exception{
		int cnt = excelService.saveExcelData(dataArr);
		return cnt;
	}
	
	// 처음에 장소매핑 화면을 호출할때 사용하는 메서드
	@GetMapping("/locationMapping")
	public ModelAndView getLocationMapping(@RequestParam(required = false,value="storeName",defaultValue="") String storeName, @RequestParam(required = false,value="storeCategory",defaultValue="") String storeCategory,@RequestParam(required = false,value="storeCategoryDetail",defaultValue="") String storeCategoryDetail,@RequestParam(required=false,defaultValue="1") int page,@RequestParam(required=false,defaultValue="1") int range) throws Exception{
		ModelAndView mv = new ModelAndView("/excel/locationMapping");
		Pagination pagination = new Pagination(); 
		LocationDto data = new LocationDto();
		ConditionDto cond = new ConditionDto();
		// 조회할 List 갯수 확인
		int listCnt = excelService.getStoreListCnt(data);
		
		data.setPage(page);
		data.setRange(range);
		pagination.setPage(page);
		pagination.setRange(range);
		
		// 페이지 정보 셋팅
		data.pageInfo(page, range, listCnt);
		pagination.pageInfo(page, range, listCnt);
		
		List<LocationDto> list = excelService.searchStoreList(data);
		List<ComboDto> comboCategory = excelService.comboCategory(cond);
		
		mv.addObject("list",list);
		mv.addObject("pagination",pagination);
		mv.addObject("comboCategory",comboCategory);
		
		return mv;
	}
	
	// 정보 검색 및 페이지 변경에 따른 검색 결과 반환 메서드
	@ResponseBody
	@GetMapping("/searchStoreList")
	public storeContainer searchStoreList(@RequestParam(required = false,value="storeName",defaultValue="") String storeName, @RequestParam(required = false,value="storeCategory",defaultValue="") String storeCategory,@RequestParam(required = false,value="storeCategoryDetail",defaultValue="") String storeCategoryDetail,@RequestParam(required=false,defaultValue="1") int page,@RequestParam(required=false,defaultValue="1") int range)throws Exception{
		
		storeContainer container = new storeContainer();
		LocationDto data = new LocationDto();
		Pagination pagination = new Pagination();
		ConditionDto cond = new ConditionDto();
		
		if(!storeName.equals("")) {
			data.setStoreName(storeName);
		}
		if(!storeCategory.equals("")) {
			data.setStoreCategory(storeCategory);
		}
		if(!storeCategoryDetail.equals("")) {
			data.setStoreCategoryDetail(storeCategoryDetail);
		}
		
		// 조회할 List 갯수 확인
		int listCnt = excelService.getStoreListCnt(data);
		data.setPage(page);
		data.setRange(range);
		pagination.setPage(page);
		pagination.setRange(range);
		
		// 페이지 정보 셋팅
		data.pageInfo(page, range, listCnt);
		pagination.pageInfo(page, range, listCnt);
		
		List<LocationDto> list = excelService.searchStoreList(data);
		List<ComboDto> comboCategory = excelService.comboCategory(cond);
		
		container.setList(list);
		container.setPage(pagination);
		container.setComboCategory(comboCategory);
		
		return container;
	}
	
	@ResponseBody
	@PostMapping("/updateLocation")
	public List<LocationDto> updateLocation(@RequestBody LocationDto data)throws Exception{
		List<LocationDto> list = excelService.updateLocation(data);
		return list;
	}
	
	@ResponseBody
	@GetMapping("/transactionHistory")
	public List<ExcelData> showTransactionHistory(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate)throws Exception {
		ConditionDto cond = new ConditionDto();
		
		String lastDate = commonService.getLastDateTimeOfMonth(fromDate);
		
		cond.setFromDate(fromDate);
		cond.setToDate(lastDate);
		List<ExcelData> list = excelService.searchTransactionHistory(cond);
		List<ComboDto> comboCategory = excelService.comboCategory(cond);
		
		
		return list;
	}
	
		
}
