package core.excel.dto;

import core.common.dto.Pagination;
import lombok.Data;

@Data
public class LocationDto extends Pagination {
	private String storeName;
	private String storeCategory;
	private String storeCategoryDetail;
	private String addressName;
	private String roadAddressName;
	private Double xcoordinate;
	private Double ycoordinate;
	private String condStoreName;
	
}
