/**
 * @author game_changer96
 */

$(document).ready(function() {
	alert("kurec");
    showChosenOption();
	
});

function setModalSearchArticles() {
    $("#pretraga-artikl-modal").modal("show");
    $("#potvrda-pretraga-artikl").unbind("click").bind("click", searchArticles());
}

function searchArticles() {
    return function(e) {
        e.preventDefault();

        var nazivRestorana = $("#pretraga-restoran-artikl").val();
        var listaHrane = [];

        $.ajax ({
            type : 'GET',
            url : 'rest/restaurant/restaurants',
            
            success : function(restorani) {

                var ukucanRestoran = false;
                var prazanStringRestoran = false;

                for(var r of restorani) { 

                    if(nazivRestorana == r.naziv) {
                        ukucanRestoran = true;

                        if(Array.isArray(r.listaJela) && r.listaJela.length) {
                            listaHrane.push(r.listaJela);
                        }

                        if(Array.isArray(r.listaPica) && r.listaPica.length) {
                            listaHrane.push(r.listaPica);
                        }
                    }

                    if(nazivRestorana == "") {
                        prazanStringRestoran = true;
                    }
                }

                showSelectedArticles(ukucanRestoran, prazanStringRestoran, listaHrane);
            },
            
            error : function() {
                alert("Greška pri učitavanju liste restorana!");
            }
        });
    }
}

function showSelectedArticles(ukucanRestoran, prazanStringRestoran, listaHrane) {
    
    $.ajax ({
        type : 'GET',
        url : 'rest/article/articles',
        
        success : function(artikli) {
            
            var nazivA = $("#pretraga-naziv-artikl").val();
            var tipA = $("#pretraga-tip-artikl").val();
            var cenaA =  $("#pretraga-cena-artikl").val();
            
            if(cenaA<0){
            	alert("Cena artikla mora biti pozitivan broj!");
            }else{
            	var num = 0;

                checkUserKind();

                $('#pretraga-artikli-tbody-id').empty();
                $('#naslov-pretraga-artikala').show();
                $('#sadrzaj-pretraga-artikala').show();
                
                for(var a of artikli) {
                    
                    var uslovNaziv = false;
                    var uslovTip = false;
                    var uslovCena = false;
                    var uslovRestoran = false;

                    if(nazivA == "" || nazivA == a.naziv) {
                        uslovNaziv = true;
                    }

                    if(tipA == "" || tipA == a.tipJela) {
                        uslovTip = true;
                    }

                    var donjaCena = parseFloat(cenaA) * 0.8;
                    var gornjaCena = parseFloat(cenaA) * 1.2;
                    var srednjaCena = parseFloat(a.jedinicnaCena);

                    var uslovZaCenu = (srednjaCena >= donjaCena && srednjaCena <= gornjaCena);

                    if(cenaA == "" ||  uslovZaCenu == true ) {
                        uslovCena = true;
                    }
                    // ako je nadjen restoran sa trazenim imenom
                    if(ukucanRestoran == true) {
                        for (i = 0; i < listaHrane.length; i++) { 
                            var stavka = listaHrane[i];
                            if(stavka == a.id) {
                                uslovRestoran = true;
                            }
                        }
                    }
                    // ako nije unesen naziv restorana
                    if(prazanStringRestoran == true) {
                        uslovRestoran = true;
                    }

                    if(a.obrisan == false ) {
                        if(uslovNaziv && uslovTip && uslovCena && uslovRestoran) {
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
                            $('#pretraga-artikli-tbody-id').append(tr);
                            
                            $('#poruci-artikl-' + a.id).click(addToCart(a));
                        }
                    }
                }
                $("#pretraga-artikl-modal").modal("hide");
            }
            
            
        },
        
        error : function() {
            alert("Greška pri učitavanju pretraženih artikala!");
        }
    });
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


function setModalSearchRestaurants() {
    
    $("#pretraga-restoran-modal").modal("show");
    $("#potvrda-pretraga-restoran").unbind("click").bind("click", searchRestaurants());
}

function searchRestaurants() {
    return function(e) {
        e.preventDefault();

        var naziv = $("#pretraga-naziv-restorana").val();
        var adresa = $("#pretraga-adresa-restorana").val();
        var kategorija =  $("#pretraga-kategorija-restorana").val();

        $.ajax ({
            type : 'GET',
            url : 'rest/restaurant/restaurants',
            
            success : function(restorani) {

                $('#sadrzaj-pretraga-restorana').empty();
                $('#naslov-pretraga-restorana').show();
                $('#sadrzaj-pretraga-restorana').show();

                for(var r of restorani) { 

                    var uslovNaziv = false;
                    var uslovAdresa = false;
                    var uslovKategorija = false;

                    if(naziv == "" || naziv == r.naziv) {
                        uslovNaziv = true;
                    }

                    if(adresa == "" || adresa == r.adresa) {
                        uslovAdresa = true;
                    }
                    
                    if(kategorija == "" || kategorija == r.kategorijaRestorana) {
                        uslovKategorija = true;
                    }

                    if(r.obrisan == false && uslovNaziv && uslovAdresa && uslovKategorija) {
                        
                        var newR = $('<div class="col-sm-3"><br><div class="card text-center" style="width: 15rem;"><img class="card-img-top" src="pictures/4_0.png" alt="Card image cap"><div class="card-body"> <h5 class="card-title">' + r.naziv + '</h5><p class="card-text">' + r.adresa + '</p><button id="indijska-hrana-restoran-id-'+ r.id + '" class="btn btn-primary">Pogledaj meni</button></div></div></div>');
                        
                        $('#sadrzaj-pretraga-restorana').append(newR);
    
                        //$('#indijska-hrana-restoran-id-'+ r.id).click(showAllArticlesFromRestaturant(r));
                    }
                }

                $("#pretraga-restoran-modal").modal("hide");
            },
            
            error : function() {
                alert("Greška pri učitavanju pretraženih restorana!");
            }
        });
    }
}

function showChosenOption() {

    hideAllChoices();
	// klik na restorane domace hrane iz restaurants.html
    $('#pretraga-restorana').click(function(e) {
		
        e.preventDefault();

        hideAllChoices();
        setModalSearchRestaurants();
	});
	// klik na rostilj restorane iz restaurants.html
	$('#pretraga-artikala').click(function(e) {
		
        e.preventDefault();

        hideAllChoices();
        setModalSearchArticles();
        
	});
}

function hideAllChoices() {

    $('#naslov-pretraga-restorana').hide();
    $('#naslov-pretraga-artikala').hide();

    $("#sadrzaj-pretraga-restorana").hide();
    $('#sadrzaj-pretraga-artikala').hide();
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