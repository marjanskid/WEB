/**
 * @author game_changer96
 */


$(document).ready(function() {

	showChosenOption();
	showRecivedOption();
	
});

//**** Prikaz picerija ****
function showPizzaRestaurants() {
    
    $.ajax ({
		type : 'GET',
		url : 'rest/restaurant/restaurants',
		
		success : function(restorani) {
			
			$('#restorani-picerija-prikaz-svi').empty();
			
			for(var r of restorani) { 
				if(r.obrisan == false && r.kategorijaRestorana == "picerija") {
					
					var newR = $('<div class="col-sm-3"><br><div class="card text-center" style="width: 15rem;"><img class="card-img-top" src="pictures/2_0.png"alt="Card image cap"><div class="card-body"><a href="#" id="picerija-restoran-id-' + r.id + '"><h5 class="card-title">' + r.naziv + '</h5></a><p class="card-text">' + r.adresa + '</p><button id="dodaj-u-omiljene-id-' + r.id + '" class="btn btn-primary">OMILJEN</button></div></div></div>');
					$('#restorani-picerija-prikaz-svi').append(newR);

					$('#picerija-restoran-id-'+ r.id).click(showAllArticlesFromRestaturant(r));
					var buttonId = "dodaj-u-omiljene-id-" + r.id;
					checkUserKindFavourite(buttonId);

					$("#dodaj-u-omiljene-id-" + r.id).click(addRestaurantToFavourite(r.id));
				}
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju picerija!");
		}
	});
}

//**** Prikaz poslasticarnica ****
function showConfectioneryRestaurants() {
    
    $.ajax ({
		type : 'GET',
		url : 'rest/restaurant/restaurants',
		
		success : function(restorani) {
			
			$('#restorani-poslasticarnica-prikaz-svi').empty();
			
			for(var r of restorani) { 
				if(r.obrisan == false && r.kategorijaRestorana == "poslastičarnica") {
					
					var newR = $('<div class="col-sm-3"><br><div class="card text-center" style="width: 15rem;"><img class="card-img-top" src="pictures/15_0.png"alt="Card image cap"><div class="card-body"><a href="#" id="poslasticarnica-restoran-id-' + r.id + '"><h5 class="card-title">' + r.naziv + '</h5></a><p class="card-text">' + r.adresa + '</p><button id="dodaj-u-omiljene-id-' + r.id + '" class="btn btn-primary">OMILJEN</button></div></div></div>');
					$('#restorani-poslasticarnica-prikaz-svi').append(newR);

					$('#poslasticarnica-restoran-id-'+ r.id).click(showAllArticlesFromRestaturant(r));
					var buttonId = "dodaj-u-omiljene-id-" + r.id;
					checkUserKindFavourite(buttonId);

					$("#dodaj-u-omiljene-id-" + r.id).click(addRestaurantToFavourite(r.id));
				}
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju poslastičarnica!");
		}
	});
}

//**** Prikaz indijskih restorana ****
function showIndianFoodRestaurants() {
    
    $.ajax ({
		type : 'GET',
		url : 'rest/restaurant/restaurants',
		
		success : function(restorani) {
			
			$('#restorani-indijska-hrana-prikaz-svi').empty();
			
			for(var r of restorani) { 
				if(r.obrisan == false && r.kategorijaRestorana == "indijski restoran") {
					
					var newR = $('<div class="col-sm-3"><br><div class="card text-center" style="width: 15rem;"><img class="card-img-top" src="pictures/ind.png"alt="Card image cap"><div class="card-body"><a href="#" id="indijska-hrana-restoran-id' + r.id + '"><h5 class="card-title">' + r.naziv + '</h5></a><p class="card-text">' + r.adresa + '</p><button id="dodaj-u-omiljene-id-' + r.id + '" class="btn btn-primary">OMILJEN</button></div></div></div>');
					$('#restorani-indijska-hrana-prikaz-svi').append(newR);

					$('#indijska-hrana-restoran-id-'+ r.id).click(showAllArticlesFromRestaturant(r));
					var buttonId = "dodaj-u-omiljene-id-" + r.id;
					checkUserKindFavourite(buttonId);

					$("#dodaj-u-omiljene-id-" + r.id).click(addRestaurantToFavourite(r.id));
				}
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju restorana indijske hrane!");
		}
	});
}

//**** Prikaz restorana kineske hrane ****
function showChineseFoodRestaurants() {
    
    $.ajax ({
		type : 'GET',
		url : 'rest/restaurant/restaurants',
		
		success : function(restorani) {
			
			$('#restorani-kineska-hrana-prikaz-svi').empty();
			
			for(var r of restorani) { 
				if(r.obrisan == false && r.kategorijaRestorana == "kineski restoran") {
					
					var newR = $('<div class="col-sm-3"><br><div class="card text-center" style="width: 15rem;"><img class="card-img-top" src="pictures/kina.png"alt="Card image cap"><div class="card-body"><a href="#" id="kineska-hrana-restoran-id-' + r.id + '"><h5 class="card-title">' + r.naziv + '</h5></a><p class="card-text">' + r.adresa + '</p><button id="dodaj-u-omiljene-id-' + r.id + '" class="btn btn-primary">OMILJEN</button></div></div></div>');
					$('#restorani-kineska-hrana-prikaz-svi').append(newR);

					$('#kineska-hrana-restoran-id-'+ r.id).click(showAllArticlesFromRestaturant(r));
					var buttonId = "dodaj-u-omiljene-id-" + r.id;
					checkUserKindFavourite(buttonId);

					$("#dodaj-u-omiljene-id-" + r.id).click(addRestaurantToFavourite(r.id));
				}
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju restorana kineske hrane!");
		}
	});
}

//**** Prikaz restorana koji nude rostilj ****
function showGrillRestaurants() {
    
    $.ajax ({
		type : 'GET',
		url : 'rest/restaurant/restaurants',
		
		success : function(restorani) {
			
			$('#restorani-rostilj-prikaz-svi').empty();
			
			for(var r of restorani) { 
				if(r.obrisan == false && r.kategorijaRestorana == "roštilj") {
					
					var newR = $('<div class="col-sm-3"><br><div class="card text-center" style="width: 15rem;"><img class="card-img-top" src="pictures/4_0.png"alt="Card image cap"><div class="card-body"><a href="#" id="rostilj-restoran-id-' + r.id + '"><h5 class="card-title">' + r.naziv + '</h5></a><p class="card-text">' + r.adresa + '</p><button id="dodaj-u-omiljene-id-' + r.id + '" class="btn btn-primary">OMILJEN</button></div></div></div>');
					$('#restorani-rostilj-prikaz-svi').append(newR);

					$('#rostilj-restoran-id-'+ r.id).click(showAllArticlesFromRestaturant(r));
					var buttonId = "dodaj-u-omiljene-id-" + r.id;
					checkUserKindFavourite(buttonId);

					$("#dodaj-u-omiljene-id-" + r.id).click(addRestaurantToFavourite(r.id));
				}
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju restorana roštilja!");
		}
	});
}

//**** Prikaz restorana domace kuhinje ****
function showDomesticFoodRestaurants() {
    
    $.ajax ({
		type : 'GET',
		url : 'rest/restaurant/restaurants',
		
		success : function(restorani) {
			
			$('#restorani-domaca-hrana-prikaz-svi').empty();
			
			for(var r of restorani) { 
				if(r.obrisan == false && r.kategorijaRestorana == "domaća kuhinja") {
					
					var newR = $('<div class="col-sm-3"><br><div class="card text-center" style="width: 15rem;"><img class="card-img-top" src="pictures/domaca.png"alt="Card image cap"><div class="card-body"><a href="#" id="domaci-restoran-id-' + r.id + '"><h5 class="card-title">' + r.naziv + '</h5></a><p class="card-text">' + r.adresa + '</p><button id="dodaj-u-omiljene-id-' + r.id + '" class="btn btn-primary">OMILJEN</button></div></div></div>');
					$('#restorani-domaca-hrana-prikaz-svi').append(newR);

					$('#domaci-restoran-id-'+ r.id).click(showAllArticlesFromRestaturant(r));
					var buttonId = "dodaj-u-omiljene-id-" + r.id;
					checkUserKindFavourite(buttonId);

					$("#dodaj-u-omiljene-id-" + r.id).click(addRestaurantToFavourite(r.id));
				}
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju restorana domaće hrane!");
		}
	});
}

function addRestaurantToFavourite(restaurantId) {
	return function(e) {
		e.preventDefault();

		$.ajax ({
			type : 'PUT',
			url : "rest/user/favourite/" + restaurantId,
			
			success : function() {
				alert("Restoran je uspešno dodat u listu omiljenih!");
			},
			
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

function showAllArticlesFromRestaturant(r) {
	return function(e) {
		e.preventDefault();

		var listaJela = r.listaJela;
		var listaPica = r.listaPica;

		hideRestaurantsAndTitles();
		$('#tabela-artikala-svi').show();
		
		$.ajax ({
			type : 'GET',
			url : 'rest/article/articles',
			
			success : function(artikli) {
				
				var num = 0;
				checkUserKind();
				// setovanje h3 - naslov za artikle restorana
				var header = document.getElementById("naslov-restorana-meni");
				header.innerHTML = "Meni restorana: \"" + r.naziv + "\"";
				
				$('#artikli-tbody-id').empty();
				
				for(var a of artikli) { 
					if(a.obrisan == false ) {
						if(listaPica.includes(a.id) || listaJela.includes(a.id)) {
							num++;
							var tr = $('<tr></tr>');
							var number = $('<td>' + num + '</td>');
							var naziv = $('<td>' + a.naziv + '</td>');
		
							var mera;
							if (a.tipJela == "jelo") {
								mera = "g";
							} else {
								mera = "ml";
							}
		
							var kolicina = $('<td>' + a.kolicina + mera + '</td>');
						
							var opis = $('<td>' + a.opis + '</td>');
							var cena = $('<td>' + a.jedinicnaCena + '</td>');

							var komada =$('<td><select class="form-control" id="kolicina-artikla-' + a.id + '" name="kolicina-artikla-' + a.id + '"><option value="1" selected>1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option></select></td>');

							var poruci = $('<td><button type="button" class="btn btn-secondary" class="kupac-poruci" id="poruci-artikl-' + a.id + '">Poruči</button></td>');

							tr.append(number).append(naziv).append(kolicina).append(opis).
								append(cena).append(komada).append(poruci);
							$('#artikli-tbody-id').append(tr);
							
							$('#poruci-artikl-' + a.id).click(addToCart(a));
						}
					}
				}
			},
			
			error : function() {
				alert("Greška pri učitavanju tabele artikala!");
			}
		});
	}
}

function addToCart(artikl) {
	return function(e) {
		e.preventDefault();
		
		var kolicina = $("#kolicina-artikla-" + artikl.id).val();

		$.ajax ({
			type : 'POST',
			url : 'rest/user/addToCart',
			data : JSON.stringify({
				kolicina : kolicina,
				artikl : artikl
			}),
			contentType : 'application/json',
			success : function() {
				alert("Dodavanje artikla u korpu je uspešno!");
			},
			
			error : function(message) {
				alert(message.responseText);
			}
		});
		
	}
}

function showChosenOption() {

	hideRestaurantsAndTitles();
	$('#tabela-artikala-svi').hide();

	// klik na restorane domace hrane iz restaurants.html
    $('#domaca-hrana-prikaz-restorana').click(function(e) {
		
		e.preventDefault();

		$('#tabela-artikala-svi').hide();

		$("#restorani-domaca-hrana-prikaz-svi").delay(250).fadeIn(250);
		$('#restorani-rostilj-prikaz-svi').hide();
		$('#restorani-kineska-hrana-prikaz-svi').hide();
		$('#restorani-indijska-hrana-prikaz-svi').hide();
		$('#restorani-poslasticarnica-prikaz-svi').hide();
		$('#restorani-picerija-prikaz-svi').hide();

		$('#naslov-restorani-domaca-hrana').delay(250).fadeIn(250);
		$('#naslov-restorani-rostilj').hide();
		$('#naslov-restorani-kineska-hrana').hide();
		$('#naslov-restorani-indijska-hrana').hide();
		$('#naslov-restorani-poslasticarnica').hide();
		$('#naslov-restorani-picerija').hide();
		
		showDomesticFoodRestaurants();
	});
	// klik na rostilj restorane iz restaurants.html
	$('#rostilj-prikaz-restorana').click(function(e) {
		
		e.preventDefault();

		$('#tabela-artikala-svi').hide();

		$('#naslov-restorani-domaca-hrana').hide();
		$('#restorani-rostilj-prikaz-svi').delay(250).fadeIn(250);
		$('#restorani-kineska-hrana-prikaz-svi').hide();
		$('#restorani-indijska-hrana-prikaz-svi').hide();
		$('#restorani-poslasticarnica-prikaz-svi').hide();
		$('#restorani-picerija-prikaz-svi').hide();

		$("#restorani-domaca-hrana-prikaz-svi").hide();
		$('#naslov-restorani-rostilj').delay(250).fadeIn(250);
		$('#naslov-restorani-kineska-hrana').hide();
		$('#naslov-restorani-indijska-hrana').hide();
		$('#naslov-restorani-poslasticarnica').hide();
		$('#naslov-restorani-picerija').hide();
		
		showGrillRestaurants();
	});
	// klik na kineske restorane iz restaurants.html
	$('#kineska-hrana-prikaz-restorana').click(function(e) {
		
		e.preventDefault();

		$('#tabela-artikala-svi').hide();

		$('#naslov-restorani-domaca-hrana').hide();
		$('#restorani-rostilj-prikaz-svi').hide();
		$('#restorani-kineska-hrana-prikaz-svi').delay(250).fadeIn(250);
		$('#restorani-indijska-hrana-prikaz-svi').hide();
		$('#restorani-poslasticarnica-prikaz-svi').hide();
		$('#restorani-picerija-prikaz-svi').hide();

		$("#restorani-domaca-hrana-prikaz-svi").hide();
		$('#naslov-restorani-rostilj').hide();
		$('#naslov-restorani-kineska-hrana').delay(250).fadeIn(250);
		$('#naslov-restorani-indijska-hrana').hide();
		$('#naslov-restorani-poslasticarnica').hide();
		$('#naslov-restorani-picerija').hide();
		
		showChineseFoodRestaurants();
	});
	// klik na indijske restorane iz restaurants.html
	$('#indijska-hrana-prikaz-restorana').click(function(e) {
		
		e.preventDefault();

		$('#tabela-artikala-svi').hide();

		$('#naslov-restorani-domaca-hrana').hide();
		$('#restorani-rostilj-prikaz-svi').hide();
		$('#restorani-kineska-hrana-prikaz-svi').hide();
		$('#restorani-indijska-hrana-prikaz-svi').delay(250).fadeIn(250);
		$('#restorani-poslasticarnica-prikaz-svi').hide();
		$('#restorani-picerija-prikaz-svi').hide();

		$("#restorani-domaca-hrana-prikaz-svi").hide();
		$('#naslov-restorani-rostilj').hide();
		$('#naslov-restorani-kineska-hrana').hide();
		$('#naslov-restorani-indijska-hrana').delay(250).fadeIn(250);
		$('#naslov-restorani-poslasticarnica').hide();
		$('#naslov-restorani-picerija').hide();
		
		showIndianFoodRestaurants();
	});
	// klik na poslasticarnice iz restaurants.html
	$('#poslasticarnica-prikaz-restorana').click(function(e) {
		
		e.preventDefault();

		$('#tabela-artikala-svi').hide();

		$('#naslov-restorani-domaca-hrana').hide();
		$('#restorani-rostilj-prikaz-svi').hide();
		$('#restorani-kineska-hrana-prikaz-svi').hide();
		$('#restorani-indijska-hrana-prikaz-svi').hide();
		$('#restorani-poslasticarnica-prikaz-svi').delay(250).fadeIn(250);
		$('#restorani-picerija-prikaz-svi').hide();

		$("#restorani-domaca-hrana-prikaz-svi").hide();
		$('#naslov-restorani-rostilj').hide();
		$('#naslov-restorani-kineska-hrana').hide();
		$('#naslov-restorani-indijska-hrana').hide();
		$('#naslov-restorani-poslasticarnica').delay(250).fadeIn(250);
		$('#naslov-restorani-picerija').hide();
		
		showConfectioneryRestaurants();
	});
	// klik na picerije iz restaurants.html
	$('#picerija-prikaz-restorana').click(function(e) {
		
		e.preventDefault();

		$('#tabela-artikala-svi').hide();

		$('#naslov-restorani-domaca-hrana').hide();
		$('#restorani-rostilj-prikaz-svi').hide();
		$('#restorani-kineska-hrana-prikaz-svi').hide();
		$('#restorani-indijska-hrana-prikaz-svi').hide();
		$('#restorani-poslasticarnica-prikaz-svi').hide();
		$('#restorani-picerija-prikaz-svi').delay(250).fadeIn(250);

		$("#restorani-domaca-hrana-prikaz-svi").hide();
		$('#naslov-restorani-rostilj').hide();
		$('#naslov-restorani-kineska-hrana').hide();
		$('#naslov-restorani-indijska-hrana').hide();
		$('#naslov-restorani-poslasticarnica').hide();
		$('#naslov-restorani-picerija').delay(250).fadeIn(250);
		
		showPizzaRestaurants();
	});
}

function hideRestaurantsAndTitles() {

	$('#naslov-restorani-domaca-hrana').hide();
	$('#naslov-restorani-rostilj').hide();
	$('#naslov-restorani-kineska-hrana').hide();
	$('#naslov-restorani-indijska-hrana').hide();
	$('#naslov-restorani-poslasticarnica').hide();
	$('#naslov-restorani-picerija').hide();

	$('#restorani-domaca-hrana-prikaz-svi').hide();
	$('#restorani-rostilj-prikaz-svi').hide();
	$('#restorani-kineska-hrana-prikaz-svi').hide();
	$('#restorani-indijska-hrana-prikaz-svi').hide();
	$('#restorani-poslasticarnica-prikaz-svi').hide();
	$('#restorani-picerija-prikaz-svi').hide();
}

function showRecivedOption() {
	var option = window.location.search;
	var parseOpt = option.split("=");
	var restType = parseOpt[1];

	if(restType == "domaca") {
		
		$('#naslov-restorani-domaca-hrana').delay(250).fadeIn(250);
		$("#restorani-domaca-hrana-prikaz-svi").delay(250).fadeIn(250);
		showDomesticFoodRestaurants();

	} else if (restType == "rostilj") {
		
		$('#naslov-restorani-rostilj').delay(250).fadeIn(250);
		$("#restorani-rostilj-prikaz-svi").delay(250).fadeIn(250);
		showGrillRestaurants();

	} else if (restType == "kineska") {
		
		$('#naslov-restorani-kineska-hrana').delay(250).fadeIn(250);
		$("#restorani-kineska-hrana-prikaz-svi").delay(250).fadeIn(250);
		showChineseFoodRestaurants();

	} else if (restType == "indijska") {
		
		$('#naslov-restorani-indijska-hrana').delay(250).fadeIn(250);
		$("#restorani-indijska-hrana-prikaz-svi").delay(250).fadeIn(250);
		showIndianFoodRestaurants();

	} else if (restType == "poslasticarnica") {
		
		$('#naslov-restorani-poslasticarnica').delay(250).fadeIn(250);
		$("#restorani-poslasticarnica-prikaz-svi").delay(250).fadeIn(250);
		showConfectioneryRestaurants();

	} else if (restType == "picerija") {
		
		$('#naslov-restorani-picerija').delay(250).fadeIn(250);
		$("#restorani-picerija-prikaz-svi").delay(250).fadeIn(250);
		showPizzaRestaurants();
	}
}

function checkUserKind() {
	$.ajax({
		url : "rest/user/loginstat"
	}).then(function(user) {

		$('td:nth-child(6),th:nth-child(6)').hide();
		$('td:nth-child(7),th:nth-child(7)').hide();

		if (user != undefined) {
			if (user.uloga == "kupac") {
				$('td:nth-child(6),th:nth-child(6)').show();
				$('td:nth-child(7),th:nth-child(7)').show();
			}
		}
	});
}

function checkUserKindFavourite(buttonId) {
	$.ajax({
		url : "rest/user/loginstat"
	}).then(function(user) {

		$('#' + buttonId ).hide();

		if (user != undefined) {
			if (user.uloga == "kupac") {
				$('#' + buttonId ).show();
			}
		}
	});
}