package core.excel.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class ExcelData {
	
	private String approvalNumber;
	private String storeName;
	private Long amountOfPayment;
	private String paymentDate;
	private String usedCardNumber;
	private String classificationOfUse;
	private Long installmentMonth;
	private String cancellation;
	private String storeCategory;
	private String storeCategoryDetail;
	private String addressName;
	private String roadAddressName;
	private Double xcoordinate;
	private Double ycoordinate;
	private LocalDateTime createDate; 
	private String creatUserId;
	private LocalDateTime updateDate;
	private String updateUserId;
	
}
