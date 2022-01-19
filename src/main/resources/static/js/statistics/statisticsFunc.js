$(document).ready(function(){
	 
    
    
    let locData ={};
    locData = {
  "positions": [
    {
      "lat": 37.27943075229118,
      "lng": 127.01763998406159
    },
    {
      "lat": 37.55915668706214,
      "lng": 126.92536526611102
    },
    {
      "lat": 35.13854258261161,
      "lng": 129.1014781294671
    }]
    }
	  
    /* MonthPicker 옵션 */
	options = {
		pattern: 'yyyy-mm', // Default is 'mm/yyyy' and separator char is not mandatory
		selectedYear: 2021,
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
		lastDate = new Date(year,parseInt(month),0);
		
		fromDt = firstDate.getFullYear()+String(parseInt(firstDate.getMonth(),10)).padStart(2,'0')+String(firstDate.getDate()).padStart(2,'0')+"000000";
		toDt = lastDate.getFullYear()+String(parseInt(lastDate.getMonth()+1,10)).padStart(2,'0')+String(lastDate.getDate()).padStart(2,'0')+"235959";
		callMonthUsageGraph(fromDt,toDt);
	});
		
	//-----------------------------------------------------------------------
	
	var now = new Date();
	var year,month,firstDate,lastDate,fromDt,toDt;
	
	firstDate = new Date(now.getFullYear(),now.getMonth(),1);
	lastDate = new Date(now.getFullYear(),now.getMonth()+1,0);
	year = firstDate.getFullYear();
	month = String(parseInt(firstDate.getMonth(),10)+1).padStart(2,'0');
	fromDt = firstDate.getFullYear()+String(parseInt(firstDate.getMonth(),10)).padStart(2,'0')+String(firstDate.getDate()).padStart(2,'0')+"000000";
	toDt = lastDate.getFullYear()+String(parseInt(lastDate.getMonth(),10)).padStart(2,'0')+String(lastDate.getDate()).padStart(2,'0')+"235959";
	// 이번달 기본 셋팅
	$("#monthpicker").val(year+"-"+month);
	
	// 월 사용 그래프 만들기
	callMonthUsageGraph(fromDt,toDt);
	
	function callMonthUsageGraph(fromDt,toDt){
		
		var dataObj = new Object();
		dataObj.fromDate = fromDt;
		dataObj.toDate = toDt;
		
		$.ajax({
			type : 'GET',
			url:'/loadStatisticsGraph',
			data : dataObj,
			dataType : 'json',
			contentType: 'application/json',
			success : function(result){
				
				graph1 = [];
				graph2 = [];
				axis = [];
				// 그래프 이름 넣기
				graph1.push('현 누적 사용금액');
				graph2.push('월 평균 일 사용금액');
				
				Array.prototype.forEach.call(result.usageAmountList,(item,index)=>{
					axis.push(item.dateOfUse);
					graph1.push(item.accumulatedDateAmount);
					graph2.push(item.averageDailyUsage);
				});
				
				let trend = c3.generate({
					bindto : "#trend",
					axis:{
						x:{
							axis
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
						height : 280,
						width : 1000
					}
				})
			
			// 장소들의 좌표를 position에 저장	
			let position = result.positionList.map(data=> {
			    let obj = {};    
			    obj['storeName'] = data.storeName;
			    obj['lat'] = data.lat;
			    obj['lng'] = data.lng;
			    return obj;
			} );
			
			var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
			    mapOption = { 
			        center: new kakao.maps.LatLng(37.5128, 127.0022), // 지도의 중심좌표
			        level : 9 // 지도의 확대 레벨
			    };
			
			var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다	
					    
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
	        	
				/* javascript 생성 정보 
				let category = result.categoryList.map(data=>data.storeCategory);
				let sumOfPayment = result.categoryList.map(data=>data.sumOfPayment);
				let categoryCount = result.categoryList.map(data=>data.count);
				let payment = {};
				let container = {};
				let count = {}; 
				let len = 5; 
				for(let loop=0;loop<len;loop++){
					payment[category[loop]] = sumOfPayment[loop];
					count[category[loop]] = categoryCount[loop];
				}
				container['payment'] = payment;
				container['count'] = count;
				container.payment = Object.fromEntries(Object.entries(container.payment).sort(([,a],[,b])=>a-b));	// 오름차순으로 정렬
				container.count = Object.fromEntries(Object.entries(container.count).sort(([,a],[,b])=>b-a));		// 내림차순으로 정렬
				*/
				
				let count = returnPickData(result.categoryList,'storeCategory','count',5,'desc');
				let payment = returnPickData(result.categoryList,'storeCategory','sumOfPayment',5,'desc');
				
				let payCategory = c3.generate({
				  bindto: "#payCategory",
				  data: {
				    //json: [container.payment],
				    /*keys: {
				      value: Object.keys(container.payment),
				    },*/
				    columns:payment,
				    type: "donut",
				  },
				  donut: {
				    title: "카테고리별 사용 금액",
				  },
				  size:{
						height : 280,
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
				    /*json: [container.count],
				    keys: {
				      value: Object.keys(container.count),
				    },*/
				    columns:count,
				    type: "bar",
				  },
				  size:{
						height : 280,
						width : 500
				  },
				  tooltip:{		// mouseover시 tooltip을 비율이 아닌 value가 나오도록 설정
					format:{
						value : function(value,ratio,id,index){return value;}
					}
				  }
				});
				
			},
			error : function(result){
				alert(`Error occured : ${result}`);
				console.log(result);
			}
			
		}); // end ajax	
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
    
});