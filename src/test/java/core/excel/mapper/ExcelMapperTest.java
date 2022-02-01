package core.excel.mapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import core.excel.dto.ExcelData;
import core.excel.dto.LocationDto;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SpringBootTest
@PropertySource("classpath:/application.properties")
public class ExcelMapperTest {
	
	@Autowired
	public ExcelMapper excelMapper;
	
//	@Test
//	public void seachExcelData() throws Exception{
//		System.out.println(excelMapper.checkApplyExcelCount());
//	}
	
//	@Test
//	public void searchStoreName()throws Exception{
//		ExcelData data = new ExcelData();
//		data.setApprovalNumber(Integer.toString(1638501025));
//		data.setStoreName("아그라 센터원점");
//		data.setAmountOfPayment(Long.valueOf(87500));
//		data.setPaymentDate("2022-02-27 19:13:01");
//		data.setUsedCardNumber(null);
//		data.setClassificationOfUse("일시불");
//		data.setInstallmentMonth(null);
//		data.setCancellation(null);
//		data.setStoreCategory("음식점");
//		data.setStoreCategoryDetail("인도음식");
//		data.setAddressName("서울 중구 수하동 67");
//		data.setRoadAddressName("서울 중구 을지로5길 26");
//		data.setXcoordinate(126.98526253983792);
//		data.setYcoordinate(37.56706848178138);
//		excelMapper.saveExcelData(data);
//	}
	
	@Test
	public void updateLocation()throws Exception{
		LocationDto data = new LocationDto();
		data.setStoreName("11번가");
		data.setAddressName("서울 중구 남대문로5가 541");
		data.setRoadAddressName("서울 중구 한강대로 416");
		data.setStoreCategory("");
		data.setStoreCategoryDetail("인터넷쇼핑몰");
		data.setXcoordinate(126.97374376330308);
		data.setYcoordinate(37.55549768397256);
		excelMapper.updateLocation(data);
	}
	
}

