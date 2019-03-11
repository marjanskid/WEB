/**
 * @author game_changer96
 */

var baseUrl = 'http://localhost:8080/DMProject/rest/';

$(document).ready(function() {
	
	$('#korpaPanel').hide();
	$('#profilPanel').hide();
	$('#logout').hide();

	checkLoginStatus();
	
	$('#logout').click(function () {
        logoutUser();
    });
});

function checkLoginStatus() {
	$.ajax({
		url : "rest/user/loginstat"
	}).then(function(user) {

		if (user != undefined) {
			$('#login').hide();
			$('#profilPanel').text(user.username);
			$('#profilPanel').show();
			$('#logout').show();
			
			if (user.uloga == "kupac") {
				$('#korpaPanel').show();
			}
		}
	});
}

function logoutUser() {
    $.ajax({
		url: "rest/user/logout",

		success : function() {
			alert("Uspešno ste se izlogovali!")
			refresh();
		},
		
		error : function(message) {
			alert(message.responseText);
		}
    });
}

function refresh() {
	window.location.href = "http://localhost:8080/DMProject/homepage.html";
}