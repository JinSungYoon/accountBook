package core.statistics.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:/application.properties")
public class StatisticsServiceTest {
	
	@Autowired
	private StatisticsService service;
	
//	@Test
//	public void searchUsageAmountStatistics() throws Exception {
//		List<AmountUsedDto> list = new ArrayList<>();
//		StatisticsCondDto dto = new StatisticsCondDto();
//		dto.setFromDate("20210101000000");
//		dto.setToDate("20210131000000");
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
		
		String lastDay = service.getLastDateTimeOfMonth("20210201");
		System.out.println(lastDay);
	}
}
