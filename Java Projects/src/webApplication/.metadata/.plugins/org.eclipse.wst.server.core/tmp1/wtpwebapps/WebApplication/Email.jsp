<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Untitled Form</title>
<link rel="stylesheet" type="text/css" href="view.css" media="all">
<script type="text/javascript" src="view.js"></script>

</head>
<body id="main_body" >
	
	<img id="top" src="top.png" alt="">
	<div id="form_container">
	
		<h1><a>Write New Email</a></h1>
		<form id="form_941229" class="appnitro"  method="post" action="ControllerEmail">
					<div class="form_description">
			<h2>Write an Email</h2>
			
		</div>						
			<ul>
		
			
					<li id="li_5" >
		<label class="description" for="toField">Email To: </label>
		<div>
			<input id="toField" name="toField" class="element text medium" type="text" maxlength="255" value=""/> 
		</div> 
		</li>		<li id="li_6" >
		<label class="description" for="ccField">CC: </label>
		<div>
			<input id="ccField" name="ccField" class="element text medium" type="text" maxlength="255" value=""/> 
		</div> 
		</li>		<li id="li_4" >
		<label class="description" for="subjectField">Subject </label>
		<div>
			<input id="subjectField" name="subjectField" class="element text medium" type="text" maxlength="255" value=""/> 
		</div> 
		</li>		<li id="li_3" >
		<label class="description" for="contentField">Content </label>
		<div>
			<textarea id="contentField" name="contentField" class="element textarea medium"></textarea> 
		</div> 
		</li>
			
					<li class="buttons">
			    <input type="hidden" name="form_id" value="941229" />
			    
				<input id="saveForm" class="button_text" type="submit" name="button1" value="Send"/>
				
				<input id="saveForm" class="button_text" type="submit" name="button2" value="Log out"/>
		</li>
			</ul>
		</form>	
		
	</div>
	<img id="bottom" src="bottom.png" alt="">
	</body>
</html>