package main.domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int productNummer;
    private String naam;
    private String beschrijving;
    private long prijs;
    private List<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();

    public Product(int productNummer, String naam, String beschrijving, long prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }


    public int getProductNummer() {
        return productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public long getPrijs() {
        return prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void addOvChipkaart(OVChipkaart ovChipkaart  ) {
        this.ovChipkaarten.add(ovChipkaart);
    }

    @Override
    public String toString() {
          String string = "Product " + productNummer + ": " + naam +  ", ovchipkaarten:\n";
          for(OVChipkaart ovChipkaart : ovChipkaarten){
              string += (ovChipkaart.getKaartNummer() + ",\n" );
          }
          return string;
    }
}
