package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import beans.StavkaPorudzbine;
import data.KorisniciDAO;
import utils.Uloge;

/**
 * @author game_changer96
 *
 */

@Path("/user")
public class UserService {

	@Context
	ServletContext context;

	@Context
	HttpServletRequest request;

	public UserService() {
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Korisnik> getUsers() {
		KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
		return korisniciDao.getKorisniciKolekcija();
	}
	
	@POST
	@Path("/addToCartAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addToCartAdmin(StavkaPorudzbine sp) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		List<StavkaPorudzbine> stavkePorudzbine = (List<StavkaPorudzbine>) session.getAttribute("cartadmin");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				if(stavkePorudzbine.add(sp)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Dodavanje stavke od strane admina nije uspešno!").build();
				}		
			}
			else {
				return Response.status(400).entity("Nemate pravo da dodajete stavke u korpu!").build();
			}
		}
		else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili akciju!").build();
		}
	}
	
	@GET
	@Path("/brojBonusPoena")
	@Produces(MediaType.APPLICATION_JSON)
	public int getBrojBonusPoena() {
		int ret = 0;
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.KUPAC)) {
				ret = user.getBonusPoeni();
			}
		}
		
		return ret;
	}
	
	@GET
	@Path("/cartItemsAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	public List<StavkaPorudzbine> showCartItemsAdmin() {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		List<StavkaPorudzbine> stavkePorudzbine = new ArrayList<StavkaPorudzbine>();
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				stavkePorudzbine = (List<StavkaPorudzbine>) session.getAttribute("cartadmin");
			}
		}
		
		return stavkePorudzbine;
	}
	
	@GET
	@Path("/cartItems")
	@Produces(MediaType.APPLICATION_JSON)
	public List<StavkaPorudzbine> showCartItems() {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		List<StavkaPorudzbine> stavkePorudzbine = new ArrayList<StavkaPorudzbine>();
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.KUPAC)) {
				stavkePorudzbine = (List<StavkaPorudzbine>) session.getAttribute("cart");
			}
		}
		
		return stavkePorudzbine;
	}
	
	@DELETE
	@Path("/removeItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeFromCart(StavkaPorudzbine sp) {
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		List<StavkaPorudzbine> stavkePorudzbine = (List<StavkaPorudzbine>) session.getAttribute("cart");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.KUPAC)) {
				for(StavkaPorudzbine stavka : stavkePorudzbine) {
					boolean uslovKolicina = stavka.getKolicina() == sp.getKolicina();
					boolean uslovArtikl = stavka.getArtikl().getNaziv().equals(sp.getArtikl().getNaziv());
					if(uslovKolicina && uslovArtikl) {
						stavkePorudzbine.remove(stavka);
						return Response.status(200).build();
					}
				}
				return Response.status(400).entity("Izbacivanje stavke iz korpe nije uspešno!").build();		
			}
			else {
				return Response.status(400).entity("Nemate pravo da izbacujete stavke iz korpe!").build();
			}
		}
		else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili akciju!").build();
		}
	}
	
	@POST
	@Path("/addToCart")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addToCart(StavkaPorudzbine sp) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		List<StavkaPorudzbine> stavkePorudzbine = (List<StavkaPorudzbine>) session.getAttribute("cart");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.KUPAC)) {
				if(stavkePorudzbine.add(sp)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Dodavanje stavke u korpu nije uspešno!").build();
				}		
			}
			else {
				return Response.status(400).entity("Nemate pravo da dodajete stavke u korpu!").build();
			}
		}
		else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili akciju!").build();
		}
	}
	
	@PUT
	@Path("/unfavourite/{restaurantId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeFromFavourite(@PathParam("restaurantId") int restaurantId) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.KUPAC)) {
				if(korisniciDao.removeRestaurantFromFavourite(user, restaurantId)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Izabrani restoran nije u listi omiljenih restorana!").build();
				}		
			}
			else {
				return Response.status(400).entity("Nemate pravo da pravite listu svojih restorana!").build();
			}
		}
		else {
			return Response.status(400).entity("Morate biti ulogovani kao administrator da biste izvršili akciju!").build();
		}
	}
	
	@PUT
	@Path("/favourite/{restaurantId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addToFavourite(@PathParam("restaurantId") int restaurantId) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.KUPAC)) {
				if(korisniciDao.addRestaurantToFavourite(user, restaurantId)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Izabrani restoran već se nalazi u listi omiljenih restorana!").build();
				}		
			}
			else {
				return Response.status(400).entity("Nemate pravo da pravite listu svojih restorana!").build();
			}
		}
		else {
			return Response.status(400).entity("Morate biti ulogovani kao administrator da biste izvršili akciju!").build();
		}
	}
	
	@PUT
	@Path("/update/{role}/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("role") String role, 
						@PathParam("username") String username) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {

			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				
				if(user.getUsername().equals(username)) {
					return Response.status(400).entity("Nemate pravo da menjate sopstvenu ulogu!").build();
				}
				
				KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");
				
				if(role.equals("administrator")) {
					korisniciDao.changeUserRole(username, Uloge.ADMINISTRATOR);
				}
				else if(role.equals("dostavljac")) {						
					korisniciDao.changeUserRole(username, Uloge.DOSTAVLJAC);
				}
				else if(role.equals("kupac")) {
					korisniciDao.changeUserRole(username, Uloge.KUPAC);
				}
				
				return Response.status(200).build();
			}
			else {
				return Response.status(400).entity("Nemate pravo da menjate uloge korisnicima!").build();
			}
		}
		else {
			return Response.status(400).entity("Morate biti ulogovani kao administrator da biste izvršili akciju!").build();
		}
	}
		
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout() {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			session.invalidate();
			return Response.status(200).build();
		}
		else {
			return Response.status(400).entity("Korisnik je vec izlogovan!").build();
		}
	}

	@GET
	@Path("/loginstat")
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik loginStat() {

		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");

		if (user != null) {
			return user;
		} else {
			return null;
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(Korisnik userToLogIn) {

		HttpSession session = request.getSession();

		if (userToLogIn.getUsername() == null || userToLogIn.getPassword() == null
				|| userToLogIn.getUsername().equals("") || userToLogIn.getPassword().equals("")) {
			return Response.status(400).entity("Prilikom logovanja unesite korisnicko ime i sifru!").build();

		}
		
		KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");

		if (korisniciDao.searchKorisnik(userToLogIn.getUsername()) != null) {

			Korisnik user = korisniciDao.searchKorisnik(userToLogIn.getUsername());

			if (user.getPassword().equals(userToLogIn.getPassword()) == true) {
				session.setAttribute("user", user);
				session.setAttribute("cart", new ArrayList<StavkaPorudzbine>());
				return Response.status(200).build();
			} else {
				return Response.status(400).entity("Pogresan password!").build();
			}
		}

		if (session.getAttribute("user") != null) {
			return Response.status(400).entity("Vec ste ulogovani!").build();
		} else {
			return Response.status(400).entity("Logovanje nije uspesno!").build();
		}
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(Korisnik userToRegister) {

		if (userToRegister.getUsername() == null || userToRegister.getPassword() == null
				|| userToRegister.getEmail() == null || userToRegister.getUsername().equals("")
				|| userToRegister.getPassword().equals("") || userToRegister.getEmail().equals("")) {
			return Response.status(400).entity("Username, password i email su obavezna polja.").build();
		}
		
		KorisniciDAO korisniciDao = (KorisniciDAO) context.getAttribute("users");

		if (korisniciDao.searchKorisnik(userToRegister.getUsername()) != null) {
			return Response.status(400).entity("Username koji ste uneli vec je zauzet.").build();
		} else {
			korisniciDao.addKorisnik(userToRegister);
			return Response.status(200).build();
		}
	}

	@PostConstruct
	public void init() {
		
		if (context.getAttribute("users") == null) {
			String contextPath = context.getRealPath("");
			KorisniciDAO korisniciDao = new KorisniciDAO(contextPath);
			context.setAttribute("users", korisniciDao);
		}
		
		HttpSession session = request.getSession();
		if (session.getAttribute("cart") == null) {
			List<StavkaPorudzbine> stavkaPorudzbine = new ArrayList<StavkaPorudzbine>();
			session.setAttribute("cart", stavkaPorudzbine);
		}
		if (session.getAttribute("cartadmin") == null) {
			List<StavkaPorudzbine> stavkaPorudzbine = new ArrayList<StavkaPorudzbine>();
			session.setAttribute("cartadmin", stavkaPorudzbine);
		}
	}

}