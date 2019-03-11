/**
 * 
 */
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

import beans.Artikl;
import beans.Korisnik;
import data.ArtikliDAO;
import utils.Uloge;

/**
 * @author game_changer96
 *
 */
@Path("/article")
public class ArticleService {
	
	@Context
	ServletContext context;

	@Context
	HttpServletRequest request;

	public ArticleService() {
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Path("/popularArticles")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Artikl> getMostPopularArticles() {
		
		ArtikliDAO artikliDao = (ArtikliDAO) context.getAttribute("articles");
		List<Artikl> najpopularniji = new ArrayList<Artikl>(); 
		if(!artikliDao.getArtikliKolekcija().isEmpty()) {
			for(Artikl a : artikliDao.getArtikliKolekcija()) {
				najpopularniji.add(a);
			}
			Collections.sort(najpopularniji, new Komparator());
		}
		
		if(najpopularniji.size() > 10) {
			najpopularniji.subList(0, 9);
		}
		
		return najpopularniji;
	}
	
	@PUT
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editArticle(Artikl a) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {	
				ArtikliDAO artikliDAO = (ArtikliDAO) context.getAttribute("articles");
				if(artikliDAO.izmeniArtikl(a)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Izmena podataka o artiklu nije uspešna!").build();
				}
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
	public Response addArticle(Artikl a) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				ArtikliDAO artikliDao = (ArtikliDAO) context.getAttribute("articles");
				artikliDao.dodajArtikl(a);
				return Response.status(200).build();
			} else {
				return Response.status(400).entity("Nemate pravo da dodajte nove artikle!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteArticle( @PathParam("id") int id) {
		
		HttpSession session = request.getSession();
		Korisnik user = (Korisnik) session.getAttribute("user");
		
		if(user != null) {
			if(user.getUloga().equals(Uloge.ADMINISTRATOR)) {
				ArtikliDAO artikliDao = (ArtikliDAO) context.getAttribute("articles");
				if(artikliDao.obrisiArtikl(id)) {
					return Response.status(200).build();
				} else {
					return Response.status(400).entity("Artikl sa datim id-jem nije pronađen!").build();
				}
			} else {
				return Response.status(400).entity("Nemate pravo da brišete artikle!").build();
			}
		} else {
			return Response.status(400).entity("Morate biti ulogovani da biste izvršili ovu akciju!").build();
		}
	}
	
	@GET
	@Path("/articles")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Artikl> getArticles() {
		ArtikliDAO artikliDao = (ArtikliDAO) context.getAttribute("articles");
		return artikliDao.getArtikliKolekcija();
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("articles") == null) {
			String contextPath = context.getRealPath("");
			ArtikliDAO artikliDao = new ArtikliDAO(contextPath);
			context.setAttribute("articles", artikliDao);
		}
	}
}

class Komparator implements Comparator<Artikl> {

	@Override
	public int compare(Artikl a1, Artikl a2) {
		return a2.getBrojNarucenihPorcija() - a1.getBrojNarucenihPorcija();
	}	
}
