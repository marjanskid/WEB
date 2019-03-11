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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import beans.Artikl;
import beans.Restoran;
import utils.KategorijeRestorana;

/**
 * @author game_changer96
 *
 */
public class RestoraniDAO {

	private HashMap<Integer, Restoran> restorani;
	private String restoraniPutanja = "";

	public RestoraniDAO() {
		// TODO Auto-generated constructor stub
	}

	public RestoraniDAO(String contextPath) {

		this.setRestorani(new HashMap<Integer, Restoran>());
		this.setRestoraniPutanja(contextPath);
		
		ucitajRestorane(contextPath);
	}
	
	public boolean obrisiRestoran(int id) {
		if(!getRestoraniKolekcija().isEmpty()) {
			for (Restoran r : getRestoraniKolekcija()) {
				if(r.getId() == id) {
					// obrisan = true je kad je restoran logicni obrisan
					r.setObrisan(true);
					sacuvajRestorane();
					return true;
				}
			}
		}
		return false;
	}

	public void dodajRestoran(Restoran r) {
		int maxId = 0;
		if(!getRestoraniKolekcija().isEmpty()) {
			for(Restoran r1 : this.getRestoraniKolekcija()) {
				if(r1.getId() > maxId) {
					maxId = r1.getId();
				}
			}
			maxId++;
			r.setId(maxId);
		}
		
		restorani.put(maxId, r);
		sacuvajRestorane();
	}
	
	public boolean izmeniRestoran(Restoran r) {
		boolean ret = false;
		if(!getRestoraniKolekcija().isEmpty()) {
			for(Restoran r1 : getRestoraniKolekcija()) {
				if(r1.getId() == r.getId()) {
					r1.setNaziv(r.getNaziv());
					r1.setAdresa(r.getAdresa());
					r1.setKategorijaRestorana(r.getKategorijaRestorana());
					sacuvajRestorane();
					ret = true;
					break;
				}
			}
		}

		return ret;
	}

	public Collection<Restoran> getRestoraniKolekcija() {
		if (!restorani.isEmpty()) {
			return restorani.values();
		}
		return null;
	}

	// GET i SET metode
	public HashMap<Integer, Restoran> getRestorani() {
		return restorani;
	}

	public void setRestorani(HashMap<Integer, Restoran> restorani) {
		this.restorani = restorani;
	}

	public String getRestoraniPutanja() {
		return restoraniPutanja;
	}

	public void setRestoraniPutanja(String restoraniPutanja) {
		this.restoraniPutanja = restoraniPutanja;
	}

	// Serijalizacija
	private void sacuvajRestorane() {
		File f = new File(restoraniPutanja + "/data/restorani.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringRestaurants = objectMapper.writeValueAsString(restorani);
			fileWriter.write(stringRestaurants);
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

	// Ucitavanje restorani iza fajla restorani.txt
	@SuppressWarnings("unchecked")
	private void ucitajRestorane(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/data/restorani.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, Restoran.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			restorani = ((HashMap<Integer, Restoran>) objectMapper.readValue(file, type));
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringRestaurants = objectMapper.writeValueAsString(restorani);
				fileWriter.write(stringRestaurants);
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
		List<Integer> l1 = new ArrayList<Integer>();

		Restoran r1 = new Restoran("Palazzo Bianco", "Bulevar Cara Dušana 21", KategorijeRestorana.PICERIJA, l1, l1);
		Restoran r2 = new Restoran("Ananda", "Petra Drapšina 51", KategorijeRestorana.DOMACA, l1, l1);
		Restoran r3 = new Restoran("Dizni", "Bulevar cara Lazara 92", KategorijeRestorana.POSLASTICARNICA, l1, l1);

		dodajRestoran(r1);
		dodajRestoran(r2);
		dodajRestoran(r3);
	}

	public boolean dodajArtikl(int restaurantId, Artikl a) {
		boolean ret = false;
		if(!getRestoraniKolekcija().isEmpty()) {
			for(Restoran r1 : getRestoraniKolekcija()) {
				if(r1.getId() == restaurantId) {
					if(r1.addArticle(a)) {
						sacuvajRestorane();
						ret = true;
					}
					break;
				}
			}
		}

		return ret;
	}
}
