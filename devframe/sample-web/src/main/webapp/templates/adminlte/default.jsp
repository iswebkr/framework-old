<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/includes/taglib.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title><tiles:getAsString name="page.title" /></title>
	<script type="text/javascript" src="${contextPath }/resources/sockjs-client/1.5.1/sockjs.min.js"></script>
	<script type="text/javascript" src="${contextPath }/resources/stomp-websocket/2.3.4/stomp.min.js"></script>
	<link rel="stylesheet" href="${contextPath }/resources/dist/vendors.css" />
	<link rel="stylesheet" href="${contextPath }/resources/dist/sample.css" />
</head>
<body class="hold-transition sidebar-mini">
	<div class="wrapper">
		<nav class="main-header navbar navbar-expand navbar-white navbar-light">
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a></li>
				<li class="nav-item d-none d-sm-inline-block"><a href="${contextPath }/" class="nav-link">Home</a></li>
			</ul>
			<ul class="navbar-nav ml-auto">
				<li class="nav-item">
					<a class="nav-link" data-widget="navbar-search" href="#" role="button"> <i class="fas fa-search"></i></a>
					<div class="navbar-search-block">
						<form class="form-inline">
							<div class="input-group input-group-sm">
								<input class="form-control form-control-navbar" type="search" placeholder="Search" aria-label="Search">
								<div class="input-group-append">
									<button class="btn btn-navbar" type="submit">
										<i class="fas fa-search"></i>
									</button>
									<button class="btn btn-navbar" type="button" data-widget="navbar-search">
										<i class="fas fa-times"></i>
									</button>
								</div>
							</div>
						</form>
					</div>
				</li>
				<li class="nav-item">
					<a class="nav-link" data-widget="fullscreen" href="#" role="button"><i class="fas fa-expand-arrows-alt"></i></a>
				</li>
				<li class="nav-item">
					<a class="nav-link" data-widget="control-sidebar" data-slide="true" href="#" role="button"> <i class="fas fa-th-large"></i></a>
				</li>
			</ul>
		</nav>
		<aside class="main-sidebar sidebar-dark-primary elevation-4">
			<a href="index3.html" class="brand-link">
		  		<span class="brand-text font-weight-light">AdminLTE 3</span>
			</a>
			<div class="sidebar">
				<div class="user-panel mt-3 pb-3 mb-3 d-flex">
		  			<div class="info">
		    			<a href="#" class="d-block">Alexander Pierce</a>
		  			</div>
				</div>
				<div class="form-inline">
					<div class="input-group" data-widget="sidebar-search">
						<input class="form-control form-control-sidebar" type="search" placeholder="Search" aria-label="Search">
						<div class="input-group-append">
							<button class="btn btn-sidebar"><i class="fas fa-search fa-fw"></i></button>
				    	</div>
					</div>
				</div>
				<nav class="mt-2">
					<ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
						<li class="nav-item">
							<a href="${contextPath }/sample-one" class="nav-link" data-pjax>
								<i class="nav-icon fas fa-th"></i>
								<p>
									SAMPLE PAGE (ONE)
									<span class="right badge badge-danger">New</span>
								</p>
							</a>
						</li>
					</ul>
				</nav>
			</div>
		</aside>
		<div class="content-wrapper">
			<div id="pjax-container">
				<tiles:insertAttribute name="page.body" />
			</div>
		</div>
		<aside class="control-sidebar control-sidebar-dark">
			<div class="p-3">
				<h5>Title</h5>
				<p>Sidebar content</p>
			</div>
		</aside>
		<footer class="main-footer">
			<div class="float-right d-none d-sm-inline">
    		</div>
			<strong>Copyright &copy; 2014-2021 <a href="https://adminlte.io">AdminLTE.io</a>.</strong> All rights reserved.
		</footer>
	</div>
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
	<script type="text/javascript" src="${contextPath }/resources/dist/vendors.bundle.js" charset="utf-8"></script>
	<script type="text/javascript" src="${contextPath }/resources/dist/sample.bundle.js" charset="utf-8"></script>
	<script type="text/javascript" src="${contextPath }/resources/dist/runtime.bundle.js" charset="utf-8"></script>
</body>
</html>