<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Email Client</title>
<link rel="stylesheet" type="text/css" href="view.css" media="all">
<script type="text/javascript" src="view.js"></script>

</head>
<body id="main_body" >
	
	<img id="top" src="top.png" alt="">
	<div id="form_container">
	
		<h1><a>Email Client</a></h1>
		<form id="form_941229" class="appnitro"  method="post" action="Controller">
					<div class="form_description">
			<h2>Email Client</h2>
			<p>Login</p>
		</div>						
			<ul >
			
					<li id="li_1" >
		<label class="description" for="element_1">Email Address </label>
		<div>
			<input id="element_1" name="element_1" class="element text medium" type="text" maxlength="255" value=""/> 
		</div> 
		</li>		<li id="li_2" >
		<label class="description" for="element_2">Password </label>
		<div>
			<input id="element_2" name="element_2" class="element text medium" type="password" maxlength="255" value=""/> 
		</div> 
		</li>
			
					<li class="buttons">
			    <input type="hidden" name="form_id" value="941229" />
			    
				<input id="saveForm" class="button_text" type="submit" name="Submit" value="Log In" />
				<!-- <input id="saveForm" class="button_text" type="reset" name="Reset" value="Log Out" /> -->
		</li>
			</ul>
		</form>	
		
	</div>
	<img id="bottom" src="bottom.png" alt="">
	</body>
</html>