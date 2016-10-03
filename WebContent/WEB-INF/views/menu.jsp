<%@include file="../layouts/include_taglib.jsp" %>
<div class="outter_menu" >
	<div id="menu">
	    <ul id="menu_list" class="sf-menu" style="width:100%">
	    	<li class="current"><a href="#a">Home</a>
	    	<li class="current"><a href="SyncJob">Jobs</a></li>
	    	<li class="current"><a href="SyncStatus">Sync Status</a>
	    	<li class="current"><a href="#a">Logs</a>
<!-- 	    	<li class="current"><a href="#a">Users</a> -->
<!-- 	    		 <ul> -->
<!-- 			    	<li class="current"><a href="syncNewObject">Add User</a></li> -->
<!-- 			    	<li><a href="s">View Users</a></li> -->
<!-- 	    		</ul> -->
<!-- 	    	</li>  	    	    	 -->
	    </ul>
	    <script type="text/javascript"> 
		    $(document).ready(function(){ 
		        //$("div#menu").superfish().find('ul').bgIframe({opacity:false}); 
		                $("ul.sf-menu").supersubs({ 
	            minWidth:    12,   // minimum width of sub-menus in em units 
	            maxWidth:    27,   // maximum width of sub-menus in em units 
	            extraWidth:  1     // extra width can ensure lines don't sometimes turn over 
	                               // due to slight rounding differences and font-family 
	        }).superfish();  // call supersubs first, then superfish, so that subs are 
	                         // not display:none when measuring. Call before initialising 
	                         // containing tabs for same reason. 
		    }); 
		</script>
	</div>
</div>