<html>
<head>
<style>
body {
	background: #222;		
}

table {
	margin: 0px auto;
}

.login_button {
	color: #fff;
	background-color: #337ab7;
	border-color: #2e6da4;
	width: 100%;
}

.login_text {
	font-weight: 700;
	color: ivory;
	text-align: center;
	font-size: 70px;
	font-family: Helvetica Neue,Helvetica,Arial,sans-serif;
}

.input_text {
	font-weight: 700;
	color: black;	
	text-align: center;
	font-size: 70px;
	font-family: Helvetica Neue,Helvetica,Arial,sans-serif;
}

</style>
</head>
<body>
	<c:set var="loginUrl">
		<c:url value="/login" />
	</c:set>
	<form method="post" action="${loginUrl}">
		<table>
			<tr>
				<td class="login_text">Login:</td>
				<td><input type="text" name="username" class="input_text"/></td>
			</tr>
			<tr>
				<td class="login_text">Haslo:</td>
				<td><input type="password" name="password" class="input_text"/></td>
			</tr>
			<tr>
				<td colspan="2"><button type="submit" class="login_button login_text">Zaloguj</button></td>
			</tr>

		</table>
	</form>
</body>
</html>