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
    <#--    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">-->
    <#--    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>-->
</head>

<body>
<div id="booking" class="section">
    <div class="section-center">
        <div class="container">
            <div class="row">
                <div class="form">
                    <table class="table table-hover table-bordered table-fixed table-dark">
                        <tr class="tableHeader">
                            <td>Flight information</td>
                            <td>Flight from/to</td>
                            <td>Departure/Arrival date</td>
                            <td>Departure/Arrival date return</td>
                            <td>Total tax</td>
                            <td>Additional information</td>
                            <td>Booking</td>
                        </tr>
                        <#foreach information in searchAirDto>
                            <tr class="tableBody">
                                <td>${information.originDestinationOptionDtos[0].flightNumber}  ${information.originDestinationOptionDtos[0].airline}</td>
                                <td>${information.originDestinationOptionDtos[0].originLocation}
                                    to ${information.originDestinationOptionDtos[0].destinationLocation}</td>
                                <td>${information.originDestinationOptionDtos[0].departureDateTime}
                                    - ${information.originDestinationOptionDtos[0].arrivalDateTime}</td>
                                <#--                                <#if (information.originDestinationOptionDtos?size>1) >-->
                                <#--                                    <td>-</td>-->
                                <#--                                <#else>-->
                                <#--                                    <td>${information.originDestinationOptionDtos[1].departureDateTime}-->
                                <#--                                        - ${information.originDestinationOptionDtos[1].arrivalDateTime}</td>-->
                                <#--                                </#if>-->
                                <td>-</td>
                                <td>${information.amount}</td>
                                <td>${information.additionalInformation}</td>
                                <td>
                                    <form method="post" action="/air/choosing">
                                        <input type="hidden" id="flightNumber" name="flightNumber"
                                               value="${information.originDestinationOptionDtos[0].flightNumber}">
                                        <input type="hidden" id="airline" name="airline"
                                               value="${information.originDestinationOptionDtos[0].airline}">
                                        <input type="hidden" id="originLocation" name="originLocation"
                                               value="${information.originDestinationOptionDtos[0].originLocation}">
                                        <input type="hidden" id="destinationLocation" name="destinationLocation"
                                               value="${information.originDestinationOptionDtos[0].destinationLocation}">
                                        <input type="hidden" id="departureDateTime" name="departureDateTime"
                                               value="${information.originDestinationOptionDtos[0].departureDateTime}">
                                        <input type="hidden" id="arrivalDateTime" name="arrivalDateTime"
                                               value="${information.originDestinationOptionDtos[0].arrivalDateTime}">
                                        <input type="hidden" id="amount" name="amount"
                                               value="${information.amount}">
                                        <input type="hidden" id="cabin" name="cabin"
                                               value="${information.cabin}">
                                        <input type="hidden" id="amountPeople" name="amountPeople"
                                               value="${information.amountPeople}">
                                        <button id="searchAirDto" name="searchAirDto" value="${information}"
                                                type="submit">
                                            <svg width="2em" height="2em" viewBox="0 0 16 16" class="bi bi-cash-stack"
                                                 fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M14 3H1a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1h-1z"/>
                                                <path fill-rule="evenodd"
                                                      d="M15 5H1v8h14V5zM1 4a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h14a1 1 0 0 0 1-1V5a1 1 0 0 0-1-1H1z"/>
                                                <path d="M13 5a2 2 0 0 0 2 2V5h-2zM3 5a2 2 0 0 1-2 2V5h2zm10 8a2 2 0 0 1 2-2v2h-2zM3 13a2 2 0 0 0-2-2v2h2zm7-4a2 2 0 1 1-4 0 2 2 0 0 1 4 0z"/>
                                            </svg>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </#foreach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
