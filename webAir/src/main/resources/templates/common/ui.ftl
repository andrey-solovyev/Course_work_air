<#macro table id rows>
    <table border="1" cellspacing="0" cellpadding="1">
        <tr class="tableHeader">
            <td>Fligth Number</td>
            <td>Date and Time</td>
            <td>Return Date and Time</td>
            <td>Total tax</td>
            <td>Baggage</td>
        </tr>
        <#foreach ticket in otaAirLowFareSearchRS.pricedItineraries.pricedItinerary>
            <tr class="tableBody">
                <td>${order.salesrepNumber}</td>
                <td>${order.orderNumber}</td>
                <td>${order.ioGuid}</td>
                <td>${order.lastUpdateDate?datetime}</td>
                <td>${order.lastUpdatedByUser}</td>
            </tr>
        </#foreach>
    </table>
</#macro>