package ssa.utils;

import ssa.models.entities.Person;

public enum  DBTestPersons {
    PERSON_1("Person1.firstName", "Person1.middleName", "Person1.secondName", "Person1.position", "10001", true),
    PERSON_2("Person2.firstName", "Person2.middleName", "Person2.secondName", "Person2.position", "10002", true),
    PERSON_3("Person3.firstName", "Person3.middleName", "Person3.secondName", "Person3.position", "10003", false);

    public static final String PERSON_NEW_FIRSTNAME = "Person new firstname";
    public static final String PERSON_NEW_POSITION = "Person new position";

    private final String firstName;
    private final String middleName;
    private final String secondName;
    private final String position;
    private final String phone;
    private final boolean isIdentified;

    DBTestPersons(String firstName,
                  String middleName,
                  String secondName,
                  String position,
                  String phone,
                  boolean isIdentified) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.secondName = secondName;
        this.position = position;
        this.phone = phone;
        this.isIdentified = isIdentified;
    }

    public Person get() {
        Person person = new Person();
        person.setFirstName(this.firstName);
        person.setMiddleName(this.middleName);
        person.setSecondName(this.secondName);
        person.setPosition(this.position);
        person.setPhone(this.phone);
        person.setIsIdentified(this.isIdentified);
        return person;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isIdentified() {
        return isIdentified;
    }
}
