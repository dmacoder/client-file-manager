<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<sec:csrfMetaTags/>
<title>클라이언트 파일 관리</title>
<link rel="stylesheet" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="/webjars/Eonasdan-bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="/webjars/bootstrap-daterangepicker/2.1.27/daterangepicker.css">

<!-- <script src="/webjars/jquery/1.11.3/jquery.min.js"></script> -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<!-- <script src="/webjars/jquery-form/3.51/jquery.form.js"></script> -->
<script src="http://malsup.github.com/jquery.form.js"></script>

<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/themes/smoothness/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/jquery-ui.min.js"></script>

<script src="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script src="/webjars/momentjs/2.8.2/min/moment-with-locales.min.js"></script>

<script src="/webjars/bootstrap-filestyle/1.2.1/src/bootstrap-filestyle.min.js"></script>

<script src="/webjars/Eonasdan-bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.js"></script>
<script src="/webjars/bootstrap-daterangepicker/2.1.27/daterangepicker.js"></script>

<script src="/js/ilovecoinAdminCommon.js?v=1"></script>
<script src="/js/is.min.js?v=1"></script>



</head>
<body>
    <sec:authentication property="principal" var="pinfo" />

    <script>
                    var uploadDivCnt = 0;

                    $(document).ready(function() {

                        initPage();
                    });

                    function initPage() {

                        initEvent();
                        
                        initFileList();
                        
                    }
                    
                    function initFileList(){
                        
                        $.ajax({
                            async : false,
                            type : "get",
                            url : "/api/v1/client-files",
                            dataType : "json",
                            cache: false,
                            success : function(result) {
                                
                                $("#fileList").empty();
                                
                                var innerHTML = "<ul>";
                                
                                var dataLength = Object.keys(result.data).length;
                                
                       
                                
                                for(var i=0 ; i<dataLength ; i++){
                                   innerHTML += '<li> <a id="fileNm'+i+'" href="/api/v1/client-files/downloads/'+result.data[i].fileId+'">'+result.data[i].fileNm+'</a> </li>';
                                       
                                }
                                
                                innerHTML += '</ul>';
                                
                                $("#fileList").html(innerHTML);
                                
                            },
                            error : function(e) {
                                alert("파일 업로드시 오류 발생 :" + e.responseText);
                                isSuccess = false;
                            }
                            
                        });
                        
                    }

                    function removeObj(obj) {

                        $(obj).remove();

                    }

                    function formValidation() {

                        var isValid = true;

                        if($("input[type='file']").length==0){
                            popLayerMsg("등록할 파일이 없습니다.<br/>파일추가를 하세요.");
                            return false; 
                        }
                        
                        $("input[type='file']").each(function(index) {

                            var inputFile = $(this);

                            var files = inputFile[0].files;

                            if (files.length != 0) {

                                var versionText = inputFile.next().next().children("input").first().val();

                                if (is.empty(versionText)) {
                                    popLayerMsg((index + 1) + "번째 파일의 버전이 입력되지 않았습니다.");
                                    isValid = false;
                                    return false; //each break 
                                }

                            } else {
                                popLayerMsg((index + 1) + "번째 파일이 첨부되지 않았습니다.");
                                isValid = false;
                                return false; //each break
                            }

                        });

                        return isValid;

                    }

                    function fileUploadStart() {
							
                        //alert("업로드 시작");
                        
                        var isSuccess = true;
                        
                        $("input[type='file']").each(function(index) {

                            var form = $('#fileUploadForm')[0];

                            var formData = new FormData();
                            
                            var inputFile = $(this);
							
                            
                            var fileVersion = inputFile.next().next().children("input").first().val();
                            
                            var file = inputFile[0].files;
                            
                            
                            formData.append('file',   file[0]);
                            
                            formData.append('fileVersion', fileVersion);
							
                            $.ajax({
                                async : false,
                                type : "POST",
                                enctype: 'multipart/form-data',
                                url : "/api/v1/client-files",
                                //dataType : "json",
                                data : formData,
                                processData : false,
                                contentType : false,
                                //cache: false,
                                success : function(result) {
                                    
                                    popLayerMsg("업로드 되었습니다.");
                                    
                                    $("#uploadDiv").empty();
                                    
                                    initFileList();
                                    

                                },
                                error : function(e) {
                                    alert("파일 업로드시 오류 발생 :" + e.responseText);
                                    isSuccess = false;
                                }
                            });
                            
                        });

                        
                    }

                    function initEvent() {

                        $("#regFileBtn").click(function() {
                            if (confirm("파일을 등록 하시겠습니까?")) {
                                if (formValidation()) {

                                    fileUploadStart();

                                }
                            }
                        });

                        $("#addFileBtn").click(function() {

                            var uploadDiv = $("#uploadDiv");

                            uploadDivCnt++;

                            var inHTML = "<div class='input-group' id='attachFileDiv"+uploadDivCnt+"'> <input type='file' id='attachFile"+uploadDivCnt+"' name='attachFile[]' class='filestyle' data-iconName='glyphicon glyphicon-inbox'>";

                            inHTML += "<span class='input-group-btn'><input type='text' id='attachFileVersion"+uploadDivCnt+"' name='attachFileVersion"+uploadDivCnt+"' placeholder='버전을 입력하세요.' class='btn btn-default'><button type='button' onclick='removeObj(\"#attachFileDiv" + uploadDivCnt + "\")' class='btn btn-default'><span class='glyphicon glyphicon-remove'></span></button></span></div>";

                            uploadDiv.append(inHTML);

                            $("#attachFile" + uploadDivCnt).filestyle({
                                iconName : "glyphicon glyphicon-inbox"
                            });
                            $("#attachFile" + uploadDivCnt).filestyle('buttonText', '파일첨부');

                        });
                    }
                </script>

    <div class="container">

        <div class="row">
            <div class="col-md-12">

                <div class="page-header">
                    <h1>클라이언트 파일 매니저</h1>
                </div>

                <div class="page-content">
                    <p>키오스크에서 필요한 파일들을 버전별로 관리할 수 있습니다.</p>
                    
                    <form id="fileUploadForm" method="post" enctype="multipart/form-data" >
                <sec:csrfInput />
                
                    <div class="row">
                        <div class="form-group col-sm-6">


                            <label>첨부파일</label>
                            <button type="button" id="addFileBtn" class="btn btn-large btn-primary">
                                <span class="glyphicon glyphicon-inbox"></span> 파일 추가
                            </button>

                            <br /> <br />

                            <div class='uploadDiv' id="uploadDiv"></div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <br />
                                    <div class="panel panel-default">

                                        <div class="panel-heading">등록된 클라이언트 파일 리스트</div>
                                        <!-- /.panel-heading -->
                                        <div class="panel-body">

                                            <div id='fileList' class='client-file-list'>
                                                <ul>
                                                </ul>
                                            </div>
                                        </div>
                                        <!--  end panel-body -->
                                    </div>
                                    <!--  end panel-body -->
                                </div>
                                <!-- end panel -->
                            </div>
                            <!-- /.row -->

                        </div>
                    </div>


                    <div class="row">

                        <div class="col-md-6 container text-right">


                            <sec:authorize access="hasAuthority('ADMIN')">
                                <button type="button" id="regFileBtn" class="btn btn-info">등록</button>
                            </sec:authorize>

                        </div>

                    </div>
                    
                    </form>
                </div>



            </div>
        </div>

    </div>

    <jsp:include page="/WEB-INF/views/common/modal_msg.jsp"></jsp:include>
</body>
</html>

