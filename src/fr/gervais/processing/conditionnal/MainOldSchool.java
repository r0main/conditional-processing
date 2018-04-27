package fr.gervais.processing.conditionnal;

import java.time.LocalDate;

public class MainOldSchool {

    public static void main(String[] args) {
        System.out.println("Start " + MainOldSchool.class.getSimpleName());
        printDescription(new Raw("P1","Prestation 1", "Ne s'est pas présenté", null));
        printDescription(new Raw("P2","Prestation 2", null, "2018-01-01"));
        printDescription(new Raw("P3","Prestation 3", null, "2020-01-01"));
    }

    private static void printDescription(Raw raw) {
        Prestation prestation;
        if (raw.getField3() != null) {
            prestation = new PrestationAnnulee(raw.getField1(), raw.getField2(), raw.getField3());
        }else if (LocalDate.parse(raw.getField4()).isBefore(LocalDate.now())) {
            prestation = new PrestationEffectuee(raw.getField1(), raw.getField2(), LocalDate.parse(raw.getField4()));
        } else if (LocalDate.parse(raw.getField4()).isAfter(LocalDate.now())) {
            prestation = new PrestationFuture(raw.getField1(), raw.getField2(), LocalDate.parse(raw.getField4()));
        }else {
            throw new RuntimeException("Prestation inconnue");
        }
        System.out.println(prestation.getDescription());
    }
}
