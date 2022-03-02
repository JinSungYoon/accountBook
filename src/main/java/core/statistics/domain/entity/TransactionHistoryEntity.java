package core.statistics.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="CARD_USAGE_HISTORY")
@Data
public class TransactionHistoryEntity {
	
	@Id
	private String approvalNumber;
	private String storeName;
	private Long amountOfPayment;
	@Column
	private LocalDateTime paymentDate;
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
	
	@Override
	public String toString() {
		return "TransactionHistoryEntity [approvalNumber=" + approvalNumber + ", storeName=" + storeName
				+ ", amountOfPayment=" + amountOfPayment + ", paymentDate=" + paymentDate + ", usedCardNumber="
				+ usedCardNumber + ", classificationOfUse=" + classificationOfUse + ", installmentMonth="
				+ installmentMonth + ", cancellation=" + cancellation + ", storeCategory=" + storeCategory
				+ ", storeCategoryDetail=" + storeCategoryDetail + ", addressName=" + addressName + ", roadAddressName="
				+ roadAddressName + ", xcoordinate=" + xcoordinate + ", ycoordinate=" + ycoordinate + ", createDate="
				+ createDate + ", creatUserId=" + creatUserId + ", updateDate=" + updateDate + ", updateUserId="
				+ updateUserId + "]";
	}
	
}
