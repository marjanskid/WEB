/**
 * 
 */
package beans;

/**
 * @author game_changer96
 *
 */
public class StavkaPorudzbine {
	
	private int kolicina;
	private Artikl artikl;
	
	public StavkaPorudzbine() {
		
	}
	
	public StavkaPorudzbine(int kolicina, Artikl artikl) {
		this.kolicina = kolicina;
		this.artikl = artikl;
	}
	
	// GET i SET metode
	public int getKolicina() {
		return kolicina;
	}
	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}
	public Artikl getArtikl() {
		return artikl;
	}
	public void setArtikl(Artikl artikl) {
		this.artikl = artikl;
	}
	
}
