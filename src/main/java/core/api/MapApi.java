package core.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.excel.dto.LocationDto;
import core.statistics.dto.StoreDto;
import core.statistics.mapper.StatisticsMapper;

@Service
@Transactional
public class MapApi {
	
	@Autowired
	public StatisticsMapper mapper;
	
	public List<LocationDto> searchNaverLocation(String keyword)throws Exception{
		List<LocationDto> list = new ArrayList<>();
		
		String encodeKeyword = "";  // 한글 주소는 encoding 해서 날려야 함
		String apiKey = "6429d4bb07e8737943994b85d7c0d793";
		try { 
			encodeKeyword = URLEncoder.encode( keyword, "UTF-8" );
			} 
		catch ( 
			UnsupportedEncodingException e ) { e.printStackTrace();
			}
		
		String apiUrl = "https://openapi.naver.com/v1/search/local.json?query="+encodeKeyword+"&display=10&start=1&sort=random";
		
		URL url = new URL( apiUrl );
	    HttpsURLConnection conn = ( HttpsURLConnection ) url.openConnection();
		conn.setRequestMethod( "GET" );
	    conn.setRequestProperty( "X-Naver-Client-Id", "5t3dDklEMvDXeZ3W2aqH" );
	    conn.setRequestProperty( "X-Naver-Client-Secret", "dervR2UJFG" );
	    
	    BufferedReader br;

	    int responseCode = conn.getResponseCode();
	    if( responseCode == 200 ) {  // 호출 OK
	    	br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8") );
	    } else {  // 에러
	    	br = new BufferedReader( new InputStreamReader(conn.getErrorStream(), "UTF-8") );
	    }
	    
	    String jsonString = new String();
	    String stringLine;
	    while ( ( stringLine= br.readLine()) != null ) {
	        jsonString += stringLine;
	    }
	    
	    JSONParser parser = new JSONParser();
	    JSONObject jsonObj = (JSONObject)parser.parse(jsonString);
	    
	    JSONArray result = (JSONArray) jsonObj.get("items");
	    
	    for(int idx=0;idx<result.size();idx++) {
	    	LocationDto store = new LocationDto();
	    	JSONObject item =  (JSONObject) result.get(idx);
	    	String categoryName = (String) item.get("category");
	    	
	    	store.setStoreName((String) item.get("title"));
	    	store.setStoreCategory(categoryName.substring(0,categoryName.indexOf(">")));
	    	store.setStoreCategoryDetail(categoryName.substring(categoryName.lastIndexOf(">")+1));
	    	store.setAddressName((String) item.get("address"));
	    	store.setRoadAddressName((String) item.get("roadAddress"));
	    	store.setXcoordinate(Double.parseDouble((String)item.get("mapx")));
	    	store.setYcoordinate(Double.parseDouble((String)item.get("mapy")));
	    	list.add(store);
	    }
	    System.out.println(list.toString());
	    return list; 
	}
	
	public List<LocationDto> searchKakaoLocation(String keyword)throws Exception{
		List<LocationDto> list = new ArrayList<>();
		String companName = keyword;
		String encodeAddress = "";  // 한글 주소는 encoding 해서 날려야 함
		String apiKey = "6429d4bb07e8737943994b85d7c0d793";
		int size = 15;
		int page = 1;
		try { 
			encodeAddress = URLEncoder.encode( companName, "UTF-8" );
			} 
		catch ( 
				UnsupportedEncodingException e ) { e.printStackTrace();
		}
		
		String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+ encodeAddress+"&size="+size+"&page="+page;
		String auth = "KakaoAK " + apiKey;
		
		URL url = new URL( apiUrl );
	    HttpsURLConnection conn = ( HttpsURLConnection ) url.openConnection();
		conn.setRequestMethod( "GET" );
	    conn.setRequestProperty( "Authorization", auth );
	    
	    BufferedReader br;

	    int responseCode = conn.getResponseCode();
	    if( responseCode == 200 ) {  // 호출 OK
	    	br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8") );
	    } else {  // 에러
	    	br = new BufferedReader( new InputStreamReader(conn.getErrorStream(), "UTF-8") );
	    }
	    
	    String jsonString = new String();
	    String stringLine;
	    while ( ( stringLine= br.readLine()) != null ) {
	        jsonString += stringLine;
	    }
	    
	    JSONParser parser = new JSONParser();
	    JSONObject jsonObj = (JSONObject)parser.parse(jsonString);
	    JSONArray result = (JSONArray) jsonObj.get("documents");
	    JSONObject meta = (JSONObject) jsonObj.get("meta");
	    
	    boolean isEnd = (boolean) meta.get("is_end");
	    
	    for(int idx=0;idx<result.size();idx++) {
	    	LocationDto store = new LocationDto();
	    	JSONObject item =  (JSONObject) result.get(idx);
	    	String categoryName = (String) item.get("category_name");
	    	store.setStoreName((String) item.get("place_name"));
	    	store.setStoreCategory((String) item.get("category_group_name"));
	    	store.setStoreCategoryDetail(categoryName.substring(categoryName.lastIndexOf(" ")+1));
	    	store.setAddressName((String) item.get("address_name"));
	    	store.setRoadAddressName((String) item.get("road_address_name"));
	    	store.setXcoordinate(Double.parseDouble((String)item.get("x")));
	    	store.setYcoordinate(Double.parseDouble((String)item.get("y")));
	    	list.add(store);
	    }
	    
	    /*
 		// 한번에 다 조회하기
	    while(!isEnd) {
	    	page+=1;
	    	apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+ encodeAddress+"&size="+size+"&page="+page;
			auth = "KakaoAK " + apiKey;
			
			url = new URL( apiUrl );
		    conn = ( HttpsURLConnection ) url.openConnection();
			conn.setRequestMethod( "GET" );
		    conn.setRequestProperty( "Authorization", auth );
		    
		    responseCode = conn.getResponseCode();
		    if( responseCode == 200 ) {  // 호출 OK
		    	br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8") );
		    } else {  // 에러
		    	br = new BufferedReader( new InputStreamReader(conn.getErrorStream(), "UTF-8") );
		    }
		    
		    jsonString = new String();
		    
		    while ( ( stringLine= br.readLine()) != null ) {
		        jsonString += stringLine;
		    }
		    
		    parser = new JSONParser();
		    jsonObj = (JSONObject)parser.parse(jsonString);
		    result = (JSONArray) jsonObj.get("documents");
		    meta = (JSONObject) jsonObj.get("meta");
		    isEnd = (boolean) meta.get("is_end");
		    
		    for(int idx=0;idx<result.size();idx++) {
		    	LocationDto store = new LocationDto();
		    	JSONObject item =  (JSONObject) result.get(idx);
		    	String categoryName = (String) item.get("category_name");
		    	store.setStoreName((String) item.get("place_name"));
		    	store.setStoreCategory((String) item.get("category_group_name"));
		    	store.setStoreCategoryDetail(categoryName.substring(categoryName.lastIndexOf(" ")+1));
		    	store.setAddressName((String) item.get("address_name"));
		    	store.setRoadAddressName((String) item.get("road_address_name"));
		    	store.setXcoordinate(Double.parseDouble((String)item.get("x")));
		    	store.setYcoordinate(Double.parseDouble((String)item.get("y")));
		    	list.add(store);
		    }
	    	
	    }
	    */
	    return list; 
	}
}
