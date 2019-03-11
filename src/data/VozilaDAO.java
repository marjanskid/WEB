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

import beans.Vozilo;
import utils.TipoviVozila;

/**
 * @author game_changer96
 *
 */
public class VozilaDAO {

	private HashMap<Integer, Vozilo> vozila;
	private String vozilaPutanja = "";

	public VozilaDAO() {
		// TODO Auto-generated constructor stub
	}

	public VozilaDAO(String contextPath) {
		this.setVozila(new HashMap<Integer, Vozilo>());
		this.setVozilaPutanja(contextPath);

		ucitajVozila(vozilaPutanja);
	}
	
	public boolean promeniStatusVozila(int vehicleId) {
		boolean ret = false;
		
		if(!getVozilaKolekcija().isEmpty()) {
			for(Vozilo v : getVozilaKolekcija()) {
				if(v.getId() == vehicleId) {
					v.setVoziloSlobodno(!v.isVoziloSlobodno());
					sacuvajVozila();
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}

	public boolean obrisiVozilo(int id) {
		if (!getVozilaKolekcija().isEmpty()) {
			for (Vozilo v : getVozilaKolekcija()) {
				if (v.getId() == id) {
					// obrisano = true je kad je vozilo logicni obrisan
					v.setObrisano(true);
					sacuvajVozila();
					return true;
				}
			}
		}
		return false;
	}

	public void dodajVozilo(Vozilo v) {
		int maxId = 0;
		if (!getVozilaKolekcija().isEmpty()) {
			for (Vozilo v1 : getVozilaKolekcija()) {
				if (v1.getId() > maxId) {
					maxId = v1.getId();
				}
			}
			maxId++;
			v.setId(maxId);
		}

		vozila.put(maxId, v);
		sacuvajVozila();
	}

	public boolean izmeniVozilo(Vozilo v) {
		boolean ret = false;
		if (!getVozilaKolekcija().isEmpty()) {
			for (Vozilo v1 : getVozilaKolekcija()) {
				if (v1.getId() == v.getId()) {
					v1.setMarka(v.getMarka());
					v1.setModel(v.getModel());
					v1.setTipVozila(v.getTipVozila());
					v1.setGodinaProizvodnje(v.getGodinaProizvodnje());
					v1.setNapomena(v.getNapomena());
					v1.setRegistarskaOznaka(v.getRegistarskaOznaka());
					sacuvajVozila();
					ret = true;
					break;
				}
			}
		}

		return ret;
	}

	public Collection<Vozilo> getVozilaKolekcija() {
		return vozila.values();
	}

	// Serijalizacija
	private void sacuvajVozila() {
		File f = new File(vozilaPutanja + "/data/vozila.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringVehicles = objectMapper.writeValueAsString(vozila);
			fileWriter.write(stringVehicles);
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

	// Ucitavanje vozila iza fajla vozila.txt
	@SuppressWarnings("unchecked")
	private void ucitajVozila(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/data/vozila.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, Integer.class, Vozilo.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			vozila = ((HashMap<Integer, Vozilo>) objectMapper.readValue(file, type));
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringVehicles = objectMapper.writeValueAsString(vozila);
				fileWriter.write(stringVehicles);
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
	public HashMap<Integer, Vozilo> getVozila() {
		return vozila;
	}

	public void setVozila(HashMap<Integer, Vozilo> vozila) {
		this.vozila = vozila;
	}

	public String getVozilaPutanja() {
		return vozilaPutanja;
	}

	public void setVozilaPutanja(String vozilaPutanja) {
		this.vozilaPutanja = vozilaPutanja;
	}

	private void ucitajTestPodatke() {

		Vozilo v1 = new Vozilo("Audi", "A4", TipoviVozila.AUTOMOBIL, "NS147DU", "2017", "Dobra masina");
		Vozilo v2 = new Vozilo("Aprillia", "SR50", TipoviVozila.SKUTER, "NS3243", "2016", "Fin skuter");
		Vozilo v3 = new Vozilo("Capriolo", "Level 7.2", TipoviVozila.BICIKL, "nema", "2018", "MTB 27.5");

		dodajVozilo(v1);
		dodajVozilo(v2);
		dodajVozilo(v3);
	}

}
