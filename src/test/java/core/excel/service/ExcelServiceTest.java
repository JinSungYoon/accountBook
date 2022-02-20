package core.excel.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import core.common.dto.ConditionDto;
import core.excel.dto.ComboDto;
import core.excel.mapper.ExcelMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:/application.properties")
public class ExcelServiceTest {
	
	@Autowired
	public ExcelMapper excelMapper;
	
	@Test
	public void searchComboBox() throws Exception {
		ConditionDto cond = new ConditionDto(); 
		List<ComboDto> list = excelMapper.comboCategory(cond);
		
		List<ComboDto> rtn = new ArrayList<ComboDto>();
		
		List<String> objectKey1 = list.stream()
										.map(data->data.getObjectKey1())
										.sorted()
										.distinct().toList();
		
		List<String> objectKey2 = list.stream()
										.map(data->data.getObjectKey2())
										.sorted()
										.distinct().toList();
		
		
		int objectKey1Len = objectKey1.size();
		int objectKey2Len = objectKey2.size();
		
		ComboDto combo = new ComboDto();
		
		int length = objectKey1Len > objectKey2Len ? objectKey1Len : objectKey2Len;  
		
		IntStream.range(0,length).forEach(index -> rtn.add(new ComboDto()));
		
		//IntStream.range(0, objectKey1Len).forEach(index->rtn.set(index, rtn.get(index).setObjectKey1(objectKey1.get(index)) ));
		//IntStream.range(0, objectKey2Len).forEach(index->rtn.add(index, objectKey2.get(index)));
		
		rtn.forEach(data -> System.out.println(data.getObjectKey1()+":"+data.getObjectKey2()));
		
		
	}
	
}
