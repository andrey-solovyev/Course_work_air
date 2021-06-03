<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

	<title>Air Booking</title>

	<!-- Google font -->
	<link href="https://fonts.googleapis.com/css?family=PT+Sans:400" rel="stylesheet">

	<!-- Bootstrap -->
	<link type="text/css" rel="stylesheet" href="../css/bootstrap.min.css" />

	<!-- Custom stlylesheet -->
	<link type="text/css" rel="stylesheet" href="../css/style.css" />

	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
		  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
		  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
		<![endif]-->

</head>

<body>
	<div id="booking" class="section">
		<div class="section-center">
			<div class="container">
				<div class="row">
					<div class="booking-form">
						<form action="" method="post">
<#--							<div class="form-group">-->
<#--								<div class="form-checkbox">-->
<#--									<label for="roundtrip">-->
<#--										<input type="radio" id="roundtrip" name="flight-type">-->
<#--										<span></span>Roundtrip-->
<#--									</label>-->
<#--									<label for="one-way">-->
<#--										<input type="radio" id="one-way" name="flight-type">-->
<#--										<span></span>One way-->
<#--									</label>-->
<#--									<label for="multi-city">-->
<#--										<input type="radio" id="multi-city" name="flight-type">-->
<#--										<span></span>Multi-City-->
<#--									</label>-->
<#--								</div>-->
<#--							</div>-->
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<span class="form-label">Flying from</span>
										<input name="originLocation" class="form-control" type="text" placeholder="originLocation">
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<span class="form-label">Flyning to</span>
										<input name="destinationLocation" class="form-control" type="text" placeholder="destinationLocation">
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<span class="form-label">Departing</span>
										<input name="startDate" placeholder="startDate" class="form-control" type="date" required>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<span class="form-label">Returning</span>
										<input name="returningDate" placeholder="returningDate" class="form-control" type="date">
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<span class="form-label">Adults (12+)</span>
										<select name="amountADTPeople" id="amountADTPeople" class="form-control">
											<option selected="selected" value="1">1</option>
											<option value="2">2</option>
											<option value="3">3</option>
											<option value="4">4</option>
										</select>
										<span class="select-arrow"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<span class="form-label">Children (2-12)</span>
										<select name="amountCNNPeople" id="amountCNNPeople" class="form-control">
											<option value="0" selected="selected">0</option>
											<option value="1">1</option>
											<option value="2">2</option>
											<option value="3">3</option>
										</select>
										<span class="select-arrow"></span>
									</div>
								</div><div class="col-md-2">
									<div class="form-group">
										<span class="form-label">Baby (0-2)</span>
										<select name="amountINFPeople" id="amountINFPeople" class="form-control">
											<option value="0" selected="selected">0</option>
											<option value="1">1</option>
											<option value="2">2</option>
											<option value="3">3</option>
										</select>
										<span class="select-arrow"></span>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<span class="form-label">Travel class</span>
										<select name="cabinPrefCabin" class="form-control">
											<option value="Y">Economy class</option>
											<option value="S">Economy+ class</option>
											<option value="C">Business class</option>
											<option value="J">Business+ class</option>
											<option value="F">First class</option>
										</select>
										<span class="select-arrow"></span>
									</div>
								</div>
								<div class="col-md-3 checkbox-text">
									<input type="checkbox" class="form-checkbox" id="food" name="food" value="true">Food<Br>
									<input type="checkbox" class="form-checkbox" id="food" name="transportationOfAnimals" value="true">Transportation of animals<Br>
									<input type="checkbox" class="form-checkbox" id="food" name="baggage" value="true">Baggage<Br>
								</div>
								<div class="col-md-3">
									<div class="form-btn">
										<button class="submit-btn">Show flights</button>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body><!-- This templates was made by Colorlib (https://colorlib.com) -->

</html>