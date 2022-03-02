package core.statistics.dto;

import lombok.Data;

@Data
public class AmountUsedDto {
	private String dailyUsage;
	private Long sumDateAmount;
	private Long accumulatedDateAmount;
	private Long averageDailyUsage;
	
	public AmountUsedDto(String dailyUsage,Long sumDateAmount,Long accumulatedDateAmount,Long averageDailyUsage) {
		this.dailyUsage = dailyUsage;
		this.sumDateAmount = sumDateAmount;
		this.accumulatedDateAmount = accumulatedDateAmount;
		this.averageDailyUsage = averageDailyUsage;
	}
	
}
