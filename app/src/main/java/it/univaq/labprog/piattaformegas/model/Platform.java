package it.univaq.labprog.piattaformegas.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "platforms")
public class Platform implements Serializable {
    public Platform() {
    }

    public static Platform parseJSon(JSONObject object) {
        if (object == null)
            return null;

        Platform p = new Platform();
        p.setDenominazione(object.optString("cdenominazione__"));
        p.setStato(object.optString("cstato"));
        p.setMinerale(object.optString("cminerale"));
        String tipo = object.optString("ctipo");
        String[] parts = tipo.split("\\|");
        String extractedString = parts[0];
        p.setTipo(extractedString);
        String operatore = object.optString("coperatore");
        String[] parts1 = operatore.split("\\|");
        String extractedString1 = parts1[0];
        p.setOperatore(extractedString1);
        String data = object.optString("canno_costruzione");
        String[] parts2 = data.split("\\|");
        p.setAnnoCostruzione(parts2[0]);


        try {
            String valueLng = object.optString("clongitudine__wgs_84__", null);
            if (valueLng != null) {
                p.setLongitudine(Double.parseDouble(valueLng));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            String valueLat = object.optString("clatitudine__wgs84__", null);
            if (valueLat != null) {
                p.setLatitudine(Double.parseDouble(valueLat));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return p;
    }
    @PrimaryKey
    private Integer id;
    private String denominazione, stato, annoCostruzione, tipo, minerale, operatore ;
    private double longitudine, latitudine;

    public Integer getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDenominazione() {
        return this.denominazione;
    }
    public void setDenominazione(String den) {
        this.denominazione = den;
    }
    public double getLatitudine() {
        return latitudine;
    }
    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }
    public double getLongitudine() {
        return longitudine;
    }
    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public String getAnnoCostruzione() {
        return this.annoCostruzione;
    }
    public void setAnnoCostruzione(String annoCostruzione) {
        this.annoCostruzione = annoCostruzione;
    }
    public String getTipo() {
        return this.tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getMinerale() {
        return minerale;
    }

    public void setMinerale(String minerale) {
        this.minerale = minerale;
    }

    public String getOperatore() {
        return operatore;
    }

    public void setOperatore(String operatore) {
        this.operatore = operatore;
    }
}
