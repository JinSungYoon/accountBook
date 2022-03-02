package core.statistics.service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.statistics.dto.StatisticsCondDto;
import core.statistics.domain.entity.TransactionHistoryEntity;
import core.statistics.domain.repository.TransactionHistoryRepository;
import core.statistics.dto.AmountUsedDto;
import core.statistics.dto.CategoryDto;
import core.statistics.dto.PositionDto;
import core.statistics.mapper.StatisticsMapper;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private StatisticsMapper statisticsMapper;
	
	@Autowired
	private TransactionHistoryRepository thr;
	
	@Override
	public List<AmountUsedDto> searchUsageAmountStatistics(StatisticsCondDto cond) throws Exception {
		// SQL Mapper를 이용한 데이터 통계자료 조회
//		List<AmountUsedDto> usageAmount = statisticsMapper.searchUsageAmountStatistics(cond);
//		List<AmountUsedDto> list = statisticsMapper.searchAverageDailyUsage(cond);
//		
//		int loop = 0;
//		for(int idx=0;idx<list.size();idx++) {
//			if(loop < usageAmount.size()) {
//				if(list.get(idx).getDailyUsage().equals(usageAmount.get(loop).getDailyUsage())) {
//					list.get(idx).setAccumulatedDateAmount(usageAmount.get(loop).getAccumulatedDateAmount());
//					list.get(idx).setSumDateAmount(usageAmount.get(loop).getSumDateAmount());
//					loop++;
//				}
//			}
//		}
		
		// JPA를 통한 통계자료 조회
		String fromDate = cond.getFromDate();
		String toDate = cond.getToDate();
		
		int Year = Integer.parseInt(fromDate.substring(0, 4));
		int Month = Integer.parseInt(fromDate.substring(4, 6));
		int Day = Integer.parseInt(fromDate.substring(6, 8));
		int Hour = Integer.parseInt(fromDate.substring(8, 10));
		int Minute = Integer.parseInt(fromDate.substring(10, 12));
		int Second = Integer.parseInt(fromDate.substring(12, 14));
		
		// 시작 날짜와 생성
		LocalDateTime startDatetime = LocalDateTime.of(Year,Month,Day,Hour,Minute,Second);
		Year = Integer.parseInt(toDate.substring(0, 4));
		Month = Integer.parseInt(toDate.substring(4, 6));
		Day = Integer.parseInt(toDate.substring(6, 8));
		Hour = Integer.parseInt(toDate.substring(8, 10));
		Minute = Integer.parseInt(toDate.substring(10, 12));
		Second = Integer.parseInt(toDate.substring(12, 14));
		// 끝 날짜와 생성
		LocalDateTime endDatetime = LocalDateTime.of(Year,Month,Day,Hour,Minute,Second);
		
		// JPA를 통해 한달간 소비 데이터 조회
		List<TransactionHistoryEntity> oneMonth =	thr.findByPaymentDateBetween(startDatetime, endDatetime);
		// 한달간 소비 데이터 일별 합계를 저장
		ConcurrentMap<Object, Long> oneMonthSum = oneMonth.stream().collect(Collectors.groupingByConcurrent(s->s.getPaymentDate().getDayOfMonth(),Collectors.summingLong(TransactionHistoryEntity::getAmountOfPayment)));
		// 조회 일자의 마지막 날을 임시 저장
		int lastDayOfMonth =  endDatetime.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
		
		System.out.println("Last date of Month : "+lastDayOfMonth);
		
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
		
		System.out.println(list.stream().map(s->s.getDailyUsage()).collect(Collectors.toList()));
		System.out.println(list.stream().map(s->s.getSumDateAmount()).collect(Collectors.toList()));
		System.out.println(list.stream().map(s->s.getAccumulatedDateAmount()).collect(Collectors.toList()));
		System.out.println(list.stream().map(s->s.getAverageDailyUsage()).collect(Collectors.toList()));
		
		
		return list;
	}

	@Override
	public List<CategoryDto> searchCategoryStatistics(StatisticsCondDto cond) throws Exception {
		
		List<CategoryDto> categoryList = statisticsMapper.searchStoreCategory(cond);
		
		return categoryList;
	}

	@Override
	public List<PositionDto> searchLocationCoordinate(StatisticsCondDto cond) throws Exception {
		List<PositionDto> locationList = statisticsMapper.searchLocationCoordinate(cond);
		return locationList;
	}

	@Override
	public String getLastDateTimeOfMonth(String date) throws Exception {
		
		String year = date.substring(0,4);
		
		String month = date.substring(4,6);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month)-1,1);
		
		String lastDay = year+month+Integer.toString(cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
		
		lastDay+="235959";
		
		return lastDay;
	}

	@Override
	public String getLastDateTimeOfWeek(String date) throws Exception {
		String year = date.substring(0,4);
		
		String month = date.substring(4,6);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month)-1,1);
		
		
		
		return null;
	}
	
}
