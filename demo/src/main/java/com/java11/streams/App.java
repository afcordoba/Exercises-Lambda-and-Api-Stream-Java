package com.java11.streams;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.model.Person;
import com.model.Product;


public class App {
    public static void main( String[] args ){
        Person p1 = new Person(1, "Mito", LocalDate.of(1991, 1 , 21));
        Person p2 = new Person(2, "Code", LocalDate.of(1990, 2 , 21));
        Person p3 = new Person(3, "Jaime", LocalDate.of(1980, 6 , 30));
        Person p4 = new Person(4, "Duke", LocalDate.of(2019, 5 , 15));
        Person p5 = new Person(5, "James", LocalDate.of(2010, 1 , 4));

        Product pr1 = new Product(1, "Ceviche", 15.0);
        Product pr2 = new Product(2, "Chilaquiles", 25.50);
        Product pr3 = new Product(3, "Bandeja Paisa", 35.50);
        Product pr4 = new Product(4, "Ceviche", 15.0);


        List<Person> peopleList = Arrays.asList(p1,p2,p3,p4,p5);
        List<Product> products = Arrays.asList(pr1,pr2,pr3,pr4);



        //Lambda (Funcion Anónima)
        //method reference
        //list.forEach(System.out::println);

        peopleList.forEach(p -> System.out.println(p) );
        peopleList.forEach(System.out::println);


        //1) Filter (param: Predicate) Me permite trabajar de una forma declarativa con las funciones
        //Predicate es una funcion Booleana
        List<Person> filteredpersonasAdultas =  peopleList.stream()
                                                .filter(p -> App.getAge(p.getBirthDate()) >= 18)
                                                .collect(Collectors.toList());
        App.printList(filteredpersonasAdultas);

        //2) Map (param : Function) // Para Mapear (Transformar los elementos de un Stream en este caso de Person a Integer)
        // Tambien le puedo aplicar un Filter y la Funcion espera un retorno dependendiendo de lo que retorne la funcion 
        List<Integer> ages = peopleList.stream()
                                    .filter(p ->App.getAge(p.getBirthDate()) >=18)
                                    .map(p -> App.getAge(p.getBirthDate()))
                                    .collect(Collectors.toList());
        App.printList(ages);

        //Otro ejemplo de Map utilizando una Function
        Function<String,String> coderFunction = name -> "Coder: " + name.toUpperCase();

        List<String> filteredCoders = peopleList.stream()
                                              .map(p -> p.getName()) //Tambien puedo hacer .map(Persona::getName)
                                              .map(coderFunction)
                                              .collect(Collectors.toList());
        App.printList(filteredCoders);         
        
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

 // Utilizando programación funcional y reactiva para calcular la suma de los cuadrados de los números pares
        int sumaCuadradosPares = numeros.stream() // Convertimos la lista en un stream
                .filter(n -> n % 2 == 0) // Filtramos solo los números pares
                .mapToInt(n -> n * n) // Mapeamos cada número a su cuadrado
                .sum(); // Sumamos los cuadrados

        System.out.println("La suma de los cuadrados de los números pares es: " + sumaCuadradosPares);

        //3) Sorted (param : Comparator)
        //Primero se debe crear un Comparator y en este caso se comparan y orden Objetos de tipo persona por nombre

        //QUE PASA SI QUEREMOS ORDENAR MILLONES DE OBJETOS VER EJEMPLOS DE SPARK
        Comparator<Person> byNmaeAss = (Person prs1, Person prs2) -> prs1.getName().compareTo(prs2.getName());
        List<Person> filteredListOrderPerson = peopleList.stream()
                                                      .sorted(byNmaeAss)
                                                      .collect(Collectors.toList());
        App.printList(filteredListOrderPerson);

        // 4) Match (param : Predicate)

        //anyMatch : No evalua todo el stream, termina la busqueda cuando encuentra alguno
        boolean all = peopleList.stream()
                              .anyMatch(p -> p.getName().startsWith("j"));


        //allMatch : Evalua todo el stream, bajo la condicion
        boolean any = peopleList.stream()
                                .allMatch(p -> p.getName().startsWith("j"));

        //noneMatch : Evalua todo el stream, bajo la condicion
        boolean none = peopleList.stream()
                                .noneMatch(p -> p.getName().startsWith("j"));

        //Using Predicate
        // Como este codigo se repite varias veces podemos refactorizarlo utilizando un Predicate
        Predicate<Person> startsWithPredicate = person -> person.getName().startsWith("j");

        //anyMatch : No evalua todo el stream, termina la busqueda cuando encuentra alguno
        all = peopleList.stream()
                                .anyMatch(startsWithPredicate);


        //allMatch : Evalua todo el stream, bajo la condicion
        any = peopleList.stream()
                                .allMatch(startsWithPredicate);

        //noneMatch : Evalua todo el stream, bajo la condicion
        none = peopleList.stream()
                                 .noneMatch(startsWithPredicate);
        

        // 5) Limit/Skip    En el ejemplo lo usa para paginar la lista por un tamaño items por pagina

        int pageNumber = 1;
        int pageSize = 2;

        List<Person> paginatePersons = peopleList.stream()
                                                 .skip(pageNumber * pageSize)
                                                 .limit(pageSize)
                                                 .collect(Collectors.toList());

        // 6) Collectors
        //GroupBy Retorna un Map
        Map<String, List<Product>> collect1 = products.stream()
                                                        .filter(p -> p.getPrice() > 20)
                                                        .collect(Collectors.groupingBy(Product::getName));
        
        System.out.println(collect1);
                                   


        //Counting
        Map<String,Long> collect2 = products.stream()
                                            .collect(Collectors.groupingBy(
                                                Product::getName, Collectors.counting()
                                                )
                                            ); 
        System.out.println(collect2);

        //Agrupando por nombre producto y sumando

        Map<String,Double> collect3 = products.stream()
                                                .collect(Collectors.groupingBy(
                                                    Product::getName, 
                                                    Collectors.summingDouble(Product::getPrice)
                                                )
                                                ); 
        System.out.println(collect3);
        // Obteniendo suma y resumen
        DoubleSummaryStatistics statistics =   products.stream()
                                                        .collect(Collectors.summarizingDouble(Product::getPrice));
        System.out.println(statistics);

        // 7) reduce
        Optional<Double> sum =  products.stream()
                                            .map(Product::getPrice)
                                            .reduce(Double::sum);  //Hace la suma de todos los precio de cada producto

        System.out.println(sum.get());
    }

    public static int getAge(LocalDate birthDate){
        return Period.between(birthDate, LocalDate.now()).getYears();
    }


    public static void printList(List<?> list){
      list.forEach(System.out::println);
    }
}
