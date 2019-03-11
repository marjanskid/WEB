/**
 * @author game_changer96
 */

$(document).ready(function() {

	hideTableAndPrizeIfEmpty();
	showCartItems();
	finishOrderClicked();

});

function showCartItems() {
	$.ajax ({
		type: 'GET',
		url: 'rest/user/cartItems',

		success : function(stavkePorudzbine) {
			var num = 0;
			var ukupnaCena = 0;
			$('#artikli-tbody-id').empty();
			
			for(var s of stavkePorudzbine) {
				num++;
				var a = s.artikl;
				var kolicina = s.kolicina;
				ukupnaCena = ukupnaCena + kolicina * a.jedinicnaCena;

				var tr = $('<tr></tr>');
				var number = $('<td>' + num + '</td>');
				var naziv = $('<td>' + a.naziv + '</td>');
				var cena = $('<td>' + s.kolicina + '  x  ' + a.jedinicnaCena + ' RSD</td>');

				var izbaci = $('<td><button type="button" class="btn btn-danger" id="izbaci-artikl-' + a.id + '">Izbaci</button></td>');

				tr.append(number).append(naziv).append(cena).append(izbaci);
				$('#artikli-tbody-id').append(tr);
				
				$('#izbaci-artikl-' + a.id).click(removeFromCart(s));
			}

			if(num > 0) {
				$("#tabela-stavki-u-korpi").show();
				document.getElementById('prikaz-cene-korpe').innerHTML = ukupnaCena + ' RSD';
				$("#deo-ispod-tabele-stavki").show();
			}
		},
		
		error : function() {
			alert("Greška pri učitavanju stavki iz korpe!");
		}
	});
}

function removeFromCart(s) {
	return function(e) {
		e.preventDefault();

		$.ajax ({
			type: 'DELETE',
			url: 'rest/user/removeItem/',
			data : JSON.stringify({
				kolicina : s.kolicina,
				artikl : s.artikl
			}),
			contentType : 'application/json',

			success : function () {
				alert("Stavka uspešno uklonjena iz korpe!");
				hideTableAndPrizeIfEmpty();
				showCartItems();
			},

			error : function (message) {
				alert(message.responseText);
			}
		});
	}
}

function finishOrderClicked() {
	$("#otvori-modal-korpe").click(function (e) {
		e.preventDefault();

		$("#rezime-porudzbine-modal").modal("show");

		$.ajax ({
			type: 'GET',
			url: 'rest/user/cartItems',
	
			success : function(stavkePorudzbine) {
				var num = 0;
				var ukupnaCena = 0;
				$('#artikli-tbody-id-modal').empty();
				
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
					$('#artikli-tbody-id-modal').append(tr);
				}

				document.getElementById('prikaz-cene-korpe-modal').innerHTML = ukupnaCena + " RSD";

				$.ajax ({
					type: 'GET',
					url: 'rest/user/brojBonusPoena',
			
					success : function(bonusPoeni) {
						var option;
						$("#koliko-bonus-poena-modal").empty();
						for(var i = 0; i <= bonusPoeni; i++) {
							if(i == 0) {
								option = $('<option value="' + i + '" selected>' + i + '</option>');
							} else {
								option = $('<option value="' + i + '">' + i + '</option>');
							}
							
							$("#koliko-bonus-poena-modal").append(option);
						} 

						$("#koliko-bonus-poena-modal").change(editFinalPrize(ukupnaCena));

						$("#potvrda-novo-porudzbina").unbind("click").bind("click",createNewOrder(stavkePorudzbine, ukupnaCena));
					},
					
					error : function() {
						alert("Greška pri učitavanju broja bonus poena korisnika!");
					}
				});		
			},
			
			error : function() {
				alert("Greška pri učitavanju stavki iz korpe!");
			}
		});
		
	})
}

function editFinalPrize(ukupnaCena) {
	return function (e) {
		e.preventDefault();

		var iskoristiBonusa = $("#koliko-bonus-poena-modal").val();
		var novaCena = ukupnaCena - ukupnaCena * 0.03 * iskoristiBonusa;

		document.getElementById('prikaz-cene-korpe-modal').innerHTML = novaCena + " RSD";
	}
}

function createNewOrder(stavkePorudzbine, punaCena) {
	return function(e) {
		e.preventDefault();

		var napomena = $('#napomena-nova-porudzbina').val();
		var bonusPoeni = $('#koliko-bonus-poena-modal').val();

		$.ajax ({
			type : 'POST',
			url : 'rest/order/add',
			data : JSON.stringify({
				stavkePorudzbine : stavkePorudzbine,
				napomena : napomena,
				bonusPoeni : bonusPoeni,
				punaCena : punaCena
			}),
			contentType : 'application/json',
			success : function() {
				alert("Kreiranje nove porudžbine je uspešno!");
				$("#rezime-porudzbine-modal").modal("hide");
				hideTableAndPrizeIfEmpty();
				showCartItems();
			},
			
			error : function(message) {
				alert(message.responseText);
			}
		});
	}
}

function hideTableAndPrizeIfEmpty() {
	$("#deo-ispod-tabele-stavki").hide();
	$("#tabela-stavki-u-korpi").hide();
}