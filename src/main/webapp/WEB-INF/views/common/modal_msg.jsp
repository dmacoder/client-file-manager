<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<section id="modal_list_div">
    
</section>

<script>
	//레이어 팝업창 (alert 왠만하면 쓰지말자.. 사용자가 짜증나니깐)
	
	
	$( document ).ready(function() {
	    
	  //ENTER,ESC 모달창 닫기
	     document.addEventListener('keydown', function(event) {
	        //|| event.keyCode == 27
	        if (event.keyCode == 13 || event.keyCode == 27) {
	            //event.preventDefault();
	            //event.stopPropagation();
	            $(".modalCloseBtn"+modalCloseNo).trigger("click");
	        }
	    }, true); 
	    
		function popModal(modalSelector) {
			$(modalSelector).modal();
		}

		
		
	    
	    $(modalVerticalCenterClass).on('show.bs.modal', function(e) {
			centerModals($(this));
		});

		$(window).on('resize', centerModals);
		
	});
	 
	var modalVerticalCenterClass = ".modal";
	var modalNo = 0;
	var modalCloseNo = 0;
	
	function centerModals($element) {
		var $modals;
		if ($element.length) {
			$modals = $element;
		} else {
			$modals = $(modalVerticalCenterClass + ':visible');
		}
		$modals.each(function(i) {
			var $clone = $(this).clone().css('display', 'block').appendTo(
					'body');
			var top = Math.round(($clone.height() - $clone.find(
					'.modal-content').height()) / 2);
			
			top = top > 0 ? top : 0;
			$clone.remove();
			$(this).find('.modal-content').css("margin-top", top);
		});
	}
	
	function popLayerMsg(content,title,htmlFlag,url) {
	    
	    modalNo++;
	    
	    modalCloseNo = modalNo;
	    
	    title = typeof title !== 'undefined' ? title : '알림';
	    
	    htmlFlag = typeof htmlFlag !== 'undefined' ? htmlFlag : 'N';
	    
	    url = typeof url !== 'undefined' ? url : 'N';
	    
	    var modalInHTML = '';
	    
	    modalInHTML += '<div class="modal fade " id="layer_msg'+modalNo+'" tabindex="-1" role="dialog" aria-labelledby="layer_msg_title'+modalNo+'" aria-hidden="true">';
	    modalInHTML += '<div class="modal-dialog">';
	        modalInHTML += '<div class="modal-content">';
	            modalInHTML += '<div class="modal-header">';
	                modalInHTML += '<button type="button" class="modalCloseBtn modalCloseBtn'+modalNo+' close" data-dismiss="modal">';
	                    modalInHTML += '<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>';
	                        modalInHTML += '</button>';
	                            modalInHTML += '<h4 class="modal-title" id="layer_msg_title'+modalNo+'">알림</h4>';
	                                modalInHTML += '</div>';
	                                    modalInHTML += '<div class="modal-body" id="layer_msg_content'+modalNo+'">...</div>';
	                                        modalInHTML += '<div class="modal-footer">';
	                                            modalInHTML += '<button type="button" class="modalCloseBtn modalCloseBtn'+modalNo+' btn btn-default" data-dismiss="modal">닫기</button>';
	                                                modalInHTML += '</div>';
	                                                    modalInHTML += '</div>';
	                                                        modalInHTML += '</div>';
	                                                            modalInHTML += '</div>';
	    
	    $("#modal_list_div").append(modalInHTML);
	    
	    
	  
	   
	    
	    /* $("#layer_msg"+modalNo).on('keydown', function ( e ) {
	        var key = e.which || e.keyCode;
	        if (key == 13) {
	            //$('#changed_dp_ok')[0].click(); // <----use the DOM click this way!!!
	            $(".modalCloseBtn"+modalNo).trigger("click");
	        }
	    }); */
	    
	   
	    
	    /* if(url != 'N')
	    {
	        
	        $("#layer_msg"+modalNo).on('hide.bs.modal', function(e){
	            
	            $(location).attr('href',url);
	            
	          });
	        
	    	
	    	//클릭 대신 hide.bs.modal로 적용시
	    	$(".modalCloseBtn"+modalNo).on("click",function() {
	    		$(location).attr('href',url);
	    	}); 
	    	
	    } */
	    
 		 $("#layer_msg"+modalNo).on('hide.bs.modal', function(e){
            
 		    if(url != 'N')
 		    {
 		       $(location).attr('href',url);
 		       return;
 		    }
	        $("#layer_msg"+modalNo).remove();
	        modalCloseNo--;
          });
	    
	    $("#layer_msg_title"+modalNo).text(title);
		
	    if(htmlFlag=="N")
	    {
			$("#layer_msg_content"+modalNo).empty();
			$("#layer_msg_content"+modalNo).append("<p>" + content + "</p>");
	    }else{
	        $("#layer_msg_content"+modalNo).empty();
	    	$("#layer_msg_content"+modalNo).html(content);        
	    }
		
	    var modal = $("#layer_msg"+modalNo).modal({
	        backdrop: 'static'
	        //keyboard: false
	    });
		
	   
	    
	    //모달 띄우고 센터 정렬
		centerModals(modal);
	}

	
</script>

<c:if test="${not empty layerMsg}">

	<c:choose>
		<c:when test="${not empty errors}">
			<script>
				$(document).ready(function() {
					popLayerMsg("${errors.detail}");
				});
			</script>
		</c:when>

		<c:otherwise>
			<script>
				$(document).ready(function() {
					popLayerMsg("${layerMsg}");
				});
			</script>
		</c:otherwise>
	</c:choose>

</c:if>