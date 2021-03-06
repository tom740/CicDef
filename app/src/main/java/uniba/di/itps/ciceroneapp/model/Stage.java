package uniba.di.itps.ciceroneapp.model;


import java.io.Serializable;

public class Stage implements Serializable{
    private String indirizzo;
    private String descrizione;

    public Stage(String indirizzo, String descrizione) {
        this.indirizzo = indirizzo;
        this.descrizione = descrizione;
    }
    public Stage(){}
    public String getIndirizzo() {
        return indirizzo;
    }


    @Override
    public String toString() {
        return indirizzo + " " + descrizione;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }


}
