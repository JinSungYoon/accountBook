package core.excel.dto;

import java.util.List;

import core.common.Pagination;
import lombok.Data;

@Data
public class storeContainer {
	List<LocationDto> list;
	Pagination page;
}
