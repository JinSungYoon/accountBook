package core.statistics.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import core.statistics.domain.entity.TransactionHistoryEntity;
import core.statistics.domain.repository.TransactionHistoryRepository;
import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.CategoryDto;
import core.statistics.dto.PositionDto;


@SpringBootTest
public class StatisticsJPARepositoryTest {
	
	private Logger logger = LoggerFactory.getLogger(StatisticsJPARepositoryTest.class);
	
	@Autowired
	TransactionHistoryRepository thr;
	
//	@Test
//	public void searchUsageAmountStatistics(){
//		// 시작 날짜와 종료 날짜를 선언
//		LocalDateTime startDatetime = LocalDateTime.of(2021,02,01,00,00,00);
//		LocalDateTime endDatetime = LocalDateTime.of(2021,04,30,23,59,59);
//		// JPA를 통해 한달간 소비 데이터 조회
//		List<TransactionHistoryEntity> oneMonth =	thr.findByPaymentDateBetween(startDatetime, endDatetime);
//		// 한달간 소비 데이터 일별 합계를 저장
//		ConcurrentMap<Object, Long> oneMonthSum = oneMonth.stream().collect(Collectors.groupingByConcurrent(s->s.getPaymentDate().getDayOfMonth(),Collectors.summingLong(TransactionHistoryEntity::getAmountOfPayment)));
//		// 조회 일자의 마지막 날을 임시 저장
//		int lastDayOfMonth =  endDatetime.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
//		System.out.println(lastDayOfMonth);
//		// 현재 월 이전의 데이터 조회
//		endDatetime = endDatetime.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
//		// 현재 월 이전 데이터 JPA로 조회
//		List<TransactionHistoryEntity> acumMonth =	thr.findByPaymentDateBefore(endDatetime);
//		// 현재 월 이전의 월 갯수 추출(월 평균 일자별 지출을 계산하기 위함)
//		List<String> months = acumMonth.stream().map(s->Integer.toString(s.getPaymentDate().getYear())+Integer.toString(s.getPaymentDate().getMonthValue())).distinct().collect(Collectors.toList());
//		
//		// 현재 월 이전의 데이터 일자별 합계 평균 금액 계산
//		Map<Object, Long> totalAcum = acumMonth.stream().collect(Collectors.groupingBy(s->s.getPaymentDate().getDayOfMonth(),Collectors.summingLong(s->s.getAmountOfPayment())));
//		
//		List<AmountUsedDto> list = new ArrayList<AmountUsedDto>();
//		
//		// 일자별 금액 매핑을 위한 작업
//		for(int day=1;day<=lastDayOfMonth;day++) {
//			
//			AmountUsedDto amountUsedDto = new AmountUsedDto(null, null, null, null);
//			Long sumDateAmount = oneMonthSum.get(day)==null?null:oneMonthSum.get(day);
//			Long averageDailyUsage = totalAcum.get(day)==null?null:totalAcum.get(day)/months.size();
//			
//			// 일자 데이터 셋업
//			amountUsedDto.setDailyUsage(Integer.toString(day));			
//			
//			// 일자별 지출 합계 금액  / 일자별 누적 지출 금액
//			if(sumDateAmount!=null) {
//				amountUsedDto.setSumDateAmount(sumDateAmount);
//				if(day==1) {
//					amountUsedDto.setAccumulatedDateAmount(sumDateAmount);
//				}else {
//					amountUsedDto.setAccumulatedDateAmount(list.get(list.size()-1).getAccumulatedDateAmount()+sumDateAmount);
//				}
//			}else if(day<=lastDayOfMonth){
//				amountUsedDto.setSumDateAmount(0L);
//				if(day==1) {
//					amountUsedDto.setAccumulatedDateAmount(0L);
//				}else {
//					amountUsedDto.setAccumulatedDateAmount(list.get(list.size()-1).getAccumulatedDateAmount()+0L);
//				}
//			}
//			// 월별 일자별 평균 지출 누적 금액 산출
//			if(averageDailyUsage!=null){
//				if(day==1) {
//					amountUsedDto.setAverageDailyUsage(averageDailyUsage);
//				}else {
//					amountUsedDto.setAverageDailyUsage(list.get(list.size()-1).getAverageDailyUsage()+averageDailyUsage);
//				}
//			}else {
//				if(day==1) {
//					amountUsedDto.setAverageDailyUsage(0L);
//				}else {
//					amountUsedDto.setAverageDailyUsage(list.get(list.size()-1).getAverageDailyUsage()+0L);
//				}
//			}
//			
//			list.add(amountUsedDto);
//		}
//		list.forEach(d->System.out.println(d));
//	}
	
	@Test
	public void searchUsageAmountOfYear() {
		// 시작 날짜와 종료 날짜를 선언
		LocalDateTime startDatetime = LocalDateTime.of(2021,03,01,00,00,00);
		LocalDateTime endDatetime = LocalDateTime.of(2022,02,28,23,59,59);
		// JPA를 통해 1년간 소비 데이터 조회
		List<TransactionHistoryEntity> oneYear =	thr.findByPaymentDateBetween(startDatetime, endDatetime);
		// 월별 지출금앱 합계 계산
		Map<Object, Long> currentYearData = oneYear.stream().collect(Collectors.groupingByConcurrent(d->d.getPaymentDate().getMonthValue(),Collectors.summingLong(d->d.getAmountOfPayment())));
		// JPA를 통해 과거 월별 지출금액 산출
		List<TransactionHistoryEntity> pastYears =	thr.findByPaymentDateBefore(startDatetime);
		// 과거 연도별 지출금액 산출
		Map<Object, Long> pastYearData = pastYears.stream().collect(Collectors.groupingByConcurrent(d->d.getPaymentDate().getMonthValue(),Collectors.summingLong(d->d.getAmountOfPayment())));
		// 과거 연도별 월 갯수 추출
		List<String> pastYearCount = pastYears.stream().map(d->String.format("%04d",d.getPaymentDate().getYear())+String.format("%02d",d.getPaymentDate().getMonthValue())).distinct().sorted().collect(Collectors.toList());
		
		List<String> pastMonth = pastYearCount.stream().map(d->d.substring(4, d.length())).toList();
		Map<String,Integer> pastMonthCount = new HashMap<String, Integer>();
		for(int i=0;i<pastMonth.size();i++) {
			pastMonthCount.put(pastMonth.get(i),Collections.frequency(pastMonth, pastMonth.get(i)));
		}
		
		Map<String,Long> sumStoreCategory = oneYear.stream().collect(Collectors.groupingBy(d->d.getStoreCategory(),Collectors.summingLong(d->d.getAmountOfPayment())));
		Map<String,Long> countStoreCategory = oneYear.stream().collect(Collectors.groupingBy(d->d.getStoreCategory(),Collectors.counting()));
		
		List<AmountUsedDto> list = new ArrayList<AmountUsedDto>();
		List<PositionDto> positionList = oneYear.stream().map(data->{
			PositionDto p = new PositionDto(data.getStoreName(),data.getXcoordinate(),data.getYcoordinate());
			return p;
					}).distinct()
					  .collect(Collectors.toList());
		List<CategoryDto> categoryList = new ArrayList<CategoryDto>();
		
		System.out.println(pastMonthCount);
		System.out.println(currentYearData);
		System.out.println(pastYearData);
		
		for(int i=0;i<currentYearData.size();i++) {
			AmountUsedDto amountUsedDto = new AmountUsedDto(null, null, null, null);
			// 12월인 경우 0으로 나오기 때문에 12로 매핑
			if(((i+startDatetime.getMonthValue())%12)==0){
				amountUsedDto.setDailyUsage(String.format("%02d",12));
				if(currentYearData.get(12) == null) {
					amountUsedDto.setSumDateAmount(0L);
				}else {
					amountUsedDto.setSumDateAmount(currentYearData.get(12));
				}
				if(pastMonthCount.get(String.format("%02d",12)) == null) {
					amountUsedDto.setAverageDailyUsage(0L);
				}else {
					amountUsedDto.setAverageDailyUsage(pastYearData.get(12)/pastMonthCount.get(String.format("%02d",12)));
				}
			}else {
				amountUsedDto.setDailyUsage(String.format("%02d",(i+startDatetime.getMonthValue())%12));
				if(currentYearData.get((i+startDatetime.getMonthValue())%12) == null) {
					amountUsedDto.setSumDateAmount(0L);
				}else {
					amountUsedDto.setSumDateAmount(currentYearData.get((i+startDatetime.getMonthValue())%12));
				}
				
				if(pastMonthCount.get(String.format("%02d",(i+startDatetime.getMonthValue())%12)) == null) {
					amountUsedDto.setAverageDailyUsage(0L);
				}else {
					amountUsedDto.setAverageDailyUsage(pastYearData.get((i+startDatetime.getMonthValue())%12)/pastMonthCount.get(String.format("%02d",(i+startDatetime.getMonthValue())%12)));
				}
			}
			
			System.out.printf("월:%s\t이번년 월 지출액:%d\t과거 월 평균 지출액:%d",amountUsedDto.getDailyUsage(),amountUsedDto.getSumDateAmount(),amountUsedDto.getAverageDailyUsage());
			System.out.println();
			
			list.add(amountUsedDto);
		}
		// 카테고리 정보를 카테고리 리스트에 추가한다.
		for(Object data : sumStoreCategory.keySet()) {
			CategoryDto item = new CategoryDto(String.valueOf(startDatetime.getYear()),data.toString(),countStoreCategory.get(data).intValue(),sumStoreCategory.get(data));
			categoryList.add(item);
		}
		
		categoryList = categoryList.stream().sorted(Comparator.comparing(CategoryDto::getSumOfPayment,Comparator.reverseOrder()).thenComparing(CategoryDto::getCount,Comparator.reverseOrder())).collect(Collectors.toList());
		
		list.forEach(d->System.out.println(d));
		positionList.forEach(p->System.out.println(p));
		categoryList.forEach(c->System.out.println(c));
	}
	
//	@Test
//	public void extractPosition() {
//		
//		//시작 날짜와 종료 날짜를 선언
//		LocalDateTime startDatetime = LocalDateTime.of(2021,04,01,00,00,00);
//		LocalDateTime endDatetime = LocalDateTime.of(2021,04,30,23,59,59);
//		// JPA를 통해 한달간 소비 데이터 조회
//		List<TransactionHistoryEntity> oneMonth =	thr.findByPaymentDateBetween(startDatetime, endDatetime);
//		// Position 데이터 추출
////		List<PositionDto> list = oneMonth.stream().map(data -> {
////							PositionDto p = new PositionDto(data.getStoreName(),data.getXcoordinate(),data.getYcoordinate());
////							return p;
////							})
////						.distinct()
////						.collect(Collectors.toList());
//		
//		Map<Object, Long> categorySumAmount = oneMonth.stream().sorted((d1,d2)->d1.getStoreCategory().compareTo(d2.getStoreCategory())).collect(Collectors.groupingBy(s->s.getStoreCategory(),Collectors.summingLong(s->s.getAmountOfPayment())));
//		
//		Map<Object, Long> categoryCount = oneMonth.stream().sorted((d1,d2)->d1.getStoreCategory().compareTo(d2.getStoreCategory())).collect(Collectors.groupingBy(s->s.getStoreCategory(),Collectors.counting()));
//		
//		List<CategoryDto> list = new ArrayList<CategoryDto>();
//		
//		
//		for(Object item : categorySumAmount.keySet()) {
//			CategoryDto c = new CategoryDto(String.valueOf(startDatetime.getMonth()),item.toString(),categoryCount.get(item).intValue(),categorySumAmount.get(item));
//			list.add(c);			
//		}
//		
//		list.forEach(d->System.out.println(d));
//		
//		List<CategoryDto> sortedList = list.stream()
//				.sorted(Comparator.comparing(CategoryDto::getSumOfPayment,Comparator.reverseOrder()).thenComparing(CategoryDto::getCount,Comparator.reverseOrder()))
//				.collect(Collectors.toList());
//		
//		
//	}
	
}
