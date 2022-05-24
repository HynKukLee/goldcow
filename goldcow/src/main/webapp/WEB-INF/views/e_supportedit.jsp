<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@page import="kr.ac.goldcow.model.*"%>
    <%@page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Cart | E-Shopper</title>
    <%
		List<Reward> rewards = (List<Reward>)request.getAttribute("rewards");
		String js1 = "";
		String js2 = "";
		for(int i = 0;i<rewards.size();i++){
			if(i == 0){
				js1 += "'" + rewards.get(i).getNo() + "'";
				js2 += "0";
			}else{
				js1 += ",'" + rewards.get(i).getNo() + "'";
				js2 += ",0";
			}
		}
	%>
	<script>

		var amount = ${support.amount};
		var rewardList = [<%out.print(js1);%>];
		var quantityList = [<%out.print(js2);%>];
		function additem(price,num,rewardNum) {
			if(amount-price >= 0){
				quantityList[num]++;
				var remainDiv = document.getElementById("remain");
				var quantityDiv = document.getElementById("quantity"+num);
				amount -= price;
				remainDiv.innerHTML = amount;
				quantityDiv.value = quantityList[num];
			}
		}
		function deleteitem(price,num,rewardNum) {
			if(quantityList[num] > 0){
				quantityList[num]--;
				var remainDiv = document.getElementById("remain");
				var quantityDiv = document.getElementById("quantity"+num);
				amount += price;
				remainDiv.innerHTML = amount;
				quantityDiv.value = quantityList[num];
			}
		}
		function check() {
			var str = "";
			var hiddenform = document.getElementById("hiddenform");
			var hiddenrewardlist = document.getElementById("hiddenrewardlist");
			for (var i=0; i<rewardList.length; i++) {
				if(quantityList[i] > 0){
					str = str + rewardList[i] + "*" + quantityList[i] + "&"; 
				}
			}
			hiddenrewardlist.setAttribute( 'value', str );
			hiddenform.submit();
		}
</script>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link href="css/prettyPhoto.css" rel="stylesheet">
    <link href="css/price-range.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
	<link href="css/main.css" rel="stylesheet">
	<link href="css/responsive.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->       
    <link rel="shortcut icon" href="images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="images/ico/apple-touch-icon-57-precomposed.png">
</head><!--/head-->

<body>
	<%@include file = "header.jsp" %>

	<section id="cart_items">
		<div class="container">
			<div class="breadcrumbs">
				<ol class="breadcrumb">
				  <li><a>후원</a></li>
				  <li class="active">보상을 선택하세요.</li>
				</ol>
				<h3>후원할 금액: ${support.amount}원 중 <div id = "remain">${support.amount}</div> 남았습니다.</h3>
				
			</div>
			<div class="table-responsive cart_info">
				<table class="table table-condensed">
					<thead>
						<tr class="cart_menu">
							<td class="image">Reward</td>
							<td class="description"></td>
							<td class="price">Price</td>
							<td class="quantity">Quantity</td>
							<td></td>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="reward" items="${rewards}" begin="0" step="1" varStatus="status" >
						<tr>
							<td class="cart_product">
								<a href=""><img src="../resources/rewards/${reward.no}.jpg" alt="" width = "110px" height = "110px"></a>
							</td>
							<td class="cart_description">
								<h4><a href="">${reward.name}</a></h4>
							</td>
							<td class="cart_price">
								<p>${reward.price}</p>
							</td>
							<td class="cart_quantity">
								<div class="cart_quantity_button">
									<a class="cart_quantity_up" onclick="additem(${reward.price},${status.index},${reward.no});"> + </a>
									<input class="cart_quantity_input" type="text" id = "quantity${status.index}" value="0" autocomplete="off" size="2" readonly>
									
									<a class="cart_quantity_down" onclick="deleteitem(${reward.price},${status.index},${reward.no});"> - </a>
								</div>
							</td>
						</tr>

						</c:forEach>
					</tbody>
				</table>
				
			</div>
			<form:form method="post" modelAttribute="support" action = "registersupport.do" id = "hiddenform">
  	<form:input type="hidden" path="amount" /><br> 
    <form:input type="hidden" path="projectno"/> 
    <form:input type="hidden" path="rewardList" id = "hiddenrewardlist" />
    <a class="btn btn-default check_out" onclick = "check();">Continue</a>
</form:form>
			
		</div>
	</section> <!--/#cart_items-->
<br/>
<br/>


	<footer id="footer"><!--Footer-->
		<div class="footer-top">
			<div class="container">
				<div class="row">
					<div class="col-sm-2">
						<div class="companyinfo">
							<h2><span>e</span>-shopper</h2>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit,sed do eiusmod tempor</p>
						</div>
					</div>
					<div class="col-sm-7">
						<div class="col-sm-3">
							<div class="video-gallery text-center">
								<a href="#">
									<div class="iframe-img">
										<img src="images/home/iframe1.png" alt="" />
									</div>
									<div class="overlay-icon">
										<i class="fa fa-play-circle-o"></i>
									</div>
								</a>
								<p>Circle of Hands</p>
								<h2>24 DEC 2014</h2>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="video-gallery text-center">
								<a href="#">
									<div class="iframe-img">
										<img src="images/home/iframe2.png" alt="" />
									</div>
									<div class="overlay-icon">
										<i class="fa fa-play-circle-o"></i>
									</div>
								</a>
								<p>Circle of Hands</p>
								<h2>24 DEC 2014</h2>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="video-gallery text-center">
								<a href="#">
									<div class="iframe-img">
										<img src="images/home/iframe3.png" alt="" />
									</div>
									<div class="overlay-icon">
										<i class="fa fa-play-circle-o"></i>
									</div>
								</a>
								<p>Circle of Hands</p>
								<h2>24 DEC 2014</h2>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="video-gallery text-center">
								<a href="#">
									<div class="iframe-img">
										<img src="images/home/iframe4.png" alt="" />
									</div>
									<div class="overlay-icon">
										<i class="fa fa-play-circle-o"></i>
									</div>
								</a>
								<p>Circle of Hands</p>
								<h2>24 DEC 2014</h2>
							</div>
						</div>
					</div>
					<div class="col-sm-3">
						<div class="address">
							<img src="images/home/map.png" alt="" />
							<p>505 S Atlantic Ave Virginia Beach, VA(Virginia)</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="footer-widget">
			<div class="container">
				<div class="row">
					<div class="col-sm-2">
						<div class="single-widget">
							<h2>Service</h2>
							<ul class="nav nav-pills nav-stacked">
								<li><a href="">Online Help</a></li>
								<li><a href="">Contact Us</a></li>
								<li><a href="">Order Status</a></li>
								<li><a href="">Change Location</a></li>
								<li><a href="">FAQ’s</a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="single-widget">
							<h2>Quock Shop</h2>
							<ul class="nav nav-pills nav-stacked">
								<li><a href="">T-Shirt</a></li>
								<li><a href="">Mens</a></li>
								<li><a href="">Womens</a></li>
								<li><a href="">Gift Cards</a></li>
								<li><a href="">Shoes</a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="single-widget">
							<h2>Policies</h2>
							<ul class="nav nav-pills nav-stacked">
								<li><a href="">Terms of Use</a></li>
								<li><a href="">Privecy Policy</a></li>
								<li><a href="">Refund Policy</a></li>
								<li><a href="">Billing System</a></li>
								<li><a href="">Ticket System</a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="single-widget">
							<h2>About Shopper</h2>
							<ul class="nav nav-pills nav-stacked">
								<li><a href="">Company Information</a></li>
								<li><a href="">Careers</a></li>
								<li><a href="">Store Location</a></li>
								<li><a href="">Affillate Program</a></li>
								<li><a href="">Copyright</a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-3 col-sm-offset-1">
						<div class="single-widget">
							<h2>About Shopper</h2>
							<form action="#" class="searchform">
								<input type="text" placeholder="Your email address" />
								<button type="submit" class="btn btn-default"><i class="fa fa-arrow-circle-o-right"></i></button>
								<p>Get the most recent updates from <br />our site and be updated your self...</p>
							</form>
						</div>
					</div>
					
				</div>
			</div>
		</div>
		
		<div class="footer-bottom">
			<div class="container">
				<div class="row">
					<p class="pull-left">Copyright © 2013 E-SHOPPER Inc. All rights reserved.</p>
					<p class="pull-right">Designed by <span><a target="_blank" href="http://www.themeum.com">Themeum</a></span></p>
				</div>
			</div>
		</div>
		
	</footer><!--/Footer-->
	


    <script src="js/jquery.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.scrollUp.min.js"></script>
    <script src="js/jquery.prettyPhoto.js"></script>
    <script src="js/main.js"></script>
</body>
</html>