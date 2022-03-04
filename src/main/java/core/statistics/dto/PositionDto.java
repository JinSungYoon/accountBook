package core.statistics.dto;

import lombok.Data;

@Data
public class PositionDto {

	private String storeName;
	private Double lng;
	private Double lat;
	
	public PositionDto(String storeName,Double lng,Double lat) {
		this.storeName = storeName;
		this.lng		= lng;
		this.lat		= lat;
	}
	
}
