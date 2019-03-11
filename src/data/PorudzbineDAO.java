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
import beans.Porudzbina;
import utils.Uloge;

/**
 * @author game_changer96
 *
 */
public class PorudzbineDAO {

	private HashMap<Integer, Porudzbina> porudzbine;
	private String porudzbinePutanja = "";

	public PorudzbineDAO() {
		
	}
	
	public PorudzbineDAO(String contextPath) {
		setPorudzbine(new HashMap<Integer, Porudzbina>());
		setPorudzbinePutanja(contextPath);
		
		ucitajPorudzbine(porudzbinePutanja);
	}
	
	// Metode
	public boolean postaviDostavljaca(int orderId, Korisnik dostavljac) {
		boolean ret = false;
		
		if(!getPorudzbineCollection().isEmpty()) {
			for(Porudzbina p : getPorudzbineCollection()) {
				if(p.getId() == orderId) {
					p.setDostavljac(dostavljac);
					sacuvajPorudzbine();
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}
	
	public boolean promeniStatusPorudzbine(int idPorudzbine, String noviStatus) {
		boolean ret = false;
		if(!getPorudzbineCollection().isEmpty()) {
			for(Porudzbina p1 : getPorudzbineCollection()){
				if(p1.getId() == idPorudzbine) {
					porudzbine.get(p1.getId()).setStatusPorudzbine(noviStatus);
					sacuvajPorudzbine();
					ret = true;
					break;
				}
			}
		}
		return ret;
	}
	
	public boolean obrisiPorudzbinu(int id) {
		if(!getPorudzbineCollection().isEmpty()) {
			for (Porudzbina p : getPorudzbineCollection()) {
				if(p.getId() == id) {
					// obrisan = true je kad je artikl logicni obrisan
					p.setObrisana(true);
					sacuvajPorudzbine();
					return true;
				}
			}
		}
		return false;
	}

	public void dodajPorudzbinu(Porudzbina p) {
		int maxId = 0;
		if(!getPorudzbineCollection().isEmpty()) {
			for(Porudzbina p1 : getPorudzbineCollection()) {
				if(p1.getId() > maxId) {
					maxId = p1.getId();
				}
			}
			maxId++;
			p.setId(maxId);
		}
		
		porudzbine.put(maxId, p);
		sacuvajPorudzbine();
	}
	
	public boolean izmeniPorudzbinu(Porudzbina p) {
		boolean ret = false;
		if(!getPorudzbineCollection().isEmpty()) {
			for(Porudzbina p1 : getPorudzbineCollection()) {
				if(p1.getId() == p.getId()) {
					p1.setStavkaPorudzbine(p.getStavkePorudzbine());
					p1.setStatusPorudzbine(p.getStatusPorudzbine());
					p1.setKupac(p.getKupac());
					p1.setDostavljac(p.getDostavljac());
					p1.setNapomena(p.getNapomena());
					p1.setBonusPoeni(p.getBonusPoeni());
					sacuvajPorudzbine();
					ret = true;
					break;
				}
			}
		}

		return ret;
	}
	
	public Collection<Porudzbina> getPorudzbineCollection() {
		return porudzbine.values();
	}
	
	// Serijalizacija
	private void sacuvajPorudzbine() {
		File f = new File(porudzbinePutanja + "/data/porudzbine.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringOrders = objectMapper.writeValueAsString(porudzbine);
			fileWriter.write(stringOrders);
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

	// Ucitavanje porudzbina iza fajla porudzbine.txt
	@SuppressWarnings("unchecked")
	private void ucitajPorudzbine(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/data/porudzbine.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, Integer.class, Porudzbina.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			porudzbine = ((HashMap<Integer, Porudzbina>) objectMapper.readValue(file, type));
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringOrders = objectMapper.writeValueAsString(porudzbine);
				fileWriter.write(stringOrders);
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

	// GET i SET metode
	public HashMap<Integer, Porudzbina> getPorudzbine() {
		return porudzbine;
	}

	public void setPorudzbine(HashMap<Integer, Porudzbina> porudzbine) {
		this.porudzbine = porudzbine;
	}

	public String getPorudzbinePutanja() {
		return porudzbinePutanja;
	}

	public void setPorudzbinePutanja(String porudzbinePutanja) {
		this.porudzbinePutanja = porudzbinePutanja;
	}

	public void promeniNapomenu(String napomena, int idPorudzbine) {
		if(!getPorudzbineCollection().isEmpty()) {
			for(Porudzbina p : getPorudzbineCollection()) {
				if(p.getId() == idPorudzbine) {
					p.setNapomena(napomena);
					sacuvajPorudzbine();
				}
			}
		}	
	}

	public void promeniBrojPoena(int bonusPoeni, int idPorudzbine) {
		if(!getPorudzbineCollection().isEmpty()) {
			for(Porudzbina p : getPorudzbineCollection()) {
				if(p.getId() == idPorudzbine) {
					p.setBonusPoeni(bonusPoeni);
					sacuvajPorudzbine();
				}
			}
		}
	}

	public void promenaKupca(KorisniciDAO korisniciDao, String kupac, int idPorudzbine) {
		if(!getPorudzbineCollection().isEmpty()) {
			for(Porudzbina p : getPorudzbineCollection()) {
				if(p.getId() == idPorudzbine) {
					p.setKupac(korisniciDao.getKorisnici().get(kupac));
					sacuvajPorudzbine();
				}
			}
		}
		
	}

	public void promenaDostavljaca(KorisniciDAO korisniciDao, String dostavljac, int idPorudzbine, String status) {
		if(!getPorudzbineCollection().isEmpty()) {
			for(Porudzbina p : getPorudzbineCollection()) {
				if(p.getId() == idPorudzbine) {
					if(dostavljac != "nema") {
						p.setDostavljac(korisniciDao.getKorisnici().get(dostavljac));
					} else {
						Korisnik dost = new Korisnik();
						dost.setUloga(Uloge.DOSTAVLJAC);
						p.setDostavljac(dost);
					}
					sacuvajPorudzbine();
				}
			}
		}
		
	}
	
}
