package core.statistics.dto;

import lombok.Data;

@Data
public class CategoryDto {
	private String month;
	private String storeCategory;
	private Integer count;
	private Long sumOfPayment;
}
