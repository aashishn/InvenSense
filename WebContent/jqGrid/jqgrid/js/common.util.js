<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.js"></script>
<script type="text/javascript">
	$(function() {
		customerAlert();
		$('#A0.R0.Primary Market').mouseout(function() {
			customerAlert();
		});
	});
	
	function customerAlert() {
	    var td = document.getElementById("A0.R0.Primary Market");
    	if (td.innerHTML == "Tier 1") {
    		$('#FormTitleBar tr').each(function() {
    			$('<td style="color:#FF0000;font-size: 14px;">Alert Customer Type is Tier 1</td>', this).insertBefore($('td:nth-child(8)', this));
    			});
    	} 		
	}
	
</script>	

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.js"></script>
<script type="text/javascript">
	$(function() {
		var tr = document.getElementById("-1192321940.2");
		tds = tr.getElementsByTagName("td");
		tds[0].innerHTML =  tds[0].innerHTML + " <a style='color:blue' href='javascript:void();' onclick='populateTime()'>Current Time</a>" ;
		
	});
	
	function populateTime() {
		  var curDate=new Date();
		  var currentTime = new Date()
		  var hours = currentTime.getHours()
		  var minutes = currentTime.getMinutes()

		  var suffix = "AM";
		  if (hours >= 12) {
		  suffix = "PM";
		  hours = hours - 12;
		  }
		  if (hours == 0) {
		  hours = 12;
		  }
		  var dateTime = (curDate.getMonth()+1) + "/" + curDate.getDate() + "/" + curDate.getFullYear() + " " + hours + ":" + minutes + " " + suffix  ;
		  var td = document.getElementById("A0.R0.ZDate_0");
		td.innerHTML = dateTime ;
	}
</script>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.js"></script>
<script type="text/javascript">
	$(function() {
		 var pathname = window.location.pathname;
		 if (pathname.indexOf("CustomObj1Edit") > 0 || pathname.indexOf("CustomObj1Insert") > 0) {
			 var inputField = "CustObj1EditForm.ZDate_0";
			 var span = $('span:contains("1st Call Start Time")');
			 span.append(" <a style='color:blue' href='javascript:void();' onclick='populateCurrentTime(\"" + inputField+ "\")'>Current Time</a>");
		 } 
	});
	
	function populateCurrentTime(inputField) {
		  var curDate=new Date();
		  var currentTime = new Date();
		  var hours = currentTime.getHours();
		  var minutes = currentTime.getMinutes();

		  var suffix = "AM";
		  if (hours >= 12) {
		  suffix = "PM";
		  hours = hours - 12;
		  }
		  if (hours == 0) {
		  hours = 12;
		  }
		  var dateTime = (curDate.getMonth()+1) + "/" + curDate.getDate() + "/" + curDate.getFullYear() + " " + hours + ":" + minutes + " " + suffix  ;
		  document.getElementById(inputField).value = dateTime ;
	}
</script>

// Insert button in the title bar  

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.7/jquery-ui.js"></script>
<link media="screen" rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" />
<script type="text/javascript">
	$(function() {
		insertButton();
	});
	
	function insertButton() {
    		$('#FormTitleBar tr').each(function() {
    			$('<td><table style="border-collapse: collapse;"><tr><td onmouseover="toggleNavButton(this);" onmouseout="toggleNavButton(this);" class="buttonTD" onclick="showMap();" onkeypress="onButtonPress(this);" style="background-image: url(&quot;https://secure-ausomxdsa.crmondemand.com/OnDemand/1.12.0.1220.0.03/base/themes_template/images/img_gradient_btn2.gif&quot;); color: rgb(0, 0, 0); border-color: rgb(153, 153, 153);">&nbsp;Google Map&nbsp;</td></tr></table></td>', this).insertBefore($('td:nth-child(8)', this));
    			});
	}
	
	function showMap() {
		var td = document.getElementById("A0.R0.ZLink_1");
		var a = td.getElementsByTagName("a");
		$(a[0]).trigger("click");
	}
	
	$(function() {
		var td = document.getElementById("A0.R0.ZLink_1");
		var a = td.getElementsByTagName("a");
		var href= a[0].href ;
		$.fx.speeds._default = 1000;
		//a[0].id="A0.R0.ZLink_1" +"_href";
		$(a[0]).click(function(e) {
			e.preventDefault();
			var $this = $(this);
			var horizontalPadding = 0;
			if(!$.browser.msie) {
				horizontalPadding = 15;
			}
			var verticalPadding = 30;
	        $('<iframe id="dialogbox" frameBorder="0" style="width:840;" class="externalSite" src="' + href + '" />').dialog({
	            title: ($this.attr('title')) ? $this.attr('title') : 'Serene Overlay div',
	            autoOpen: true,
	            width: 840,
	            height: 500,
	            modal: true,
	            resizable: true,
				autoResize: false,
				hide: 'drop',
	            overlay: {
	                opacity: 0.5,
	                background: "black"
	            }
	        }).width(840 - horizontalPadding).height(500 - verticalPadding);				
			return false;
			});
	});
</script>


// display dialog box on home page 

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.7/jquery-ui.js"></script>
<link media="screen" rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" />
<script type="text/javascript">
	$(function() {
		var rows = searchContain();
		alert(rows);
	});
	
	function searchContain() {
		var div = $("TaskList HomeDiv");
		var table = $("table",div);
		var rows = $('TaskList HomeDiv table tr:contains('  + '63' + ')'); 
		return rows ;
	}
	

</script>

// override js function 
var org_doNamedInvoke = window.doNamedInvoke ;

window.doNamedInvoke = function (name,obj) {
	if (name == 'MarkAsCompleted') {
		// call ws 
	} else {
		org_doNamedInvoke(name,obj);
	}
};


// insert button in child title bar and update parent
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.7/jquery-ui.js"></script>
<link media="screen" rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" />
<script type="text/javascript">
var accountId = "%%%Row_Id%%%";
var ssoTaken = "%%%SSO Token%%%";

$(function() {
	insertRevenueButton();
});

function insertRevenueButton() {
		var tr = $("#OptyChildList_Title").parent() ;
		$(tr).each(function() {
			$('<td><table style="border-collapse: collapse;"><tbody><tr><td class="buttonChildTitleBarTD" onmouseover="toggleNavButton(this);" onkeypress="onButtonPress(this);" onmouseout="toggleNavButton(this);" onclick="updateRevenue">&nbsp;Update Revenue&nbsp;</td></tr></tbody></table></td>', this).insertBefore($('td:nth-child(4)', this));
		}); 
}

</script>