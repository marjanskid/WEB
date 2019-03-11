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

import beans.Korisnik;
import beans.Vozilo;
import data.VozilaDAO;
import utils.Uloge;

/**
 * @author game_changer96
 *
 */
@Path("/vehicle")
public class VehicleService {
	
	@Context
	ServletContext context;

	@Context
	HttpServletRequest request;

	public VehicleService() {
		// TODO Auto-generated constructor stub
	}
	
	@PUT
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editVehicle(Vozilo v) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {	
				VozilaDAO vozilaDao = (VozilaDAO) context.getAttribute("vehicles");
				if(vozilaDao.izmeniVozilo(v)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Izmena podataka o vozilu nije uspešna!").build();
				}
			} else {
				return Response.status(400).entity("Nemate pravo da menjate podatke o vozilima!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addVehicle(Vozilo v) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				VozilaDAO vozilaDao = (VozilaDAO) context.getAttribute("vehicles");
				vozilaDao.dodajVozilo(v);
				return Response.status(200).build();
			} else {
				return Response.status(400).entity("Nemate pravo da dodajte nova vozila!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteVehicle( @PathParam("id") int id) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				VozilaDAO vozilaDao = (VozilaDAO) context.getAttribute("vehicles");
				if(vozilaDao.obrisiVozilo(id)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Vozilo sa datim ID-jem nije pronađeno!").build();
				}
			} else {
				return Response.status(400).entity("Nemate pravo da brišete vozila!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@GET
	@Path("/vehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vozilo> getVehicles() {
		VozilaDAO vozilaDao = (VozilaDAO) context.getAttribute("vehicles");
		return vozilaDao.getVozilaKolekcija();
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("vehicles") == null) {
			String contextPath = context.getRealPath("");
			VozilaDAO vozilaDao = new VozilaDAO(contextPath);
			context.setAttribute("vehicles", vozilaDao);
		}
	}
}
