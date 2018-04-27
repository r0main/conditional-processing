package fr.gervais.processing.conditionnal;

import java.time.LocalDate;

public class PrestationFuture extends Prestation {

    LocalDate dateAction;

    public PrestationFuture(String code, String libelle, LocalDate raison) {
        super(code, libelle);
        this.dateAction = raison;
    }

    public LocalDate getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDate dateAction) {
        this.dateAction = dateAction;
    }

    @Override
    public String getDescription() {
        return String.format("La prestation '%s' est programmée le %s", code, dateAction);
    }
}
