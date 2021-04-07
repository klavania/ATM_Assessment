<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript">
    function ShowHideDiv() {
        var chkYes = document.getElementById("chkYes");
        var showWithdraw = document.getElementById("showWithdraw");
        showWithdraw.style.display = chkYes.checked ? "block" : "none";
    }
</script>

</head>


<body>
<div align="center">
<h1>Neueda ATM</h1>
<form action="<%= request.getContextPath() %>/atm" method="post">
<table style="with: 60%">
<tr>
<td>Account Number</td>
<td><input type="number" name="accountNumber" required/></td>
</tr>


<tr>
<td>Pin</td>
<td><input type="number" name="pin" required/></td>
</tr>

<tr>
<span>Banking Operation ?</span>
<label for="chkYes">
    <input type="radio" id="chkYes" name="showTextBox" onclick="ShowHideDiv()" />
    Withdraw Money
</label>
<label for="chkNo">
<input type="radio" id="chkNo" name="showTextBox" onclick="ShowHideDiv()" />
    Check Balance
</label>
<hr />
</tr>

<tr>
<div id="showWithdraw" style="display: none">
    Withdrawl Amount
    <input type="number" id="txtPassportNumber" name="withdrawlAmount"/>
</div>
</tr>


</table>

<input type="submit" value="Submit"/>
</form>


</div>
	
</body>
</html>