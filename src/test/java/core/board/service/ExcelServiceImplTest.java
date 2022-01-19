package core.board.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import core.excel.dto.ExcelData;

public class ExcelServiceImplTest {

	@Test
	public void saveExcelDate() {
		
		
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor){
		Map<Object,Boolean> map = new HashMap<>();
		return t-> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
