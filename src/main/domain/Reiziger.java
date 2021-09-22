package main.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboorteDatum;
    private List<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();
//kijk naar hashmap
    public Reiziger(String naam, Date geboorteDatum) {
        String[] list=naam.split(" ");
        this.voorletters = list[0];
        this.tussenvoegsel = list[1];
        this.achternaam = list[2];

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
        if(tussenvoegsel != null) {
            return (String.format("reiziger: %s. %s %s", voorletters, tussenvoegsel, achternaam));
        }
        else{
            return (String.format("reiziger: %s. %s", voorletters, achternaam));
        }    }

    public void addToKaarten(OVChipkaart ovChipkaart){
        ovChipkaarten.add(ovChipkaart);
    }
    @Override
    public String toString() {
        if(tussenvoegsel != "null") {
            return (String.format("reiziger: %s. %s %s (%s)", voorletters, tussenvoegsel, achternaam, geboorteDatum));
        }
        else{
            return (String.format("reiziger: %s. %s (%s)", voorletters, achternaam, geboorteDatum));
        }
    }

}
