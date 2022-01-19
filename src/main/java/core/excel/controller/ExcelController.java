package core.excel.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import core.api.MapApi;
import core.board.dto.BoardDto;
import core.excel.dto.ExcelData;
import core.excel.dto.LocationDto;
import core.excel.service.ExcelService;


@Controller
public class ExcelController {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ExcelService excelService;
	
	
	@GetMapping("/accountBook")
	public String main() {
	    return "/excel/accountBook";
	}
	
	@ResponseBody
	@PostMapping("/accountBook")
	public String readExcel(@RequestParam("file") MultipartFile file,Model model) throws IOException {
		List<ExcelData> dataList = new ArrayList<>(); 
		dataList = excelService.showExcelData(file);
		model.addAttribute("datas",dataList);
		return "/excel/accountBook";
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
		
}
