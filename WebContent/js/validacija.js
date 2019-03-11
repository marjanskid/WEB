function isValidAddRestaurant(){
	var naziv = $("#naziv-novi-restoran").val();
	var adresa = $("#adresa-novi-restoran").val();
	var numOfErrors=0;
	if(naziv==""){
		alert("Morate uneti naziv restorana!");
		numOfErrors++;
	}
	if(adresa==""){
		alert("Morate uneti adresu restorana!");
		numOfErrors++;
	}
	if(numOfErrors==0){
		return true;
	}
	else{
		return false;
	}
}

function isValidEditRestaurant(){
	var naziv = $("#naziv-izmeni-restoran").val();
	var adresa = $("#adresa-izmeni-restoran").val();
	var numOfErrors=0;
	if(naziv==""){
		alert("Morate uneti naziv restorana!");
		numOfErrors++;
	}
	if(adresa==""){
		alert("Morate uneti adresu restorana!");
		numOfErrors++;
	}
	if(numOfErrors==0){
		return true;
	}
	else{
		return false;
	}
}

function isValidAddArticle(){
	var naziv = $("#naziv-novi-artikl").val();
	var kolicina = $("#kolicina-novi-artikl").val();
	var jedinicnaCena = $("#jed-cena-novi-artikl").val();
	var numOfErrors=0;
	if(naziv==""){
		alert("Morate uneti naziv artikla!");
		numOfErrors++;
	}
	if(kolicina==""){
		alert("Morate uneti količinu artikla!");
		numOfErrors++;
	}else if(kolicina<0){
		alert("Količina mora biti pozitivan broj");
		numOfErrors++;
	}else if(!($.isNumeric(kolicina))){
		alert("Količina mora biti broj");
		numOfErrors++;
	}
	if(jedinicnaCena==""){
		alert("Morate uneti količinu cenu!");
		numOfErrors++;
	}else if(jedinicnaCena<0){
		alert("Cena mora biti pozitivan broj");
		numOfErrors++;
	}else if(!($.isNumeric(jedinicnaCena))){
		alert("Cena mora biti broj");
		numOfErrors++;
	}
	if(numOfErrors==0){
		return true;
	}
	else{
		return false;
	}
}

function isValidEditArticle(){
	var naziv = $("#naziv-izmeni-artikl").val();
	var kolicina = $("#kolicina-izmeni-artikl").val();
	var jedinicnaCena = $("#jed-cena-izmeni-artikl").val();
	var numOfErrors=0;
	
	if(naziv==""){
		alert("Morate uneti naziv artikla!");
		numOfErrors++;
	}
	if(kolicina==""){
		alert("Morate uneti količinu artikla!");
		numOfErrors++;
	}else if(kolicina<0){
		alert("Količina mora biti pozitivan broj");
		numOfErrors++;
	}else if(!($.isNumeric(kolicina))){
		alert("Količina mora biti broj");
		numOfErrors++;
	}
	if(jedinicnaCena==""){
		alert("Morate uneti količinu cenu!");
		numOfErrors++;
	}else if(jedinicnaCena<0){
		alert("Cena mora biti pozitivan broj");
		numOfErrors++;
	}else if(!($.isNumeric(jedinicnaCena))){
		alert("Cena mora biti broj");
		numOfErrors++;
	}
	if(numOfErrors==0){
		return true;
	}
	else{
		return false;
	}
}

function isValidAddVehicle(){
	var marka = $("#marka-novo-vozilo").val();
	var model = $("#model-novo-vozilo").val();
	var registarskaOznaka = $("#registracija-novo-vozilo").val();
	var godinaProizvodnje = $("#godina-novo-vozilo").val();
	var numOfErrors=0;

	if(marka==""){
		alert("Morate uneti marku vozila!");
		numOfErrors++;
	}
	if(model==""){
		alert("Morate uneti model vozila!");
		numOfErrors++;
	}
	if(registarskaOznaka==""){
		alert("Morate uneti registarsku oznaku vozila!");
		numOfErrors++;
	}
	if(godinaProizvodnje==""){
		alert("Morate uneti godište vozila!");
		numOfErrors++;
	}else if(!($.isNumeric(godinaProizvodnje))){
		alert("Godina mora biti broj!");
		numOfErrors++;
	}else if(godinaProizvodnje<0){
		alert("Godina mora biti pozitivna!");
		numOfErrors++;
	}else if(!((Math.floor(godinaProizvodnje)==godinaProizvodnje))&&($.isNumeric(godinaProizvodnje))){
		alert("Godina mora biti ceo broj!");
		numOfErrors++;
	}else if(godinaProizvodnje>2018){
		alert("Godina mora biti manja od 2018!");
		numOfErrors++;
	}
	if(numOfErrors==0){
		return true;
	}
	else{
		return false;
	}
}

function isValidEditVehicle(){
	var marka = $("#marka-izmeni-vozilo").val();
	var model = $("#model-izmeni-vozilo").val();
	var godinaProizvodnje = $("#godina-izmeni-vozilo").val();
	var registarskaOznaka = $("#registracija-izmeni-vozilo").val();
	var numOfErrors=0;

	if(marka==""){
		alert("Morate uneti marku vozila!");
		numOfErrors++;
	}
	if(model==""){
		alert("Morate uneti model vozila!");
		numOfErrors++;
	}
	if(registarskaOznaka==""){
		alert("Morate uneti registarsku oznaku vozila!");
		numOfErrors++;
	}
	if(godinaProizvodnje==""){
		alert("Morate uneti godište vozila!");
		numOfErrors++;
	}else if(!($.isNumeric(godinaProizvodnje))){
		alert("Godina mora biti broj!");
		numOfErrors++;
	}else if(godinaProizvodnje<0){
		alert("Godina mora biti pozitivna!");
		numOfErrors++;
	}else if(!((Math.floor(godinaProizvodnje)==godinaProizvodnje))&&($.isNumeric(godinaProizvodnje))){
		alert("Godina mora biti ceo broj!");
		numOfErrors++;
	}else if(godinaProizvodnje>2018){
		alert("Godina mora biti manja od 2018!");
		numOfErrors++;
	}
	if(numOfErrors==0){
		return true;
	}
	else{
		return false;
	}
}