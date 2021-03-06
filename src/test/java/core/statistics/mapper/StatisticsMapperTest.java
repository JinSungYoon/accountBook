package core.statistics.mapper;

import java.util.ArrayList;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.CategoryDto;
import core.statistics.dto.PositionDto;
import core.statistics.dto.StatisticsCondDto;

import core.statistics.dto.StoreDto;

@SpringBootTest
@PropertySource("classpath:/application.properties")
public class StatisticsMapperTest {
	
	@Autowired
	public StatisticsMapper mapper;
	
//	@Test
//	public void searchUsageAmountStatistics() throws Exception {
//		List<AmountUsedDto> list = new ArrayList<AmountUsedDto>();
//		StatisticsCondDto cond = new StatisticsCondDto(); 
//		cond.setFromDate("20210101000000");
//		cond.setToDate("20210131235959");
//		list = mapper.searchUsageAmountStatistics(cond);
//		list.forEach(data -> System.out.println(data.toString()));
//	}
	
//	@Test
//	public void searchAverageDailyUsage() throws Exception {
//		List<AmountUsedDto> list = new ArrayList<AmountUsedDto>();
//		StatisticsCondDto cond = new StatisticsCondDto(); 
//		cond.setFromDate("20210101000000");
//		cond.setToDate("20210630235959");
//		list = mapper.searchAverageDailyUsage(cond);
//		list.forEach(data -> System.out.println(data.toString()));
//	}
	
//	@Test
//	public void searchCategoryStatistics() throws Exception{
//		List<CategoryDto> list = new ArrayList<>();
//		StatisticsCondDto cond = new StatisticsCondDto(); 
//		cond.setFromDate("20210101000000");
//		cond.setToDate("20210131235959");
//		list = mapper.searchStoreCategory(cond);
//		list.forEach(data -> System.out.println(data.toString()));
//	}
	
	@Test
	public void searchLocationCoordinate()throws Exception{
		List<PositionDto> list = new ArrayList<>();
		StatisticsCondDto cond = new StatisticsCondDto(); 
		cond.setFromDate("20210101000000");
		cond.setToDate("20210131235959");
		list = mapper.searchLocationCoordinate(cond);
		list.forEach(data->System.out.println(data.toString()));
	}
	
//	@Test
//	public void insertStoreInformation() throws Exception{
//		StoreDto store = new StoreDto();
//		store.setBusinessNumber("21721254");
//		store.setStoreName("??????????????? ????????????");
//		store.setStoreCategory("?????????");
//		store.setStoreCategoryDetail("???????????????");
//		store.setAddressName("?????? ????????? ????????? 40-1");
//		store.setRoadAddressName("?????? ????????? ?????????735?????? 3");
//		store.setXLocation(126.943276805412);
//		store.setYLocation(37.3708480690711);
//		mapper.insertStoreInformation(store);
//		
//	}
}
