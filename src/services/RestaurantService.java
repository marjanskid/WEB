/**
 * 
 */
package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Artikl;
import beans.Korisnik;
import beans.Restoran;
import data.ArtikliDAO;
import data.RestoraniDAO;
import utils.Uloge;

/**
 * @author game_changer96
 *
 */
@Path("/restaurant")
public class RestaurantService {

	@Context
	ServletContext context;

	@Context
	HttpServletRequest request;

	public RestaurantService() {
		// TODO Auto-generated constructor stub
	}

	@PUT
	@Path("/{rid}/article/{aid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addArticleInRestaurant(@PathParam("rid") int restaurantId, @PathParam("aid") int articleId) {

		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		if (user != null) {
			if (user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				ArtikliDAO artikliDao = (ArtikliDAO) context.getAttribute("articles");
				Artikl a = artikliDao.getArtikli().get(articleId);
				RestoraniDAO restoraniDao = (RestoraniDAO) context.getAttribute("restaurants");

				if (restoraniDao.dodajArtikl(restaurantId, a)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Dati artikl već postoji u okviru restorana!").build();
				}
			} else {
				return Response.status(400).entity("Nemate pravo da menjate podatke o restoranima!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}

	@PUT
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editRestaurant(Restoran r) {

		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		if (user != null) {
			if (user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				RestoraniDAO restoraniDao = (RestoraniDAO) context.getAttribute("restaurants");
				if (restoraniDao.izmeniRestoran(r)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Izmena podataka o restoranu nije uspešna!").build();
				}
			} else {
				return Response.status(400).entity("Nemate pravo da menjate podatke o restoranima!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurant(Restoran r) {

		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		if (user != null) {
			if (user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				RestoraniDAO restoraniDao = (RestoraniDAO) context.getAttribute("restaurants");
				restoraniDao.dodajRestoran(r);
				return Response.status(200).build();
			} else {
				return Response.status(400).entity("Nemate pravo da dodajte nove restorane!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}

	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRestaurant(@PathParam("id") int id) {

		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		if (user != null) {
			if (user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				RestoraniDAO restoraniDao = (RestoraniDAO) context.getAttribute("restaurants");
				if (restoraniDao.obrisiRestoran(id)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Restoran sa datim id-jem nije pronađen!").build();
				}
			} else {
				return Response.status(400).entity("Nemate pravo da brišete restorane!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}

	@GET
	@Path("/restaurants")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Restoran> getRestaurants() {
		RestoraniDAO restoraniDao = (RestoraniDAO) context.getAttribute("restaurants");
		return restoraniDao.getRestoraniKolekcija();
	}

	@PostConstruct
	public void init() {
		if (context.getAttribute("restaurants") == null) {
			String contextPath = context.getRealPath("");
			RestoraniDAO restoraniDao = new RestoraniDAO(contextPath);
			context.setAttribute("restaurants", restoraniDao);
		}
	}
}
