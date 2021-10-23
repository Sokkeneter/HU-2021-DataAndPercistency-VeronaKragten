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

    public void setOvChipkaarten(List<OVChipkaart> ovChipkaarten) {
        this.ovChipkaarten = ovChipkaarten;
    }

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
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

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public long getPrijs() {
        return prijs;
    }

    public void setPrijs(long prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public List<Integer> getovChipKaartNummers() {
        List<Integer> nummers = new ArrayList<Integer>();
        for(OVChipkaart ovChipkaart : ovChipkaarten){
            nummers.add(ovChipkaart.getKaartNummer());
        }
        return nummers;
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
