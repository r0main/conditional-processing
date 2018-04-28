# Conditionnal processing

Qui n'a jamais écrit ce genre de code :

```java
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
```

Ce genre de code reste lisible à petit echelle mais devient rapidement illisible quand on a plusieurs dizaines de cas 
avec autant de conditions à chaque fois.

Il est possible de modéliser ce traitement avec l'interface suivante :
```java
interface PrestationMapper extends Predicate<Raw> {
    boolean test(Raw raw);
    Prestation map(Raw raw);
}
```
L'interface changera selon votre cas d'usage. Dans le cas présent, nous cherchons à mapper un objet vers un autre.

Il est ainsi possible de coder les différents cas dans des classes différentes :

```java
class PrestationAnnuleeMapper implements PrestationMapper {

    public boolean test(Raw raw) {
        return raw.getField3() != null;
    }

    public Prestation map(Raw raw) {
        return new PrestationAnnulee(raw.getField1(), raw.getField2(), raw.getField3());
    }
}
```

```java
class PrestationEffectueeMapper implements PrestationMapper {

    public boolean test(Raw raw) {
        return LocalDate.parse(raw.getField4()).isBefore(LocalDate.now());
    }

    public Prestation map(Raw raw) {
        return new PrestationEffectuee(raw.getField1(), raw.getField2(), LocalDate.parse(raw.getField4()));
    }
}
```

```java
class PrestationFutureMapper implements PrestationMapper {

    public boolean test(Raw raw) {
        return LocalDate.parse(raw.getField4()).isAfter(LocalDate.now());
    }

    public Prestation map(Raw raw) {
        return new PrestationFuture(raw.getField1(), raw.getField2(), LocalDate.parse(raw.getField4()));
    }
}
```

Ainsi le code qui permet de mapper un objet sans le moindre `if` (ou un seul `if` si on considère le `ifPresent`) :

```java
List<PrestationMapper> prestationMappers = Arrays.asList(new PrestationAnnuleeMapper(), new PrestationEffectueeMapper(), new PrestationFutureMapper());
Optional<PrestationMapper> prestationMapper = prestationMappers.stream().filter(mapper -> mapper.test(raw)).findFirst();
prestationMapper.ifPresent((mapper)-> System.out.println(mapper.map(raw).getDescription()));
```

J'espère que ça pourra vous donner des idées. Je ne dis pas qu'il faut remplacer le moindre "if" par une interface et 
différentes classes pour chaque cas. Cependant si vous devez implémenter un grand nombre de règles sur un objet ou une 
liste d'objets, ce pattern peut vous permettre d'améliorer la lisibilité de votre code.