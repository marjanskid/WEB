/**
 * 
 */
package beans;

import java.util.ArrayList;
import java.util.List;

import utils.TipJela;

/**
 * @author game_changer96
 *
 */
public class Restoran {

	private String naziv;
	private String adresa;
	private String kategorijaRestorana;
	private List<Integer> listaJela;
	private List<Integer> listaPica;
	// id - jedinstcena oznaka
	private int id;
	private boolean obrisan;

	// KONSTRUKTORI
	public Restoran() {
		
		this.naziv = "";
		this.adresa = "";
		this.kategorijaRestorana = "";
		this.listaJela = new ArrayList<Integer>();
		this.listaPica = new ArrayList<Integer>();
		this.obrisan = false;
	}

	public Restoran(String naziv, String adresa, String kategorijaRestorana, List<Integer> listaJela,
			List<Integer> listaPica) {
		
		super();
		
		this.setNaziv(naziv);
		this.setAdresa(adresa);
		this.setKategorijaRestorana(kategorijaRestorana);
		this.setListaJela(listaJela);
		this.setListaPica(listaPica);
	}
	
	public boolean addArticle(Artikl a) {
		boolean ret = false;
		
		if(a.getTipJela().equals(TipJela.PICE)) {
			if(!(listaPica.contains(a))) {
				listaPica.add(a.getId());
				ret = true;
			}
		} else if(a.getTipJela().equals(TipJela.JELO)) {
			if(!listaJela.contains(a)) {
				listaJela.add(a.getId());
				ret = true;
			}
		}
		
		return ret;
	}

	// GET i SET metode
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getKategorijaRestorana() {
		return kategorijaRestorana;
	}

	public void setKategorijaRestorana(String kategorijaRestorana) {
		this.kategorijaRestorana = kategorijaRestorana;
	}

	public List<Integer> getListaJela() {
		return listaJela;
	}

	public void setListaJela(List<Integer> listaJela) {
		this.listaJela = listaJela;
	}

	public List<Integer> getListaPica() {
		return listaPica;
	}

	public void setListaPica(List<Integer> listaPica) {
		this.listaPica = listaPica;
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
