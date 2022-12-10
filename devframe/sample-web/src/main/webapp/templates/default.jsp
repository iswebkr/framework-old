<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/includes/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title><tiles:getAsString name="page.title" /></title>
<script type="text/javascript" src="/resources/sockjs-client/1.5.1/sockjs.min.js"></script>
<script type="text/javascript" src="/resources/stomp-websocket/2.3.4/stomp.min.js"></script>
<link rel="stylesheet" href="<%= buildPath(request,"/vendors.css")%>" /><link rel="stylesheet" href="<%= buildPath(request,"/sample.css")%>" /></head>
<body>
	<tiles:insertAttribute name="page.body" />
	<script type="text/javascript">
		var ws = null;
		var socket = new SockJS('/websocket');
		ws = Stomp.over(socket);
		ws.connect({}, function(frame) {
			var url = ws.ws._transport.url;

			/**
			url = url.replace("ws://localhost:9090/websocket/",  "");
	        url = url.replace("/websocket", "");
	        url = url.replace(/^[0-9]+\//, "");

	        console.log('session id >>>', url);
	        **/

			ws.subscribe('/user/topic/message', function(message) {
				console.log('PRIVATE MESSAGE : ' + message.body);
			});
			ws.subscribe('/topic/message', function(message) {
				console.log('PUBLIC MESSAGE : ' + message.body);
			});
			ws.send('/app/private', {}, new Date())
			ws.send('/app/public', {}, '전체메시지');
		}, function(error) {
			alert(error);
		});

	</script>
<script type="text/javascript" src="<%= buildPath(request,"/vendors.bundle.js")%>" charset="utf-8"></script><script type="text/javascript" src="<%= buildPath(request,"/sample.bundle.js")%>" charset="utf-8"></script><script type="text/javascript" src="<%= buildPath(request,"/runtime.bundle.js")%>" charset="utf-8"></script></body>
</html>