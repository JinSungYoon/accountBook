$(document).ready(function(){
	
	searchLocation(document.getElementById("keyword").value,$("#selectSearch option:selected").val());
	
	$("#searchLocation").click(function(){
		searchLocation(document.getElementById("keyword").value,$("#selectSearch option:selected").val());
	});
	
	$("#returnLocation").click(function(){
		
		if($("input[name=radioBox]:checked").length>0){
			// 리스트에서 checkBox를 제외한 값들 추출하기
			let id = Object.values($("input[name=radioBox]:checked").parent().parent().children()).filter(data=>data.id!=undefined);
			id = Object.values(id).map(data=>data.id);
			id.shift(0);
			let text = Object.values($("input[name=radioBox]:checked").parent().parent().children()).filter(data=>data.innerText!=undefined);
			text = Object.values(text).map(data=>data.innerText);
			text.shift(0);
			let rowIndex = document.getElementById("rowIndex").value;
			
			let tr = window.opener.document.getElementById("listBody").children[rowIndex];
			
			parentTdId = Object.values(tr.children)
			
			// 부모창에 검색한 정보 매핑
			for(let loop=0;loop<id.length;loop++){
			    var index = Object.values(tr.children).map(data =>data.id).indexOf(id[loop]);
			    tr.children[index].children[0].children[0].value = text[loop];
			}
			
			 window.close();
		}else{
			alert("하나의 지역을 선택해주세요");
		}
		
	});
	
	function searchLocation(keyword,browser){
		
		let dataObj = {};
	
		dataObj.keyword = keyword;  
		dataObj.browser = browser;
						
		$.ajax({
			type : 'GET',
			url:'/searchMapInfo',
			data : dataObj,
			dataType : 'json',
			contentType: 'application/json',
			success : function(result){
				var tbody = '';
				var col = Object.keys(result[0]);
				col.unshift('radio');
				console.log(col);
				//
				for(var row=0;row<result.length;row++){
					// Header 칼럼과 동일한 데이터는 추가하지 않기 위해
					tbody += '<tr>';
					
					for(var idx=0; idx<col.length;idx++){
						var data = '';
						if(idx==0){
							data = '<input class="radioBox" name="radioBox" type="radio" value="">';
						}else{
							 data = result[row][col[idx]]==undefined?'':result[row][col[idx]];
							
						}
						tbody += `<td id=${col[idx]}>`+data+'</td>';	
					}
					
					tbody += '</tr>';
				}
				
				$("#locationBody").empty();
				$("#locationBody").append(tbody);
			},
			error : function(result){
				alert(`Error occured : ${result}`);
				console.log(result);
			}
			
		});	// End ajax
	}
});