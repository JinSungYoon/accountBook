package core.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import core.excel.mapper.ExcelMapper;

@SpringBootTest
public class MapApiTest {
	
	@Autowired
	private MapApi mapApi;
	@Autowired
	private ExcelMapper excelMapper;
	
//	@Test
//	public void storeStoreInformation() throws Exception{
//		//List<String> location = excelMapper.storeName();
//		List<String> location = Arrays.asList("홈플러스익스프레스","콘텐츠웨이브","스타벅스","서울도시가스","여기어때컴퍼니","버거킹상암","롯데쇼핑","KT 본사","케이케이데이","교보문고","우아한형제들","아그라센터원","톡톡아카데미","코리아세븐","다이소","보람연합의원","사위식당 인사동점","그집김밥애라면","누리꿈 서울아산내과의원","예니롱","인생닭강정 증산","한솥상암","화일링","웰빙쌀빵","세븐일레븐 누리꿈","버거앤프라이즈","콩카페 연남점");
//		List<Map<String,Integer>> map = new ArrayList<Map<String,Integer>>();
//		for(String item : location) {
//			map.add(mapApi.searchLocationInformation(item));
//		}
//		System.out.println(map.toString());
//	}
	
	@Test
	public void searchKakaoLocation()throws Exception{
		
		System.out.println(mapApi.searchKakaoLocation("11번가").toString());
		
	}
	
//	@Test
//	public void searchNaverLocation()throws Exception{
//		mapApi.searchNaverLocation("스타벅스");
//	}
}
