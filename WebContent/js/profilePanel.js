/**
 * @author game_changer96
 */

var baseUrl = 'http://localhost:8080/DMProject/rest/';

$(document).ready(function() {

	$('#admin-part').hide();
	$('#deliverer-part').hide();
	$('#buyer-part').hide();

	checkUserKind();
	
	showChosenOptionAdmin();
	showChosenOptionBuyer();
	showChosenOptionDeliverer();

});

function showChosenOptionDeliverer() {
	hideDelivererThings();

	$('#aktuelne-porudzbine').click(function(e) {
		$("#aktuelne-porudzbine-prikaz-dostavljac").delay(250).fadeIn(250);
		$("#preuzeta-porudzbina-prikaz-dostavljac").hide();
		$("#istorija-porudzbina-prikaz-dostavljac").hide();
		$("#dobrodosli").hide();
		e.preventDefault();

		showAvailableOrders();
	});

	$('#dodeljena-porudzbina').click(function(e) {
		$("#aktuelne-porudzbine-prikaz-dostavljac").hide();
		$("#preuzeta-porudzbina-prikaz-dostavljac").delay(250).fadeIn(250);
		$("#istorija-porudzbina-prikaz-dostavljac").hide();
		$("#dobrodosli").hide();
		e.preventDefault();
		
		showCurrentOrder();
	});

	$('#istorija-porudzbina').click(function(e) {
		$("#aktuelne-porudzbine-prikaz-dostavljac").hide();
		$("#preuzeta-porudzbina-prikaz-dostavljac").hide();
		$("#istorija-porudzbina-prikaz-dostavljac").delay(250).fadeIn(250);
		$("#dobrodosli").hide();
		e.preventDefault();
		
		showOrdersHistoryDeliverer();
	});
}

function showOrdersHistoryDeliverer() {

	$.ajax({
		type : 'GET',
		url : "rest/order/orders",

		success : function(orders) {

			$.ajax({
				type : "GET",
				url : "rest/user/loginstat", 
		
				success : function(user) {

					$('#istorija-porudzbina-prikaz-dostavljac-tbody-id').empty();

					for(var o of orders) {
						if(o.obrisana == false  && user.username == o.dostavljac.username) {
							var tr = $('<tr></tr>');
							var id = $('<td>' + o.id + '</td>');
							var datumIVremePorudzbine = $('<td>' + o.datumIVremePorudzbine + '</td>');
							var kupac = $('<td>' + o.kupac.username + '</td>');
							var status = $('<td>' + o.statusPorudzbine + '</td>');
							var detalji = $('<td><button type="button" class="btn btn-primary" id="detalji-porudzbina-' + o.id + '">Detalji</button></td>');
		
							tr.append(id).append(datumIVremePorudzbine).append(kupac).append(status).append(detalji);
							$("#istorija-porudzbina-prikaz-dostavljac-tbody-id").append(tr);
							
							$('#detalji-porudzbina-' + o.id).click(setModalOrderDetails(o));
						}				 
					}
				}
			});
		},
		
		error : function() {
			alert("Greška pri učitavanju trenutne porudžbine!");
		}
	});
}

function showCurrentOrder() {
	$.ajax({
		type : 'GET',
		url : "rest/order/orders",

		success : function(orders) {

			$.ajax({
				type : "GET",
				url : "rest/user/loginstat", 
		
				success : function(user) {

					$('#preuzeta-porudzbina-prikaz-dostavljac-tbody-id').empty();

					for(var o of orders) {
						if(o.obrisana == false && o.statusPorudzbine == "dostava u toku" && user.username == o.dostavljac.username) {
							var tr = $('<tr></tr>');
							var id = $('<td>' + o.id + '</td>');
							var datumIVremePorudzbine = $('<td>' + o.datumIVremePorudzbine + '</td>');
							var kupac = $('<td>' + o.kupac.username + '</td>');
							var status = $('<td>' + o.statusPorudzbine + '</td>');
							var detalji = $('<td><button type="button" class="btn btn-primary" id="detalji-porudzbina-' + o.id + '">Detalji</button></td>');
							var dostavi = $('<td><button type="button" class="btn btn-secondary" id="dostavi-porudzbina-' + o.id + '">Dostavi</button></td>');
		
							tr.append(id).append(datumIVremePorudzbine).append(kupac).append(status).append(detalji).append(dostavi);
							$("#preuzeta-porudzbina-prikaz-dostavljac-tbody-id").append(tr);
							
							$('#detalji-porudzbina-' + o.id).click(setModalOrderDetails(o));
							$('#dostavi-porudzbina-' + o.id).unbind("click").bind("click",deliverOrder(o));
						}				 
					}
				}
			});
		},
		
		error : function() {
			alert("Greška pri učitavanju trenutne porudžbine!");
		}
	});
}

function deliverOrder(o) {
	return function(e) {
		e.preventDefault();

		$.ajax ({
			type : "PUT",
			url : "rest/order/deliver/" + o.id,

			success : function () {
				alert("Porudžbina je uspešno dostavljena");
				showCurrentOrder();
			},

			error : function (message) {
				alert(message.responseText);
			}
		});	
	}
}

function showAvailableOrders() {
	$.ajax({
		type : 'GET',
		url : "rest/order/orders",

		success : function(orders) {
			
			$('#aktuelne-porudzbine-prikaz-dostavljac-tbody-id').empty();

			for(var o of orders) {
				if(o.obrisana == false && o.statusPorudzbine == "poručeno") {
					var tr = $('<tr></tr>');
					var id = $('<td>' + o.id + '</td>');
					var datumIVremePorudzbine = $('<td>' + o.datumIVremePorudzbine + '</td>');
					var kupac = $('<td>' + o.kupac.username + '</td>');
					var status = $('<td>' + o.statusPorudzbine + '</td>');
					var detalji = $('<td><button type="button" class="btn btn-primary" id="detalji-porudzbina-' + o.id + '">Detalji</button></td>');
					var preuzmi = $('<td><button type="button" class="btn btn-secondary" id="preuzmi-porudzbina-' + o.id + '">Preuzmi</button></td>');

					tr.append(id).append(datumIVremePorudzbine).append(kupac).append(status).append(detalji).append(preuzmi);
					$("#aktuelne-porudzbine-prikaz-dostavljac-tbody-id").append(tr);
					
					$('#detalji-porudzbina-' + o.id).click(setModalOrderDetails(o));
					$('#preuzmi-porudzbina-' + o.id).click(setModalTakeOrder(o));
				}				 
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju tabele novih porudžbina!");
		}
	});
}

function setModalTakeOrder(o) {
	return function(e) {
		e.preventDefault();

		$.ajax({
			type : "GET",
			url : "rest/user/loginstat", 
	
			success : function(user) {
				if(user.uloga == "dostavljač" && user.zauzet == false) {
					$("#dostavljac-izbor-vozila-modal").modal("show");
					$.ajax ({
						type : 'GET',
						url : 'rest/vehicle/vehicles',
						
						success : function(vozila) {
	
							$('#dostupna-vozila-dostavljac').empty();
							var option= $('<option value="' + -1 + '" selected>Peške</option>');
							$("#dostupna-vozila-dostavljac").append(option);

							for(var v of vozila) {
								if(v.obrisano == false && v.voziloSlobodno == true) {
									option= $('<option value="' + v.id + '">' + v.marka + ' ' + v.model + '</option>');
									$("#dostupna-vozila-dostavljac").append(option);
								}
							}

							$('#potvrda-preuzmi-porudzbinu').unbind("click").bind("click",takeOrder(o));
						}
					});
	
				} else {
					alert("Novu porudžbinu možete preuzeti nakon što dostavite aktuelnu!");
				}
			}
		});
	}
}

function takeOrder(o) {
	return function(e) {
		e.preventDefault();

		var idVozila = $("#dostupna-vozila-dostavljac").val();
		var idPorudzbina = o.id;

		$.ajax ({
			type : "PUT",
			url : "rest/order/takeOrder/" + idPorudzbina + "/" + idVozila,

			success : function () {
				alert("Porudžbina je uspešno preuzeta!");
				showAvailableOrders();
				$("#dostavljac-izbor-vozila-modal").modal("hide");
			},

			error : function (message) {
				alert(message.responseText);
			}
		});
	}
}

function hideDelivererThings() {

	$("#aktuelne-porudzbine-prikaz-dostavljac").hide();
	$("#preuzeta-porudzbina-prikaz-dostavljac").hide();
	$("#istorija-porudzbina-prikaz-dostavljac").hide();
	$("#dobrodosli").show();
}

function showChosenOptionBuyer() {

	hideBuyerThings();

	$('#omiljeni-restorani').click(function(e) {
		$("#omiljeni-restorani-prikaz-kupac").delay(250).fadeIn(250);
		$("#lista-porudzbina-prikaz-kupac").hide();
		$("#dobrodosli").hide();
		e.preventDefault();

		setUserFavouriteRestaurants();
	});

	$('#lista-porudzbina').click(function(e) {
		$("#omiljeni-restorani-prikaz-kupac").hide();
		$("#lista-porudzbina-prikaz-kupac").delay(250).fadeIn(250);
		$("#dobrodosli").hide();
		e.preventDefault();
		
		showOrdersHistory();
	});
}

function showOrdersHistory() {
	$.ajax({
		type : 'GET',
		url : "rest/order/orders",

		success : function(orders) {
			
			$('#lista-porudzbina-prikaz-kupac-tbody-id').empty();

			$.ajax({
				type : 'GET',
				url : "rest/user/loginstat"
			}).then(function(user) {
				
				var num = 0;

				for(var o of orders) {
					if(o.obrisana == false && o.kupac.username == user.username) {
						num++;
						var tr = $('<tr></tr>');
						var number = $('<td>' + num + '</td>');
						var datumIVremePorudzbine = $('<td>' + o.datumIVremePorudzbine + '</td>');
						var status = $('<td>' + o.statusPorudzbine + '</td>');
						var detalji = $('<td><button type="button" class="btn btn-primary" id="detalji-porudzbina-' + o.id + '">Detalji</button></td>');
						tr.append(number).append(datumIVremePorudzbine).append(status).append(detalji);
						$("#lista-porudzbina-prikaz-kupac-tbody-id").append(tr);
						
						$('#detalji-porudzbina-' + o.id).click(setModalOrderDetails(o));
					}				 
				}
			});
		},
		
		error : function() {
			alert("Greška pri učitavanju tabele istorije porudžbina!");
		}
	});
}

function setModalOrderDetails(order) {
	return function(e) {
		e.preventDefault();

		$("#detalji-porudzbine-kupac-modal").modal("show");

		$('#artikli-tbody-id-modal').empty();
		var num = 0;
		for(var s of order.stavkePorudzbine) {
			num++;
			var a = s.artikl;

			var tr = $('<tr></tr>');
			var number = $('<td>' + num + '</td>');
			var naziv = $('<td>' + a.naziv + '</td>');
			var cena = $('<td>' + s.kolicina + '  x  ' + a.jedinicnaCena + ' RSD</td>');

			tr.append(number).append(naziv).append(cena);
			$('#artikli-tbody-id-modal').append(tr);
		}

		document.getElementById('prikaz-cene-korpe-modal').innerHTML = order.punaCena - order.punaCena * 0.03 * order.bonusPoeni + " RSD";
		document.getElementById('napomena-detalji-porudzbine-korisnik').innerHTML = order.napomena;
		document.getElementById('bonus-poeni-detalji-porudzbine-korisnik').innerHTML = order.bonusPoeni;
	}
}

function setUserFavouriteRestaurants() {

	$.ajax({
		url : "rest/user/loginstat"
	}).then(function(user) {
	
		if (user != undefined) {
			if (user.uloga == "kupac") {
				showFavouriteRestaurants(user);
			}
		}
	});
}

function showFavouriteRestaurants(user) {

	$.ajax ({
		type : 'GET',
		url : 'rest/restaurant/restaurants',
		
		success : function(restorani) {
			
			$('#omiljeni-restorani-prikaz').empty();
			
			for(var r of restorani) { 
				if(r.obrisan == false && user.listaOmiljenihRestorana.includes(r.id)) {
					
					var newR = $('<div class="col-sm-3"><br><div class="card text-center" style="width: 15rem;"><img class="card-img-top" src="pictures/domaca.png"alt="Card image cap"><div class="card-body"><a href="#" id="omiljeni-restoran-id-' + r.id + '"><h5 class="card-title">' + r.naziv + '</h5></a><p class="card-text">' + r.adresa + '</p><button id="izbaci-iz-omiljenih-id-' + r.id + '" class="btn btn-danger">IZBACI</button></div></div></div>');
					$('#omiljeni-restorani-prikaz').append(newR);

					//$('#omiljeni-restoran-id-'+ r.id).click(showAllArticlesFromRestaturant(r));
					$("#izbaci-iz-omiljenih-id-" + r.id).click(removeRestaurantFromFavourite(r.id, user));
				}
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju restorana domaće hrane!");
		}
	});
}

function removeRestaurantFromFavourite(restaurantId, user) {
	return function(e) {
		e.preventDefault();

		$.ajax ({
			type : 'PUT',
			url : "rest/user/unfavourite/" + restaurantId,
			
			success : function() {
				alert("Restoran sa ID-jem" + restaurantId + " nije više u listi omiljenih!");
				setUserFavouriteRestaurants();
			},
			
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

function showAllArticlesFromRestaturant(r) {
	
}

function hideBuyerThings() {

	$("#omiljeni-restorani-prikaz-kupac").hide();
	$("#lista-porudzbina-prikaz-kupac").hide();
	$("#dobrodosli").show();
}

function showChosenOptionAdmin() {

	hideAdminTables();

	$('#korisnici').click(function(e) {
		$("#korisnici-admin-table").delay(250).fadeIn(250);
		$("#restorani-admin-table").hide();
		$("#vozila-admin-table").hide();
		$("#artikli-admin-table").hide();
		$("#porudzbine-admin-table").hide();
		$("#artikli-admin-order-table").hide();
		$("#dobrodosli").hide();
		e.preventDefault();
		
		showUsersTable();
	});

	$('#restorani').click(function(e) {
		$("#restorani-admin-table").delay(250).fadeIn(250);
		$("#korisnici-admin-table").hide();
		$("#vozila-admin-table").hide();
		$("#artikli-admin-table").hide();
		$("#porudzbine-admin-table").hide();
		$("#artikli-admin-order-table").hide();
		$("#dobrodosli").hide();
		e.preventDefault();
		
		showRestaurantsTable();
	});

	$('#vozila-admin').click(function(e) {
		$("#vozila-admin-table").delay(250).fadeIn(250);
		$("#korisnici-admin-table").hide();
		$("#restorani-admin-table").hide();
		$("#artikli-admin-table").hide();
		$("#porudzbine-admin-table").hide();
		$("#artikli-admin-order-table").hide();
		$("#dobrodosli").hide();
		e.preventDefault();
		
		showVehiclesTable();
	});

	$('#artikli').click(function(e) {
		$("#artikli-admin-table").delay(250).fadeIn(250);
		$("#korisnici-admin-table").hide();
		$("#restorani-admin-table").hide();
		$("#vozila-admin-table").hide();
		$("#porudzbine-admin-table").hide();
		$("#artikli-admin-order-table").hide();
		$("#dobrodosli").hide();
		e.preventDefault();
		
		showArticlesTable();
	});

	$('#porudzbine').click(function(e) {
		$("#porudzbine-admin-table").delay(250).fadeIn(250);
		$("#korisnici-admin-table").hide();
		$("#restorani-admin-table").hide();
		$("#vozila-admin-table").hide();
		$("#artikli-admin-table").hide();
		$("#artikli-admin-order-table").hide();
		$("#dobrodosli").hide();
		e.preventDefault();
		
		showOrdersTable();
	});
}

function showOrdersTable() {
	$.ajax({
		type : 'GET',
		url : "rest/order/orders",

		success : function(orders) {
			
			$('#porudzbine-tbody-id').empty();
			for(var o of orders) {
				if(o.obrisana == false) {
					var tr = $('<tr></tr>');
					var id = $('<td>' + o.id + '</td>');
					var datumIVremePorudzbine = $('<td>' + o.datumIVremePorudzbine + '</td>');
					var kupac = $('<td>' + o.kupac.username + '</td>');
					var dostavljac = $('<td>' + o.dostavljac.username + '</td>');
					var status = $('<td>' + o.statusPorudzbine + '</td>');
					var detalji = $('<td><button type="button" class="btn btn-primary" id="detalji-porudzbina-' + o.id + '">Detalji</button></td>');
					var izmena = $('<td><button type="button" class="btn btn-secondary" id="izmeni-porudzbina-' + o.id + '">Izmeni</button></td>');
					var brisanje = $('<td><button type="button" class="btn btn-danger" id="obrisi-porudzbina-' + o.id + '">Obriši</button></td>');

					tr.append(id).append(datumIVremePorudzbine).append(kupac).append(dostavljac).append(status).append(detalji).append(izmena).append(brisanje);
					$("#porudzbine-tbody-id").append(tr);
					
					$('#detalji-porudzbina-' + o.id).click(setModalOrderDetails(o));
					$('#izmeni-porudzbina-' + o.id).click(setModalEditOrderAdmin(o));
					$('#obrisi-porudzbina-' + o.id).click(deleteOrderAdmin(o));
				}				 
			}

			$("#dodaj-novu-porudzbinu").unbind("click").bind("click",showAllArticlesForAdminOrder());
		},
		
		error : function() {
			alert("Greška pri učitavanju tabele postojećih porudžbina!");
		}
	});
}

function setModalEditOrderAdmin(o) {
	return function(e) {
		e.preventDefault();

		$("#izmena-porudzbine-admin-modal").modal("show");
		
		// punjenje modala postojecim podacima pre izmene
		document.getElementById('prikaz-cene-izmena-porudzbine-admin-modal').innerHTML = o.punaCena - o.punaCena * 0.03 * o.bonusPoeni + " RSD";	
		$('select[name=status-izmena-porudzbine-admin-modal]').val(o.statusPorudzbine);
		document.getElementById('napomena-dostavljac-izmena-porudzbine-admin-modal').value = o.napomena;

		// popunjavanje stavki porudzbine
		$('#tabela-izmena-porudzbine-admin-tbody-id-modal').empty();
		var num = 0;
		for(var s of o.stavkePorudzbine) {
			num++;
			var a = s.artikl;

			var tr = $('<tr></tr>');
			var number = $('<td>' + num + '</td>');
			var naziv = $('<td>' + a.naziv + '</td>');
			var cena = $('<td>' + s.kolicina + '  x  ' + a.jedinicnaCena + ' RSD</td>');

			tr.append(number).append(naziv).append(cena);
			$('#tabela-izmena-porudzbine-admin-tbody-id-modal').append(tr);
		}

		$.ajax ({
			type : 'GET',
			url : 'rest/user/users',
			
			success : function(korisnici) {

				$("#kupac-izmena-porudzbine-admin-modal").empty();

				$("#dostavljac-izmena-porudzbine-admin-modal").empty();
				var optionD = $('<option value="nema">nema dostavljača</option>');
				$("#dostavljac-izmena-porudzbine-admin-modal").append(optionD);

				for(var k of korisnici) {
					if(k.uloga == "kupac") {
						var optionK = $('<option value="' + k.username + '">' + k.username + '</option>');
						$("#kupac-izmena-porudzbine-admin-modal").append(optionK);
					}
					if(k.username == o.dostavljac.username) {
						optionD = $('<option value="' + k.username + '">' + k.username + '</option>');
						$("#dostavljac-izmena-porudzbine-admin-modal").append(optionD);
					}
					if(k.uloga == "dostavljač" && k.zauzet == false) {
						optionD = $('<option value="' + k.username + '">' + k.username + '</option>');
						$("#dostavljac-izmena-porudzbine-admin-modal").append(optionD);
					}			
				}

				$('select[name=kupac-izmena-porudzbine-admin-modal]').val(o.kupac.username);

				$('select[name=koliko-bonus-poena-izmena-porudzbine-admin-modal]').val(o.bonusPoeni);

				if(o.dostavljac.username != "") {
					$('select[name=dostavljac-izmena-porudzbine-admin-modal]').val(o.dostavljac.username);
				} else {
					$('select[name=dostavljac-izmena-porudzbine-admin-modal]').val("");
				}
				
				$("#koliko-bonus-poena-izmena-porudzbine-admin-modal").change(editFinalPrize(o.punaCena));

				$("#potvrda-izmena-porudzbine-admin-modal").unbind("click").bind("click",sendChangedOrder(o.id));
			}
		});	
	}
}

function showAllArticlesForAdminOrder() {
	return function (e) {
		e.preventDefault();

		$.ajax ({
			type : 'GET',
			url : 'rest/article/articles',
			
			success : function(artikli) {
				$('#artikli-admin-order-tbody-id').empty();
				
				for(var a of artikli) { 
					if(a.obrisan == false) {
						var tr = $('<tr></tr>');
						var number = $('<td>' + a.id + '</td>');
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
						var komada =$('<td><select class="form-control" id="kolicina-artikla-admin' + a.id + '" name="kolicina-artikla-admin' + a.id + '"><option value="1" selected>1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option></select></td>');
						var dodavanje = $('<td><button type="button" class="btn btn-info" id="dodaj-artikl-admin-' + a.id + '">Izaberi</button></td>');
						tr.append(number).append(naziv).append(kolicina).append(opis).
							append(cena).append(komada).append(dodavanje);
						$('#artikli-admin-order-tbody-id').append(tr);
						$('#dodaj-artikl-admin-' + a.id).unbind("click").bind("click",addArticleToAdminOrder1(a));				
					}
				}
				
				$('#zavrsi-ubacivanje-u-korpu').unbind("click").bind("click", setModalCreateNewOrderAdmin1());
				hideAdminTables();
				$("#artikli-admin-order-table").show();
			},
			
			error : function() {
				alert("Greška pri učitavanju tabele artikala!");
			}
		});
	}	
}

function addArticleToAdminOrder1(artikl) {
	return function(e) {
		e.preventDefault();

		var kolicina = $("#kolicina-artikla-admin" + artikl.id).val();

		$.ajax ({
			type : 'POST',
			url : 'rest/user/addToCartAdmin',
			data : JSON.stringify({
				kolicina : kolicina,
				artikl : artikl
			}),
			contentType : 'application/json',
			success : function() {
				alert("Dodavanje artikla od strane admina je uspešno!");
			},
			
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

function setModalCreateNewOrderAdmin1() {
	return function(e) {
		e.preventDefault();

		$.ajax ({
			type: 'GET',
			url: 'rest/user/cartItemsAdmin',
	
			success : function(stavkePorudzbine) {
				var num = 0;
				var ukupnaCena = 0;

				// popunjavanje stavki porudzbine
				$('#tabela-kreiranje-porudzbine-admin-tbody-id-modal').empty();
				for(var s of stavkePorudzbine) {
					num++;
					var a = s.artikl;
					var kolicina = s.kolicina;
					ukupnaCena = ukupnaCena + kolicina * a.jedinicnaCena;
	
					var tr = $('<tr></tr>');
					var number = $('<td>' + num + '</td>');
					var naziv = $('<td>' + a.naziv + '</td>');
					var cena = $('<td>' + s.kolicina + '  x  ' + a.jedinicnaCena + ' RSD</td>');
	
					tr.append(number).append(naziv).append(cena);
					$('#tabela-kreiranje-porudzbine-admin-tbody-id-modal').append(tr);
				}
	
				if(num = 0) {
					alert("Da biste nastavili s kreiranjem porudžbine, morate prvo dodati bar jedan artikl u nju!");
				} else {
					$("#kreiranje-porudzbine-admin-modal").modal("show");
					$("#tabela-kreiranje-porudzbine-admin-tbody-id-modal").show();
					document.getElementById('prikaz-cene-kreiranje-porudzbine-admin-modal').innerHTML = ukupnaCena + ' RSD';
					$("#deo-ispod-tabele-stavki-kreiranje-porudzbine-admin-modal").show();

					$.ajax ({
						type : 'GET',
						url : 'rest/user/users',
						
						success : function(korisnici) {
			
							$("#kupac-kreiranje-porudzbine-admin-modal").empty();
			
							$("#dostavljac-kreiranje-porudzbine-admin-modal").empty();
							var optionD = $('<option value="nema">nema dostavljača</option>');
							$("#dostavljac-kreiranje-porudzbine-admin-modal").append(optionD);
			
							for(var k of korisnici) {
								if(k.uloga == "kupac") {
									var optionK = $('<option value="' + k.username + '">' + k.username + '</option>');
									$("#kupac-kreiranje-porudzbine-admin-modal").append(optionK);
								}
								if(k.uloga == "dostavljač" && k.zauzet == false) {
									optionD = $('<option value="' + k.username + '">' + k.username + '</option>');
									$("#dostavljac-kreiranje-porudzbine-admin-modal").append(optionD);
								}			
							}
							
							$("#koliko-bonus-poena-kreiranje-porudzbine-admin-modal").change(editFinalPrize1(ukupnaCena));
			
							$("#potvrda-kreiranje-porudzbine-admin-modal").unbind("click").bind("click",sendNewOrderAdmin(stavkePorudzbine));
						}
					});	
				}
			},
			
			error : function() {
				alert("Greška pri učitavanju stavki iz admin korpe!");
			}
		});
	}
}

function editFinalPrize1(ukupnaCena) {
	return function (e) {
		e.preventDefault();

		var iskoristiBonusa = $("#koliko-bonus-poena-kreiranje-porudzbine-admin-modal").val();
		var novaCena = ukupnaCena - ukupnaCena * 0.03 * iskoristiBonusa;
		document.getElementById('prikaz-cene-kreiranje-porudzbine-admin-modal').innerHTML = novaCena + " RSD";
	}
}

function sendNewOrderAdmin(stavkePorudzbine) {
	return function(e) {
		e.preventDefault();

		var kupac = $("#kupac-kreiranje-porudzbine-admin-modal").val();
		var dostavljac = $("#dostavljac-kreiranje-porudzbine-admin-modal").val();
		var status = $("#status-kreiranje-porudzbine-admin-modal").val();
		var napomena = $('#napomena-dostavljac-kreiranje-porudzbine-admin-modal').val();
		var bonusPoeni = $("#koliko-bonus-poena-kreiranje-porudzbine-admin-modal").val();

		if(dostavljac == "nema" && (status == "dostava u toku" || status == "dostavljeno")) {
			alert("Ukoliko je porudžbina bez dostavljača, njen status može biti \"poručeno\" ili \"otkazano\"!");
		} else if(dostavljac != "nema" && status == "poručeno"){
			alert("Ukoliko je porudžbina ima dostavljača, njen status ne može biti \"poručeno\"!");
		} else {
			if(napomena == "") {
				napomena = "+";
			}
			$.ajax ({
				type : "POST",
				url : "rest/order/addAdmin/" + dostavljac + "/" + kupac + "/" + status + "/" + napomena + "/" + bonusPoeni,
				
				success : function () {
					alert("Porudžbina je uspešno kreirana!");
					$("#kreiranje-porudzbine-admin-modal").modal("hide");
					hideAdminTables();
					showOrdersTable();
				},

				error : function (message) {
					alert(message.responseText);
				}
			});
		}
	}
}

function sendChangedOrder(id) {
	return function(e) {
		e.preventDefault();

		var kupac = $("#kupac-izmena-porudzbine-admin-modal").val();
		var dostavljac = $("#dostavljac-izmena-porudzbine-admin-modal").val();
		var status = $("#status-izmena-porudzbine-admin-modal").val();
		var napomena = $('#napomena-dostavljac-izmena-porudzbine-admin-modal').val();
		var bonusPoeni = $("#koliko-bonus-poena-izmena-porudzbine-admin-modal").val();

		if(dostavljac == "nema" && (status == "dostava u toku" || status == "dostavljeno")) {
			alert("Ukoliko je porudžbina bez dostavljača, njen status može biti \"poručeno\" ili \"otkazano\"!");
		} else if(dostavljac != "nema" && status == "poručeno"){
			alert("Ukoliko je porudžbina ima dostavljača, njen status ne može biti \"poručeno\"!");
		} else {
			$.ajax ({
				type : "PUT",
				url : "rest/order/edit/" + id + "/" + dostavljac + "/" + kupac + "/" + status + "/" + napomena + "/" + bonusPoeni,
				
				success : function () {
					alert("Porudžbina je uspešno izmenjena!");
					$("#izmena-porudzbine-admin-modal").modal("hide");
					showOrdersTable();
				},
	
				error : function (message) {
					alert(message.responseText);
				}
			});
		}
	}
}

function editFinalPrize(ukupnaCena) {
	return function (e) {
		e.preventDefault();

		var iskoristiBonusa = $("#koliko-bonus-poena-izmena-porudzbine-admin-modal").val();
		var novaCena = ukupnaCena - ukupnaCena * 0.03 * iskoristiBonusa;
		document.getElementById('prikaz-cene-izmena-porudzbine-admin-modal').innerHTML = novaCena + " RSD";
	}
}

function deleteOrderAdmin(o) {
	return function(e) {
		e.preventDefault();

		$.ajax({
			type : "DELETE",
			url : "rest/order/delete/" + o.id,

			success : function () {
				alert("Porudžbina uspešno obrisana!");
				showOrdersTable();
			},

			error : function (message) {
				alert(message.responseText);
			}
		});
	}
}

//**** Admin - Vozila ****
function showVehiclesTable() {
	$.ajax ({
		type : 'GET',
		url : 'rest/vehicle/vehicles',
		
		success : function(vozila) {
			var num = 0;
			$('#vozila-tbody-id').empty();
			
			for(var v of vozila) { 
				if(v.obrisano == false) {
					num++;
					var tr = $('<tr></tr>');
					var number = $('<td>' + num + '</td>');
					var tip = $('<td>' + v.tipVozila + '</td>');
					var marka = $('<td>' + v.marka + '</td>');
					var model = $('<td>' + v.model + '</td>');
					var napomena = $('<td>' + v.napomena + '</td>');
					var registarskaOznaka = $('<td>' + v.registarskaOznaka + '</td>');
					var godinaProizvodnje = $('<td>' + v.godinaProizvodnje + '</td>');
					var izmena = $('<td><button type="button" class="btn btn-secondary" id="izmeni-vozilo-' + v.id + '">Izmeni</button></td>');
					var brisanje = $('<td><button type="button" class="btn btn-danger" id="obrisi-vozilo-' + v.id + '">Obriši</button></td>');
					tr.append(number).append(tip).append(marka).append(model).
						append(godinaProizvodnje).append(registarskaOznaka).append(napomena).append(izmena).append(brisanje);
					$("#vozila-tbody-id").append(tr);
					
					$('#izmeni-vozilo-' + v.id).click(setModalEditVehicle(v));
					$('#obrisi-vozilo-' + v.id).click(deleteVehicle(v.id));
				}
			}
			
			$("#dodaj-novo-vozilo").unbind("click").bind("click",setModalAddVehicle());
		},
		
		error : function() {
			alert("Greška pri učitavanju tabele vozila!");
		}
	});
}

function setModalAddVehicle() {
	return function(e) {
		e.preventDefault();
		
		$("#novo-vozilo-modal").modal('show');
		clearFormFields("novo-vozilo-forma");
		$("#potvrda-novo-vozilo").unbind("click").bind("click",createNewVehicle());
	}
}

function createNewVehicle() {
	return function(e) {
		e.preventDefault();

		var tipVozila = $("#tip-novo-vozilo").val();
		var marka = $("#marka-novo-vozilo").val();
		var model = $("#model-novo-vozilo").val();
		var registarskaOznaka = $("#registracija-novo-vozilo").val();
		var godinaProizvodnje = $("#godina-novo-vozilo").val();
		var napomena = $("#napomena-novo-vozilo").val();

		if(isValidAddVehicle()){
			$.ajax ({
				type : 'POST',
				url : 'rest/vehicle/add',
				data : JSON.stringify({
					marka :	marka,
					model : model,
					tipVozila : tipVozila,
					registarskaOznaka : registarskaOznaka,
					godinaProizvodnje : godinaProizvodnje,
					napomena : napomena,
				}),
				contentType : 'application/json',
				success : function() {
					alert("Kreiranje novog vozila je uspešno!");
					$("#novo-vozilo-modal").modal('hide');
					showVehiclesTable();
				},
				
				error : function(message) {
					alert(message.responseText);
				}
			});
		}
		
	}
}

function setModalEditVehicle(vozilo) {
	return function(e) {
		e.preventDefault();
		
		$("#izmeni-vozilo-modal").modal('show');

		document.getElementById('marka-izmeni-vozilo').value = vozilo.marka;
		document.getElementById('model-izmeni-vozilo').value = vozilo.model;
		document.getElementById('godina-izmeni-vozilo').value = vozilo.godinaProizvodnje;
		document.getElementById('registracija-izmeni-vozilo').value = vozilo.registarskaOznaka;
		document.getElementById('napomena-izmeni-vozilo').value = vozilo.napomena;

		$('select[name=tip-izmeni-vozilo]').val(vozilo.tipVozila);

		$("#potvrda-izmeni-vozilo").unbind("click").bind("click", editVehicle(vozilo.id));
	}
}

function editVehicle(id) {
	return function(e) {
		e.preventDefault();

		var tipVozila = $("#tip-izmeni-vozilo").val();
		var marka = $("#marka-izmeni-vozilo").val();
		var model = $("#model-izmeni-vozilo").val();
		var godinaProizvodnje = $("#godina-izmeni-vozilo").val();
		var registarskaOznaka = $("#registracija-izmeni-vozilo").val();
		var napomena = $("#napomena-izmeni-vozilo").val();

		if(isValidEditVehicle()){
			$.ajax ({
				type : 'PUT',
				url : 'rest/vehicle/edit' ,
				data : JSON.stringify({
					id : id,
					marka : marka,
					model : model,
					tipVozila : tipVozila,
					godinaProizvodnje : godinaProizvodnje,
					registarskaOznaka : registarskaOznaka,
					napomena : napomena,
				}),
				contentType : 'application/json',
				success : function() {
					alert("Izmena uspešno izvršena!");
					$("#izmeni-vozilo-modal").modal('hide');
					showVehiclesTable();
				},
				
				error : function(message) {
					alert(message.responseText);
				}
			});
		}
		
	}
}

function deleteVehicle(id) {
	return function(e) {
		e.preventDefault();
		$.ajax({
			type : 'DELETE',
			url: 'rest/vehicle/delete/' + id,
			success : function() {
				alert("Uspešno obrisano vozilo sa id: " + id);
				showVehiclesTable();
			},
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

//**** Admin - Artikli ****
function showArticlesTable() {
	$.ajax ({
		type : 'GET',
		url : 'rest/article/articles',
		
		success : function(artikli) {
			var num = 0;
			$('#artikli-tbody-id').empty();
			
			for(var a of artikli) { 
				if(a.obrisan == false) {
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
					var izmena = $('<td><button type="button" class="btn btn-secondary" id="izmeni-artikl-' + a.id + '">Izmeni</button></td>');
					var brisanje = $('<td><button type="button" class="btn btn-danger" id="obrisi-artikl-' + a.id + '">Obriši</button></td>');
					var dodavanje = $('<td><button type="button" class="btn btn-info" id="dodaj-artikl-' + a.id + '-restoran">Izaberi</button></td>');
					tr.append(number).append(naziv).append(kolicina).append(opis).
						append(cena).append(izmena).append(brisanje).append(dodavanje);
					$('#artikli-tbody-id').append(tr);
					
					$('#izmeni-artikl-' + a.id).click(setModalEditArticle(a));
					$('#obrisi-artikl-' + a.id).click(deleteArticle(a.id));
					$('#dodaj-artikl-' + a.id + '-restoran').click(setModalAddArticleInRestaurant(a.id));
				}
			}
			
			$('#dodaj-novi-artikl').unbind("click").bind("click",setModalAddArticle());
		},
		
		error : function() {
			alert("Greška pri učitavanju tabele artikala!");
		}
	});
}

function setModalAddArticleInRestaurant(articleId) {
	return function(e) {
		e.preventDefault();
		
		$('#artikl-u-restoran-modal').modal('show');

		$('#moguci-restorani-artikl').empty();

		$.ajax ({
			type : 'GET',
			url : 'rest/restaurant/restaurants',
			
			success : function(restorani) {
				for(var r of restorani) { 
					if(r.obrisan == false) {
						var option = $('<option value="' + r.id + '-' + r.naziv + '">' + r.naziv + '</option>');
						$('#moguci-restorani-artikl').append(option);
					}
				}
			}
		});

		$('#potvrda-artikl-u-restoran').unbind("click").bind("click",addArticleInRestaurant(articleId));
	}
}

function addArticleInRestaurant(articleId) {
	return function(e) {
		e.preventDefault();
		
		var resIdName = $("#moguci-restorani-artikl").val();
		var resIdNameArray= resIdName.split("-");
		var restaurantId = resIdNameArray[0];
		
		$.ajax ({
			type : 'PUT',
			url : 'rest/restaurant/' + restaurantId + '/article/' + articleId,
			success : function() {
				alert("Dodavanje novog artikla u restoran je uspešno!");
				$("#artikl-u-restoran-modal").modal('hide');
				showArticlesTable();
			},
			
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

function setModalAddArticle() {
	return function(e) {
		e.preventDefault();
		
		$("#novi-artikl-modal").modal('show');
		clearFormFields("novi-artikl-forma");
		$("#potvrda-novi-artikl").unbind("click").bind("click",createNewArticle());
	}
}

function createNewArticle() {
	return function(e) {
		e.preventDefault();

		var naziv = $("#naziv-novi-artikl").val();
		var kolicina = $("#kolicina-novi-artikl").val();
		var opis = $("#opis-novi-artikl").val();
		var jedinicnaCena = $("#jed-cena-novi-artikl").val();
		var tipJela = $("#tip-novi-artikl").val();
		if(isValidAddArticle()){
			$.ajax ({
				type : 'POST',
				url : 'rest/article/add',
				data : JSON.stringify({
					naziv :	naziv,
					kolicina : kolicina,
					opis : opis,
					jedinicnaCena : jedinicnaCena,
					tipJela : tipJela,
				}),
				contentType : 'application/json',
				success : function() {
					alert("Kreiranje novog artikla je uspešno!");
					$("#novi-artikl-modal").modal('hide');
					showArticlesTable();
				},
				
				error : function(message) {
					alert(message.responseText);
				}
			});
		}
	}
}

function setModalEditArticle(artikl) {
	return function(e) {
		e.preventDefault();
		
		$("#izmeni-artikl-modal").modal('show');

		document.getElementById('naziv-izmeni-artikl').value = artikl.naziv;
		document.getElementById('kolicina-izmeni-artikl').value = artikl.kolicina;
		document.getElementById('opis-izmeni-artikl').value = artikl.opis;
		document.getElementById('jed-cena-izmeni-artikl').value = artikl.jedinicnaCena;

		var tip = artikl.tipJela;
		if(tip = "piće")
			tip = "pice";

		$('select[name=tip-izmeni-artikl]').val(tip);

		$("#potvrda-izmeni-artikl").unbind("click").bind("click", editArticle(artikl.id));
	}
}

function editArticle(id) {
	return function(e) {
		e.preventDefault();

		var naziv = $("#naziv-izmeni-artikl").val();
		var kolicina = $("#kolicina-izmeni-artikl").val();
		var opis = $("#opis-izmeni-artikl").val();
		var jedinicnaCena = $("#jed-cena-izmeni-artikl").val();
		var tipJela = $("#tip-izmeni-artikl").val();
		
		if(isValidEditArticle()){
			$.ajax ({
				type : 'PUT',
				url : 'rest/article/edit' ,
				data : JSON.stringify({
					id : id,
					naziv : naziv,
					kolicina : kolicina,
					opis : opis,
					jedinicnaCena : jedinicnaCena,
					tipJela : tipJela,
				}),
				contentType : 'application/json',
				success : function() {
					alert("Izmena uspešno izvršena!");
					$("#izmeni-artikl-modal").modal('hide');
					showArticlesTable();
				},
				
				error : function(message) {
					alert(message.responseText);
				}
			});
		}
		
	}
}

function deleteArticle(id) {
	return function(e) {
		e.preventDefault();
		$.ajax({
			type : 'DELETE',
			url: 'rest/article/delete/' + id,
			success : function() {
				alert("Uspešno obrisan artikl sa id: " + id);
				showArticlesTable();
			},
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

// **** Admin - Restorani ****
function showRestaurantsTable() {
	$.ajax ({
		type : 'GET',
		url : 'rest/restaurant/restaurants',
		
		success : function(restorani) {
			var num = 0;
			$('#restorani-tbody-id').empty();
			
			for(var r of restorani) { 
				if(r.obrisan == false) {
					num++;
					var tr = $('<tr></tr>');
					var number = $('<td>' + num + '</td>');
					var naziv = $('<td>' + r.naziv + '</td>');
					var adresa = $('<td>' + r.adresa + '</td>');
					var kategorija = $('<td>' + r.kategorijaRestorana + '</td>');
					var izmena = $('<td><button type="button" class="btn btn-secondary" id="izmeni-' + r.id + '">Izmeni</button></td>');
					var brisanje = $('<td><button type="button" class="btn btn-danger" id="obrisi-' + r.id + '">Obriši</button></td>');
					tr.append(number).append(naziv).append(adresa).append(kategorija)
						.append(izmena).append(brisanje);
					$("#restorani-tbody-id").append(tr);
					
					$('#izmeni-' + r.id).click(setModalEditRestaurant(r));
					$('#obrisi-' + r.id).click(deleteRestaurant(r.id));
				}
			}
			
			$("#dodaj-novi-restoran").unbind("click").bind("click",setModalAddRestaurant());
		},
		
		error : function() {
			alert("Greška pri učitavanju tabele restorana!");
		}
	});
}

function setModalAddRestaurant() {
	return function(e) {
		e.preventDefault();
		
		$("#novi-restoran-modal").modal('show');
		clearFormFields("novi-restoran-forma");
		$("#potvrda-novi-restoran").unbind("click").bind("click",createNewRestaurant());
	}
}

function createNewRestaurant() {
	return function(e) {
		e.preventDefault();

		var naziv = $("#naziv-novi-restoran").val();
		var adresa = $("#adresa-novi-restoran").val();
		var kategorijaRestorana = $("#kategorija-novi-restoran").val();
		if(isValidAddRestaurant()){
			$.ajax ({
				type : 'POST',
				url : 'rest/restaurant/add',
				data : JSON.stringify({
					naziv : naziv,
					adresa : adresa,
					kategorijaRestorana : kategorijaRestorana,
				}),
				contentType : 'application/json',
				success : function() {
					alert("Kreiranje novog restorana je uspešno!");
					$("#novi-restoran-modal").modal('hide');
					showRestaurantsTable();
				},
				
				error : function(message) {
					alert(message.responseText);
				}
			});
		}
	}
}

function setModalEditRestaurant(restoran) {
	return function(e) {
		e.preventDefault();
		
		$("#izmeni-restoran-modal").modal('show');

		document.getElementById('naziv-izmeni-restoran').value = restoran.naziv;
		document.getElementById('adresa-izmeni-restoran').value = restoran.adresa;

		$('select[name=kategorija-izmeni-restoran]').val(restoran.kategorijaRestorana);

		$("#potvrda-izmeni-restoran").unbind("click").bind("click",editRestaurant(restoran.id));
	}
}

function editRestaurant(id) {
	return function(e) {
		e.preventDefault();

		var naziv = $("#naziv-izmeni-restoran").val();
		var adresa = $("#adresa-izmeni-restoran").val();
		var kategorijaRestorana = $("#kategorija-izmeni-restoran").val();
		if(isValidEditRestaurant()){
			$.ajax ({
				type : 'PUT',
				url : 'rest/restaurant/edit' ,
				data : JSON.stringify({
					id : id,
					naziv : naziv,
					adresa : adresa,
					kategorijaRestorana : kategorijaRestorana,
				}),
				contentType : 'application/json',
				success : function() {
					alert("Izmena uspešno izvršena!");
					$("#izmeni-restoran-modal").modal('hide');
					showRestaurantsTable();
				},
				
				error : function(message) {
					alert(message.responseText);
				}
			});
		}
	}
}

function deleteRestaurant(id) {
	return function(e) {
		e.preventDefault();
		$.ajax({
			type : 'DELETE',
			url: 'rest/restaurant/delete/' + id,
			success : function() {
				alert("Uspešno obrisan restoran sa id: " + id);
				showRestaurantsTable();
			},
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

function clearFormFields(forma) {
	$("#" + forma).trigger("reset");
}

//**** Admin - Korisnici ****
function showUsersTable() {
	$.ajax ({
		type : 'GET',
		url : 'rest/user/users',
		
		success : function(korisnici) {
			var num = 0;
			$('#korisnici-tbody-id').empty();
			
			for(var k of korisnici) { 
				num++;
				var tr = $('<tr></tr>');
				var number = $('<td>' + num + '</td>');
				var username = $('<td>' + k.username + '</td>');
				var ime = $('<td>' + k.ime + '</td>');
				var prezime = $('<td>' + k.prezime + '</td>');
				var kontaktTelefon = $('<td>' + k.kontaktTelefon + '</td>');
				var datumRegistracije = $('<td>' + k.datumRegistracije + '</td>');
				var uloga = $('<td>' + k.uloga + '</td>');
				var email = $('<td>' + k.email + '</td>');
				var izmena = $('<td><button type="button" class="btn btn-secondary" id="izmeni-' + k.username + '">Izmeni</button></td>');
				
				tr.append(number).append(uloga).append(username).append(ime).
					append(prezime).append(datumRegistracije).append(kontaktTelefon).
					append(email).append(izmena);
				
				$("#korisnici-tbody-id").append(tr);

				$("#izmeni-" + k.username).click(setModalChangeUserRole(k.username, k.uloga));
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju tabele korisnika!");
		}
	});
}

function changeUserRole(username) {
	return function(e) {
		e.preventDefault();
		
		var role = $("#uloga-izmena-korisnika").val();
		
		$.ajax ({
			type : 'PUT',
			url : 'rest/user/update/' + role + "/" + username,
			
			success : function() {
				alert("Izmena uloge uspešno izvršena!");
				$("#izmeni-korisnika-modal").modal('hide');
				showUsersTable();
			},
			
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

function setModalChangeUserRole(username, role) {
	return function(e) {
		e.preventDefault();

		$("#izmeni-korisnika-modal").modal('show');

		document.getElementById('username-izmena-korisnika').value = username;

		if(role == "dostavljač") {
			role = "dostavljac";
		}

		$('select[name=uloga-izmena-korisnika]').val(role);
		
		$("#potvrda-izmene-korisnika").unbind("click").bind("click",changeUserRole(username));
	}
}

function hideAdminTables() {
	$('#korisnici-admin-table').hide();
	$('#restorani-admin-table').hide();
	$('#vozila-admin-table').hide();
	$('#artikli-admin-table').hide();
	$('#porudzbine-admin-table').hide();
	$('#artikli-admin-order-table').hide();
}

// **** Ostalo sa profilPanela ****

function checkUserKind() {
	$.ajax({
		url : "rest/user/loginstat"
	}).then(function(user) {

		if (user != undefined) {
			if (user.uloga == "administrator") {
				$('#admin-part').show();
			} else if (user.uloga == "dostavljač") {
				$('#deliverer-part').show();
			} else if (user.uloga == "kupac") {
				$('#buyer-part').show();
			}
		}
	});
}