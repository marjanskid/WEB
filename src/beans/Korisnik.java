/**
 * 
 */
package beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Uloge;

/**
 * @author game_changer96
 *
 */
public class Korisnik {

	protected String uloga;

	protected String username;
	protected String password;
	protected String ime;
	protected String prezime;
	protected String kontaktTelefon;
	protected String email;
	protected String datumRegistracije;
	// polja za kupca
	private List<Integer> listaPorudzbina;
	private List<Integer> listaOmiljenihRestorana;
	// polja za dostavljaca
	private int vozilo;
	private List<Integer> listaDodeljenihPorudzbina;
	private boolean zauzet;
	// polje za bonus poene
	private int bonusPoeni;
	

	// KONSTRUKTORI
	public Korisnik() {

		this.uloga = Uloge.KUPAC;
		this.username = "";
		this.password = "";
		this.ime = "";
		this.prezime = "";
		this.kontaktTelefon = "";
		this.email = "";
		this.datumRegistracije = getVremeIDatum();
		// kupac
		this.setListaPorudzbina(new ArrayList<Integer>());
		this.setListaOmiljenihRestorana(new ArrayList<Integer>());
		// dostavljac
		this.setListaDodeljenihPorudzbina(new ArrayList<Integer>());
		this.setVozilo(-1);
		this.setZauzet(false);
		// bonus
		this.bonusPoeni = 0;
		
	}

	public Korisnik(String korisnickoIme, String lozinka, 
					String ime, String prezime,
					String kontaktTelefon, String emailAdresa) {

		super();
		
		this.uloga = Uloge.KUPAC;
		this.username = korisnickoIme;
		this.password = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.kontaktTelefon = kontaktTelefon;
		this.email = emailAdresa;
		this.datumRegistracije = getVremeIDatum();
		// kupac
		this.setListaPorudzbina(new ArrayList<Integer>());
		this.setListaOmiljenihRestorana(new ArrayList<Integer>());
		// dostavljac
		this.setListaDodeljenihPorudzbina(new ArrayList<Integer>());
		this.setVozilo(-1);
		this.setZauzet(false);
		// bonus
		this.bonusPoeni = 0;
	}
	
	public boolean izbaciRestoranIzOmiljenih(int restaurantId) {
		boolean ret = false;
		if(listaOmiljenihRestorana.contains(restaurantId)) {
			int indexR = listaOmiljenihRestorana.indexOf(restaurantId);
			listaOmiljenihRestorana.remove(indexR);
			ret = true;
		}
		
		return ret;
	}

	public boolean dodajRestoranUOmiljene(int restaurantId) {
		boolean ret = false;
		if(!listaOmiljenihRestorana.contains(restaurantId)) {
			listaOmiljenihRestorana.add(restaurantId);
			ret = true;
		}
		
		return ret;
	}

	// GET i SET metode
	public String getUloga() {
		return uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String korisnickoIme) {
		this.username = korisnickoIme;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String lozinka) {
		this.password = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getKontaktTelefon() {
		return kontaktTelefon;
	}

	public void setKontaktTelefon(String kontaktTelefon) {
		this.kontaktTelefon = kontaktTelefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String emailAdresa) {
		this.email = emailAdresa;
	}

	public String getDatumRegistracije() {
		return datumRegistracije;
	}

	public void setDatumRegistracije(String datumRegistracije) {
		this.datumRegistracije = datumRegistracije;
	}
	
	public List<Integer> getListaPorudzbina() {
		return listaPorudzbina;
	}

	public void setListaPorudzbina(List<Integer> listaPorudzbina) {
		this.listaPorudzbina = listaPorudzbina;
	}

	public List<Integer> getListaOmiljenihRestorana() {
		return listaOmiljenihRestorana;
	}

	public void setListaOmiljenihRestorana(List<Integer> listaOmiljenihRestorana) {
		this.listaOmiljenihRestorana = listaOmiljenihRestorana;
	}
	
	public List<Integer> getListaDodeljenihPorudzbina() {
		return listaDodeljenihPorudzbina;
	}

	public void setListaDodeljenihPorudzbina(List<Integer> listaDodeljenihPorudzbina) {
		this.listaDodeljenihPorudzbina = listaDodeljenihPorudzbina;
	}

	public int getVozilo() {
		return vozilo;
	}

	public void setVozilo(int i) {
		this.vozilo = i;
	}
	
	public int getBonusPoeni() {
		return bonusPoeni;
	}

	public void setBonusPoeni(int bonusPoeni) {
		this.bonusPoeni = bonusPoeni;
	}
	
	public boolean isZauzet() {
		return zauzet;
	}

	public void setZauzet(boolean zauzet) {
		this.zauzet = zauzet;
	}
	
	// DATUM i VREME
	private String getVremeIDatum() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String datumIVreme = (formatter.format(date)).toString();
		String ret[] = datumIVreme.split(" ");
		
		return ret[0];
	}
}
