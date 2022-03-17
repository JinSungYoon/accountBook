$(document).ready(function(){

	let eventSource;
		
    /* MonthPicker 옵션 */
	options = {
		pattern: 'yyyy-mm', // Default is 'mm/yyyy' and separator char is not mandatory
		selectedYear: 2022,
		startYear: 2008,
		finalYear: 2999,
		openOnFocus : true,
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
	};
	
	/* MonthPicker Set */
	$('#monthpicker').monthpicker(options);
	
	$("#monthpicker").change(function(){
		year = $("#monthpicker").val().slice(0,4); 
		month = $("#monthpicker").val().slice(-2);
		firstDate = new Date(year,month,1);
		
		fromDt = String(year)+String(month)+String(firstDate.getDate()).padStart(2,'0')+"000000";
		
		callMonthUsageGraph(fromDt);
				
	});
		
	//-----------------------------------------------------------------------
	
	let now = new Date();
	let year,month,firstDate,fromDt;
	
	firstDate = new Date(now.getFullYear(),now.getMonth(),1);
	year = firstDate.getFullYear();
	month = String(parseInt(firstDate.getMonth(),10)+1).padStart(2,'0');
	

	// 월과 관련된 기능 동작
	if(window.location.href.includes('month')){
		// 이번달 기본 셋팅
		$("#monthpicker").val(year+"-"+month);
		fromDt = firstDate.getFullYear()+month+String(firstDate.getDate()).padStart(2,'0')+"000000";
		callMonthUsageGraph(fromDt);	
	}

	
	// 연도와 관련된 기능 동작
	if(window.location.href.includes('year')){
		// 10년치 연도 combobox 셋팅
		getYears(year);
		// 이번연도 기본 셋팅
		$("#yearpicker").val(year).prop("selected",true);
		
		$("#yearpicker").change(function(){
			let changeYear = $(this).val();
			getYears(changeYear);
			$("#yearpicker").val(changeYear);
			fromDt = changeYear+"01"+String(firstDate.getDate()).padStart(2,'0')+"000000";
			callMonthUsageGraph(fromDt);	
		});
		
		fromDt = year+"01"+String(firstDate.getDate()).padStart(2,'0')+"000000";
		callMonthUsageGraph(fromDt);
	}
	
	
	function getYears(currentYear){
		$("#yearpicker option").remove();
		
		let beforeYear = Number(currentYear)-5;
		let afterYear  = Number(currentYear)+5;
		
		for(let y = beforeYear; y<=afterYear;y++){
			let option = document.createElement("option");
			option.value = y;
			option.text = y;
			$("#yearpicker").append(option);
		} 
	}
		
	function callMonthUsageGraph(fromDt){
		
		let dataObj = new Object();
		dataObj.fromDate = fromDt;
		
		if(window.location.href.includes('month')){
			dataObj.unit = 'month';
		}
		
		if(window.location.href.includes('year')){
			dataObj.unit = 'year';
		}
					
		$.ajax({
			type : 'GET',
			url:'/loadStatisticsGraph',
			data : dataObj,
			dataType : 'json',
			contentType: 'application/json',
			success : function(result){
				
				drawDonutGraph(result);
				drawClustererGraph(result);
				drawUsageGraph(result);
				if(window.location.href.includes('year')){
					drawUsageBarGraph(result);
				}
				if(window.location.href.includes('month')){
					let yy = $("#monthpicker").val().slice(0,4); 
					let mm = $("#monthpicker").val().slice(-2);
					drawCanlendar(new Date(yy,mm-1),result.usageAmountList);
				}
			},
			error : function(result){
				alert(`Error occured : ${result}`);
				console.log(result);
			}
			
		}); // end ajax
		
	} 
	
	function drawUsageGraph(data){
		let graph1 = [];
		let graph2 = [];
		let axis = [];
		let curAcum = [];
		let pastAcum = [];
		if(window.location.href.includes('month')){
			// 그래프 이름 넣기
			graph1.push('현 누적 사용금액');
			graph2.push('월 평균 일 사용금액');
		}else if(window.location.href.includes('year')){
			// 그래프 이름 넣기
			graph1.push('현 누적 사용금액');
			graph2.push('연 평균 월 사용금액');
		}
		
		if(window.location.href.includes('year')){
			// 현재 월 데이터 추출
			let currentMonth = data.usageAmountList.map(d=>d.sumDateAmount);
			// 과거 월 데이터 추출
			let pastMonth = data.usageAmountList.map(d=>d.averageDailyUsage);
			// 시작달 데이터 추가
			curAcum.push(currentMonth[0])
			pastAcum.push(pastMonth[0])
			// 현재 연도 월 누적 데이터 생성
			currentMonth.reduce((now,data)=>{
			    curAcum.push(now+data)
			    return now+data
			});
			// 과거 연도 월 누적 데이터 생성
			pastMonth.reduce((now,data)=>{
			    pastAcum.push(now+data)
			    return now+data
			});
			
		}
		if(window.location.href.includes('month')){
			Array.prototype.forEach.call(data.usageAmountList,(item,index)=>{
				axis.push(item.dailyUsage);
				graph1.push(item.accumulatedDateAmount);
				graph2.push(item.averageDailyUsage);
			});
		}else{
			axis = data.usageAmountList.map(d=>d.dailyUsage);
			for(let index=0;index<data.usageAmountList.length;index++){
				graph1.push(curAcum[index]);
				graph2.push(pastAcum[index]);
			}
		}
		let trend = c3.generate({
			bindto : "#trend",
			axis:{
				x:{
					type : 'category',
					categories : axis
				}
			},
			data : {
				columns:[
					graph1,
					graph2
				],
				types: {
			      data1: 'area',
			      data2: 'area'
			    }
			},
			size:{
				height : 300,
				width : 500
			}
		})
	}
	
	function drawUsageBarGraph(data){
		graph1 = [];
		graph2 = [];
		axis = [];
		// 그래프 이름 넣기
		graph1.push('월 평균 사용금액');
		graph2.push('이번년 사용금액');
		
		Array.prototype.forEach.call(data.usageAmountList,(item,index)=>{
			axis.push(item.dailyUsage);
			graph1.push(item.averageDailyUsage);
			graph2.push(item.sumDateAmount);
		});
		
		let trend = c3.generate({
			bindto : "#barGraph",			// 어떤 html에 연결할지
			axis:{						// x축 y축에 대한 정보
				x:{
					type : 'category',
					categories : axis
				}
			},
			data : {					// data 옵션
				columns:[
					graph1,
					graph2
				],
				type: 'bar'
			},
			bar: {
		        /*
		        width: {
		            ratio: 0.5 // this makes bar width 50% of length between ticks
		        }
		        */
		        width: 10 // this makes bar width 100px
		    },
			size:{
				height : 300,
				width : 500
			}
		})
	}
	
	function drawDonutGraph(data){
		let count = returnPickData(data.categoryList,'storeCategory','count',5,'desc');
		let payment = returnPickData(data.categoryList,'storeCategory','sumOfPayment',5,'desc');
		
		let payCategory = c3.generate({
		  bindto: "#payCategory",
		  data: {
		    //json: [container.payment],
		    //keys: {
		    //  value: Object.keys(container.payment),
		    //},
		    columns:payment,
		    type: "donut",
		  },
		  donut: {
		    title: "카테고리별 사용 금액",
		  },
		  size:{
				height : 300,
				width : 500
		  },
		  tooltip:{
			format:{
				value : function(value,ratio,id,index){return value;}
			}
		  }
		});
		
		let countCategory = c3.generate({
		  axis : {	// 막대그래프를 가로로 눕힌다
			rotated: true
		  },
		  bindto: "#countCategory",
		  data: {
		    //json: [container.count],
		    //keys: {
		    //  value: Object.keys(container.count),
		    //},
		    columns:count,
		    type: "bar",
		    labels: true
		  },
		  size:{
				height : 300,
				width : 500
		  },
		  tooltip:{		// mouseover시 tooltip을 비율이 아닌 value가 나오도록 설정
			format:{
				value : function(value,ratio,id,index){return value;}
			}
		  }
		  
		});
	}
	
	function drawClustererGraph(data){
		// 장소들의 좌표를 position에 저장	
		let position = data.positionList.map(data=> {
		    let obj = {};    
		    obj['storeName'] = data.storeName;
		    obj['lat'] = data.lat;
		    obj['lng'] = data.lng;
		    return obj;
		} );
		
		let mapContainer = document.getElementById('map'), // 지도를 표시할 div 
		    mapOption = { 
		        center: new kakao.maps.LatLng(37.5128, 127.0022), // 지도의 중심좌표
		        level : 9 // 지도의 확대 레벨
		    };
		
		let map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다	
				    
	    let clusterer = new kakao.maps.MarkerClusterer({
	        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
	        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정 
	        minLevel: 5 // 클러스터 할 최소 지도 레벨 
	    });
	   
		let markers = [];
		position.forEach((pos)=>{
			let marker = new kakao.maps.Marker({
				position : new kakao.maps.LatLng(pos.lat, pos.lng)
			});
			
			let infowindow = new kakao.maps.InfoWindow({
				content : pos.storeName,
			});
			kakao.maps.event.addListener(
		        marker,
		        "mouseover",
		        makeOverListener(map, marker, infowindow)
		      );
		      kakao.maps.event.addListener(
		        marker,
		        "mouseout",
		        makeOutListener(infowindow)
		      );
		      // 인포윈도우를 표시하는 클로저를 만드는 함수입니다
			  function makeOverListener(map, marker, infowindow) {
			    return function () {
			      infowindow.open(map, marker);
			    };
			  }
			
		     // 인포윈도우를 닫는 클로저를 만드는 함수입니다
			 function makeOutListener(infowindow) {
			    return function () {
			      infowindow.close();
			    };
			  }
			  
			markers.push(marker);  
		})
		
        // 클러스터러에 마커들을 추가합니다
		clusterer.addMarkers(markers);
		
		// 마커 클러스터리에 클릭 이벤트 넣기
		kakao.maps.event.addListener(clusterer,'clusterclick',function(clusterer){
			let level = map.getLevel()-1;
			
			map.setLevel(level,{anchor:clusterer.getCenter()});
		});
	}
	
	function returnPickData(source,key,value,count=1,order='asc'){
		
		let len = source.length <= count ? source.length : count;
		let data = '';
		
		if(order=='asc'){
			data = source.map(data => [data[key],data[value]]).sort(function(a,b){return a[1] - b[1]});	
		}else{
			data = source.map(data => [data[key],data[value]]).sort(function(a,b){return b[1] - a[1]});
		}
		
		data = data.slice(0,len);
		
		let dataKeys = data.map(data => data[0]);
		
		return data;
	}
    
    const init = {
	  monList: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
	  dayList: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
	  today: new Date(),
	  monForChange: new Date().getMonth(),
	  activeDate: new Date(),
	  getFirstDay: (yy, mm) => new Date(yy, mm, 1),
	  getLastDay: (yy, mm) => new Date(yy, mm + 1, 0),
	  nextMonth: function () {
	    let d = new Date();
	    d.setDate(1);
	    d.setMonth(++this.monForChange);
	    this.activeDate = d;
	    return d;
	  },
	  prevMonth: function () {
	    let d = new Date();
	    d.setDate(1);
	    d.setMonth(--this.monForChange);
	    this.activeDate = d;
	    return d;
	  },
	  addZero: (num) => (num < 10) ? '0' + num : num,
	  activeDTag: null,
	  getIndex: function (node) {
	    let index = 0;
	    while (node = node.previousElementSibling) {
	      index++;
	    }
	    return index;
	  }
	};
    
    const $calBody = document.querySelector('.cal_body');
    
    function drawCanlendar (fullDate,data) {
	  let yy = fullDate.getFullYear();
	  let mm = fullDate.getMonth();
	  
	  let firstDay = init.getFirstDay(yy, mm);
	  let lastDay = init.getLastDay(yy, mm);
	  let markToday;  // for marking today date
	  
	  if (mm === init.today.getMonth() && yy === init.today.getFullYear()) {
	    markToday = init.today.getDate();
	  }
	
	  document.querySelector('.cal_month').textContent = init.monList[mm];
	  document.querySelector('.cal_year').textContent = yy;
	
	  let trtd = '';
	  let startCount;
	  let countDay = 0;
	  for (let i = 0; i < 6; i++) {
	    trtd += '<tr>';
	    for (let j = 0; j < 7; j++) {
	      if (i === 0 && !startCount && j === firstDay.getDay()) {
	        startCount = 1;
	      }
	      if (!startCount) {
	        trtd += '<td>'
	      } else {
	        let fullDate = yy + '.' + init.addZero(mm + 1) + '.' + init.addZero(countDay + 1);
	        trtd += '<td class="day';
	        trtd += (markToday && markToday === countDay + 1) ? ' today" ' : '"';
	        trtd += ` data-date="${countDay + 1}" data-fdate="${fullDate}">`;
	      }
	      
	      trtd += (startCount) ? ++countDay : '';
	      if(startCount){
		      let usage = data[countDay-1].sumDateAmount;
		      trtd += usage>=0?'<br><span class="spent">':'<br><span class="refund">';
		      trtd += usage!=0?usage:'';
		      trtd += '</span>';
	      }
	      if (countDay === lastDay.getDate()) { 
	        startCount = 0; 
	      }
	      trtd += '</td>';
	    }
	    trtd += '</tr>';
	  }
	  $calBody.innerHTML = trtd;
	}
    
});