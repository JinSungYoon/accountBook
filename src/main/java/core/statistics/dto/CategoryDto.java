package core.statistics.dto;

import lombok.Data;

@Data
public class CategoryDto {
	private String month;
	private String storeCategory;
	private Integer count;
	private Long sumOfPayment;
	
	public CategoryDto(String month,String storeCategory, Integer count,Long sumOfPayment) {
		this.month = month;
		this.storeCategory = storeCategory;
		this.count = count;
		this.sumOfPayment = sumOfPayment;
	}
}
