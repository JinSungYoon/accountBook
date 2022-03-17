package core.statistics.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.StatisticsCondDto;
import core.statistics.dto.StatisticsDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:/application.properties")
public class StatisticsServiceTest {
	
	private Logger logger = LoggerFactory.getLogger(StatisticsServiceTest.class);
	
	@Autowired
	private StatisticsService service;
	
//	@Test
//	public void searchUsageAmountStatistics() throws Exception {
//		List<AmountUsedDto> list = new ArrayList<>();
//		StatisticsCondDto dto = new StatisticsCondDto();
//		dto.setFromDate("20220301000000");
//		dto.setToDate("20220331000000");
//		list = service.searchUsageAmountStatistics(dto);
//		list.forEach(data -> System.out.println(data.toString()));
//	}
	
//	@Test
//	public void searchLocationCoordinate() throws Exception{
//		List<PositionDto> list = new ArrayList<>();
//		StatisticsCondDto cond = new StatisticsCondDto();
//		cond.setFromDate("20210101000000");
//		cond.setToDate("20210131000000");
//		list = service.searchLocationCoordinate(cond);
//		list.forEach(data -> System.out.println(data.toString()));
//		
//	}
	
	@Test
	public void getLastDateOfMonth() throws Exception{
		String fromDt = "20210101000000";
		int div = Integer.valueOf(fromDt.substring(4,6))/12;
		String changeDt = fromDt.substring(0,4)+String.valueOf((div*12)+12)+fromDt.substring(6);
		System.out.println(changeDt);
		String lastDay = service.getLastDateTimeOfMonth(changeDt);
		System.out.println(lastDay);
	}
	
	@Test
	public void getLastDateOfWeek() throws Exception{
//		Calendar cal = Calendar.getInstance();
		//cal.set(new Date());
//		
//		System.out.println(cal.DAY_OF_MONTH);
//		System.out.println(cal.DAY_OF_WEEK);
//		System.out.println(cal.DAY_OF_WEEK_IN_MONTH);
//		System.out.println(cal.DAY_OF_YEAR);
//		System.out.println(cal.HOUR_OF_DAY);
//		System.out.println(cal.WEEK_OF_MONTH);
//		System.out.println(cal.WEEK_OF_YEAR);		
	}
	
//	@Test
//	public void searchStatistics() throws Exception{
//		StatisticsCondDto dto = new StatisticsCondDto();
//		dto.setFromDate("20220101000000");
//		dto.setToDate("20221231235959");
//		StatisticsDto statistics = service.searchStatistics(dto);
//		statistics.getUsageAmountList().forEach(a->System.out.println(a));
//		System.out.println(statistics.getPositionList());
//		System.out.println(statistics.getCategoryList());
//	}
	
	
}
