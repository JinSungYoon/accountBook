/*
var reader = new FileReader();

var excelFileDate = null;

var inputElement = document.getElementById("inputExcelRead");

inputElement.addEventListener('change',function(){
	reader.readAsArrayBuffer(event.target.files[0]);
},false);

reader.onload = function (e) {
	
	var data = e.target.result;
	var workbook = XLSX.read(data,{type:"array"});
	
	var sheetNames = workbook.SheetNames;
	var sheet = workbook.Sheets[sheetNames];
	excelFileData = XLSX.utils.sheet_to_json(sheet);
	
	var thead = '';
	var tbody = '';
	var theadObject;
	var col = Object.keys(excelFileData[0]);
	
	console.log(excelFileData);
	
	for(var row=0;row<excelFileData.length;row++){
		if(row==0){
			// Header와 동일한 데이터는 제거하기 위해
			theadObject = excelFileData[row];
			thead += '<tr>';
			for(var idx=0; idx<col.length;idx++){
				var data = excelFileData[row][col[idx]]==undefined?'':excelFileData[row][col[idx]];
				thead += '<th scope="col">'+data+'</th>';
			}
			thead += '</tr>';
		}else{
			// Header 칼럼과 동일한 데이터는 추가하지 않기 위해
			if(JSON.stringify(theadObject) != JSON.stringify(excelFileData[row])){
				tbody += '<tr>';
				for(var idx=0; idx<col.length;idx++){
					var data = excelFileData[row][col[idx]]==undefined?'':excelFileData[row][col[idx]];
					tbody += '<td>'+data+'</td>';
				}
				tbody += '</tr>';
			}
		}
	}
	
	$("#excelHead").empty();
	$("#excelHead").append(thead);
	$("#excelBody").empty();
	$("#excelBody").append(tbody);
	
};
*/


let excelCol = '';

function readExcel() {
    let input = event.target;
    let reader = new FileReader();
    reader.onload = function () {
        let data = reader.result;
        let workBook = XLSX.read(data, { type: 'binary' });
        workBook.SheetNames.forEach(function (sheetName) {
	
	        let excelFileData = XLSX.utils.sheet_to_json(workBook.Sheets[sheetName]);
	
	        let thead = '';
			let tbody = '';
			let data = '';
			let radio = '<input class="excelRadio" name="excelRadio" type="radio" value="">';
			let checkBox = '<input id="excelCheckAll" type="checkbox" value="" aria-label="Checkbox for following text input">';
			excelCol = Object.keys(excelFileData[0]);
			// 체크박스 column 추가
			excelCol.unshift(undefined);
			
			thead += '<tr>';
			for(let idx=0; idx<excelCol.length;idx++){
				data = excelCol[idx]==undefined?'':excelCol[idx];	
				if(data==''){
					thead += '<th scope="col">'+checkBox+data+'</th>';
				}else{
					thead += '<th scope="col">'+radio+data+'</th>';	
				}
				
			}
			thead += '</tr>';
			
			for(let row=0;row<excelFileData.length;row++){
				tbody += '<tr>';
				for(let idx=0; idx<excelCol.length;idx++){
					if(excelCol[idx]==undefined){
						data = '<input id="excelCheckBox" name="excelCheckBox" type="checkBox" value="">';
					}else{
						data = excelFileData[row][excelCol[idx]]==undefined?'':excelFileData[row][excelCol[idx]];	
					}
					
					tbody += '<td>'+data+'</td>';
				}
				tbody += '</tr>';
			}
			
			$("#excelHeader").empty();
			$("#excelHeader").append(thead);
			$("#excelBody").empty();
			$("#excelBody").append(tbody);
            
        })
    };
    reader.readAsBinaryString(input.files[0]);
}

function addRow(index='',val=''){
	let tr = document.getElementById("listHeader").children[0];
	let th = document.getElementById("listHeader").children[0].children;
	let tList = document.getElementById("listBody").children;
	let tbody = '';
	let value = '';
	let text = '^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z_0-9!@#$%^&*()-_=+{}\'\"\.\,\`\\s\\t~]+$';
	let float = '^(0|(([-][0-9][.]d+)|([0]+[.][0-9]+)|([-]?[1-9][0-9]*([.][0-9]+)?)))$';
	let date = '^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\\s([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$';
	let number = '^[0-9]+$';
	//'[0-9]{8}'
	let money = '^[+-]?[\\d,]*(\\.?\\d*)$';
	let mandatory = ['','required','required','required','required','','','','','','','','','',''];
	let dataPattern = ['',number,text,money,date,text,text,number,text,text,text,text,text,float,float];
	
	tbody += `<tr id=${tList.length+1} class="tRow">`;
	for(let idx=0;idx<th.length;idx++){
    	if(idx==0){
			tbody += `<td id=${th[idx]["id"]} scope="row"><div class="dataContainer"><input class="checkBox" name="checkboxName" type="checkbox" value="" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
		}
    	else if(idx<11){
			if(idx==index){
				value=val;
			}else{
				value="";
			}
			if(idx==2){	// 상호명입력 및 상호명에 대한 지리 데이터를 검색하기 위한 버튼
				tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input class="dataInput" type="text" value="${value}" pattern=${dataPattern[idx]} ${mandatory[idx]}><button th:type="button" id="searchBtn"><i class="fas fa-search"></i></button></div></td>`;
			}else if(idx==4){	
				// datepicker 적용
				//tbody += `<td id=${th[idx]["id"]}><input name="datepicker" type="text" value="${value}" style="position:relative width:100%; border: 0;"></td>`;
				// datetimepicker 적용
				tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input id="datetimepicker" type="text" value="${changeDateFormat(value)}" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
			}else if(idx==8){
				tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input class="cancelBox" name="cancelName" type="checkbox" value="N" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
			}else{
				tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input class="dataInput" type="text" value="${value}" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
			}
		}else{
			tbody += `<td id=${th[idx]["id"]} style="display:none"><div class="dataContainer"><input class="dataInput type="text" value="" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
		}
	}
	tbody += `</tr>`;
	
	$("#listBody").append(tbody);
	/*
	// datepicker 적용
	$(document).find("input[name=datepicker]").removeClass('hasDatepicker').datepicker({
		changeMonth: true, // 월을 바꿀수 있는 셀렉트 박스를 표시한다. 
		changeYear : true,	// 년을 바꿀 수 있는 셀렉트 박스를 표시한다.
		dateFormat : "yy-mm-dd",
		
	});
	*/	
}

// datetimepicker 적용
$(document).on(('click','focus'), '#datetimepicker', function (event) {
   
    var datetime_ele = $(event.target);
    datetime_ele.datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    }).datetimepicker("show");
    
});

function changeDateFormat(input){
	let value = input;
	
	if(value!=''){
	
	let splitDate = value.split(' ');
	
	let yearMonth = splitDate[0].split(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi);
	let time = splitDate[1].split(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi);
	
	let year    = yearMonth.filter(data=>data.length>=4)[0]!=undefined ? yearMonth.filter(data=>data.length>=4)[0] : String(new Date().getFullYear());
	let month   = yearMonth.filter(data=>data.length==2)[0]!=undefined ? yearMonth.filter(data=>data.length==2)[0] : String(new Date().getMonth()+1).padStart(2,'0');
	let date    = yearMonth.filter(data=>data.length==2)[1]!=undefined ? yearMonth.filter(data=>data.length==2)[1] : String(new Date().getDate()).padStart(2,'0');
	let hours   = time[0]!=undefined ? time[0] : String(new Date().getHours()).padStart(2,'0');
	let minutes = time[1]!=undefined ? time[1] : String(new Date().getMinutes()).padStart(2,'0');
	let seconds = time[2]!=undefined ? time[2] : String(new Date().getSeconds()).padStart(2,'0');
	
	// 연,월,일,시,분,초가 다 있는경우 이렇게 하려고 했었으나....
    //let pureNumber = value.replace(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi,"");
    let pureNumber = year+month+date+hours+minutes+seconds;
	value = pureNumber.replace(/(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/, '$1-$2-$3 $4:$5:$6');
	
	}
	return value;
}

function addExcelRow(){
	let tbody = '<tr>';
	for(let idx=0; idx<excelCol.length;idx++){
		if(excelCol[idx]==undefined){
			data = '<input id="excelCheckBox" name="excelCheckBox" type="checkBox" value="">';
		}else{
			data='';		
		}
		
		tbody += '<td>'+data+'</td>';
	}
	tbody += '</tr>';
	
	$("#excelBody").append(tbody);
	
}


function deleteRow(){
	let checkedTr = $("input[name=checkboxName]:checked").parent().parent().parent();
	
	for(let idx=checkedTr.length;idx>0;idx--){
		$("#listBody").children().eq(checkedTr[idx-1].rowIndex-1).remove();
	}
	
}

function deleteExcelRow(){
	let checkedTr = $("input[name=excelCheckBox]:checked").parent().parent();
	for(let idx=checkedTr.length;idx>0;idx--){
		$("#excelBody").children().eq(checkedTr[idx-1].rowIndex-1).remove();
	}
}

function addExcelColumn(){
	let bodySelectedIndex = $("input[name=radio]:checked").parent().index();
	let excelSelectedIndex = $("input[name=excelRadio]:checked").parent().index();
	
	if(bodySelectedIndex != -1 && excelSelectedIndex != -1){
		let bodySelectedName = $("#listHeader").children().children()[bodySelectedIndex].innerText;
		let excelSelectedNmae = $("#excelHeader").children().children()[excelSelectedIndex].innerText;
		if(confirm(`${excelSelectedNmae}의 데이터를 ${bodySelectedName}로 추가하시겠습니까?`)){
			for(let i=0;i<$("#excelBody").children().length;i++){
				addRow(bodySelectedIndex,$("#excelBody").children()[i].children[excelSelectedIndex].innerText);
			}
		}	
	}else{
		alert("복사하려면 복사할 엑셀 데이터 칼럼과 복사될 칼럼 데이터 칼럼을 선택해주세요");
	}	
}

function copyExcelColumn(){
	let bodySelectedIndex = $("input[name=radio]:checked").parent().index();
	let excelSelectedIndex = $("input[name=excelRadio]:checked").parent().index();
	if(bodySelectedIndex != -1 && excelSelectedIndex != -1){
		let bodySelectedName = $("#listHeader").children().children()[bodySelectedIndex].innerText;
		let excelSelectedNmae = $("#excelHeader").children().children()[excelSelectedIndex].innerText;
		if(confirm(`${excelSelectedNmae}의 데이터를 ${bodySelectedName}로 복사하시겠습니까?`)){
			if($("#listBody").children().length<$("#excelBody").children().length){
				alert(`Excel 데이터의 row갯수와 List의 데이터 row갯수가 다릅니다.\n List:${$("#listBody").children().length} Excel:${$("#excelBody").children().length}`);
			}else{
				// list box의 뒤에서부터 추가하기
				for(let idx=1;idx<=$("#excelBody").children().length;idx++){
					if(bodySelectedIndex!=4){
						$("#listBody").children()[$("#listBody").children().length-idx].children[bodySelectedIndex].children[0].children[0].value = $("#excelBody").children()[$("#excelBody").children().length-idx].children[excelSelectedIndex].innerText;
					}else{
						$("#listBody").children()[$("#listBody").children().length-idx].children[bodySelectedIndex].children[0].children[0].value = changeDateFormat($("#excelBody").children()[$("#excelBody").children().length-idx].children[excelSelectedIndex].innerText);
					}
					
				}				
			}
			
		}	
	}else{
		alert("복사하려면 복사할 엑셀 데이터 칼럼과 복사될 칼럼 데이터 칼럼을 선택해주세요");
	}
	
}

$('#checkAll').click(function(){
	var checkAll = $('#checkAll').is(':checked');
	
	if(checkAll){
		$("input[name=checkboxName]").prop('checked',true);
	}else{
		$("input[name=checkboxName]").prop('checked',false);
	}
	
});

$(document).on("click", "#excelCheckAll", function(){
	let checkAll = $('#excelCheckAll').is(':checked');
	
	if(checkAll){
		$("input[name=excelCheckBox]").prop('checked',true);
	}else{
		$("input[name=excelCheckBox]").prop('checked',false);
	}
});

$(document).on("click",".cancelBox",function(){
	
	if($(event.target).is(":checked")){
		$(event.target).val('Y');
	}else{
		$(event.target).val('N');
	}
	
	console.log($(event.target.value));
});

// 동적으로 tr/td 생성시 아래와 같은 방법으로 이벤트 줘야 동작함
$(document).on("click", "#searchBtn", function(){
    var tr = $(this).closest('tr');
	var td = tr.find('td:eq(2)').find('input');
    var data = td.val();
    
    if(data!=""){
		
	    var childWin = window.open('','searchLocation','width=1200,height=500,location=no,status=no,scrollbars=yes');
	    popupForm = document.createElement("form");
		popupForm.setAttribute("name", "popupForm");
		popupForm.setAttribute("method", "post");
		popupForm.setAttribute("action", "/searchLocation");
		popupForm.setAttribute("target", "searchLocation");
		var input1 = document.createElement('input');
		input1.setAttribute("type", "hidden");
		input1.setAttribute("name", "keyword");
		input1.setAttribute("id", "keyword");
		input1.setAttribute("value", data);
		popupForm.appendChild(input1);
		var input2 = document.createElement('input');
		input2.setAttribute("type", "hidden");
		input2.setAttribute("name", "rowIndex");
		input2.setAttribute("id", "rowIndex");
		input2.setAttribute("value", tr.index());
		popupForm.appendChild(input2);
		document.body.appendChild(popupForm);
		popupForm.submit();
		document.body.removeChild(popupForm);
	}else{
		alert("가맹점명을 입력해주시요");
	}
	
});

function isValidType(mandatory,type,value){
	
	var regex = RegExp(type);
	
	if(mandatory){
		return regex.test(value);
	}else{
		return value!=''?regex.test(value):true;	
	}
	
}

function dataSave(){
	let tr =  document.getElementById("accountBook").tBodies[0].children;
	let dataArr = [];
	let colName = ['checkBox','approvalNumber','storeName','amountOfPayment','paymentDate','usedCardNumber','classificationOfUse','installmentMonth','cancellation','storeCategory','storeCategoryDetail','addressName','roadAddressName','xcoordinate','ycoordinate']
	let dataType = ['','String','String','Int','Date','String','String','Int','String','String','String','String','String','Double','Double'];
	
	for(let idx=0;idx<tr.length;idx++){
		let dataObj = {};
		let tdCnt = tr[idx].children.length;
		// Type이 맞지 않을경우 반복문을 종료해야해서 for loop를 사용
		for(let index=0;index<tdCnt;index++){
			let td = tr[idx].children[index];
			let div = td.children[0];
			let input = div.children[0];
			let value = input.value;
			let required = input.required;
			// 설정한 타엡이 맞을때만 통과
			if(isValidType(required,input.pattern,value)){
				//
				if(dataType[index]=='Int'){
					value = parseInt(value.replace(/[^\d.-]/g, ''));
				}else if(dataType[index]=='Double'){
					value = parseFloat(value.replace(/[^\d.-]/g, ''));
				}
				
				dataObj[colName[index]] = value;	
				
			}else{
				alert(`${value}는 ${dataType[index]}타입이 아닙니다.`);
				return;
			}
		}
		
		/*
		// 각 row에 있는 데이터들을 object로 만드는 과정
		Array.prototype.forEach.call(td,(item,index)=>{
				// 설정한 타엡이 맞을때만 통과
				if(isValidType(dataType[index],item.innerText)){
					if(dataType[index]=='Int' || dataType[index]=='Double'){
						var number = parseInt(item.innerText.replace(/[^\d.-]/g, ''));
						if(!isNaN(number)){
							dataObj[colName[index]] = parseInt(item.innerText.replace(/[^\d.-]/g, ''));	
						}{
							alert("숫자 형식이 아닌 값이 들어왔습니다.");
						}
					}else{
						dataObj[colName[index]] = item.innerText;	
					}
				}else{
					alter(`${item.innerText}는 ${dataType[index]}타입이 아닙니다.`);
					return;
				}
		});
		*/
		// 만들어진 object를 Array로 만드는 과정.
		dataArr.push(dataObj);
	}
	
	console.log(dataArr);
	
	
	$.ajax({
		type : 'POST',
		url:'/saveExcelData',
		data : JSON.stringify(dataArr),
		dataType : 'json',
		contentType: 'application/json',
		success : function(result){
			alert(`${result} Data successfully saved`);
		},
		error : function(result){
			alert(`Error occured : ${result}`);
			console.log(result);
		}
		
	});	// End ajax
	
}

