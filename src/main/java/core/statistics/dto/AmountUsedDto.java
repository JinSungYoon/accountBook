package core.statistics.dto;

import lombok.Data;

@Data
public class AmountUsedDto {
	private String dailyUsage;
	private Long sumDateAmount;
	private Long accumulatedDateAmount;
	private Long averageDailyUsage;
	
}
