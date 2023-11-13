package fr.atlas.hyperplanning.fxapp2.model;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.LongStream;

public class Group {
    private Group() {}
    @NoArgsConstructor(staticName = "newInstance")
    public static class DAO {
        public Page<Person> findAll() {return findAll(10,1);}
        public Page<Person> findAll(int pageSize, int pageNumber) {
            return findAll(pageSize, pageNumber, Comparator.comparing(Person::getName));
        }
        public Page<Person> findAll(int pageSize, int pageNumber, Comparator<? super Person> comparateur) {
            return new Page<>(DATA_SIZE, pageSize, pageNumber,
                    members.values().stream().sorted(comparateur).skip((long) pageNumber *pageSize).limit(pageSize).toList());
        }

        public Page<Person> search(String criteria) {
            return search(10, 1, criteria);
        }
        public Page<Person> search(int pageSize, int pageNumber, String criteria) {
            return search(pageSize, pageNumber, Comparator.comparing(Person::getName), criteria);
        }
        public Page<Person> search(int pageSize, int pageNumber, Comparator<? super Person> comparateur, String criteria) {
            return new Page<>(DATA_SIZE, pageSize, pageNumber,
                    members.values().stream()
                            .filter(p->p.getName().contains(criteria))
                            .sorted(comparateur)
                            .skip((long) (pageNumber - 1) *pageSize).limit(pageSize).toList());
        }
    }

    private static final long DATA_SIZE = 1000;

    private static final Map<UUID, Person> members = new ConcurrentHashMap<>();

    static {
        Faker faker = new Faker(new Locale("fr"));
        LongStream.range(0, DATA_SIZE)
                .mapToObj(id-> {
                        Name name=faker.name();
                        Address address=faker.address();
                        return Person.of(name.lastName()+", "+name.firstName(),
                        address.streetAddress()+" "+address.zipCode()+" "+address.cityName());})
                .forEach(p->members.put(p.getUuid(), p));
    }
}
