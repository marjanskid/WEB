/**
 * @author game_changer96
 */

$("#sidebar-toggle").click(function(e) {
	$("#page-content").toggleClass("menuDisplayed");
	e.preventDefault();
});