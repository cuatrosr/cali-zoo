package co.edu.icesi.zoo.model;

import co.edu.icesi.zoo.constant.AnimalGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Data
@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Animal {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private String name;
    private AnimalGender sex;
    private int age;
    private double height;
    private double weight;
    private Date arrivalDate;
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID father_id;
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID mother_id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }
}