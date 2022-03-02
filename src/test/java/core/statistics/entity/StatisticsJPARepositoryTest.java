package core.statistics.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import core.statistics.domain.entity.TransactionHistoryEntity;
import core.statistics.domain.repository.TransactionHistoryRepository;
import core.statistics.dto.AmountUsedDto;


@SpringBootTest
public class StatisticsJPARepositoryTest {
	
	private Logger logger = LoggerFactory.getLogger(StatisticsJPARepositoryTest.class);
	
	@Autowired
	TransactionHistoryRepository thr;
	
	@Test
	public void test(){
		// 시작 날짜와 종료 날짜를 선언
		LocalDateTime startDatetime = LocalDateTime.of(2021,02,01,00,00,00);
		LocalDateTime endDatetime = LocalDateTime.of(2021,04,30,23,59,59);
		// JPA를 통해 한달간 소비 데이터 조회
		List<TransactionHistoryEntity> oneMonth =	thr.findByPaymentDateBetween(startDatetime, endDatetime);
		// 한달간 소비 데이터 일별 합계를 저장
		ConcurrentMap<Object, Long> oneMonthSum = oneMonth.stream().collect(Collectors.groupingByConcurrent(s->s.getPaymentDate().getDayOfMonth(),Collectors.summingLong(TransactionHistoryEntity::getAmountOfPayment)));
		// 조회 일자의 마지막 날을 임시 저장
		int lastDayOfMonth =  endDatetime.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
		System.out.println(lastDayOfMonth);
		// 현재 월 이전의 데이터 조회
		endDatetime = endDatetime.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		// 현재 월 이전 데이터 JPA로 조회
		List<TransactionHistoryEntity> acumMonth =	thr.findByPaymentDateBefore(endDatetime);
		// 현재 월 이전의 월 갯수 추출(월 평균 일자별 지출을 계산하기 위함)
		List<String> months = acumMonth.stream().map(s->Integer.toString(s.getPaymentDate().getYear())+Integer.toString(s.getPaymentDate().getMonthValue())).distinct().collect(Collectors.toList());
		
		// 현재 월 이전의 데이터 일자별 합계 평균 금액 계산
		Map<Object, Long> totalAcum = acumMonth.stream().collect(Collectors.groupingBy(s->s.getPaymentDate().getDayOfMonth(),Collectors.summingLong(s->s.getAmountOfPayment())));
		
		List<AmountUsedDto> list = new ArrayList<AmountUsedDto>();
		
		// 일자별 금액 매핑을 위한 작업
		for(int day=1;day<=lastDayOfMonth;day++) {
			
			AmountUsedDto amountUsedDto = new AmountUsedDto(null, null, null, null);
			Long sumDateAmount = oneMonthSum.get(day)==null?null:oneMonthSum.get(day);
			Long averageDailyUsage = totalAcum.get(day)==null?null:totalAcum.get(day)/months.size();
			
			// 일자 데이터 셋업
			amountUsedDto.setDailyUsage(Integer.toString(day));			
			
			// 일자별 지출 합계 금액  / 일자별 누적 지출 금액
			if(sumDateAmount!=null) {
				amountUsedDto.setSumDateAmount(sumDateAmount);
				if(day==1) {
					amountUsedDto.setAccumulatedDateAmount(sumDateAmount);
				}else {
					amountUsedDto.setAccumulatedDateAmount(list.get(list.size()-1).getAccumulatedDateAmount()+sumDateAmount);
				}
			}else if(day<=lastDayOfMonth){
				amountUsedDto.setSumDateAmount(0L);
				if(day==1) {
					amountUsedDto.setAccumulatedDateAmount(0L);
				}else {
					amountUsedDto.setAccumulatedDateAmount(list.get(list.size()-1).getAccumulatedDateAmount()+0L);
				}
			}
			// 월별 일자별 평균 지출 누적 금액 산출
			if(averageDailyUsage!=null){
				if(day==1) {
					amountUsedDto.setAverageDailyUsage(averageDailyUsage);
				}else {
					amountUsedDto.setAverageDailyUsage(list.get(list.size()-1).getAverageDailyUsage()+averageDailyUsage);
				}
			}else {
				if(day==1) {
					amountUsedDto.setAverageDailyUsage(0L);
				}else {
					amountUsedDto.setAverageDailyUsage(list.get(list.size()-1).getAverageDailyUsage()+0L);
				}
			}
			
			list.add(amountUsedDto);
		}
		list.forEach(d->System.out.println(d));
	}
	
	
}