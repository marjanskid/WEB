/**
 * 
 */
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import beans.Porudzbina;
import beans.StavkaPorudzbine;
import data.ArtikliDAO;
import data.KorisniciDAO;
import data.PorudzbineDAO;
import data.VozilaDAO;
import utils.StatusPorudzbine;
import utils.Uloge;

/**
 * @author game_changer96
 *
 */
@Path("/order")
public class OrderService {

	@Context
	ServletContext context;

	@Context
	HttpServletRequest request;
	
	public OrderService() {
		
	}
	
	@POST
	@Path("/addAdmin/{dostavljac}/{kupac}/{status}/{napomena}/{bonusPoeni}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrderAdmin(@PathParam ("dostavljac") String dostavljac, @PathParam ("kupac") String kupac,
			@PathParam ("status") String status, @PathParam ("napomena") String napomena, @PathParam ("bonusPoeni") int bonusPoeni) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		List<StavkaPorudzbine> stavkePorudzbine = (List<StavkaPorudzbine>) session.getAttribute("cartadmin");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {	
				PorudzbineDAO porudzbineDao = (PorudzbineDAO) context.getAttribute("orders");
				KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
				
				Korisnik kupacP = korisniciDao.getKorisnici().get(kupac);
				Korisnik dostavljacP;
				if(dostavljac.equals("nema")) {
					dostavljacP = new Korisnik();
					dostavljacP.setUloga(Uloge.DOSTAVLJAC);
				} else {
					dostavljacP = korisniciDao.getKorisnici().get(dostavljac);
				}
				if(napomena.equals("+")) {
					napomena = "";
				}
				Porudzbina p = new Porudzbina(stavkePorudzbine, kupacP, dostavljacP, status, napomena);
				p.setBonusPoeni(bonusPoeni);
				
				porudzbineDao.dodajPorudzbinu(p);
				
				korisniciDao.addBuyerDeliverer(p.getId(), kupac, dostavljac, status);
				
				List<StavkaPorudzbine> stavkePorudzbinePrazne = new ArrayList<StavkaPorudzbine>();
				session.setAttribute("cartadmin", stavkePorudzbinePrazne);
				
				return Response.status(200).build();
			} else {
				return Response.status(400).entity("Nemate pravo da menjate podatke o artiklima!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@PUT
	@Path("/deliver/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deliverOrder(@PathParam("orderId") int orderId) {
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.DOSTAVLJAC)) {	
				PorudzbineDAO porudzbineDao = (PorudzbineDAO) context.getAttribute("orders");
				if(porudzbineDao.promeniStatusPorudzbine(orderId, StatusPorudzbine.DOSTAVLJENO) != true) {
					return Response.status(400).entity("Greška pri izmeni statusa porudžbine!").build();
				}
				
				VozilaDAO vozilaDao = (VozilaDAO) context.getAttribute("vehicles");
				if(user.getVozilo() != -1) {
					if(!vozilaDao.promeniStatusVozila(user.getVozilo())) {
						return Response.status(400).entity("Greška pri izmeni statusa vozila!").build();
					}
				}
				
				KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
				if(!korisniciDao.promeniStatusDostavljaca(user.getUsername(), -1)) {
					return Response.status(400).entity("Greška pri izmeni statusa dostavljača!").build();
				}
				
				return Response.status(200).build();
				
			} else {
				return Response.status(400).entity("Nemate pravo da menjate podatke o artiklima!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@PUT
	@Path("/takeOrder/{orderId}/{vehicleId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response takeOrder(@PathParam("orderId") int orderId, @PathParam("vehicleId") int vehicleId) {
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.DOSTAVLJAC)) {	
				PorudzbineDAO porudzbineDao = (PorudzbineDAO) context.getAttribute("orders");
				if(porudzbineDao.promeniStatusPorudzbine(orderId, StatusPorudzbine.DOSTAVA_U_TOKU) != true) {
					return Response.status(400).entity("Greška pri izmeni statusa porudžbine!").build();
				}
				
				if(!porudzbineDao.postaviDostavljaca(orderId, user)) {
					return Response.status(400).entity("Greška pri izmeni dostavljača porudžbine!").build();
				}
				
				VozilaDAO vozilaDao = (VozilaDAO) context.getAttribute("vehicles");
				if(vehicleId != -1) {
					if(!vozilaDao.promeniStatusVozila(vehicleId)) {
						return Response.status(400).entity("Greška pri izmeni statusa vozila!").build();
					}
				}
				
				KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
				if(vehicleId != -1) {
					if(!korisniciDao.promeniStatusDostavljaca(user.getUsername(), vehicleId)) {
						return Response.status(400).entity("Greška pri izmeni statusa dostavljača!").build();
					}
				}
				
				if(!korisniciDao.dodajUListuDodeljenihPorudzbina(user.getUsername(), orderId)) {
					return Response.status(400).entity("Greška pri dodavanja u listu dodeljenih!").build();
				}
				
				return Response.status(200).build();
				
			} else {
				return Response.status(400).entity("Nemate pravo da menjate podatke o artiklima!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@PUT
	@Path("/edit/{id}/{dostavljac}/{kupac}/{status}/{napomena}/{bonusPoeni}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editOrder(@PathParam ("id") int idPorudzbine,
			@PathParam ("dostavljac") String dostavljac, @PathParam ("kupac") String kupac,
			@PathParam ("status") String status, @PathParam ("napomena") String napomena, @PathParam ("bonusPoeni") int bonusPoeni) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {	
				PorudzbineDAO porudzbineDao = (PorudzbineDAO) context.getAttribute("orders");
				Porudzbina staraP = porudzbineDao.getPorudzbine().get(idPorudzbine);
				
				KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
				if(!staraP.getKupac().getUsername().equals(kupac)) {
					korisniciDao.promenaKupca(staraP.getKupac(), kupac, idPorudzbine);
					porudzbineDao.promenaKupca(korisniciDao, kupac, idPorudzbine);
				}
				if(!staraP.getDostavljac().getUsername().equals(dostavljac)) {
					korisniciDao.promenaDostavljaca(staraP.getDostavljac(), dostavljac, idPorudzbine, status);
					porudzbineDao.promenaDostavljaca(korisniciDao, dostavljac, idPorudzbine, status);
				}
				if(!staraP.getNapomena().equals(napomena)) {
					porudzbineDao.promeniNapomenu(napomena, idPorudzbine);
				}
				if(!staraP.getStatusPorudzbine().equals(status)) {
					boolean ret = porudzbineDao.promeniStatusPorudzbine(idPorudzbine, status);
				}
				if(staraP.getBonusPoeni() != bonusPoeni) {
					porudzbineDao.promeniBrojPoena(bonusPoeni, idPorudzbine);
				}
				
				return Response.status(200).build();
			} else {
				return Response.status(400).entity("Nemate pravo da menjate podatke o artiklima!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrder(Porudzbina p) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				PorudzbineDAO porudzbineDao = (PorudzbineDAO) context.getAttribute("orders");
				porudzbineDao.dodajPorudzbinu(p);
				
				return Response.status(200).build();
			} else if(user.getUloga().equals(Uloge.KUPAC)) {
				PorudzbineDAO porudzbineDao = (PorudzbineDAO) context.getAttribute("orders");
				
				p.setKupac(user);
				p.setStatusPorudzbine(StatusPorudzbine.PORUCENO);
				
				KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
				// azuriranje bonus poena za korisnika
				korisniciDao.changeBonusPoints(user.getUsername(), p.getBonusPoeni(), p.getPunaCena());
				// azuriranje liste porudzbina
				korisniciDao.changeOrdersList(user.getUsername(), p.getId());
				
				
				// azuriranje broja porucenih porcija
				ArtikliDAO artikliDao = (ArtikliDAO) context.getAttribute("articles");
				artikliDao.editFavouriteCount(p.getStavkePorudzbine());
				
				porudzbineDao.dodajPorudzbinu(p);
				
				List<StavkaPorudzbine> stavkaPorudzbine = new ArrayList<StavkaPorudzbine>();
				session.setAttribute("cart", stavkaPorudzbine);
				
				return Response.status(200).build();
			} else {
				return Response.status(400).entity("Nemate pravo da dodajte nove porudžbine!").build();
			} 
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrder(@PathParam("id") int id) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			// samo admin moze da obrise porudzbinu
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				PorudzbineDAO porudzbineDao = (PorudzbineDAO) context.getAttribute("orders");
				if(porudzbineDao.obrisiPorudzbinu(id)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Porudžbina sa datim id-jem nije pronađena!").build();
				}
			} else {
				return Response.status(400).entity("Nemate pravo da brišete artikle!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@GET
	@Path("/orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Porudzbina> getOrders() {
		PorudzbineDAO porudzbineDao = (PorudzbineDAO) context.getAttribute("orders");
		return porudzbineDao.getPorudzbineCollection();
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("orders") == null) {
			String contextPath = context.getRealPath("");
			PorudzbineDAO porudzbineDao = new PorudzbineDAO(contextPath);
			context.setAttribute("orders", porudzbineDao);
		}
	}
}
