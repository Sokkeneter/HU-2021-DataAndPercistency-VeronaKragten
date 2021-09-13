package domain;

import java.util.Date;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboorteDatum;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboorteDatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboorteDatum = geboorteDatum;
    }


    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboorteDatum() {
        return geboorteDatum;
    }

    public void setGeboorteDatum(Date geboorteDatum) {
        this.geboorteDatum = geboorteDatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNaam(){
       return String.format("%s. %s. %s", voorletters,tussenvoegsel,achternaam);
    }

    @Override
    public String toString() {
        return (String.format("#%s: %s. %s. %s (%s)", id,voorletters, tussenvoegsel, achternaam, geboorteDatum));

    }

}
