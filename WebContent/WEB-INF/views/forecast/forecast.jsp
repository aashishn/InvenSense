<%@page import="java.util.LinkedHashMap"%>
<%@page import="net.sf.json.JSONFunction"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.invensense.util.CommonUtil" %>

<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" isELIgnored="false"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="jqGrid/jqgrid2/js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="jqGrid/jqgrid2/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="jqGrid/jqgrid2/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="jqGrid/jqgrid2/js/jquery.jqGrid.src.js"></script>
<script type="text/javascript" src="jqGrid/jqgrid2/js/autoNumeric.js"></script>
<link media="screen" rel="stylesheet" href="jqGrid/jqgrid2/css/jquery-ui-1.10.3.custom.min.css" />
<!-- <link media="screen" rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/redmond/jquery-ui.css" /> -->
<link media="screen" rel="stylesheet" href="jqGrid/jqgrid2/css/ui.jqgrid.css" />

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>InvenSense Forecasting</title>
<style type="text/css">
.ui-dialog{font-size: medium; }
div.ui-dialog div.ui-dialog-titlebar {padding: 0.3em 0.2em 0.2em 0.3em;}
.prior_year {
	background-color: #DFEFFC;
}
.prior_month {
	background-color: #E6E6E6;
}
.quarter_column {
	background-color: #E6E6E6;
}
.revenue {
	background-color: #E6E6E6;
}
    .ui-jqgrid tr.jqgrow td {
        white-space: normal !important;
    }
    td input {
    	color:#5D5252;
    	font-style: normal;
    	font-family: serif;
    }
    
    .no_border{
    	border-bottom:none !important;
    	border-top:none !important; 
    }
    .ui-jqgrid-ftable tr {
		height: 30px;
		font-size:1.1em;
		color: #FFFFFF;
    }
    .ui-jqgrid-ftable tr td { 
/* 		background-color: #00FFFF !important; */
		background-color: #5CA7CD !important; 
     } 
    td.deletebutton {
    }
    
    .pretty-hover {
 		cursor: pointer;
 		cursor: hand;
	}
 .dropDownTable .ui-button{ 
     font-size: 15px; 
 } 
 .dropDownTable .ui-button-text-only .ui-button-text { 
     padding: .1em .6em .2em; 
 } 
.createForecastHeader{
 text-align: left;
}
.dropDownTable td,th{
	padding:0px 11px 0px 11px;
	font-family: Lucida Grande,Lucida Sans,Arial,sans-serif;
 	font-size:small;
 	color: #2E6E9E;
}
</style>
</head>
<body>
	<div id="createForecastDiv">
	<table id="createForecastTable" class="dropDownTable">
	<thead class="createForecastHeader">
	<th>
		Customer
	</th>
	<th>
		Market
	</th>
		<th>
		Sub Market
	</th>
		<th>
		Program
	</th>
		<th>
		BU
	</th>
			<th>
		Part #
	</th>
	<th>
	</th>
	</thead>	
	<tbody class="createForecastBody">
	<tr>
	<td>
	<select id="accountName" name="customerId">
	<option value="select">--Select--</option>
		<c:forEach items="${accountList}" var="account">
			<option value="${account.id}">${account.name}</option>
		</c:forEach>
	</select>
	</td>
			<td>
	<select id="marketDropdown" name="marketSegment">
	<option value="select">--Select--</option>
		<c:forEach items="${marketList}" var="market">
			<option value="${market.lookupValue}">${market.lookupValue}</option>
		</c:forEach>
	</select>
	</td>
			<td>
	<select id="subMarketDropdown" name="subMarket">
	<option value="select">--Select--</option>
		<c:forEach items="${subMarketList}" var="subMarket">
			<option value="${subMarket.lookupValue}">${subMarket.lookupValue}</option>
		</c:forEach>
	</select>
	</td>
			<td>
		<input type="text" id="program" name="program" style="font-family: Lucida Grande,Lucida Sans,Arial,sans-serif;font-size:small;" maxlength="100"></input>
	</td>
				<td>
	<select id="buDropdown" name="bu">
	<option value="select">--Select--</option>
		<c:forEach items="${buList}" var="bu">
			<option value="${bu.lookupValue}">${bu.lookupValue}</option>
		</c:forEach>
	</select>
	</td>
		<td>
	<select id="partNumber" name="productId">
	<option value="select">--Select--</option>
		<c:forEach items="${productList}" var="product">
			<option value="${product.id}">${product.productName}</option>
		</c:forEach>
	</select>
	</td>
			<td>
	<div id='addForecast' style='text-align:right;'><button type='submit'>Create</button></div>
	</td>
	</tr>
	</tbody>
	</table>
	</div>
	<div id="validationMessageDiv" style="padding:15px 20px 20px 20px;font-family: Lucida Grande,Lucida Sans,Arial,sans-serif;font-size:small;color:red;display:none;">
		<span>Fields marked in RED are mandatory. Please fill the required fields.</span>
	</div>
	<div id="messageDiv" style="padding:15px 20px 20px 20px;font-family: Lucida Grande,Lucida Sans,Arial,sans-serif;font-size:small;color:red;">
	<span></span>
	</div>
	<div id="salesRepDiv" style="padding:1px 0px 4px 0px;">
	<table id="salesRepDropDown" class="dropDownTable">	
	<tr>
	<td>
	<b>Sales Rep : </b><select id="salesRepName" name="salesRepName" onchange="refresh();">
		<c:forEach items="${salesRepList}" var="user">
			<option value="${user.id}">${user.breadcrumName}</option>
		</c:forEach>
	</select>
	</td>
	<td>
		<button id="createfinanceforecast" style="display:none;">Create Finance Forecast</button>
		<button id="viewfinanceforecast" style="display:none;">View Finance Forecast</button>
	</td>
	</tr>
	</table>
	</div>
	<table id="rowed3"></table> 
	<div id="locdiv" style="display:none; font-size: medium; font-family: Lucida Grande,Lucida Sans,Arial,sans-serif;">
		<div id="success_msg" style="margin-top:20px; font-size: small; text-align: center; color: black; font-weight: bold; display:none;"></div>
		<div id="error_msg" style="margin-top:20px; font-size: small; text-align: center; color: black; font-weight: bold; display:none;"></div>
		<div id="lockmonth" style="color:#2E6E9E; margin-top:20px; font-weight:bold; font-size: small;" align="center"></div>
		<div id="lockyear"></div>
		<div id="buttons" align="center" style="margin-top:10px; font-size: 11px;">
			<button id="savelock">Lock</button>
			<button id="okdone" style="display:none;">Ok</button>
			<button id="cancellock">Cancel</button>
		</div>
	</div>
	<div id="deleteConfirmationDialog" title="Confirmation Required" style="display:none;"> Are you sure about this? </div>		
	<div id="pager2"></div>
</body>
<script type="text/javascript">
var quantityLabel = "Quantity (U)";
var aspLabel = "ASP ($/U)";
var revenueLabel = "Forecast ($)";
var customerTotalLabel = "Customer Total ($)";
var noColumnsUptoTotal = 26; // Total number of columns upto the 'Total' column.
var dataColIndex = 7; // Column Index for the Data column
var monthColsStartIndex = 9;
//  10-01-2013 Fix for forecast not showing correctly in January
//var currentYear = (new Date).getFullYear()+1;
var currentYear = getCurrentFiscalYear();

var forecastYear = currentYear;
var currentYearLabel = "FY "+ currentYear;
var nextYearLabel =  "FY "+ (currentYear+1);
var previousYearLabel =  "FY "+ (currentYear-1);
var curMonth = "${currentMonth}";
var cMonth = "${currentMth}";
var sny = "${sny}" ; // sny="P"=>Previous-Year  sny="N"=>Current-Year  sny="Y"=>Next-Year
var vpRole = "${vpRole}" ;
var loginSalesRepId = "${param.salesRepId}" ;
var salesRepId = "${param.salesRepId}" ;
$("#salesRepName").val("${param.salesRepId}");
salesRepId = $("#salesRepName").val();
var parentSRN = "${parentSalesRepName}";
var canLockSalesForecast = "${canLockSalesForecast}";
var showButton = "${showButton}"; 
//alert(showButton);

var lockedMonths;
var sortByDate = "Y";
$("button, input:submit", "#addForecast" ).button();
var gUrl = 'getForecastData?salesRepId='+ salesRepId + '&sny='+sny+"&parentSalesRepName=" +'${parentSalesRepName}'+"&request_locale=en"+"&sortByDate="+sortByDate;
jQuery("#rowed3").jqGrid({
			url : gUrl,
			datatype : "json", 
			colNames : ${forecast.columns},
			colModel : ${forecast.columnsDef},
			rowNum : 12,
			rowList: [12, 24, 36, 48, 60],
			pager : '#pager2',
			toolbarfilter : true,
			multiselect: true,
			beforeProcessing : function(data, status, xhr){
				lockedMonths = data.lockedMonths;
				prevLockedMonths = data.prevLockedMonths;
			},
			afterInsertRow : function(rowid,rowdata,rowelem) {
// 				console.log("row inserted sny="+sny+" rowelem="+rowelem);
				var row = $( "#" + rowid);

				// Make locked forecast months non-editable
				if(sny=="N"){
					for(i=0; i<lockedMonths.length; i++){
						$(this).setCell(rowid,lockedMonths[i].substring(0,3),'','prior_month not-editable-cell');	
					}
				}	
				
				// Make locked forecast previous months non-editable
				if(sny=="P"){
					for(i=0; i<prevLockedMonths.length; i++){
						$(this).setCell(rowid,prevLockedMonths[i].substring(0,3),'','prior_month not-editable-cell');	
					}
				}	
								
				var isCustomerTotal = jQuery.inArray(customerTotalLabel,rowelem) >= 0 ;
				var isRevenue =  jQuery.inArray(revenueLabel,rowelem) >= 0 ;
				
				// Make all months non-editable for previous year, Revenue Row and Customer Total Row
				//if (isCustomerTotal || isRevenue || sny=="P") {
				if (isCustomerTotal || isRevenue) {
					if(isCustomerTotal){
						$(row).removeClass("ui-widget-content");
						$(row).addClass("ui-state-default");	
					}
					var i = 1;
					$("#" + rowid +" td").each(function() {
					    $(this).addClass("not-editable-cell");
					    if(i>9){
					    	if(sny=="P"){
					    		//$(this).addClass("prior_month");
					    	}
							if(isCustomerTotal || isRevenue ){
					    		$(this).addClass("revenue");
					    	}
					    }
					    i = i+1;
					});
				}
				
				//Disable checkbox
				if(sny=="P"){
					$("#" + rowid +" td:first").find(":input").hide();//css("display","none");
				}else{
					var isQuantity = jQuery.inArray(quantityLabel,rowelem) >= 0; 
					if(!isQuantity){
						console.log("is not a qty");
						$("#" + rowid +" td:first").find(":input").hide();//css("display","none");
					}else{
						console.log("is a qty");
					}
				}
			},
			afterEditCell : function(rowid, cellname, value, iRow, iCol) {
// 				console.log("in after edit cell. cellname="+cellname+" iRow="+iRow);			
				var inputElemId = iRow+"_"+cellname;
				var dataType = jQuery("#rowed3").jqGrid('getCell',rowid,"Data");
				if(dataType==quantityLabel) {
					$("#"+inputElemId).autoNumeric('update',{mDec: '0'});
				}
				if(value == 0) {
					$("#"+inputElemId).val('');
				}
			},
			beforeSaveCell : function(rowid,name,val,iRow,iCol) {
// 				console.log('in beforeSaveCell val='+val+' name='+name+" iRow="+iRow);
				if(val==null || val==""){
					val = "0";
				}else{
					val = replaceComma(val)+"";
				}
				return val;
			},
			afterSaveCell : function(rowid,name,val,iRow,iCol) {
// 				console.log('in after save cell. name='+name+' iRow='+iRow);
				val = replaceComma(val)+"";
				reComputeQuarterTotal(rowid,name,val,iCol);
				reComputeTotal(rowid);
				reComputeGrandTotal(rowid,name,val,iCol);
				$("#save button").attr("disabled",false);				
			},	
			onSortCol : function(index,iCol,sortorder) {
				saveEditedForecastData();
				$("#"+"jqgh_rowed3_"+index+" .s-ico").attr('style','');	
				sortByDate = "N";
				gUrl = 'getForecastData?salesRepId='+ salesRepId + '&sny='+sny+"&parentSalesRepName=" +'${parentSalesRepName}'+"&request_locale=en"+"&sortByDate="+sortByDate;
				$("#rowed3").jqGrid('setGridParam',{url:gUrl});				
			},
			onPaging : function(pgButton) {
	           saveEditedForecastData();
			},
			onSelectRow: function (id, status) {
				//alert('row selected + status='+status);
				var selRowIds = jQuery('#rowed3').jqGrid('getGridParam', 'selarrrow');
				//alert('selRowIds.length='+selRowIds.length);
				if(selRowIds.length<=0){
					$("#delete button").attr("disabled",true);	
				}else{
					$("#delete button").attr("disabled",false);	
				}
			},
			loadComplete: function () {
				$('#cb_rowed3').hide();
			},			
			cellEdit:true,
			autowidth: true,
			shrinkToFit:false,
			forceFit : true,
		    footerrow: true,
		    userDataOnFooter: true,
			cellsubmit:"clientArray"
});
	jQuery("#rowed3").jqGrid('navGrid','#pager2', {edit : false,add : false,del : false,search:false});
	jQuery("#rowed3").filterToolbar({searchOnEnter:false});
	$("#rowed3").setGridHeight("70%");
	$("#rowed3").setCaption("<div>&nbsp;&nbsp;&nbsp;Invensense Forecasting - Fiscal Year: ${forecestYear}</div>");
	
	$(".ui-jqgrid-title").before("<div id='save' style='text-align:right;float:left;'><button type='submit'>Save</button></div>");
	$(".ui-jqgrid-title").before("<div id='delete' style='text-align:right;float:left;'><button type='submit'>Delete</button></div>");
	
	if(canLockSalesForecast=='true'){
		$(".ui-jqgrid-title").before("<div id='lock' style='text-align:right;float:left;margin-left:10px;'><button type='submit'>Lock</button></div>");
		if(showButton=='CREATE')
			$("#createfinanceforecast" ).show();
		else if(showButton=='VIEW')
			$("#viewfinanceforecast" ).show();
	}
	else{
		$("#createfinanceforecast" ).hide();
		$("#viewfinanceforecast" ).hide();
	}
	$(".ui-jqgrid-title").after("<div id='buttonsDiv' style='text-align:center;'></div>");
	
	$(".ui-jqgrid-title").after("<div id='nextyear' style='text-align:right;'><button style='position:absolute;left:55%;' type='submit'>"+nextYearLabel+"</button></div>");
	$(".ui-jqgrid-title").after("<div id='currentyear' style='text-align:right;'><button style='position:absolute;left:49%;' type='submit'>"+currentYearLabel+"</button></div>");
	$(".ui-jqgrid-title").after("<div id='previousyear' style='text-align:right;'><button style='position:absolute;left:43%;' type='submit'>"+previousYearLabel+"</button></div>");
	
	$(".ui-jqgrid-title").css("font-size","medium");

	$('#nextyear').click(function() {
		if(saveEditedForecastData()){
			sny="Y";
			gUrl = 'getForecastData?salesRepId='+ salesRepId + '&sny='+sny+"&parentSalesRepName=" +'${parentSalesRepName}'+"&request_locale=en"+"&sortByDate="+sortByDate;
			$("#rowed3").setGridParam({url:gUrl});
			$("#rowed3")[0].triggerToolbar();
			$("#rowed3").setCaption("<div>&nbsp;&nbsp;&nbsp;Invensense Forecasting - Fiscal Year: "+ (currentYear+1) +"</div>");
			$('#nextyear button').attr("disabled",true);
			$('#currentyear button').attr("disabled",false);
			$('#previousyear button').attr("disabled",false);
			
			$('#lock').hide();
			$('#delete').attr("disabled",false);
		}
	});

	$('#previousyear').click(function() {
		if(saveEditedForecastData()){
			sny="P";
			gUrl = 'getForecastData?salesRepId='+ salesRepId + '&sny='+sny+"&parentSalesRepName=" +'${parentSalesRepName}'+"&request_locale=en"+"&sortByDate="+sortByDate;		
			$("#rowed3").setGridParam({url:gUrl});
			$("#rowed3")[0].triggerToolbar();	
			$("#rowed3").setCaption("<div>&nbsp;&nbsp;&nbsp;Invensense Forecasting - Fiscal Year: "+ (currentYear-1) +"</div>");
			$('#previousyear button').attr("disabled",true);
			$('#currentyear button').attr("disabled",false);
			$('#nextyear button').attr("disabled",false);
			
			$('#lock').hide();
			$('#delete').attr("disabled",true);
		}
	});

	$('#currentyear').click(function() {
		if(saveEditedForecastData()){
			sny="N";
			gUrl = 'getForecastData?salesRepId='+ salesRepId + '&sny='+sny+"&parentSalesRepName=" +'${parentSalesRepName}'+"&request_locale=en"+"&sortByDate="+sortByDate;
			$("#rowed3").setGridParam({url:gUrl});
			$("#rowed3")[0].triggerToolbar();	
			$("#rowed3").setCaption("<div>&nbsp;&nbsp;&nbsp;Invensense Forecasting - Fiscal Year: "+ (currentYear) +"</div>");
			$('#currentyear button').attr("disabled",true);
			$('#previousyear button').attr("disabled",false);
			$('#nextyear button').attr("disabled",false);
			
			$('#lock').show();
			$('#delete').attr("disabled",false);
		}
	});
	
	$("#createfinanceforecast" ).button();
	$("#viewfinanceforecast" ).button();
	$("#savelock" ).button();
	$("#okdone" ).button();
	$("#cancellock" ).button();
	
	$('#cancellock').click(function() {
		$('#locdiv').dialog('destroy');
	});
	$('#okdone').click(function() {
		$('#success_msg').html("");
		$('#success_msg').hide();
		$('#lockmonth').html("");
		$('#lockmonth').hide();
		$('#locdiv').dialog('destroy');
		refresh();
	});

	$('#savelock').click(function() {
		//$('#locdiv').dialog('destroy');
		
		$('#savelock').attr("disabled",true);
		$("#error_msg").fadeOut("slow");
		$("#success_msg").fadeOut("slow");
		
		var d = new Date();
		var currentMonth = d.getMonth();
		var currentYear = d.getFullYear();
		if(currentMonth==0 || currentMonth==1 || currentMonth==2)
			var fiscalYear = d.getFullYear();
		else
			var fiscalYear = d.getFullYear()+1;
		
		var selMonth = $("#lockmonths").val();
		var displaylockmonth = $("#displaylockmonth").val();
		//alert(displaylockmonth);
		/*
		$('#lockmonth input[type=checkbox]').each(function () {
			if(this.checked)
				selMonth = selMonth + "," + ($(this).val());
		});*/
		//alert(selMonth);
		if(selMonth==""){
			$('#lockmonth').html("");
			$("#error_msg").html("All the previous months of FY "+fiscalYear+" are already locked. You can access the historical data from Reports.");
			$("#error_msg").show();
			//setTimeout(function(){$("#error_msg").fadeOut("slow"); $('#locdiv').dialog('destroy');},5000);
			//$('#savelock').attr("disabled",false);
			$("#savelock").hide();
			//$("#okdone").show();
			$('#cancellock').html('<span class="ui-button-text">Ok</span>');
			return;
		}
		
		//var searchurl = "insertForecastLockInfo.action?salesRepId="+salesRepId+"&selectedMonth="+selMonth+"&year="+fiscalYear;
		var searchurl = "insertForecastLockInfo.action?salesRepId="+salesRepId+"&selectedMonth="+selMonth;
		//alert(searchurl);
		$.ajax({
				url:searchurl,
				cache:false,
				success:function(data) {
					//alert(data);
					if(data=="error"){
						//$('#lockmonth').html("");
						$( "#locdiv" ).dialog( "option", "height", 230 );
						$('#error_msg').html("There was some error Processing your request. Please try again after some time!<hr>");
						$("#error_msg").show();
						setTimeout(function(){$("#error_msg").fadeOut("slow");},5000);
						$('#savelock').attr("disabled",false);
					}
					else if (data=="success"){
						$('#lockmonth').html("");
						$('#success_msg').html("Sales Forecast data for Month(s) "+displaylockmonth.substring(0, displaylockmonth.length - 2)+" are locked successfully !!");
						$("#success_msg").show();
						$("#savelock").hide();
						$("#okdone").show();
						$("#cancellock").hide();
						$("#createfinanceforecast").show();
						//setTimeout(function(){$("#success_msg").fadeOut("slow"); $('#locdiv').dialog('destroy');},5000);
						//refresh();
						//$("#messageDiv span").hide().html("New Forecast record created.").fadeIn('fast').delay(1200).fadeOut('fast');
					}
					else{
						//$('#error_msg').html("The Forecast for the selected month "+data+" has already been locked!<hr>");
						$('#lockmonth').html("");
						var prevYear =  parseInt(fiscalYear)-1;
						$("#error_msg").html("All the previous months of FY "+fiscalYear+" and FY "+prevYear+" are already locked.");
						$("#error_msg").show();
						$("#savelock").hide();
						//$("#okdone").show();
						$('#cancellock').html('<span class="ui-button-text">Ok</span>');
						//setTimeout(function(){$("#error_msg").fadeOut("slow"); $('#locdiv').dialog('destroy');},5000);
						//$('#savelock').attr("disabled",false);
					}
					//
				},
				error:function(xhr, status, error){
					var err = eval("(" + xhr.responseText + ")");
					//alert(err);
					$( "#locdiv" ).dialog( "option", "height", 230 );
					$('#error_msg').html("There was some error Processing your request. Please try again after some time!<hr>");
					$("#error_msg").show();
					setTimeout(function(){$("#error_msg").fadeOut("slow");},5000);
					$('#savelock').attr("disabled",false);
				}
	 	});
	});
	
	$('#lock').click(function() {
		//alert("Locked button clicked!");
		//alert($("#rowed3").jqGrid('getDataIDs'));
		var d = new Date();
		var currentMonth = d.getMonth();
		var currentYear = d.getFullYear();
		if(currentMonth==0 || currentMonth==1 || currentMonth==2)
			var fiscalYear = d.getFullYear();
		else
			var fiscalYear = d.getFullYear()+1;
		var prevYear = parseInt(fiscalYear)-1;
		
		$('#savelock').attr("disabled",false);
		$("#error_msg").html("");
		
		var status = "";
		var details = "";
		var searchurl = "getForcastYearsandLockInfo.action?currentYear="+fiscalYear+"&prevYear="+prevYear;
		//alert(searchurl);
		$.ajax({
				url:searchurl,
				cache:false,
				async: false,
				success:function(data) {
					//alert(data);
					if(data!=""){
						dataSplit = data.split("::");
						status = dataSplit[0];
						details = dataSplit[1];
						//alert(details);
					}
				},
				error:function(xhr, status, error){
					var err = eval("(" + xhr.responseText + ")");
					
				}
	 	});
		
		
		if(status=="" || details==""){
			var showText = "";
			var prevYear =  parseInt(fiscalYear)-1;
			$("#error_msg").html("There was some error Processing your request. Please try again after some time!");
			$("#error_msg").show();
			$("#savelock").hide();
			$('#cancellock').html('<span class="ui-button-text">Ok</span>');
		}
		else if(status=="ERROR" && details=="NoForecastData"){
			var showText = "";
			var prevYear =  parseInt(fiscalYear)-1;
			$("#error_msg").html("There are no Forecast data to Lock. You can access the historical data from reports");
			$("#error_msg").show();
			$("#savelock").hide();
			$('#cancellock').html('<span class="ui-button-text">Ok</span>');
		}
		else if(status=="ERROR" && details=="PreviousMonthLocked"){
			var showText = "";
			var prevYear =  parseInt(fiscalYear)-1;
			$("#error_msg").html("All the previous months of FY "+fiscalYear+" and FY "+prevYear+" are already locked. You can access the historical data from reports.");
			$("#error_msg").show();
			$("#savelock").hide();
			$('#cancellock').html('<span class="ui-button-text">Ok</span>');
		}
		else if(status=="INFO" && details!=""){
			var detailsSplit = details.split("**");
			var lockmonth = detailsSplit[0];
			var displaylockmonth = detailsSplit[1];
			
			var showText = "Are you sure you want to lock Sales Forecast data for Month(s) : "+displaylockmonth.substring(0, displaylockmonth.length - 2)
				+"<br><input type='hidden' id='lockmonths' name='lockmonth' value='"+lockmonth+"'>"
				+"<br><input type='hidden' id='displaylockmonth' name='displaylockmonth' value='"+displaylockmonth+"'>";
				
			$('#lockmonth').show();
			$("#savelock").show();
			$('#cancellock').html('<span class="ui-button-text">Cancel</span>');
			$('#cancellock').show();
			$("#okdone").hide();
		}
		else{
			var showText = "";
			var prevYear =  parseInt(fiscalYear)-1;
			$("#error_msg").html("There was some error Processing your request. Please try again after some time!");
			$("#error_msg").show();
			$("#savelock").hide();
			$('#cancellock').html('<span class="ui-button-text">Ok</span>');
		}
		
		//alert(lockmonth);
		//$('#lockyear').html(currentYear);
		//alert(showText);
		$('#lockmonth').html(showText);
		$('#locdiv').dialog({ 
			title: "Lock Forecast",
			height: 200,
			width: 600,
		});
	});
	
	$("button, input:submit", "#save" ).button();
	$("button, input:submit", "#delete" ).button();
	$("button, input:submit", "#lock" ).button();
	$("button, input:submit", "#previousyear" ).button();
	$("button, input:submit", "#currentyear" ).button();
	$("button, input:submit", "#nextyear" ).button();
	$("#save button").attr("disabled","disabled");
	$("#delete button").attr("disabled",true);
	$('#currentyear button').attr("disabled",true);
	
	$('#hc').click( function() {
		$("#rowed3").hideCol(["Segment","SubSegment","CCT","CRI"]);
		$(".ui-jqgrid-ftable tr.footrow-ltr td:nth-child(4)").hide();
		$(".ui-jqgrid-ftable tr.footrow-ltr td:nth-child(5)").hide();
		$(".ui-jqgrid-ftable tr.footrow-ltr td:nth-child(6)").hide();
		$(".ui-jqgrid-ftable tr.footrow-ltr td:nth-child(7)").hide();
		}); 
	$('#sc').click( function() { 
		$("#rowed3").showCol(["Segment","SubSegment","CCT","CRI"]);
		$(".ui-jqgrid-ftable tr.footrow-ltr td:nth-child(4)").show();
		$(".ui-jqgrid-ftable tr.footrow-ltr td:nth-child(5)").show();
		$(".ui-jqgrid-ftable tr.footrow-ltr td:nth-child(6)").show();
		$(".ui-jqgrid-ftable tr.footrow-ltr td:nth-child(7)").show();
	});


	$('#save').click(function() {
		var error = false;
		var rids = $('#rowed3').jqGrid('getDataIDs');
		var columnNames = jQuery('#rowed3')[0].p.colNames;
		
		var input = $("td.edit-cell input:text");
		if (input.length > 0) {
			var cell = $("td.edit-cell");
			var cIndex = cell.parent("tr").children().index(cell);
			var id = input.attr("id");
			var n = id.split("_");
			var rowId = n[0];
			$("#rowed3").saveCell(rowId,cIndex);
		}
		var changeRows = $("#rowed3").getChangedCells('all');
		if (changeRows.length <= 0) {
			alert("No Changes found to save");
			return ;
		}

		if(validateForecastData(changeRows)){
			//alert("Error saving data. Quantity and ASP both are required.");
			$("#messageDiv span").hide().html("Error saving data. ASP is required if Quantity is not 0.").fadeIn('fast').delay(2400).fadeOut('fast');
			return;
		}
		
		for (row in changeRows) {		
			changeRows[row]['Market']=changeRows[row]['MarketH']; 
			changeRows[row]['SubMarket']=changeRows[row]['SubMarketH'];
			changeRows[row]['Program']=changeRows[row]['ProgramH']; 
			changeRows[row]['BU']=changeRows[row]['BUH'];

			var params = jQuery.param(changeRows[row]) +"&sny="+sny +"&request_locale=en";	
				$.ajax({
					  url: "saveForecast.action",
					  data:params,
					  context: document.body,
					  async:false,
					  type:"POST",
					  success: function(data,textStatus,jqXHR){
	//		 			  console.log('in success'+' jqXHR='+jqXHR+' textStatus='+textStatus+' data='+data);
					  },
					  error: function(jqXHR,textStatus,errorThrown){
	// 					  console.log('in error'+' jqXHR='+jqXHR+' textStatus='+textStatus+'errorThrown='+errorThrown);
	// 					  alert("Error occurred while saving Forecast.\n Error Details: "+errorThrown);					  
					  }
				});
		}
		$("#rowed3").trigger("reloadGrid");
		$('#save button').attr("disabled","disabled");
	});

	function validateForecastData(rows){
		//Check for quantity or asp 0 
		
		var error = false;
		var rids = $('#rowed3').jqGrid('getDataIDs');
		var columnNames = jQuery('#rowed3')[0].p.colNames;
		
		for(j in rows){			
			var correspondingDataRowIndex = rows[j]["Data"] == quantityLabel?(parseInt(rows[j].id)):(parseInt(rows[j].id) - 2);
			var correspondingDataRow = jQuery('#rowed3').jqGrid ('getRowData', rids[correspondingDataRowIndex]);		
			console.log("Edited row type ="+rows[j]["Data"])
			console.log("Edited row index= "+(parseInt(rows[j].id)-1)+" Corresponding data row index= "+correspondingDataRowIndex);
			for(var k=monthColsStartIndex;k<(noColumnsUptoTotal-1);k++) {
				var key = columnNames[k];
			  	if(key=="Q1" || key =="Q2" || key == "Q3" || key == "Q4"){
			  		continue;
				}
			  	var value1 = rows[j][key]!=""?rows[j][key]:"0";
			    var value2 = correspondingDataRow[key]!=""?correspondingDataRow[key]:"0";
			    console.log("key="+key+" value1="+value1 + " value2="+value2);
			    if(rows[j]["Data"]==quantityLabel && (value1!="0" && value2=="0")){
			    	error = true;
			    	break;
				}else if(rows[j]["Data"]==aspLabel && (value1=="0" && value2!="0")){
			    	error = true;
			    	break;
				}				   
			}
			if(error){
				break;
			}
		}
		return error;
	}	
	
	$('#delete').click(function() {
		$("#deleteConfirmationDialog").dialog({
	        buttons : {
	          "Confirm" : function() {    	  
	        	  console.log("confirmed");
	        	  deleteForecasts();
	        	  $(this).dialog("close");	
	          },
	          "Cancel" : function() {
	        	  console.log("closed");
	        	  $(this).dialog("close");	       
	          }
	        }
	      });
		console.log("calling dialog");
	    $("#deleteConfirmationDialog").dialog("open");
		console.log("dialog called");		
	});
	
	function deleteForecasts(){
		var selRowIds = $('#rowed3').jqGrid ('getGridParam', 'selarrrow');
		var rids = $('#rowed3').jqGrid('getDataIDs');
		var columnNames = jQuery('#rowed3')[0].p.colNames;
		var deletedRecordsArray = new Array();
		var result = false;
		//console.log("first col name ="+columnNames[0]);
			for(i in selRowIds){
				console.log(selRowIds[i]);
				var selectedRow = jQuery('#rowed3').jqGrid('getRowData', rids[selRowIds[i]-1]);
				var deletedRecordKey = ""; 
				
				console.log("RevenueId="+selectedRow['RevenueId']);
				console.log("ProductId="+selectedRow['RevenueId'].split(":")[0]);
				console.log("Year="+selectedRow['RevenueId'].split(":")[1]);
				console.log("CustomerId="+selectedRow['RevenueId'].split(":")[2]);
				for(var j=0;j<columnNames.length;j++){
					console.log("key="+columnNames[j]+" value="+selectedRow[columnNames[j]]);					
				}
				
				deletedRecordKey = deletedRecordKey + selectedRow['RevenueId'].split(":")[2];
				deletedRecordKey = deletedRecordKey + "|" + selectedRow['MarketH'];
				deletedRecordKey = deletedRecordKey + "|" + selectedRow['SubMarketH'];
				deletedRecordKey = deletedRecordKey + "|" + selectedRow['ProgramH'];
				deletedRecordKey = deletedRecordKey + "|" + selectedRow['BUH'];
				deletedRecordKey = deletedRecordKey + "|" + selectedRow['RevenueId'].split(":")[0];
				deletedRecordKey = deletedRecordKey + "|" + selectedRow['RevenueId'].split(":")[1];
				deletedRecordsArray.push(deletedRecordKey);				
			}
			
			var deletedRecordsJsonArray = JSON.stringify(deletedRecordsArray);
			console.log("deletedRecordsJsonArray="+deletedRecordsJsonArray);
	
			$.ajax({
				  url: "deleteForecast.action",
				  dataType: "json",
				  contentType: 'application/json',
				  data:deletedRecordsJsonArray,
	//			  context: document.body,			
				  async:false,
				  type:"POST",
				  success: function(data,textStatus,jqXHR){
		 			  console.log('in success'+' jqXHR='+jqXHR+' textStatus='+textStatus+' data='+data);	
		 			  if(data == "success"){
					  	result = true;
					  	console.log('success');
		 			  }else{
		 				result = false;
		 				console.log('error');
		 			  }
				  },
				  error: function(jqXHR,textStatus,errorThrown){
						console.log('in error'+' jqXHR='+jqXHR+' textStatus='+textStatus+'errorThrown='+errorThrown);
	//					  alert("Error occurred while saving Forecast.\n Error Details: "+errorThrown);		
					  result = false;
				  }
			});		
			
			if(result){
				$("#messageDiv span").hide().html("Forecast records deleted successfully").fadeIn('fast').delay(1200).fadeOut('fast');
				$("#rowed3").trigger("reloadGrid");
				$('#delete button').attr("disabled",true);
			}else{
				$("#messageDiv span").hide().html("Error occurred while deleting forecast record").fadeIn('fast').delay(1200).fadeOut('fast');		
			}
	}	
	
	$('#createfinanceforecast').click(function() {
		var sel = document.getElementById("salesRepName");
		salesRepId =  sel.options[sel.selectedIndex].value;	
		var ssoToken = getUrlVars()["ssoToken"];
		location.href = 'createFinanceForecast?salesRepId='+salesRepId+'&parentSalesRepName=${parentSalesRepName}&sny=N&ssoToken='+ssoToken+'&action=CreateFinanceForecast';
	});
	
	$('#viewfinanceforecast').click(function() {
		var sel = document.getElementById("salesRepName");
		salesRepId =  sel.options[sel.selectedIndex].value;	
		var ssoToken = getUrlVars()["ssoToken"];
		location.href = 'viewFinanceForecast?salesRepId='+salesRepId+'&parentSalesRepName=${parentSalesRepName}&sny=N&ssoToken='+ssoToken+'&action=ViewFinanceForecast';
	});
	
	$('#addForecast').click(function() {
		if(!validateCreateForecast()){
			 $("#validationMessageDiv").show();
			return;
		}else{
			 $("#validationMessageDiv").hide();
		}
		var curl = 'createForecast?loginSalesRepId=' + loginSalesRepId + '&';
		curl=curl+$("#createForecastDiv input,#createForecastDiv select").serialize();
	
		$.ajax({
			  url: curl,
			  context: document.body,
			  async:false,
			  type:"POST",
			  success: function(data,textStatus,jqXHR){
				 if(data=="error"){
					  $("#messageDiv span").hide().html("Record for the selected combination already exists.").fadeIn('fast').delay(1200).fadeOut('fast');
					  $("#gs_Customer").val("");
					  $("#gs_Market").val("");
					  $("#gs_SubMarket").val("");
					  $("#gs_Program").val("");
					  $("#gs_BU").val("");	
					  $("#gs_BasePart").val("");
					  $("#gs_Customer").val($("#accountName :selected").text());
					  $("#gs_Market").val($("#marketDropdown :selected").text());
					  $("#gs_SubMarket").val($("#subMarketDropdown :selected").text());
					  $("#gs_Program").val($("#program").val());
					  $("#gs_BU").val($("#buDropdown :selected").text());	
					  $("#gs_BasePart").val($("#partNumber :selected").text());
					  //$("#rowed3").jqGrid('triggerToolbar');
					  $("#rowed3")[0].triggerToolbar();
					  $('#save button').attr("disabled","disabled");
					  $("#createForecastDiv select").each(function(){
						  $(this).val("select");					 
					  });
					  $("#createForecastDiv input").val("");
			  	  }else if(data=="success"){
					  refresh();
			  		  $("#messageDiv span").hide().html("New Forecast record created.").fadeIn('fast').delay(1200).fadeOut('fast');			  		  
			  		  $("#createForecastDiv select").each(function(){
						  $(this).val("select");					 
					  });
					  $("#createForecastDiv input").val("");
			  	  }
			  },
			  error: function(jqXHR,textStatus,errorThrown){
// 				  console.log('in error'+' jqXHR='+jqXHR+' textStatus='+textStatus+'errorThrown='+errorThrown);
			  }
		});
	});
	
	function validateCreateForecast() {
		var returnFlag = true;
		$("#createForecastDiv input,#createForecastDiv select").each(function(){			
			if($(this).val()==null||$(this).val()==""||$(this).val()=="select"){
				$(this).attr('style','border: solid red 2px');
				returnFlag = false;
			}else{
				$(this).removeAttr('style');				
			}
		});
		return returnFlag;
	}
	
	
	$('#marketDropdown').change(function() {
		if($(this).val()=="select"){
			$("#subMarketDropdown").empty();
			$("#subMarketDropdown").append($('<option></option>').val("select").html("--Select--"));
			<c:forEach items="${subMarketList}" var="subMarket">
			$("#subMarketDropdown").append(
					  $('<option></option>').val("${subMarket.lookupValue}").html("${subMarket.lookupValue}"));
			</c:forEach>
			return;
		}
		var mkturl = 'getSubSegmentList?';
		mkturl=mkturl+$(this).serialize();
		
		$.ajax({
			  url: mkturl,
			  context: document.body,
			  dataType:"json",
			  success: function(data,textStatus,jqXHR){
				  $("#subMarketDropdown").empty();
				  $("#subMarketDropdown").append($('<option></option>').val("select").html("--Select--"));
				  for(var i=0; i<data.subSegmentList.length; i++){
					  $("#subMarketDropdown").append(
							  $('<option></option>').val(data.subSegmentList[i].lookupValue).html(data.subSegmentList[i].lookupValue));
				  }
			  }
		});
	});

	function validateNumeric(el) {
		$(el).autoNumeric();	
	}

	/* Custom Formatter function for all the Month column cells. This is called by jQgrid while setting values in the cells 
	  during intial load of the grid and after updating values in the cells */
	function formatCells(cellvalue, options, rowObject){
// 		console.log("before cellval="+cellvalue);
		cellvalue = replaceComma(cellvalue);	
// 		console.log("after cellval="+cellvalue);
			if(!$.isNumeric(cellvalue)){
				return 0;
			}else{
				return addCommas(cellvalue);
			}
	}


	/* Custom Formatter function for 'Total' column cells. This is called by jQgrid while setting values in 'Total' column cells 
	  during intial load of the grid and after updating values in 'Total' column cells */
	function computeTotal(cellvalue, options, rowObject) {
		if (rowObject.length == undefined) {	
			return addCommas(cellvalue);
		}
		var total = computeRowTotal(rowObject);
		total = addCommas(total);
		return total;
	}

	function addCommas(nStr){
		nStr += '';
		x = nStr.split('.');
		x1 = x[0];
		x2 = x.length > 1 ? '.' + x[1] : '';
		var rgx = /(\d+)(\d{3})/;
		while (rgx.test(x1)) {
			x1 = x1.replace(rgx, '$1' + ',' + '$2');
		}
		return x1 + x2;
	}

	/* Custom Formatter function for 'Qx' (x=1,2..) column cells. This is called by by jQgrid while setting values in 'Qx' column cells
	    during intial load of the grid and after updating values in 'Qx' column cells */
	function computeQuarterTotal(cellvalue, options, rowObject) {
// 		console.log("in computeQuarterTotal cellvalue="+cellvalue);
		if (rowObject.length == undefined) {	
			return addCommas(cellvalue);
		}
		if(rowObject[7]==aspLabel){
			return "";
		}
		var qTotal=0;
		if(options.colModel.name == "Q1"){
			qTotal=computeQtrTotal(rowObject[7],rowObject[9],rowObject[10],rowObject[11]);
		}else if(options.colModel.name == "Q2"){
			qTotal=computeQtrTotal(rowObject[7],rowObject[13],rowObject[14],rowObject[15]);
		}else if(options.colModel.name == "Q3"){
			qTotal=computeQtrTotal(rowObject[7],rowObject[17],rowObject[18],rowObject[19]);
		}else if(options.colModel.name == "Q4"){
			qTotal=computeQtrTotal(rowObject[7],rowObject[21],rowObject[22],rowObject[23]);
		}
		if(!$.isNumeric(qTotal))
			qTotal = 0;
		
		qTotal = addCommas(qTotal);
	 	return qTotal;
	}
	
	function replaceComma(value){
		if (value != null && value != '') {
			try {
				value = value.replace(new RegExp(",", 'g'),"");
			} catch (e) {
				//alert(e);
			}
		}
		return value;
	}
	
	function computeQtrTotal(dataType,mth1,mth2,mth3){
		var total = 0.000;
		if(dataType==revenueLabel || dataType==quantityLabel || dataType==customerTotalLabel){
			total = parseFloat(replaceComma(mth1))+parseFloat(replaceComma(mth2))+parseFloat(replaceComma(mth3));	
			if(dataType==quantityLabel){
				total = total.toFixed(0);
			}else{
				total = total.toFixed(1);
			}
		}	
		return total;
	}

	function reComputeQuarterTotal(rowid,name,val,iCol) {
		var status=false;
		var qTotal =0.000;
		var rowData = jQuery('#rowed3').getRowData(rowid);
		var dataType=rowData["Data"];
		if(dataType==revenueLabel || dataType==quantityLabel || dataType==customerTotalLabel){		
			var footerDataObj = {};
			var footerDataValue = jQuery("#rowed3").jqGrid('footerData','get',null,false);
			var prevQtrTotal = 0.000;
			if(name=="Jan" || name=="Feb" || name=="Mar"){
				qTotal = parseFloat(replaceComma(rowData["Jan"]))+parseFloat(replaceComma(rowData["Feb"]))+parseFloat(replaceComma(rowData["Mar"]));
				if(isNaN(qTotal)==false){
					 if(dataType==quantityLabel){ 
						qTotal=qTotal.toFixed(0);
					 }else{
						 qTotal=qTotal.toFixed(1);
					 }					
				}
				prevQtrTotal = parseFloat(replaceComma(rowData["Q4"]));
				status = jQuery("#rowed3").jqGrid('setRowData',rowid,{Q4:qTotal});
				if(dataType==revenueLabel){
					footerDataObj['Q4']	= (parseFloat(replaceComma(footerDataValue['Q4']))-prevQtrTotal+parseFloat(replaceComma(qTotal))).toFixed(1);
					jQuery("#rowed3").jqGrid('footerData','set',footerDataObj,true);
				}
			}else if(name=="Apr" || name=="May" || name=="Jun"){
				qTotal = parseFloat(replaceComma(rowData["Apr"]))+parseFloat(replaceComma(rowData["May"]))+parseFloat(replaceComma(rowData["Jun"]));
				if(isNaN(qTotal)==false){
					 if(dataType==quantityLabel){ 
						qTotal=qTotal.toFixed(0);
					}else{
						qTotal=qTotal.toFixed(1);
					}	
				}
				prevQtrTotal = parseFloat(replaceComma(rowData["Q1"]));
				status = jQuery("#rowed3").jqGrid('setRowData',rowid,{Q1:qTotal});
				if(dataType==revenueLabel){
					footerDataObj['Q1']	= (parseFloat(replaceComma(footerDataValue['Q1']))-prevQtrTotal+parseFloat(replaceComma(qTotal))).toFixed(1);
					jQuery("#rowed3").jqGrid('footerData','set',footerDataObj,true);
				}
			}else if(name=="Jul" || name=="Aug" || name=="Sep"){
				qTotal = parseFloat(replaceComma(rowData["Jul"]))+parseFloat(replaceComma(rowData["Aug"]))+parseFloat(replaceComma(rowData["Sep"]));
				if(isNaN(qTotal)==false){
					 if(dataType==quantityLabel){ 
						qTotal=qTotal.toFixed(0);
					 }else{
						qTotal=qTotal.toFixed(1);
					 }	
				}
				prevQtrTotal = parseFloat(replaceComma(rowData["Q2"]));
				status = jQuery("#rowed3").jqGrid('setRowData',rowid,{Q2:qTotal});
				if(dataType==revenueLabel){
					footerDataObj['Q2']	= (parseFloat(replaceComma(footerDataValue['Q2']))-prevQtrTotal+parseFloat(replaceComma(qTotal))).toFixed(1);
					jQuery("#rowed3").jqGrid('footerData','set',footerDataObj,true);
				}
			} else if(name=="Oct" || name=="Nov" || name=="Dec"){
				qTotal = parseFloat(replaceComma(rowData["Oct"]))+parseFloat(replaceComma(rowData["Nov"]))+parseFloat(replaceComma(rowData["Dec"]));
				if(isNaN(qTotal)==false){
					 if(dataType==quantityLabel){ 
						qTotal=qTotal.toFixed(0);
					 }else{
						qTotal=qTotal.toFixed(1);
					 }	
				}
				prevQtrTotal = parseFloat(replaceComma(rowData["Q3"]));
				status = jQuery("#rowed3").jqGrid('setRowData',rowid,{Q3:qTotal});
				if(dataType==revenueLabel){
					footerDataObj['Q3']	= (parseFloat(replaceComma(footerDataValue['Q3']))-prevQtrTotal+parseFloat(replaceComma(qTotal))).toFixed(1);
					jQuery("#rowed3").jqGrid('footerData','set',footerDataObj,true);
				}
			}
		}
		return status;
	}

	function reComputeGrandTotal(rowid,name,val,iCol){
		var rowData = jQuery('#rowed3').getRowData(rowid);
		var dataType=rowData["Data"];
		var qty = 0.000;
		var asp = 0.000;
		var rev = 0.000;
		var prevRev = 0;
		if(dataType==quantityLabel){
			var aspRowId=(parseInt(rowid)+1)+"";
			var revRowId=(parseInt(rowid)+2)+"";
			var data = {};
			var footerDataObj = {};
			qty = parseFloat(replaceComma(rowData[name]));			
			asp = parseFloat(replaceComma(jQuery("#rowed3").jqGrid('getCell',aspRowId,iCol)));		
			prevRev = parseFloat(replaceComma(jQuery("#rowed3").jqGrid('getCell',revRowId,iCol)));
			rev = qty*asp;
			data[name]=rev.toFixed(1);
			jQuery("#rowed3").jqGrid('setRowData',revRowId,data);
			reComputeQuarterTotal(revRowId,name,rev,iCol);
			reComputeTotal(revRowId);
			var footerDataValue = jQuery("#rowed3").jqGrid('footerData','get',null,false);	
			footerDataObj[name]=((parseFloat(replaceComma(footerDataValue[name]))-prevRev)+rev).toFixed(1);
			footerDataObj['Total']=((parseFloat(replaceComma(footerDataValue['Total']))-prevRev)+rev).toFixed(1);
			jQuery("#rowed3").jqGrid('footerData','set',footerDataObj,true);
		}else if(dataType==aspLabel){
			var qtyRowId=(parseInt(rowid)-1)+"";
			var revRowId=(parseInt(rowid)+1)+"";
			var data = {};
			var footerDataObj = {};
			qty = parseFloat(replaceComma(jQuery("#rowed3").jqGrid('getCell',qtyRowId,iCol)));
			asp =parseFloat(replaceComma(rowData[name]));
			prevRev = parseFloat(replaceComma(jQuery("#rowed3").jqGrid('getCell',revRowId,iCol)));
			rev = qty*asp;
			data[name]=rev.toFixed(1);
			jQuery("#rowed3").jqGrid('setRowData',revRowId,data);	
			reComputeQuarterTotal(revRowId,name,rev,iCol);
			reComputeTotal(revRowId);
			var footerDataValue = jQuery("#rowed3").jqGrid('footerData','get',null,false);	
			footerDataObj[name]=((parseFloat(replaceComma(footerDataValue[name]))-prevRev)+rev).toFixed(1);
			footerDataObj['Total']=((parseFloat(replaceComma(footerDataValue['Total']))-prevRev)+rev).toFixed(1);
			jQuery("#rowed3").jqGrid('footerData','set',footerDataObj,true);	
		}
	}

	function reComputeCustomerTotal(editedRevRowId,name,rev,prevRev,iCol){
		var nextRowId = (parseInt(editedRevRowId)+1)+"";
		var prevCustRowId = "";
		var dataType=jQuery("#rowed3").jqGrid('getCell',nextRowId,dataColIndex);	
		if(dataType==customerTotalLabel){
			var data = {};
			rev=(parseFloat(replaceComma(jQuery("#rowed3").jqGrid('getCell',nextRowId,iCol)))-prevRev)+rev;
			data[name] = rev;
			jQuery("#rowed3").jqGrid('setRowData',nextRowId,data);	
			reComputeQuarterTotal(nextRowId,name,rev,iCol);
			reComputeTotal(nextRowId);
		}else{
			var i = parseInt(editedRevRowId)-3;
			while(i>0){
				prevCustRowId = i+"";			
				var dataType=jQuery("#rowed3").jqGrid('getCell',prevCustRowId,dataColIndex);			
				if(dataType==customerTotalLabel){
					var data = {};
					rev=(parseFloat(replaceComma(jQuery("#rowed3").jqGrid('getCell',prevCustRowId,iCol)))-prevRev)+rev;
					data[name] = rev;
					jQuery("#rowed3").jqGrid('setRowData',prevCustRowId,data);	
					reComputeQuarterTotal(prevCustRowId,name,rev,iCol);
					reComputeTotal(prevCustRowId);
					break;
				}
				i = i-3;
			}
		}
	}

	function reComputeTotal(iRow) {
		var rowData = jQuery('#rowed3').getRowData(iRow);
		if(rowData['Data'] == aspLabel){
			return "";
		}
		var columnNames = jQuery('#rowed3')[0].p.colNames;
		var cellData = new Array();
		for (i = 0 ;i < noColumnsUptoTotal-1; i++) {
			cellData[i] = rowData[columnNames[i]];
		}
		var rowTotal = computeRowTotal(cellData);
		var rTotal = 0;
		if (rowTotal == NaN) {
			jQuery("#rowed3").jqGrid('setRowData',iRow,{Total:""});
		} else {
			if(rowData['Data'] == quantityLabel){
				rTotal = parseFloat(rowTotal).toFixed(0);
			}else{
				rTotal = parseFloat(rowTotal).toFixed(1);
			}
			jQuery("#rowed3").jqGrid('setRowData',iRow,{Total:rTotal});
		}
	}

	function computeRowTotal(rowObject) {
		var total = "" ;
		var noItems=0;
		var len=0;
		if (jQuery.inArray(aspLabel,rowObject) >= 0) {
			return "" ;	
		}
		if (rowObject.length == undefined) {
			total =rowObject["Total"] ;
		}

		len = noColumnsUptoTotal-1;
			
		for (i = 9 ; i < len ; i++) {
			if(i==12||i==16||i==20||i==24){
				continue;
			}
			var value = rowObject[i] ;
			if (value != null && value != '') {
				try {
					value = value.replace(new RegExp(",", 'g'),"");
				} catch (e) {
					//alert(e);
				}
			}
			if (value == null || value == ' ' ||  value === '' || isNaN(value)) {
				continue ;
			}
			
			if (total == "") {
				total = 0.00 ;
			}
			if (jQuery.inArray(aspLabel,rowObject) >= 0) {	
			  noItems=noItems+1;
			}
			if(i!=12 && i!=16 && i!= 20 && i!=24){
				total = total + parseFloat(value);
			}
		}		
		if (isNaN(total)) {
			return "" ;
		}
		var t = parseFloat(total);
		if (jQuery.inArray(quantityLabel,rowObject) >= 0) {
			t = t.toFixed(0);	
		}else{
			t = t.toFixed(1);
		}		
		if (isNaN(t)) {
			return "" ;
		} 
		return t;
	}

	function refresh() {		  		  
		  $("#gs_Customer").val("");
		  $("#gs_Market").val("");
		  $("#gs_SubMarket").val("");
		  $("#gs_Program").val("");
		  $("#gs_BU").val("");	
		  $("#gs_BasePart").val("");
		  $("th[id^='rowed3_'] .s-ico").each(function(index){
		  	$(this).attr("style","display:none");
		  });
		  sortByDate = "Y";
		  salesRepId = $("#salesRepName").val();
		  sny = "N";
		  gUrl = 'getForecastData?salesRepId='+ salesRepId + '&sny='+sny+"&parentSalesRepName=" +'${parentSalesRepName}'+"&request_locale=en"+"&sortByDate="+sortByDate;
		  $("#rowed3").setGridParam({url:gUrl});
		  $("#rowed3")[0].triggerToolbar();
		  $("#rowed3").setCaption("<div>&nbsp;&nbsp;&nbsp;Invensense Forecasting - Fiscal Year: "+ (currentYear) +"</div>");
		  $('#currentyear button').attr("disabled",true);
		  $('#previousyear button').attr("disabled",false);
		  $('#nextyear button').attr("disabled",false);		
		  $('#save button').attr("disabled","disabled");
		  $('#lock').show();
	}
	
	function getUrlVars() {
	    var vars = {};
	    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
	        vars[key] = value;
	    });
	    return vars;
	}
	
	function saveEditedForecastData() {
		var input = $("td.edit-cell input:text");
		if (input.length > 0) {
			var cell = $("td.edit-cell");
			var cIndex = cell.parent("tr").children().index(cell);
			var id = input.attr("id");
			var n = id.split("_");
			var rowId = n[0];
			$("#rowed3").saveCell(rowId,cIndex);
		}
		
		var changeRows = $("#rowed3").getChangedCells('all');
		//if (changeRows.length <= 0) {
			//alert("No Changes found to save");
			//return ;
		//}
		
		if (changeRows.length > 0) {
		
			if(validateForecastData(changeRows)){
				//alert("Error saving data. Quantity and ASP both are required.");
				$("#messageDiv span").hide().html("Error saving data. ASP is required if Quantity is not 0.").fadeIn('fast').delay(1200).fadeOut('fast');
				return false;
			}
			
		for (row in changeRows) {		
			changeRows[row]['Market']=changeRows[row]['MarketH']; 
			changeRows[row]['SubMarket']=changeRows[row]['SubMarketH'];
			changeRows[row]['Program']=changeRows[row]['ProgramH']; 
			changeRows[row]['BU']=changeRows[row]['BUH'];

			var params = jQuery.param(changeRows[row]) +"&sny="+sny +"&request_locale=en";	
				$.ajax({
					  url: "saveForecast.action",
					  data:params,
					  context: document.body,
					  async:false,
					  type:"POST",
					  success: function(data,textStatus,jqXHR){
	//		 			  console.log('in success'+' jqXHR='+jqXHR+' textStatus='+textStatus+' data='+data);
					  },
					  error: function(jqXHR,textStatus,errorThrown){
	// 					  console.log('in error'+' jqXHR='+jqXHR+' textStatus='+textStatus+'errorThrown='+errorThrown);
	// 					  alert("Error occurred while saving Forecast.\n Error Details: "+errorThrown);					  
					  }
				});
		}
		//$("#rowed3").trigger("reloadGrid");
		$('#save button').attr("disabled","disabled");
	   }
		return true;	
	}
	
//  10-01-2013 Fix for forecast not showing correctly in January
	function getCurrentFiscalYear(){
		var d = new Date();
		var currentMonth = d.getMonth();
		var fiscalYear = d.getFullYear()+1;
		if(currentMonth==0 || currentMonth==1 || currentMonth==2){
			fiscalYear = d.getFullYear();
		}
		return fiscalYear;
	}

</script>
</html>