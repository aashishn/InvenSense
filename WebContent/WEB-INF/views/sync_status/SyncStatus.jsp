<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page isELIgnored="false" %>

<link rel="stylesheet" href="styles/slickgrid/css/slick.grid.css" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="styles/slickgrid/css/examples.css" type="text/css" media="screen" charset="utf-8" />

<script src="styles/jquery/jquery.event.drag-2.0.min.js"></script>
<script	src="styles/slickgrid/js/slick.core.js"	type="text/javascript"></script>
<script	src="styles/slickgrid/js/slick.grid.js"	type="text/javascript"></script>
<script src="styles/slickgrid/plugins/slick.rowselectionmodel.js"></script>
<script src="styles/slickgrid/js/slick.dataview.js"></script>
  <style>
    .slick-headerrow-column {
      background: #87ceeb;
      text-overflow: clip;
      -moz-box-sizing: border-box;
      box-sizing: border-box;
    }

    .slick-headerrow-column input {
      margin: 0;
      padding: 0;
      width: 100%;
      height: 100%;
      -moz-box-sizing: border-box;
      box-sizing: border-box;
    }
  </style>
<script>
var dataView;
var grid;
var searchString = "";
var data = ${objs};
var columnFilters = {};
  var columns = [
    {id: "object", name: "Object", field: "object",filter:true},
    {id: "id", name: "Id", field: "id",cannotTriggerInsert: true,filter:true},
    {id: "name", name: "Name", field: "name",filter:false},
    {id: "syncStep", name: "Step", field: "syncStep",filter:false},
    {id: "syncStatus", name: "Status", field: "syncStatus",filter:false},
    {id: "errorMessage", name: "Error Message", field: "errorMessage",filter:false},
    {id: "retryCount", name: "Retry Count", field: "retryCount",filter:false},
  ];

  var options = {
    enableCellNavigation: false,
    enableColumnReorder: false,
    forceFitColumns: true,
    showHeaderRow: true,
    headerRowHeight: 30
  };

function updateHeaderRow() {
	    for (var i = 0; i < columns.length; i++) {
	      if (columns[i].id !== "selector") {
	        var header = grid.getHeaderRowColumn(columns[i].id);
	        $(header).empty();
	        $("<input type='text'>")
	            .data("columnId", columns[i].id)
	            .val(columnFilters[columns[i].id])
	            .appendTo(header);
	      }
	    }
}
function filter(item) {
	    for (var columnId in columnFilters) {
			var i= columnId;
	    	if (columnId !== undefined && columnFilters[columnId] !== "") {
	        var c = grid.getColumns()[grid.getColumnIndex(columnId)];
	        if (item[c.field] == null || (""+item[c.field]).toLowerCase().indexOf(columnFilters[columnId]) <= -1) {
	          return false;
	        }
	      }
	    }
	    return true;
}

  $(function () {
    //dataView = new Slick.Data.DataView({ inlineFilters: true });
    dataView = new Slick.Data.DataView();
 	// initialize the model after all the events have been hooked up
    dataView.beginUpdate();
    dataView.setItems(data);
    dataView.setFilter(filter);
    dataView.endUpdate();
    
    grid = new Slick.Grid("#syncStatusGrid", dataView, columns, options);
    //grid.setSelectionModel(new Slick.RowSelectionModel());
    //dataView.syncGridSelection(grid, true);    

    $(grid.getHeaderRow()).delegate(":input", "change keyup", function (e) {
        columnFilters[$(this).data("columnId")] = $.trim($(this).val());
        dataView.refresh();
    });
    
    dataView.onRowCountChanged.subscribe(function (e, args) {
        grid.updateRowCount();
        grid.render();
      });

      dataView.onRowsChanged.subscribe(function (e, args) {
        grid.invalidateRows(args.rows);
        grid.render();
      });
      
    $(".grid-header .ui-icon")
    .addClass("ui-state-default ui-corner-all")
    .mouseover(function (e) {
      $(e.target).addClass("ui-state-hover")
    })
    .mouseout(function (e) {
      $(e.target).removeClass("ui-state-hover")
    });
    updateHeaderRow();
  });
  
function toggleFilterRow() {
  if ($(grid.getHeaderRow()).is(":visible")) {
    grid.hideHeaderRowColumns();
  } else {
    grid.showHeaderRowColumns();
  }
}
</script>
<div>
    <div class="grid-header" style="width:100%">
      <label>Data Sync Stage</label>
      <span style="float:right" class="ui-icon ui-icon-search" title="Toggle search panel"
            onclick="toggleFilterRow()"></span>
    </div>	
	<div id="syncStatusGrid" style="width:100%;height:500px;border: 1px solid #99BBE8;"></div>
</div>