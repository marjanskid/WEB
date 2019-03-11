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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import beans.Artikl;
import beans.StavkaPorudzbine;
import utils.TipJela;

/**
 * @author game_changer96
 *
 */
public class ArtikliDAO {
	
	private HashMap<Integer, Artikl> artikli;
	private String artikliPutanja = "";

	public ArtikliDAO() {
		// TODO Auto-generated constructor stub
	}

	public ArtikliDAO(String contextPath) {
		this.setArtikli(new HashMap<Integer, Artikl>());
		this.setArtikliPutanja(contextPath);
		
		ucitajArtikle(artikliPutanja);
	}
	
	// metode
	public void editFavouriteCount(List<StavkaPorudzbine> stavkePorudzbine) {
		if(!getArtikliKolekcija().isEmpty()) {
			for(StavkaPorudzbine sp : stavkePorudzbine) {
				int idArtikla = sp.getArtikl().getId();
				int brojNarucenihPorcija = sp.getKolicina();
				int trenutniBrojPorcija = artikli.get(idArtikla).getBrojNarucenihPorcija();
				artikli.get(idArtikla).setBrojNarucenihPorcija(brojNarucenihPorcija + trenutniBrojPorcija);
				sacuvajArtikle();
			}
		}
	}
	
	public boolean obrisiArtikl(int id) {
		if(!getArtikliKolekcija().isEmpty()) {
			for (Artikl a : getArtikliKolekcija()) {
				if(a.getId() == id) {
					// obrisan = true je kad je artikl logicni obrisan
					a.setObrisan(true);
					sacuvajArtikle();
					return true;
				}
			}
		}
		return false;
	}

	public void dodajArtikl(Artikl a) {
		int maxId = 0;
		if(!getArtikliKolekcija().isEmpty()) {
			for(Artikl a1 : getArtikliKolekcija()) {
				if(a1.getId() > maxId) {
					maxId = a1.getId();
				}
			}
			maxId++;
			a.setId(maxId);
		}
		
		artikli.put(maxId, a);
		sacuvajArtikle();
	}
	
	public boolean izmeniArtikl(Artikl a) {
		boolean ret = false;
		if(!getArtikliKolekcija().isEmpty()) {
			for(Artikl a1 : getArtikliKolekcija()) {
				if(a1.getId() == a.getId()) {
					a1.setNaziv(a.getNaziv());
					a1.setKolicina(a.getKolicina());
					a1.setJedinicnaCena(a.getJedinicnaCena());
					a1.setOpis(a.getOpis());
					a1.setTipJela(a.getTipJela());
					sacuvajArtikle();
					ret = true;
					break;
				}
			}
		}

		return ret;
	}

	public Collection<Artikl> getArtikliKolekcija() {
		return artikli.values();
	}

	// Serijalizacija
	private void sacuvajArtikle() {
		File f = new File(artikliPutanja + "/data/artikli.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringArticles = objectMapper.writeValueAsString(artikli);
			fileWriter.write(stringArticles);
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

	// Ucitavanje artikala iza fajla artikli.txt
	@SuppressWarnings("unchecked")
	private void ucitajArtikle(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/data/artikli.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, Integer.class, Artikl.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			artikli = ((HashMap<Integer, Artikl>) objectMapper.readValue(file, type));
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringArticles = objectMapper.writeValueAsString(artikli);
				fileWriter.write(stringArticles);
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
	public HashMap<Integer, Artikl> getArtikli() {
		return artikli;
	}

	public void setArtikli(HashMap<Integer, Artikl> artikli) {
		this.artikli = artikli;
	}

	public String getArtikliPutanja() {
		return artikliPutanja;
	}

	public void setArtikliPutanja(String artikliPutanja) {
		this.artikliPutanja = artikliPutanja;
	}
	
	
	private void ucitajTestPodatke() {
		System.out.println("ucitajTestPodatke");
		
		Artikl a1 = new Artikl("Belo meso", "250", "Vrlo ukusno", "150", TipJela.JELO);
		Artikl a2 = new Artikl("Budweiser", "230", "Točeno crno pivo", "500", TipJela.PICE);
		Artikl a3 = new Artikl("Nutella-plazma", "180", "Slakta zanimacija", "230", TipJela.JELO);
		Artikl a4 = new Artikl("Rose", "270", "Slatkasto vino", "175", TipJela.PICE);
		Artikl a5 = new Artikl("Quattro stagioni", "970", "Italijanska pica", "800", TipJela.JELO);
		
		dodajArtikl(a1);
		dodajArtikl(a2);
		dodajArtikl(a3);
		dodajArtikl(a4);
		dodajArtikl(a5);
	}

}
