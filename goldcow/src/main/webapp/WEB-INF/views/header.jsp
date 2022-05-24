<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<header id="header"><!--header-->
		<div class="header_top"><!--header_top-->
			<div class="container">
				<div class="row">
					<div class="col-sm-6">
						<div class="contactinfo">
							<ul class="nav nav-pills">
							<c:if test="${not empty cookie.no}">
								<li><a href="#"><i class="fa fa-star"></i> 안녕하십니까 ${cookie.name.value} 회원님</a></li>
								    <c:if test="${cookie.isbusiness.value eq true}">
								    <li><a href="#">사업자 회원임.</a></li>
								    
										</c:if>
								<li><a href="#">사이버 머니: ${cookie.money.value}</a></li>
							</c:if>
							</ul>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="social-icons pull-right">
							<ul class="nav navbar-nav">
								<li><a href="#"><i class="fa fa-facebook"></i></a></li>
								<li><a href="#"><i class="fa fa-twitter"></i></a></li>
								<li><a href="#"><i class="fa fa-linkedin"></i></a></li>
								<li><a href="#"><i class="fa fa-dribbble"></i></a></li>
								<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header_top-->
		
		<div class="header-middle"><!--header-middle-->
			<div class="container">
				<div class="row">
					<div class="col-sm-4">
						<div class="logo pull-left">
							<a href="/goldcow"><img src="images/home/gclogo.png" alt="" height = "39" width = "39"/><img src="images/home/goldcow.png" alt="" /></a>
						</div>
					</div>
					<div class="col-sm-8">
						<div class="shop-menu pull-right">
							<ul class="nav navbar-nav">
							<c:if test="${empty cookie.no}">
								<li><a href="/goldcow/user/edit.do"><i class="fa fa-user"></i> 회원가입</a></li>
								<li><a href="/goldcow/user/loginedit"><i class="fa fa-lock"></i> 로그인</a></li>
							</c:if>
								<c:if test="${not empty cookie.no}">
								<li><a href="/goldcow/user/logout.do"><i class="fa fa-lock"></i> 로그아웃</a></li>
								<li><a href="/goldcow/user/money"><i class="fa fa-user"></i> 머니 충전</a></li>
   									 <c:if test="${cookie.isbusiness.value eq false}">
   									 <li><a href="/goldcow/user/businessedit"><i class="fa fa-user"></i> 사업자 등록</a></li>
									</c:if>
									<c:if test="${cookie.isbusiness.value eq true}">
   									<li><a href="/goldcow/project/projectedit"><i class="fa fa-star"></i> 프로젝트 등록</a></li>
									</c:if>
								</c:if>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header-middle-->
	
		<div class="header-bottom"><!--header-bottom-->
			<div class="container">
				<div class="row">
					<div class="col-sm-9">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
								<span class="sr-only">Toggle navigation</span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
							</button>
						</div>
						<div class="mainmenu pull-left">
							<ul class="nav navbar-nav collapse navbar-collapse">
								<li><a href="/goldcow" class="active">Home</a></li>
								<c:if test="${not empty cookie.no}">
								<li class="dropdown"><a href="#">User<i class="fa fa-angle-down"></i></a>
                                    <ul role="menu" class="sub-menu">
                                        <li><a href="/goldcow/support/mysupportlist?page=1">나의 후원목록</a></li>
                                    </ul>
                                </li> 
                                <c:if test="${cookie.isbusiness.value eq true}">
								<li class="dropdown"><a href="#">Project<i class="fa fa-angle-down"></i></a>
                                    <ul role="menu" class="sub-menu">
                                        <li><a href="/goldcow/project/myprojectlist?page=1">내 프로젝트</a></li>
                                    </ul>
                                </li>                                 
                                </c:if>
								</c:if>
								
							</ul>
						</div>
					</div>
					<div class="col-sm-3">
						
						
						<div class="search_box pull-right">
						<form action="/goldcow/project/search?" id = "searchForm">
							<input type="text" name = "keyWord" onkeypress="if(event.keyCode==13){document.getElementById('searchForm').submit();}" placeholder="Search"/>
							<input type="hidden" name = "page" value = "1"/>
							</form>
						</div>
						
					</div>
				</div>
			</div>
		</div><!--/header-bottom-->
	</header><!--/header-->