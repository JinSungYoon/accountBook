
let today = new Date();

/* MonthPicker 옵션 */
options = {
	pattern: 'yyyy-mm', // Default is 'mm/yyyy' and separator char is not mandatory
	selectedYear: today.getFullYear(),
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
	callTransactionHistory(fromDt,toDt);
});

function callTransactionHistory(from,to){
	let dataObj = new Object();
		dataObj.fromDate = fromDt;
		dataObj.toDate = toDt;
		
		console.log(dataObj);
			
		$.ajax({
			type : 'GET',
			url:'/transactionHistory',
			data : dataObj,
			dataType : 'json',
			contentType: 'application/json',
			success : function(result){
				console.log(result);
				
				// 기존에 존재하는 데이터 모두 제거
				removeAllChildNods($("#listBody")[0]);
				// 조회한 데이터 Display
				for(let item of result){
					addRow('','',item);	
				}
			},
			error : function(result){
				alert(`Error occured : ${result}`);
				console.log(result);
			}
		});
}

let now = new Date();
let year,month,firstDate,lastDate,fromDt,toDt;

firstDate = new Date(now.getFullYear(),now.getMonth(),1);
lastDate = new Date(now.getFullYear(),now.getMonth()+1,0);
year = firstDate.getFullYear();
month = String(parseInt(firstDate.getMonth(),10)+1).padStart(2,'0');
fromDt = firstDate.getFullYear()+String(parseInt(firstDate.getMonth(),10)).padStart(2,'0')+String(firstDate.getDate()).padStart(2,'0')+"000000";
toDt = lastDate.getFullYear()+String(parseInt(lastDate.getMonth(),10)).padStart(2,'0')+String(lastDate.getDate()).padStart(2,'0')+"235959";

// 이번달 기본 셋팅
$("#monthpicker").val(year+"-"+month);


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

function addRow(index='',val='',data=''){
	let tr = document.getElementById("listHeader").children[0];
	let th = document.getElementById("listHeader").children[0].children;
	let tList = document.getElementById("listBody").children;
	let tbody = '';
	let value = '';
	let text = '^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z_0-9!@#$%^&*()-_=+{}\'\"\.\,\`\\s\\t~]+$';
	let float = '^(0|(([-][0-9][.]d+)|([0]+[.][0-9]+)|([-]?[1-9][0-9]*([.][0-9]+)?)))$';
	let date = '^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\\s([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$';
	let number = '^[0-9]+$';

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
			if(data!=""){
				let v = data[$("#listHeader")[0].children[0].children[idx].id];
				value = v!=null ? v : '';
			}
			
			if(idx==2){	// 상호명입력 및 상호명에 대한 지리 데이터를 검색하기 위한 버튼
				tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input class="dataInput" type="text" value="${value}" pattern=${dataPattern[idx]} ${mandatory[idx]}><button th:type="button" id="searchBtn"><i class="fas fa-search"></i></button></div></td>`;
			}else if(idx==3){	
				tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input class="costInput" type="text" value="${value}" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
			}else if(idx==4){	
				// datepicker 적용
				//tbody += `<td id=${th[idx]["id"]}><input name="datepicker" type="text" value="${value}" style="position:relative width:100%; border: 0;"></td>`;
				// datetimepicker 적용
				tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input id="datetimepicker" type="text" value="${changeDateFormat(value)}" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
			}else if(idx==8){
				if(value != 'N' && value !=''){
					tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input class="cancelBox" name="cancelName" type="checkbox" value="${value}" pattern=${dataPattern[idx]} ${mandatory[idx]} checked></div></td>`;
				}else{
					tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input class="cancelBox" name="cancelName" type="checkbox" value="${value}" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;	
				}
				
			}else{
				tbody += `<td id=${th[idx]["id"]}><div class="dataContainer"><input class="dataInput" type="text" value="${value}" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
			}
		}else{
			tbody += `<td id=${th[idx]["id"]} style="display:none"><div class="dataContainer"><input class="dataInput type="text" value="" pattern=${dataPattern[idx]} ${mandatory[idx]}></div></td>`;
		}
	}
	tbody += `</tr>`;
	
	$("#listBody").append(tbody);
	
		
}

// datetimepicker 적용
$(document).on(('click','focus'), '#datetimepicker', function (event) {
    let datetime_ele = $(event.target);
    datetime_ele.datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    }).datetimepicker("show");
    
});

// 거래금액에 음수값이 입력되었을 경우 취소여부를 자동으로 체크하는 로직
$(document).on('change', '.costInput', function (event) {
	if($(this)[0].value.includes('-')){
		$("#listBody").children()[$(this).closest('tr').index()].children[8].children[0].children[0].click();
	}else{
		$("#listBody").children()[$(this).closest('tr').index()].children[8].children[0].children[0].click();
	}
});

function changeDateFormat(input){
	let value = input;
	
	let year,month,date,hours,minutes,seconds;
	let cur = new Date();
	
	year = month = date = hours = minutes = seconds = '';
	
	if(value!=''){
	
		let splitDate = value.split(' ');
				
		let yearMonth = splitDate[0]!=undefined ? splitDate[0].split(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi) : '';
		year    = yearMonth.filter(data=>data.length>=4)[0]!=undefined ? yearMonth.filter(data=>data.length>=4)[0] : String(new Date().getFullYear());
		month   = yearMonth.filter(data=>data.length==2)[0]!=undefined ? yearMonth.filter(data=>data.length==2)[0] : String(new Date().getMonth()+1).padStart(2,'0');
		date    = yearMonth.filter(data=>data.length==2)[1]!=undefined ? yearMonth.filter(data=>data.length==2)[1] : String(new Date().getDate()).padStart(2,'0');	
	
		let time = splitDate[1]!= undefined ? splitDate[1].split(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi) : '';
		hours   = time[0]!=undefined ? time[0] : String(new Date().getHours()).padStart(2,'0');
		minutes = time[1]!=undefined ? time[1] : String(new Date().getMinutes()).padStart(2,'0');
		seconds = time[2]!=undefined ? time[2] : String(new Date().getSeconds()).padStart(2,'0');	
		
		
		// 연,월,일,시,분,초가 다 있는경우 이렇게 하려고 했었으나....
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
		alert("추가하려면 추가할 엑셀 데이터 칼럼과 추가될 칼럼 데이터 칼럼을 선택해주세요");
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
						$("#listBody").children()[$("#listBody").children().length-idx].children[bodySelectedIndex].children[0].children.change;
					}else{
						$("#listBody").children()[$("#listBody").children().length-idx].children[bodySelectedIndex].children[0].children[0].value = changeDateFormat($("#excelBody").children()[$("#excelBody").children().length-idx].children[excelSelectedIndex].innerText);
						$("#listBody").children()[$("#listBody").children().length-idx].children[bodySelectedIndex].children[0].children.change;
					}
				}
						
			}
			
		}	
	}else{
		alert("복사하려면 복사할 엑셀 데이터 칼럼과 복사될 칼럼 데이터 칼럼을 선택해주세요");
	}
	
}

$('#checkAll').click(function(){
	let checkAll = $('#checkAll').is(':checked');
	
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

});

// 동적으로 tr/td 생성시 아래와 같은 방법으로 이벤트 줘야 동작함
$(document).on("click", "#searchBtn", function(){
    let tr = $(this).closest('tr');
	let td = tr.find('td:eq(2)').find('input');
    let data = td.val();
    
    if(data!=""){
		
	    let childWin = window.open('searchLocation','searchLocation','width=1200,height=500,location=no,status=no,scrollbars=yes');
	    document.getElementById("keyword").value = data;
	    document.getElementById("rowIndex").value = tr.index();
		
	}else{
		alert("가맹점명을 입력해주시요");
	}
	
});

function isValidType(mandatory,type,value){
	
	let regex = RegExp(type);
	
	if(mandatory){
		return regex.test(value);
	}else{
		return value!=''?regex.test(value):true;	
	}
	
}

 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {   
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}

function dataSave(){
	
	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content");
	
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
		
		// 만들어진 object를 Array로 만드는 과정.
		dataArr.push(dataObj);
	}
	
	console.log(dataArr);
	
	
	$.ajax({
		type : 'POST',
		url:'/saveExcelData',
		data : JSON.stringify(dataArr),
		dataType : 'json',
		beforeSend : function(xhr)
        {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
			xhr.setRequestHeader(header, token);
        },
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

