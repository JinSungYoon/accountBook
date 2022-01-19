package core.statistics.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StoreDto {
	private String businessNumber;
	private String storeName;
	private String storeCategory;
	private String storeCategoryDetail;
	private String addressName;
	private String roadAddressName;
	private Double xLocation;
	private Double yLocation;
	private LocalDateTime createDate; 
	private String creatUserId;
	private LocalDateTime updateDate;
	private String updateUserId;
}
