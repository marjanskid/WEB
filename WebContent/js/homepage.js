/**
 * @author game_changer96
 */


$(document).ready(function() {

    showChosenRestaurantOption();
    showMostBoughtItems();

});

function showChosenRestaurantOption() {
    showDomesticFoodRestaurants1();
    showGrillRestaurants1();
    showChineseFoodRestaurants1();
    showIndianFoodRestaurants1();
    showConfectioneryRestaurants1();
    showPizzaRestaurants1();
}

function showMostBoughtItems() {
    $.ajax ({
        type : "GET",
        url : "rest/article/popularArticles",

        success : function (popularArt) {
            $("#slide-to-opcije").empty();
            $("#ilista-jela-i-pica").empty();
            var num = 0;
            for(var art of popularArt) {
                if(num == 0) {
                    var newLi = $('<li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>');
                    var newItem = $('<div class="carousel-item active"><img class="d-block w-100" src="pictures/novaind.png"><div class="carousel-caption d-none d-md-block"><h2>' + art.naziv +'</h2><h4>' + art.jedinicnaCena + '</h4></div></div>');
                } else {
                    var newLi = $('<li data-target="#carouselExampleIndicators" data-slide-to="' + num + '" ></li>');
                    var newItem = $('<div class="carousel-item"><img class="d-block w-100" src="pictures/novaind.png"><div class="carousel-caption d-none d-md-block"><h2>' + art.naziv +'</h2><h4>' + art.jedinicnaCena + '</h4></div></div>');
                }
                $("#slide-to-opcije").append(newLi);
                
                $("#lista-jela-i-pica").append(newItem);
                num++;
            }
        }
    });
}

// klik na prikaz picerija
function showPizzaRestaurants1() {
    $('#picerija-restoran').click(function(e) {
		
        e.preventDefault();
        
        window.location.href='http://localhost:8080/DMProject/restaurants.html?type=picerija';
	});
}
// klik na prikaz poslasticarnica
function showConfectioneryRestaurants1() {
    $('#poslasticarnica-restoran').click(function(e) {
		
        e.preventDefault();
        
        window.location.href='http://localhost:8080/DMProject/restaurants.html?type=poslasticarnica';
	});
}
// klik na prikaz indijskih restorana
function showIndianFoodRestaurants1() {
    $('#indijska-hrana-restoran').click(function(e) {
		
        e.preventDefault();
        
        window.location.href='http://localhost:8080/DMProject/restaurants.html?type=indijska';
	});
}
// klik na prikaz kineskih restorana
function showChineseFoodRestaurants1() {
    $('#kineska-hrana-restoran').click(function(e) {
		
        e.preventDefault();
        
        window.location.href='http://localhost:8080/DMProject/restaurants.html?type=kineska';
	});
}
// klik na prikaz rostilja
function showGrillRestaurants1() {
    $('#rostilj-restoran').click(function(e) {
		
        e.preventDefault();
        
        window.location.href='http://localhost:8080/DMProject/restaurants.html?type=rostilj';
	});
}
// klik na prikaz restorana domace kuhinje
function showDomesticFoodRestaurants1() {
    $('#domaca-hrana-restoran').click(function(e) {
		
        e.preventDefault();
        
        window.location.href='http://localhost:8080/DMProject/restaurants.html?type=domaca';
	});
}