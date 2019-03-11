/**
 * 
 */
package beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.StatusPorudzbine;
import utils.Uloge;

/**
 * @author game_changer96
 *
 */
public class Porudzbina {
	
	private List<StavkaPorudzbine> stavkePorudzbine;
	private String datumIVremePorudzbine;
	private Korisnik kupac;
	private Korisnik dostavljac;
	private String statusPorudzbine;
	private String napomena;
	// obrisana - false ako nije, true ako jeste
	private boolean obrisana;
	// id - jedinstvena oznaka
	private int id;
	// bonus poeni koje kupac koristi
	private int bonusPoeni;
	// puna cena (bez popusta)
	private int punaCena;
	
	// KONSTRUKTORI
	public Porudzbina() {

		this.setStavkaPorudzbine(new ArrayList<StavkaPorudzbine>());
		this.setDatumIVremePorudzbine();
		this.kupac = new Korisnik();
		this.dostavljac = new Korisnik();
		this.dostavljac.setUloga(Uloge.DOSTAVLJAC);
		this.setStatusPorudzbine(StatusPorudzbine.PORUCENO);
		this.napomena = "";
		this.setObrisana(false);
		this.setBonusPoeni(0);
		this.setPunaCena(0);
	}

	public Porudzbina(List<StavkaPorudzbine> stavkePorudzbine, 
					Korisnik kupac, Korisnik dostavljac,
					String statusPorudzbine, String napomena) {
		
		super();
		
		this.setStavkaPorudzbine(stavkePorudzbine);
		this.setDatumIVremePorudzbine();
		this.setKupac(kupac);
		this.setDostavljac(dostavljac);
		this.setStatusPorudzbine(statusPorudzbine);
		this.setNapomena(napomena);
	}
	
	// GET i SET metode
	public List<StavkaPorudzbine> getStavkePorudzbine() {
		return stavkePorudzbine;
	}

	public void setStavkaPorudzbine(List<StavkaPorudzbine> stavkaPorudzbine) {
		this.stavkePorudzbine = stavkaPorudzbine;
	}

	public String getStatusPorudzbine() {
		return statusPorudzbine;
	}

	public void setStatusPorudzbine(String statusPorudzbine) {
		this.statusPorudzbine = statusPorudzbine;
	}

	public String getNapomena() {
		return napomena;
	}

	public void setNapomena(String napomena) {
		this.napomena = napomena;
	}

	public Korisnik getKupac() {
		return kupac;
	}

	public void setKupac(Korisnik kupac) {
		this.kupac = kupac;
	}

	public Korisnik getDostavljac() {
		return dostavljac;
	}

	public void setDostavljac(Korisnik dostavljac) {
		this.dostavljac = dostavljac;
	}

	public String getDatumIVremePorudzbine() {
		return datumIVremePorudzbine;
	}

	public void setDatumIVremePorudzbine() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date();
		this.datumIVremePorudzbine = (formatter.format(date)).toString();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isObrisana() {
		return obrisana;
	}

	public void setObrisana(boolean obrisana) {
		this.obrisana = obrisana;
	}

	public int getBonusPoeni() {
		return bonusPoeni;
	}

	public void setBonusPoeni(int bonusPoeni) {
		this.bonusPoeni = bonusPoeni;
	}

	public int getPunaCena() {
		return punaCena;
	}

	public void setPunaCena(int punaCena) {
		this.punaCena = punaCena;
	}
}
