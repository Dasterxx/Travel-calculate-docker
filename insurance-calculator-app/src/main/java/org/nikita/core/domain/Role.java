package org.nikita.core.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private String name;
    @ManyToMany(mappedBy = "roles")
    @Builder.Default
     private Collection<PersonEntity> users = new HashSet<>();
    public Role(String name) {
        this.name = name;
    }
    public  String getName(){
        return name  != null ? name : "";
    }
    @Override
    public String toString() {
        return name;
    }


}
