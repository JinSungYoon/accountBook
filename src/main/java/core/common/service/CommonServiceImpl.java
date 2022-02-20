package core.common.service;

import java.util.Calendar;

import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {

	@Override
	public String getLastDateTimeOfMonth(String date) throws Exception {
		String year = date.substring(0,4);
		
		String month = date.substring(4,6);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month)-1,1);
		
		String lastDay = year+month+Integer.toString(cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
		
		lastDay+="235959";
		
		return lastDay;
	}

}
