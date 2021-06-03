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
    <link type="text/css" rel="stylesheet" href="../css/bootstrap.min.css"/>

    <!-- Custom stlylesheet -->
    <link type="text/css" rel="stylesheet" href="../css/style.css"/>

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
                    <form action="/air/booking" method="post" name="bookingInfoDto">
                        <#list 1..information.amountPeople as x>
                            <h4 style="color: white">Passenger ${x}</h4>
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
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <span class="form-label">Name</span>
                                        <input name="bookingInfoDto[${x}].name" class="form-control" type="text"
                                               placeholder="name">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <span class="form-label">Surname</span>
                                        <input name="bookingInfoDto[${x}].surname" class="form-control" type="text"
                                               placeholder="surname">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <span class="form-label">Middle</span>
                                        <input name="bookingInfoDto[${x}].middle" class="form-control" type="text"
                                               placeholder="middle">
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <div class="form-group">
                                        <span class="form-label">Age</span>
                                        <select name="bookingInfoDto[${x}].passengerType" id="passengerType"
                                                class="form-control">
                                            <option selected="selected" value="Adt">12+ age</option>
                                            <option value="CNN">2-12 age</option>
                                            <option value="INF">0-2 age(With place to sit)</option>
                                        </select>
                                        <span class="select-arrow"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">

                                <div class="col-md-2">
                                    <div class="form-group">
                                        <span class="form-label">Gender of the person</span>
                                        <select name="bookingInfoDto[${x}].genderOfThePerson" id="genderOfThePerson"
                                                class="form-control">
                                            <option selected="selected" value="M">man</option>
                                            <option value="F">woman</option>
                                        </select>
                                        <span class="select-arrow"></span>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <span class="form-label">Passport Number</span>
                                        <input name="bookingInfoDto[${x}].passportNumber" class="form-control"
                                               type="text"
                                               placeholder="passportNumber">
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <div class="form-group">
                                        <span class="form-label">Expiration date passport</span>
                                        <input name="bookingInfoDto[${x}].expirationDate" placeholder="expirationDate"
                                               class="form-control"
                                               type="date"
                                               required>
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <div class="form-group">
                                        <span class="form-label">Date Of Birth</span>
                                        <input name="bookingInfoDto[${x}].dateOfBirth" placeholder="dateOfBirth"
                                               class="form-control"
                                               type="date"
                                               required>
                                    </div>
                                </div>
                            </div>
                        </#list>
                        <input type="hidden" id="flightNumber" name="flightNumber"
                               value="${information.flightNumber}">
                        <input type="hidden" id="airline" name="airline"
                               value="${information.airline}">
                        <input type="hidden" id="originLocation" name="originLocation"
                               value="${information.originLocation}">
                        <input type="hidden" id="destinationLocation" name="destinationLocation"
                               value="${information.destinationLocation}">
                        <input type="hidden" id="departureDateTime" name="departureDateTime"
                               value="${information.departureDateTime}">
                        <input type="hidden" id="arrivalDateTime" name="arrivalDateTime"
                               value="${information.arrivalDateTime}">
                        <input type="hidden" id="amount" name="amount"
                               value="${information.amount}">
                        <input type="hidden" id="cabin" name="cabin"
                               value="${information.cabin}">
                        <input type="hidden" id="amountPeople" name="amountPeople"
                               value="${information.amountPeople}">
                        <div class="col-md-3">
                            <div class="form-btn">
                                <button class="submit-btn">Show flights</button>
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
