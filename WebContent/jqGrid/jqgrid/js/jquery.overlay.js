<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.7/jquery-ui.js"></script>
<link media="screen" rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" />
<script type="text/javascript">
	$(function() {

		var td = document.getElementById("A0.R0.ZLink_1");
		var a = td.getElementsByTagName("a");
		var href= a[0].href ;
		$.fx.speeds._default = 1000;
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
	        //showDialog(horizontalPadding);
	        //$( "#dialogbox" ).dialog( "open" );
			//$('#dialogbox').load(href);
			return false;
			});
	});
/*	
	function showDialog(horizontalPadding){
		   $("#dialogbox").dialog("open");
		   document.getElementById("dialogbox").style.width = (840 - horizontalPadding) + "px";
		   return false;
		}*/
</script>