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
	
	// ????????? ?????? ?????? ???????????? ?????? ??? ?????? ResponseBody
	@ResponseBody
	@PostMapping("/saveExcelData")
	public int saveExcelData(@RequestBody List<ExcelData> dataArr) throws Exception{
		int cnt = excelService.saveExcelData(dataArr);
		return cnt;
	}
	
	// ????????? ???????????? ????????? ???????????? ???????????? ?????????
	@GetMapping("/locationMapping")
	public ModelAndView getLocationMapping(@RequestParam(required = false,value="storeName",defaultValue="") String storeName, @RequestParam(required = false,value="storeCategory",defaultValue="") String storeCategory,@RequestParam(required = false,value="storeCategoryDetail",defaultValue="") String storeCategoryDetail,@RequestParam(required=false,defaultValue="1") int page,@RequestParam(required=false,defaultValue="1") int range,@RequestParam(required=false,defaultValue="10") int rangeSize) throws Exception{
		ModelAndView mv = new ModelAndView("/excel/locationMapping");
		Pagination pagination = new Pagination(); 
		LocationDto data = new LocationDto();
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
		
		// ????????? List ?????? ??????
		int listCnt = excelService.getStoreListCnt(data);
		
		pagination.setPage(page);
		pagination.setRange(range);
		pagination.setListCnt(listCnt);
		pagination.setRangeSize(rangeSize);
		
		// ????????? ?????? ??????
		pagination.pageInfo(page, range, rangeSize, listCnt);
		// List??? ????????? ?????? startList??? listSize??? ??????
		data.setStartList(pagination.getStartList()); 
		data.setListSize(pagination.getListSize());
		
		List<LocationDto> list = excelService.searchStoreList(data);
		List<ComboDto> comboCategory = excelService.comboCategory(cond);
		
		mv.addObject("list",list);
		mv.addObject("pagination",pagination);
		mv.addObject("comboCategory",comboCategory);
		
		return mv;
	}
	
	// ?????? ?????? ??? ????????? ????????? ?????? ?????? ?????? ?????? ?????????
	@ResponseBody
	@GetMapping("/searchStoreList")
	public storeContainer searchStoreList(@RequestParam(required = false,value="storeName",defaultValue="") String storeName, @RequestParam(required = false,value="storeCategory",defaultValue="") String storeCategory,@RequestParam(required = false,value="storeCategoryDetail",defaultValue="") String storeCategoryDetail,@RequestParam(required=false,defaultValue="1") int page,@RequestParam(required=false,defaultValue="1") int range,@RequestParam(required=false,defaultValue="10") int rangeSize)throws Exception{
		
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
		
		// ????????? List ?????? ??????
		int listCnt = excelService.getStoreListCnt(data);
		data.setPage(page);
		data.setRange(range);
		data.setRangeSize(rangeSize);
		data.setListCnt(listCnt);
		pagination.setPage(page);
		pagination.setRange(range);
		pagination.setListCnt(listCnt);
		pagination.setRangeSize(rangeSize);
		
		// ????????? ?????? ??????
		data.pageInfo(page, range, rangeSize, listCnt);
		pagination.pageInfo(page, range, rangeSize, listCnt);
		
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
