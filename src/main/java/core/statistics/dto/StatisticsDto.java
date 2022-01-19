package core.statistics.dto;

import java.util.List;
import lombok.Data;


@Data
public class StatisticsDto {
	private List<AmountUsedDto> usageAmountList;
	private List<CategoryDto> categoryList;
	private List<PositionDto> positionList;
}
