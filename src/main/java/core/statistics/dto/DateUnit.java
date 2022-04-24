package core.statistics.dto;

public enum DateUnit {
	year(12),half(6),quarter(3);

	private final int value;
	DateUnit(int value) {
		this.value = value;
	}
	public int getValue() {return value;}
}
