/**
 * 
 */
package beans;

import java.util.Comparator;

/**
 * @author game_changer96
 *
 */
public class Artikl {

	private String naziv;
	private String jedinicnaCena;
	private String opis;
	private String kolicina;
	private String tipJela;
	// u svrhe izlistavanja najpopularnijih jela i pica
	private int brojNarucenihPorcija;
	// id - jedinstcena oznaka
	private int id;
	private boolean obrisan;

	// KONSTRUKTORI
	public Artikl() {

		this.naziv = "";
		this.jedinicnaCena = "";
		this.opis = "";
		this.kolicina = "";
		this.tipJela = "";
		this.brojNarucenihPorcija = 0;
		this.setObrisan(false);
	}

	public Artikl(String naziv, String jedinicnaCena, String opis, String kolicina, String tipJela) {

		super();

		this.setNaziv(naziv);
		this.setJedinicnaCena(jedinicnaCena);
		this.setOpis(opis);
		this.setKolicina(kolicina);
		this.setTipJela(tipJela);
	}

	// GET i SET metode
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getJedinicnaCena() {
		return jedinicnaCena;
	}

	public void setJedinicnaCena(String jedinicnaCena) {
		this.jedinicnaCena = jedinicnaCena;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getKolicina() {
		return kolicina;
	}

	public void setKolicina(String kolicina) {
		this.kolicina = kolicina;
	}

	public String getTipJela() {
		return tipJela;
	}

	public void setTipJela(String tipJela) {
		this.tipJela = tipJela;
	}

	public int getBrojNarucenihPorcija() {
		return brojNarucenihPorcija;
	}

	public void setBrojNarucenihPorcija(int brojNarucenihPorcija) {
		this.brojNarucenihPorcija = brojNarucenihPorcija;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isObrisan() {
		return obrisan;
	}

	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}
}