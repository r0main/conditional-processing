package fr.gervais.processing.conditionnal;

public class PrestationAnnulee extends Prestation {

    String raison;

    public PrestationAnnulee(String code, String libelle, String raison) {
        super(code, libelle);
        this.raison = raison;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    @Override
    public String getDescription() {
        return String.format("La prestation '%s' est annulee car : %s", code, raison);
    }
}
