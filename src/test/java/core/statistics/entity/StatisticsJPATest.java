package core.statistics.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import core.statistics.domain.entity.TransactionHistoryEntity;
import core.statistics.domain.repository.TransactionHistoryRepository;


@SpringBootTest
public class StatisticsJPATest {
	
	private Logger logger = LoggerFactory.getLogger(StatisticsJPATest.class);
	
	@Autowired
	TransactionHistoryRepository thr;
	
//	@Test
//	public void test(){
//		LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(365), LocalTime.of(0,0,0)); //어제 00:00:00
//		LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59)); //오늘 23:59:59
//		System.out.println(startDatetime);
//		List<TransactionHistoryEntity> list =	thr.findByPaymentDateBetween(startDatetime, endDatetime);
//		
//		
//		
//		list.forEach(data -> logger.debug(list.toString()));
//	}
	
	@Test
	public void stream() {
//		List<Integer> list = Arrays.asList(1,2,3,4,5);
//		Stream<Integer> intStream = list.stream();
//		
//		intStream = list.stream();
//		intStream.forEach(System.out::print);
		IntStream intStream = Arrays.stream(new int[]{1,2,3,4,5});
		intStream.forEach(System.out::println);
	}
	
}
