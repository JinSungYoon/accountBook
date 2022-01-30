let categoryCombo;

$(document).ready(function(){
	
	comboCategory();
	
	function comboCategory(){
		$.ajax({
			type:'GET'
			,url:'/comboCategory'
			,contetType : 'application/json'
			,success : function(result){
				
			console.log(result);
			categoryCombo = result;
			
			let category = Array.from(new Set(categoryCombo.map(data => data.objectKey1)));
			let categoryDetail = Array.from(new Set(categoryCombo.filter(data=>data.objectKey1 == category[0]).map(data => data.objectKey2)));
			
			let categoryComboBox = document.getElementById("categoryCombo");
			let categoryDetailComboBox = document.getElementById("categoryDetailCombo");
			
			// 옵션 추가
			categoryComboBox = addOptions(categoryComboBox,category);
			categoryDetailComboBox = addOptions(categoryDetailComboBox,categoryDetail);
			
			} 
			,error : function(result){
				console.log(result);
			}
		}) // End Ajax
	}
	
	function searchStoreList(keyword,category,categoryDetail){
		
		let object = {};
		object['storeName'] = keyword;
		object['storeCategory'] = category;
		object['storeCategoryDetail'] = categoryDetail;
		
		$.ajax({
			type:'GET'
			,url:'/searchStoreList'
			,contetType : 'application/json'
			,data : object 
			,dataType : 'json'
			,success : function(result){
				
				console.log(result);
				
				//기존에 검색된 데이터 제거
				removeAllChildNods($("#listBody")[0]);
				// 새로 검색한 데이터 추가
				result.forEach(item => addStoreRow(item));
			} 
			,error : function(result){
				console.log(result);
			}
		}) // End Ajax
		
	}
	
	function addStoreRow(item){
		let tbody = '';
		let tList = document.getElementById("listBody").children;
		tbody += `<tr id=${tList.length+1} class="tRow">`;
		tbody += `<td id="${tList.length+1}"><div class="dataContainer"><span id="sequence" class="sequence">${tList.length+1}</span></div></td>`;
		tbody += `<td id="${tList.length+1}"><div class="dataContainer"><span id="storeName" class="storeName">${item.storeName}</span></div></td>`;
		tbody += `<td id="${tList.length+1}"><div class="dataContainer"><span id="storeCategory" class="storeCategory">${item.storeCategory}</span></div></td>`;
		tbody += `<td id="${tList.length+1}"><div class="dataContainer"><span id="storeCategoryDetail" class="storeCategoryDetail">${item.storeCategoryDetail}</span></div></td>`;
		tbody += `<td id="${tList.length+1}" style="display:none"><div class="dataContainer"><span id="addressName" class="addressName"></span></div></td>`;
		tbody += `<td id="${tList.length+1}" style="display:none"><div class="dataContainer"><span id="roadAddressName" class="roadAddressName"></span></div></td>`;
		tbody += `<td id="${tList.length+1}" style="display:none"><div class="dataContainer"><span id="xcoordinate" class="roadAddressName"></span></div></td>`;
		tbody += `<td id="${tList.length+1}" style="display:none"><div class="dataContainer"><span id="ycoordinate" class="roadAddressName"></span></div></td>`;
		tbody += `</tr>`;
		//새로 검색한 데이터 추가
		$("#listBody").append(tbody);
		
	}
	
	// 동적으로 tRow에 click event를 추가
	$(document).on("click", "tr", function(){
	    let tr = $(this).closest('tr');
		let indexTd = tr.find('td:eq(0)');
		let storeTd = tr.find('td:eq(1)');
		let span = storeTd.find('span')[0];
	    let data = span.innerText;
	    
	    // 선택된 row의 색을 변경
	    $(this).addClass("tRowSelected").siblings().removeClass("tRowSelected");
	    
	    document.getElementById("tRowIndex").value = indexTd[0].innerText;
	    
	    document.getElementById("mapKeyword").value = data;
	    
	    searchLocation(data,'kakao');
	    
	});
	
	$(document).on("click","li",function(e){
		
		var itemElement = $(this).closest('li');
		
		let trIndex = parseInt(document.getElementById("tRowIndex").value)-1;
		
		let tr  = document.getElementById("listBody").children[trIndex];
		
		if(trIndex==''){
			alert("좌측 표에셔 변경할 장소명을 선택해 주세요");
		}else{
			let storeName = document.getElementById("listBody").children[trIndex].children[1].children[0].children[0].innerText;
			
			let location = {};
			
			console.log(storeName);
			
			for(let item of itemElement[0].children[1].children){
	
				location[item.id] = item.innerText;
			}
			
			location['condStoreName'] = storeName;
			
			if(confirm(`${storeName}을 선택하신 지역 정보로 업데이트 하시겠습니까?`)){
				updateLocation(location);
			}
		}
				
	});
	
	$("#categoryCombo").change(function(e){
		let selected = $("#categoryCombo option:selected").val();
		let categoryDetail = Array.from(new Set(categoryCombo.filter(data=>data.objectKey1 == selected).map(data => data.objectKey2)));
		
		$('#categoryDetailCombo').children('option').remove();
		
		let categoryDetailComboBox = document.getElementById("categoryDetailCombo");
		
		categoryDetailComboBox = addOptions(categoryDetailComboBox,categoryDetail);
		
	});
	
	// 키워드 입력란에 Enter key 이벤트 추가
	$("#mapKeyword").keypress(function(e){
		if(e.keyCode==13) {
			let data  = document.getElementById("mapKeyword").value; 
	    	searchLocation(data,'kakao');
    	}
	});
	
	// 버튼으로 정보검색 실행
	$("#mapSearch").click(function(e){
		let data  = document.getElementById("mapKeyword").value; 
    	searchLocation(data,'kakao');
	});
	
	$("#storeKeyword").keypress(function(e){
		
		let keyword = document.getElementById("storeKeyword").value;
		let category = $("#categoryCombo option:selected").val();
		let categoryDetail = $("#categoryDetailCombo option:selected").val();
		
		if(e.keyCode==13) {
			searchStoreList(keyword,category,categoryDetail);		
		}
	});
	
	$("#searchBtn").click(function(e){
		let keyword = document.getElementById("storeKeyword").value;
		let category = $("#categoryCombo option:selected").val();
		let categoryDetail = $("#categoryDetailCombo option:selected").val();
		
		searchStoreList(keyword,category,categoryDetail);
		
	});
	
	function searchLocation(keyword,browser){
		
		let token = $("meta[name='_csrf']").attr("content");
		let header = $("meta[name='_csrf_header']").attr("content");
		
		let dataObj = {};
	
		dataObj.keyword = keyword;  
		dataObj.browser = browser;
						
		$.ajax({
			type : 'GET',
			url:'/searchMapInfo',
			data : dataObj,
			beforeSend : function(xhr)
            {   
				xhr.setRequestHeader(header, token);
            },
			dataType : 'json',
			contentType: 'application/json',
			success : function(result){
				//console.log(result);
				
				let storeList = [];
				
				for (let item of result) {
				  let storeObject = {};
				  
				  storeObject["address_name"] = item.addressName;
				  storeObject["road_address_name"] = item.roadAddressName;
				  storeObject["category_group_name"] = item.storeCategory;
				  storeObject["category_name"] = item.storeCategoryDetail;
				  storeObject["place_name"] = item.storeName;
				  storeObject["x"] = item.xcoordinate;
				  storeObject["y"] = item.ycoordinate;
				  
				  storeList.push(storeObject);
				   
				}
				// 검색한 결과를 지도에 표시해준다.
				displayPlaces(storeList);	
			},
			error : function(result){
				alert(`Error occured : ${result}`);
				console.log(result);
			}
			
		});	// End ajax
	}
	
	
	// 마커를 담을 배열입니다
	var markers = [];
	
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = {
	        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
	    };  
	
	// 지도를 생성합니다    
	var map = new kakao.maps.Map(mapContainer, mapOption); 
	
	// 장소 검색 객체를 생성합니다
	var ps = new kakao.maps.services.Places();  
	
	// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
	var infowindow = new kakao.maps.InfoWindow({zIndex:1});
	
	// 키워드 검색을 요청하는 함수입니다
	function searchPlaces() {
	
	    var keyword = document.getElementById('keyword').value;
	
	    if (!keyword.replace(/^\s+|\s+$/g, '')) {
	        alert('키워드를 입력해주세요!');
	        return false;
	    }
	
	    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
	    ps.keywordSearch( keyword, placesSearchCB); 
	}
	
	// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
	function placesSearchCB(data, status, pagination) {
	    if (status === kakao.maps.services.Status.OK) {
	
	        // 정상적으로 검색이 완료됐으면
	        // 검색 목록과 마커를 표출합니다
	        displayPlaces(data);
	
	        // 페이지 번호를 표출합니다
	        displayPagination(pagination);
	
	    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
	
	        alert('검색 결과가 존재하지 않습니다.');
	        return;
	
	    } else if (status === kakao.maps.services.Status.ERROR) {
	
	        alert('검색 결과 중 오류가 발생했습니다.');
	        return;
	
	    }
	}
	
	// 검색 결과 목록과 마커를 표출하는 함수입니다
	function displayPlaces(places) {
	
	    var listEl = document.getElementById('placesList'), 
	    menuEl = document.getElementById('menu_wrap'),
	    fragment = document.createDocumentFragment(), 
	    bounds = new kakao.maps.LatLngBounds(), 
	    listStr = '';
	    
	    // 검색 결과 목록에 추가된 항목들을 제거합니다
	    removeAllChildNods(listEl);
	
	    // 지도에 표시되고 있는 마커를 제거합니다
	    removeMarker();
	    
	    for ( var i=0; i<places.length; i++ ) {
	
	        // 마커를 생성하고 지도에 표시합니다
	        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
	            marker = addMarker(placePosition, i), 
	            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다
	
	        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
	        // LatLngBounds 객체에 좌표를 추가합니다
	        bounds.extend(placePosition);
	
	        // 마커와 검색결과 항목에 mouseover 했을때
	        // 해당 장소에 인포윈도우에 장소명을 표시합니다
	        // mouseout 했을 때는 인포윈도우를 닫습니다
	        (function(marker, title) {
	            kakao.maps.event.addListener(marker, 'mouseover', function() {
	                displayInfowindow(marker, title);
	            });
	
	            kakao.maps.event.addListener(marker, 'mouseout', function() {
	                infowindow.close();
	            });
	
	            itemEl.onmouseover =  function () {
	                displayInfowindow(marker, title);
	            };
	
	            itemEl.onmouseout =  function () {
	                infowindow.close();
	            };
	            
	        })(marker, places[i].place_name);
	
	        fragment.appendChild(itemEl);
	    }
	
	    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
	    listEl.appendChild(fragment);
	    menuEl.scrollTop = 0;
	
	    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
	    map.setBounds(bounds);
	}
	
	// 검색결과 항목을 Element로 반환하는 함수입니다
	function getListItem(index, places) {
	
	    var el = document.createElement('li'),
	    itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
	                '<div class="info">' +
	                '   <h3 id="storeName">' + places.place_name + '</h3>';
	
	    if (places.road_address_name) {
	        itemStr += '    <span id="roadAddressName">' + places.road_address_name + '</span>' +
	                    '   <span id="addressName" class="jibun gray">' +  places.address_name  + '</span>';
	    } else {
	        itemStr += '    <span id="addressName">' +  places.address_name  + '</span>'; 
	    }
	                 
	      itemStr += '  <span id="storeCategory">' + places.category_group_name  + '</span>';
	      itemStr += '  <span id="storeCategoryDetail">' + places.category_name  + '</span>';
	      itemStr += '  <span id="xcoordinate" hidden="hidden" >' + places.x  + '</span>';
	      itemStr += '  <span id="ycoordinate" hidden="hidden" >' + places.y  + '</span>' + '</div>';
	
	    el.innerHTML = itemStr;
	    el.className = 'item';
	
	    return el;
	}
	
	// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
	function addMarker(position, idx, title) {
	    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
	        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
	        imgOptions =  {
	            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
	            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
	            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
	        },
	        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
	            marker = new kakao.maps.Marker({
	            position: position, // 마커의 위치
	            image: markerImage 
	        });
	
	    marker.setMap(map); // 지도 위에 마커를 표출합니다
	    markers.push(marker);  // 배열에 생성된 마커를 추가합니다
	
	    return marker;
	}
	
	// 지도 위에 표시되고 있는 마커를 모두 제거합니다
	function removeMarker() {
	    for ( var i = 0; i < markers.length; i++ ) {
	        markers[i].setMap(null);
	    }   
	    markers = [];
	}
	
	// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
	function displayPagination(pagination) {
	    var paginationEl = document.getElementById('pagination'),
	        fragment = document.createDocumentFragment(),
	        i; 
	
	    // 기존에 추가된 페이지번호를 삭제합니다
	    while (paginationEl.hasChildNodes()) {
	        paginationEl.removeChild (paginationEl.lastChild);
	    }
	
	    for (i=1; i<=pagination.last; i++) {
	        var el = document.createElement('a');
	        el.href = "#";
	        el.innerHTML = i;
	
	        if (i===pagination.current) {
	            el.className = 'on';
	        } else {
	            el.onclick = (function(i) {
	                return function() {
	                    pagination.gotoPage(i);
	                }
	            })(i);
	        }
	
	        fragment.appendChild(el);
	    }
	    paginationEl.appendChild(fragment);
	}
	
	// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
	// 인포윈도우에 장소명을 표시합니다
	function displayInfowindow(marker, title) {
	    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';
	
	    infowindow.setContent(content);
	    infowindow.open(map, marker);
	}
	
	 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
	function removeAllChildNods(el) {   
	    while (el.hasChildNodes()) {
	        el.removeChild (el.lastChild);
	    }
	}
	
	function addOptions(comboBox,data){
		
		let option = document.createElement('option');
		option.innerText = '';
		comboBox.append(option);
		for(let combo of data){
			option = document.createElement('option');
			option.innerText = combo;
			comboBox.append(option);	
		}
		
		return comboBox;
	}
	
	function updateLocation(location){
		
		let token = $("meta[name='_csrf']").attr("content");
		let header = $("meta[name='_csrf_header']").attr("content");
							
		$.ajax({
			type : 'POST'
			,url:'/updateLocation'
			,data : JSON.stringify(location)
			,dataType : 'json'
			,beforeSend : function(xhr)
            {   
				xhr.setRequestHeader(header, token);
            }
			,dataType : 'json'
			,contentType: 'application/json'
			,success : function(result){
					
					alert(`${location.storeName} 정보로 정상적으로 업데이트 되었습니다.`);
					
					let keyword = document.getElementById("storeKeyword").value;
					let category = $("#categoryCombo option:selected").val();
					let categoryDetail = $("#categoryDetailCombo option:selected").val();
					
					searchStoreList(keyword,category,categoryDetail);
			}
			,error : function(result){
				alert(`Error occured : ${result}`);
				console.log(result);
			}
			
		});	// End ajax
		
	}
	
});