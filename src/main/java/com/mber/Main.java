package com.mber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        var people = new ArrayList<Person>();
        var mike = new Person(null, 20);
        var tom = new Person("Tom", 21);
        var elen = new Person("Elen", 22);

        addPeople(people, mike, tom, elen);
        System.out.println(people);

        var mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Path path = Paths.get("src/main/java/com/mber", "people.JSON");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            String peopleDest = mapper.writeValueAsString(people);
            Files.write(path, peopleDest.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);

            var peopleSrc = Files.readAllBytes(path);
            var personList = mapper.readValue(peopleSrc, new TypeReference<List<Person>>(){});
            System.out.println(personList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The end");
    }

    private static void addPeople(ArrayList<Person> people, Person person, Person... persons) {
        people.add(person);
        people.addAll(Arrays.asList(persons));
    }
}


@NoArgsConstructor
@AllArgsConstructor
@Data
class Person {
    private String name;
    private int age;
}