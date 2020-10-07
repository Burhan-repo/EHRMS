
function generate() {
		var noticeId = $("#noticeId").val();
		console.log(noticeId);
//		var random = 'html';
		var xhr = new XMLHttpRequest();
	    xhr.open('GET', '/pdf/'+noticeId, true);
	    xhr.responseType = 'arraybuffer';
	    xhr.onload = function(e) {
	       if (this.status == 200) {
	          var blob=new Blob([this.response], {type:"application/pdf"});
	          var link=document.createElement('a');
	          link.href=window.URL.createObjectURL(blob);
	          document.getElementById("embedPDF").data=link.href;
//	          link.download="Report_"+new Date()+".pdf";
//	          link.click();
	       }
	    };
	    xhr.send();		
}



	function FileTypeValidate() {
		
		
		var noticeId = ($('#noticeId').val());
		
		
		 console.log('hello');
	    var fileUpload = $('#FileUpload1').val();
	    
	    var totalvacancy = parseInt($('#totalvacancy').val());
	    var casteTotal = 0;
	    
	    var ss = $('#castvacancy0').val();
	    
	    for(var i=0;i<noticeObj.category.length;i++) {
	    	if(noticeObj.category[i].vacancy != 0) {
 	    		var name = '#castvacancy'+i;
	    		casteTotal += parseInt($(name).val());
	    	}
	    }
	
	    var extension = fileUpload.substring(fileUpload.lastIndexOf('.'));
	    var ValidFileType = ".pdf";
		var fileTypeCheck = false;
		var vacancycheck = false;
		
		if(totalvacancy == (casteTotal)) {
			vacancycheck = true;
		} else {
			alert("Please check total number of vacancy should combination of sc,st,obc and general");
		}

		if(noticeObj != undefined && noticeObj != null 
		&& noticeObj.filename != null && noticeObj.filename.length>0) {
			fileTypeCheck = true;
		}


	    if (fileUpload.length > 0) {
	
	        if (ValidFileType.toLowerCase().indexOf(extension) < 0) {
	            alert("please select valid file type...(PDF)");
	        }
	        else {
				fileTypeCheck = true;
	        }
	    } else if(noticeObj == undefined || noticeObj == null || noticeObj.filename  == null || noticeObj.filename.length == 0) {
	        alert("please select file for upload...");
	        $('#FileUpload1').focus();
	    }
	    console.log(vacancycheck+""+fileTypeCheck);
		if(vacancycheck && fileTypeCheck) {
			return true;
		}
		return false; 
	}
	
	function FileTypeValidateExcel(sender) {
				
		console.log('helloExcel');
	    var fileUpload = sender.value;
	    
	    var extension = fileUpload.substring(fileUpload.lastIndexOf('.'));
	    var ValidFileType = new Array(".csv",".xls",".xlsx");
		var fileTypeCheck = false;
		

	    if (fileUpload.length > 0) {
	
	        if (ValidFileType.indexOf(extension) < 0) {
	            alert("please select valid file type...(Excel)");
	        }
	        else {
				fileTypeCheck = true;
	        }
	    }
	    else {
	        alert("please select file for upload...");
	    }
		if(fileTypeCheck) {
			$('#uploadMPSC').prop('disabled',false);		
			return true;
		}
		$('#uploadMPSC').prop('disabled',true);				
		return false; 
	}
	
	$(function(){
		$('#language_selection').change(function(){
    		var selections = $("#language_selection :selected");
        var html = '';
        $.each(selections,function(i,item){
        	html += $(item).text()+':<input type="file" name="'+$(item).val()+'" accept=".pdf" id="'+$(item).val()+'" /><br>';
        })
        $('#divFileInput').html(html);
    })

})


	function downloadCandiDoc(id,fileName) {
	
		var xhr = new XMLHttpRequest();
	    xhr.open('GET', '/downloadDoc/'+id+'/'+fileName, true);
	    xhr.responseType = 'arraybuffer';
	    xhr.onload = function(e) {
	       if (this.status == 200) {
	          var blob=new Blob([this.response], {type:"application/pdf"});
	          var link=document.createElement('a');
	          link.href=window.URL.createObjectURL(blob);
	          link.download=fileName+".pdf";
	          link.click();
	       }
	    };
	    xhr.send();		
	}
	
		function sendReminder(id,fileName) {
	
		var xhr = new XMLHttpRequest();
	    xhr.open('GET', '/sendReminder/'+id+'/'+fileName, true);
	    xhr.responseType = 'arraybuffer';
	    xhr.onload = function(e) {
	       if (this.status == 200) {

	       }
	    };
	    xhr.send();		
	}
	
	
	function changeValue(casteCode,index) {
		console.log('change event');
		var ind = 0;
		for(var i=0;i<noticeObj.category.length;i++) {
			if(noticeObj.category[i].category_Code == casteCode) {
				ind = i;
				break;
			}			
		}
		var currentvalue = parseInt($('#castvacancy'+index).val());
//		var oldvalue = parseInt($('#castvacancy'+ind).val());
		var oldvalue = parseInt(noticeObj.category[ind].vacancy);
		if(oldvalue>currentvalue || oldvalue== currentvalue) {
			document.getElementById('castvacancy'+ind).value = oldvalue - currentvalue;
		} 
	}
	
	function candidateValid() {
		var id = $('#docId').val();
		if(id != '0' && id.length>0) {
			return true;
		} else {
			alert('Please Input Doc Id');
			$('#docId').focus();
		}
		return false; 
	}
$(document).ready(function() {	
		$('#editor').summernote({
		  height: 100,                 // set editor height
		  minHeight: null,             // set minimum height of editor
		  maxHeight: null,             // set maximum height of editor
		  focus: false,                  // set focus to editable area after initializing summernote
		  toolbar: [
			  ['style', ['style']],
			  ['font', ['bold', 'underline', 'clear']],
			  ['fontname', ['fontname']],
			  ['color', ['color']],
			  ['para', ['ul', 'ol', 'paragraph']],
			  ['table', ['table']]
			],
		  tabDisable: false,
		  codeviewFilter: false,
		  codeviewIframeFilter: true		  
		});
	});
	
	function getPDFFF() {
	
		var xhr = new XMLHttpRequest();
	    xhr.open('GET', '/getPDFFF', true);
	    xhr.responseType = 'arraybuffer';
	    xhr.onload = function(e) {
	       if (this.status == 200) {
	          var blob=new Blob([this.response], {type:"application/pdf"});
	          var link=document.createElement('a');
	          link.href=window.URL.createObjectURL(blob);
	          link.download="xyz.pdf";
	          link.click();
	       }
	    };
	    xhr.send();		
	}
$('form').attr('autocomplete', 'off');

	function FileTypeValidatePDF(sender) {
				
	    var fileUpload = sender.value;
	    
	    var extension = fileUpload.substring(fileUpload.lastIndexOf('.'));
	    var ValidFileType = new Array(".pdf");
		var fileTypeCheck = false;
		

	    if (fileUpload.length > 0) {
	
	        if (ValidFileType.indexOf(extension) < 0) {
	            alert("please select valid file type...(PDF)");
	        }
	        else {
				fileTypeCheck = true;
	        }
	    }
	    else {
	        alert("please select file for upload...");
	    }
		if(fileTypeCheck) {
			return true;
		}
		return false; 
	}
		