<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page isELIgnored="false" %>

<link rel="stylesheet" href="styles/slickgrid/css/slick.grid.css" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="styles/slickgrid/css/examples.css" type="text/css" media="screen" charset="utf-8" />

<script src="styles/jquery/jquery.event.drag-2.0.min.js"></script>
<script	src="styles/slickgrid/js/slick.core.js"	type="text/javascript"></script>
<script src="styles/slickgrid/plugins/slick.rowselectionmodel.js"></script>
<script src="styles/slickgrid/js/slick.dataview.js"></script>

<script src="styles/slickgrid/plugins/slick.cellselectionmodel.js"></script>

<script src="styles/slickgrid/js/slick.formatters.js"></script>
<script src="styles/slickgrid/js/slick.editors.js"></script>
<script	src="styles/slickgrid/js/slick.grid.js"	type="text/javascript"></script>
<script	src="js/jquery.blockUI.js"	type="text/javascript"></script>
<style type="text/css">
div.growlUI { background: url(images/check48.png) no-repeat 5px 5px; }
div.growlUI h1, div.growlUI h2 {
	color: white; padding: 5px 5px 5px 75px; text-align: left;
	height: 40px;
	opacity:0.9;
}
</style>
<script type="text/javascript">
var dataView;
var grid;
var searchString = "";
var data = ${objs};
  var columns = [
    {id: "jobName", name: "Job Name", field: "jobName",cannotTriggerInsert: true,filter:true},
    {id: "description", name: "Description", field: "description",filter:false},
    {id: "active", name: "Active", field: "active",filter:false,formatter: Slick.Formatters.YesNo, editor: Slick.Editors.YesNoSelect},
    {id: "lastRun", name: "Last Run", field: "lastRun",filter:false},
    {id: "status", name: "Last Run Status", field: "status",filter:false},
    {id: "errorLog", name: "Error Message", field: "errorLog",filter:false},
    {id: "enableNotification", name: "Email Notification", field: "enableNotification",filter:false,formatter: Slick.Formatters.YesNo},
    {id: "frequency", name: "Frequency", field: "emailId",filter:false},
  ];

  var options = {
    editable: true,
    enableAddRow: false,
    enableCellNavigation: true,
    asyncEditorLoading: false,
    autoEdit: false,
    forceFitColumns: true,
    showHeaderRow: false,
    headerRowHeight: 30
  };

  $(function () {
    dataView = new Slick.Data.DataView();
 	// initialize the model after all the events have been hooked up
    dataView.beginUpdate();
    dataView.setItems(data,"jobName");
    dataView.endUpdate();
    
    grid = new Slick.Grid("#syncJobGrid", dataView, columns, options);

    dataView.onRowCountChanged.subscribe(function (e, args) {
        grid.updateRowCount();
        grid.render();
      });

      dataView.onRowsChanged.subscribe(function (e, args) {
        grid.invalidateRows(args.rows);
        grid.render();
      });
      
      grid.onCellChange.subscribe(function (e, args) {
    	  var url ="SyncJob/" + args.item.jobName + "/" + args.item.active;
    		 $.ajax({
    			    type: "POST",
    			    url: url,
    			    success: function(jsondata,stat) {
    			    	dataView.updateItem(args.item.jobName, args.item);
    			    	$.growlUI('Data saved successfully');
    			    },
    			    error: function(error){
						alert(error);
    			    }
    		 }); 
      });
});
</script>
<div>
    <div class="grid-header" style="width:100%">
      <label>Job Details</label>
    </div>	
	<div id="syncJobGrid" style="width:100%;height:300px;border: 1px solid #99BBE8;"></div>
</div>