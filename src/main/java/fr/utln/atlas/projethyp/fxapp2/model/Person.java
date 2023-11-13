package fr.utln.atlas.projethyp.fxapp2.model;

import lombok.*;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode()
public class Person {
    @EqualsAndHashCode.Include
    private final UUID uuid = UUID.randomUUID();
    private String name;
    private String address;
}
