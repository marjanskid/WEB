/**
 * 
 */
package beans;

/**
 * @author game_changer96
 *
 */
public class Vozilo {

	private String marka;
	private String model;
	private String tipVozila;
	private String registarskaOznaka;
	private String godinaProizvodnje;
	private boolean voziloSlobodno;
	private String napomena;
	// id - jedinstcena oznaka
	private int id;
	private boolean obrisano;

	// KONSTRUKTORI
	public Vozilo() {
		
		this.marka = "";
		this.model = "";
		this.tipVozila = "";
		this.registarskaOznaka = "";
		this.godinaProizvodnje = "";
		this.voziloSlobodno = true; // po difoltu je slobodno
		this.napomena = "";
		this.obrisano = false; // po difoltu nije obrisano
	}

	public Vozilo(String marka, String model, String tipVozila, String registarskaOznaka,
			String godinaProizvodnje, String napomena) {
		
		super();
		
		this.setMarka(marka);
		this.setModel(model);
		this.setTipVozila(tipVozila);
		this.setRegistarskaOznaka(registarskaOznaka);
		this.setGodinaProizvodnje(godinaProizvodnje);
		this.setNapomena(napomena);
	}

	// GET i SET metode
	public String getMarka() {
		return marka;
	}

	public void setMarka(String marka) {
		this.marka = marka;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTipVozila() {
		return tipVozila;
	}

	public void setTipVozila(String tipVozila) {
		this.tipVozila = tipVozila;
	}

	public String getRegistarskaOznaka() {
		return registarskaOznaka;
	}

	public void setRegistarskaOznaka(String registarskaOznaka) {
		this.registarskaOznaka = registarskaOznaka;
	}

	public String getGodinaProizvodnje() {
		return godinaProizvodnje;
	}

	public void setGodinaProizvodnje(String godinaProizvodnje) {
		this.godinaProizvodnje = godinaProizvodnje;
	}

	public boolean isVoziloSlobodno() {
		return voziloSlobodno;
	}

	public void setVoziloSlobodno(boolean voziloSlobodno) {
		this.voziloSlobodno = voziloSlobodno;
	}

	public String getNapomena() {
		return napomena;
	}

	public void setNapomena(String napomena) {
		this.napomena = napomena;
	}

	public boolean isObrisano() {
		return obrisano;
	}

	public void setObrisano(boolean obrisano) {
		this.obrisano = obrisano;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
