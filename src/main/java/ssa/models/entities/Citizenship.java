package ssa.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "citizenship")
public class Citizenship {

    @Id
    @GeneratedValue
    @Column(name = "code")
    private short code;

    @Column(name = "name", length = 50)
    private String name;

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
