package fr.gervais.processing.conditionnal;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MainCool {

    public static void main(String[] args) {
        System.out.println("Start " + MainCool.class.getSimpleName());

        List<PrestationMapper> prestationMappers = Arrays.asList(new PrestationAnnuleeMapper(), new PrestationEffectueeMapper(), new PrestationFutureMapper());
        printDescription(prestationMappers, new Raw("P1","Prestation 1", "Ne s'est pas présenté", null));
        printDescription(prestationMappers, new Raw("P2","Prestation 2", null, "2018-01-01"));
        printDescription(prestationMappers, new Raw("P3","Prestation 3", null, "2020-01-01"));
    }

    private static void printDescription(List<PrestationMapper> prestationMappers, Raw raw) {
        Optional<PrestationMapper> prestationMapper = prestationMappers.stream().filter(mapper -> mapper.test(raw)).findFirst();
        prestationMapper.ifPresent((mapper)-> System.out.println(mapper.map(raw).getDescription()));
    }

    interface PrestationMapper extends Predicate<Raw> {
        boolean test(Raw raw);
        Prestation map(Raw raw);
    }

    static class PrestationAnnuleeMapper implements PrestationMapper {

        public boolean test(Raw raw) {
            return raw.getField3() != null;
        }

        public Prestation map(Raw raw) {
            return new PrestationAnnulee(raw.getField1(), raw.getField2(), raw.getField3());
        }
    }

    static class PrestationEffectueeMapper implements PrestationMapper {

        public boolean test(Raw raw) {
            return LocalDate.parse(raw.getField4()).isBefore(LocalDate.now());
        }

        public Prestation map(Raw raw) {
            return new PrestationEffectuee(raw.getField1(), raw.getField2(), LocalDate.parse(raw.getField4()));
        }
    }

    static class PrestationFutureMapper implements PrestationMapper {

        public boolean test(Raw raw) {
            return LocalDate.parse(raw.getField4()).isAfter(LocalDate.now());
        }

        public Prestation map(Raw raw) {
            return new PrestationFuture(raw.getField1(), raw.getField2(), LocalDate.parse(raw.getField4()));
        }
    }
}
