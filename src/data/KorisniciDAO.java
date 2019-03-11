/**
 * 
 */
package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import beans.Korisnik;
import utils.StatusPorudzbine;
import utils.Uloge;

/**
 * @author game_changer96
 *
 */
public class KorisniciDAO {

	private HashMap<String, Korisnik> korisnici;
	private String korisniciPutanja = "";

	public KorisniciDAO() {
		
	}

	public KorisniciDAO(String korisniciPutanja) {

		this.setKorisnici(new HashMap<String, Korisnik>());
		this.setKorisniciPutanja(korisniciPutanja);
		
		loadKorisnici(korisniciPutanja);
	}

	// Metode
	public boolean dodajUListuDodeljenihPorudzbina(String username, int orderId) {
		boolean ret = false;
		
		if(!getKorisniciKolekcija().isEmpty()) {
			for(Korisnik k : getKorisniciKolekcija()) {
				if(k.getUsername().equals(username)) {
					k.getListaDodeljenihPorudzbina().add(orderId);
					
					saveKorisnici();
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}
	
	public boolean promeniStatusDostavljaca(String username, int vehicleId) {
		boolean ret = false;
		
		if(!getKorisniciKolekcija().isEmpty()) {
			for(Korisnik k : getKorisniciKolekcija()) {
				if(k.getUsername().equals(username)) {
					k.setVozilo(vehicleId);
					k.setZauzet(!k.isZauzet());
					
					saveKorisnici();
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}
	
	public void changeBonusPoints(String username, int iskoristenoBonusPoena, int punaCena) {
		if(korisnici.containsKey(username)) {
			
			int trenutnoBonusPoena = korisnici.get(username).getBonusPoeni();
			if(iskoristenoBonusPoena == 0 && punaCena >= 500 && trenutnoBonusPoena < 10) {
				korisnici.get(username).setBonusPoeni(trenutnoBonusPoena + 1);
				saveKorisnici();
			} else {
				korisnici.get(username).setBonusPoeni(trenutnoBonusPoena - iskoristenoBonusPoena);
				saveKorisnici();
			}
		}
	}
	
	public boolean removeRestaurantFromFavourite(Korisnik user, int restaurantId) {
		boolean ret = false;
		for(Korisnik k : getKorisniciKolekcija()) {
			if(k.getUsername().equals(user.getUsername())) {
				if(k.izbaciRestoranIzOmiljenih(restaurantId)) {
					saveKorisnici();
					ret = true;
					break;
				}
			}
		}
	
		return ret;
	}
	
	public boolean addRestaurantToFavourite(Korisnik user, int restaurantId) {
		boolean ret = false;
		for(Korisnik k : getKorisniciKolekcija()) {
			if(k.getUsername().equals(user.getUsername())) {
				if(k.dodajRestoranUOmiljene(restaurantId)) {
					saveKorisnici();
					ret = true;
					break;
				}
			}
		}
	
		return ret;
	}
	
	public void addKorisnik(Korisnik k) {
		getKorisnici().put(k.getUsername(), k);
		saveKorisnici();
	}

	public Korisnik searchKorisnik(String username) {
		if (getKorisnici() != null) {
			for (Korisnik k : getKorisnici().values()) {
				if (k.getUsername().equals(username)) {
					return k;
				}
			}
		}
		return null;
	}

	public void changeUserRole(String username, String role) {
		for (Korisnik k : getKorisnici().values()) {
			if (k.getUsername().equals(username)) {
				k.setUloga(role);
				saveKorisnici();
				return;
			}
		}
	}

	// GET i SET metode
	public HashMap<String, Korisnik> getKorisnici() {
		if (!korisnici.isEmpty()) {
			return korisnici;
		}

		return null;
	}

	public void setKorisnici(HashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}

	public String getKorisniciPutanja() {
		return korisniciPutanja;
	}

	public void setKorisniciPutanja(String korisniciPutanja) {
		this.korisniciPutanja = korisniciPutanja;
	}

	public Collection<Korisnik> getKorisniciKolekcija() {
		if (!korisnici.isEmpty()) {
			return korisnici.values();
		}

		return null;
	}

	// Serijalizacija
	private void saveKorisnici() {
		File f = new File(korisniciPutanja + "/data/korisnici.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringUsers = objectMapper.writeValueAsString(korisnici);
			fileWriter.write(stringUsers);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Ucitavanje korisnika iza fajla korisnici.txt
	@SuppressWarnings("unchecked")
	private void loadKorisnici(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/data/korisnici.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, Korisnik.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			korisnici = ((HashMap<String, Korisnik>) objectMapper.readValue(file, type));
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringUsers = objectMapper.writeValueAsString(korisnici);
				fileWriter.write(stringUsers);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Ucitavanje test podataka korisnika
	private void ucitajTestPodatke() {

		Korisnik admin = new Korisnik("marjanskid", "himym", "Dusan", "Marjanski", "0695297515",
				"marjanskid@yahoo.com");

		Korisnik kupac = new Korisnik("kupac", "kupac", "Milana", "Marjanski", "06945327535", "marjanskim@yahoo.com");

		Korisnik dostavljac = new Korisnik("dostava", "dostava", "Zivko", "Popovic", "0653829284",
				"doktorzile@akademija.rs");

		admin.setUloga(Uloge.ADMINISTRATOR);
		dostavljac.setUloga(Uloge.DOSTAVLJAC);

		korisnici.put(admin.getUsername(), admin);
		korisnici.put(kupac.getUsername(), kupac);
		korisnici.put(dostavljac.getUsername(), dostavljac);
	}

	public void promenaKupca(Korisnik stariKupac, String kupac, int idPorudzbine) {
		if(!getKorisniciKolekcija().isEmpty()) {
			for(Korisnik k : getKorisniciKolekcija()) {
				if(k.equals(stariKupac)) {
					// brisanje iz liste porudzbina starog kupca
					int index = k.getListaPorudzbina().indexOf(idPorudzbine);
					k.getListaPorudzbina().remove(index);
					saveKorisnici();
				}
				if(k.getUsername().equals(kupac)) {
					// dodavanje u listu porudzbina novog kupca
					k.getListaPorudzbina().add(idPorudzbine);
					saveKorisnici();
				}
			}
		}	
	}

	public void promenaDostavljaca(Korisnik stariDostavljac, String noviDostavljacUsername, int idPorudzbine, String statusPorudzbine) {
		if(!getKorisniciKolekcija().isEmpty()) {
			for(Korisnik k : getKorisniciKolekcija()) {
				if(k.equals(stariDostavljac)) {
					// brisanje iz liste porudzbina starog kupca
					int index = k.getListaDodeljenihPorudzbina().indexOf(idPorudzbine);
					k.getListaDodeljenihPorudzbina().remove(index);
					if(statusPorudzbine.equals(StatusPorudzbine.DOSTAVA_U_TOKU)) {
						k.setZauzet(false);
					}
					saveKorisnici();
				}
				if(k.getUsername().equals(noviDostavljacUsername)) {
					// dodavanje u listu porudzbina novog kupca
					k.getListaDodeljenihPorudzbina().add(idPorudzbine);
					if(statusPorudzbine.equals(StatusPorudzbine.DOSTAVA_U_TOKU)) {
						k.setZauzet(true);
					}
					saveKorisnici();
				}
			}
		}	
	}

	public void changeOrdersList(String username, int id) {
		if(!getKorisniciKolekcija().isEmpty()) {
			for(Korisnik k : getKorisniciKolekcija()) {
				if(k.getUsername().equals(username)) {
					k.getListaPorudzbina().add(id);
					saveKorisnici();
				}
			}
		}	
		
	}

	public void addBuyerDeliverer(int id, String kupac, String dostavljac, String status) {
		if(!getKorisniciKolekcija().isEmpty()) {
			for(Korisnik k : getKorisniciKolekcija()) {
				if(k.getUsername().equals(kupac)) {
					k.getListaDodeljenihPorudzbina().add(id);
					saveKorisnici();
				}
				if(!dostavljac.equals("nema")) {
					k.getListaDodeljenihPorudzbina().add(id);
					if(status.equals("dostava u toku")) {
						k.setZauzet(true);
					}
					saveKorisnici();
				}
			}
		}
		
	}
}
