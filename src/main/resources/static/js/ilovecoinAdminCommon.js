/*
	설명 : 공통으로 사용되는 함수 정의
*/

(function($) {

    //for csrf
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    
	$.ajaxSetup({
	    beforeSend: function(xhr) {
	        xhr.setRequestHeader("AJAX", true);
	        //for csrf
	        xhr.setRequestHeader(header, token);
	     },
	     error: function(xhr, status, err) {
	        if (xhr.status == 401) {
	            alert("인증에 실패 했습니다. 로그인 페이지로 이동합니다.");
	             $(".b-close").click();
	            location.href = "/login/logout";
	         } else if (xhr.status == 403) {
	            alert("세션이 만료가 되었습니다. 로그인 페이지로 이동합니다.");
	            $(".b-close").click();
	              location.href = "/login/logout";
	         }
	     }
	});


})(jQuery);

if (typeof ilovecoinAdmin == "undefined") ilovecoinAdmin = {};

/*
 * 그룹코드를 이용하여 그룹 네이밍을 가져오는 기능
 *
 */
ilovecoinAdmin.setGroupNm = function(groupCd,selectId,selectTemplate){
	$.ajax({
		type: "get",
		url: "/common/groupNm/"+groupCd,
	    dataType : "json",
	    async: false, //동기화 처리
	    success: function(result) {
	    	var templateTag = $(selectTemplate);
	        var compiled = Handlebars.compile(templateTag.html());
	        var inputHtml = compiled(result);
	        $(selectId).append(inputHtml);
	        $('.selectpicker').selectpicker('refresh');
	    }, error: function(e) {
	    	alert("setGroupNm Error : "+e.responseText);
	    }
	});
};

/*
 * 기본키를 가져오는 기능
 *
 */
ilovecoinAdmin.getPk = function(pkCd,selectId){
	$.ajax({
		type: "get",
		url: "/common/pk/"+pkCd,
	    dataType : "json",
	    async: false, //동기화 처리
	    success: function(result) {
	    	$(selectId).val(result.outParam01);
	    }
	});
};
/*
 * 거래번호를 가져오는 기능
 *
 */
ilovecoinAdmin.getOrderNo = function(orderPk,selectId){
	$.ajax({
		type: "get",
		url: "/common/orderNo/"+orderPk,
		dataType : "json",
		async: false, //동기화 처리
		success: function(result) {
			$(selectId).val(result.outParam01);
		}
	});
};


/*
 * 페이지 이동 기능
 *
 */
ilovecoinAdmin.goList = function(url){
	$(location).attr('href',url);
};


/*
 * 거래처명 조회 기능
 *
 */
ilovecoinAdmin.getMerchantNm = function(divId,selectTemplate){
	$.ajax({
		type : "get",
		url : "/merchants/getList",
		dataType : "json",
		async: false, //동기화 처리
		success : function(result) {
			//debugger;
			var source = $(selectTemplate).html();
			var template = Handlebars.compile(source);
			var data = result.data;
			var html = template(data);
			$(divId).append(html);
			$('.selectpicker').selectpicker('refresh');
		}
	});
};


/*
 * 거래처명에 따른 기기ID조회 (With serviceTp)
 */
ilovecoinAdmin.getDeviceId = function(divId,selectTemplate,merchantId,serviceTp){
    
    serviceTp = typeof serviceTp !== 'undefined' ? serviceTp : "";
    
    var insertData = "serviceTp="+serviceTp;
    
	$.ajax({
		type : "get",
		url : "/devices/deviceId/"+merchantId,
		dataType : "json",
		async: false, //동기화 처리
		data : insertData,
		success : function(result) {
			var source = $(selectTemplate).html();
			var template = Handlebars.compile(source);
			var data = result.data;
			var html = template(data);
			$(divId).append(html);
			$('.selectpicker').selectpicker('refresh');
		}
	});
};


/*
 * 가맹점번호에 따른 기기번호, 설치장소 조회
 */
ilovecoinAdmin.getInstallPlaceNm = function(divId,selectTemplate,merchantId,serviceTp){
    serviceTp = typeof serviceTp !== 'undefined' ? serviceTp : "";
    var insertData = "serviceTp="+serviceTp;
    
    $.ajax({
        type : "get",
        url : "/devices/installPlaceNm/"+merchantId,
        dataType : "json",
        async: false, //동기화 처리
        data : insertData,
        success : function(result) {
            var source = $(selectTemplate).html();
            var template = Handlebars.compile(source);
            var data = result.data;
            var html = template(data);
            $(divId).append(html);
            $('.selectpicker').selectpicker('refresh');
        }
    });
};

/*
 * 게시판 모듈 ID에 해당하는 카테고리 정보 가져오기
 */
ilovecoinAdmin.getCategoriesByModuleId = function(divId,selectTemplate,moduleId){
    
    //var insertData = "serviceTp="+serviceTp;
    
    $.ajax({
        type : "get",
        url : "/documents/categories/"+moduleId,
        dataType : "json",
        async: false, //동기화 처리
        //data : insertData,
        success : function(result) {
            var source = $(selectTemplate).html();
            var template = Handlebars.compile(source);
            var data = result.data;
            var html = template(data);
            $(divId).append(html);
            $('.selectpicker').selectpicker('refresh');
        }
    });
};

ilovecoinAdmin.getServiceTpByDeviceId = function(deviceId){
    var serviceTp = "null";
    $.ajax({
        type : "get",
        url : "/api/v1/devices/"+deviceId,
        dataType : "json",
        async: false, //동기화 처리
        //data : insertData,
        success : function(result) {
           
            if(result.status=="fail"){
                
            }
            else{
                serviceTp = result.serviceTp;
            }
           
          
        }
    });
    return serviceTp;
};



//금액관련 콤마(,)처리 와 숫자만 입력받게 해주는 함수
ilovecoinAdmin.inputMoney = function(id) {
	 var selector="input[id="+id+"]";
	 $(selector).keypress(function(event) {
	  if(event.which && (event.which < 48 || event.which > 57) ) {
	   event.preventDefault();
	  }
	 }).keyup(function(){
	  if( $(this).val() != null && $(this).val() != '' ) {
	   $(this).val( $(this).val().replace(/[^0-9]/g, '') );
	   $(this).val( addComma($(this).val() ) );
	  }
	 });
};

//금액 콤마 제거 함수
ilovecoinAdmin.removeComma = function(id) {
	 var selector="input[id="+id+"]";
	 $(selector).keypress(function(event) {
	  if(event.which && (event.which < 48 || event.which > 57) ) {
	   event.preventDefault();
	  }
	 }).keyup(function(){
	  if( $(this).val() != null && $(this).val() != '' ) {
	   $(this).val( $(this).val().replace(/[^0-9]/g, '') );
	   $(this).val( addComma($(this).val() ) );
	  }
	 });
};




//콤마 추가
function addComma(num){
	 if(num<1000){
	  return num;
	 }
	    var len, point, str;

	    num = num + "";
	    point = num.length % 3;
	    len = num.length;

	    str = num.substring(0, point);
	    while (point < len) {
	        if (str != "") str += ",";
	        str += num.substring(point, point + 3);
	        point += 3;
	    }
	    return str;
}

/*
 * 숫자 3자리 콤마 찍기
 */
function numberWithCommasOld(x) {
	if(x == null || x ==""){
		x = 0;
	}
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

/*
 * 숫자 3자리 콤마 찍기 (소수점 제외)
 */
function numberWithCommas(n) {
    if(n == null || n ==""){
        n = 0;
    }
    var parts=n.toString().split(".");
    return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : "");
}

/*
 * 숫자 3자리 콤마 삭제
 */
function deleteComma(x) {
	return x.toString().replace(/\,/g,''); //특정문자 제거
}
/*
 * 숫자 3자리 콤마 삭제후 값 set
 */
function deleteCommaSetVal(id,x) {
	//debugger;
	 $(id).val(x.toString().replace(/\,/g,''));
}
/*
 * form action실행 할 필요없이 파라미터 전송이 가능 처리 함수
 *
 */
function gfn_isNull(str) {
    if (str == null) return true;
    if (str == "NaN") return true;
    if (new String(str).valueOf() == "undefined") return true;
    var chkStr = new String(str);
    if( chkStr.valueOf() == "undefined" ) return true;
    if (chkStr == null) return true;
    if (chkStr.toString().length == 0 ) return true;
    return false;
}
function ComSubmit(opt_formId) {
    this.formId = gfn_isNull(opt_formId) == true ? "frmInsert" : opt_formId;
    this.url = "";

    if(this.formId == "frmInsert"){
        $("#frmInsert")[0].reset();
    }

    this.setUrl = function setUrl(url){
        this.url = url;
    };

    this.addParam = function addParam(key, value){
        $("#"+this.formId).append($("<input type='hidden' name='"+key+"' id='"+key+"' value='"+value+"' >"));
    };

    this.submit = function submit(){
        var frm = $("#"+this.formId)[0];
        frm.action = this.url;
        frm.method = "post";
        frm.submit();
    };
}
/*
 * 조회 일짜 컨버터
 * formatSt = 0 2016/05/11 -> 20165011
 * formatSt = 1 2016-05-11 -> 2016-05-11
 */
function formatDate(sDateId,eDateId){
	//debugger;
	var searchRegDate = $("#searchRegDate").val();
	var splitDate = searchRegDate.split("-");
	var sDate = splitDate[0].trim().replace(/\//gi, "");
	var eDate = splitDate[1].trim().replace(/\//gi, "");
	$(sDateId).val(sDate);
	$(eDateId).val(eDate);
}

//변수 empty 검사
var isEmpty = function(value) {
    if (value == "" || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
        return true
    } else {
        return false
    }
};

//배열안 키값 검사 search->array
function keyExists(key, search) {
    if (!search || (search.constructor !== Array && search.constructor !== Object)) {
        return false;
    }
    for (var i = 0; i < search.length; i++) {
        if (search[i] === key) {
            return true;
        }
    }
    return key in search;
}

/*
 * 벨소프트 키오스크(무인환전 기기)에  적용된 최신 환율 정보를 조회
 *
 */
ilovecoinAdmin.getExchangeRate = function(currencyNm,selectId){
	$.ajax({
		type: "get",
		url: "/common/exchangeRate/"+currencyNm,
	    dataType : "json",
	    async: false, //동기화 처리
	    success: function(result) {
	    	$(selectId).val(result.saleStandardRate);
	    }
	});
};