package ssa.utils;

import ssa.models.entities.Office;

public enum DBTestOffices {
    OFFICE_1("Test office #1", "Test address #1", "101", true),
    OFFICE_2("Test office #2", "Test address #2", "102", true),
    OFFICE_3("Test office #3", "Test address #3", "201", false);

    public static final String OFFICE_NEW_NAME = "Office new name";
    public static final String OFFICE_NEW_ADDRESS = "Office new address";

    private final String name;
    private final String address;
    private final String phone;
    private final boolean isActive;

    DBTestOffices(String name,
                  String address,
                  String phone,
                  boolean isActive) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.isActive = isActive;
    }

    public Office get() {
        Office office = new Office();
        office.setName(this.name);
        office.setAddress(this.address);
        office.setPhone(this.phone);
        office.setIsActive(this.isActive);
        return office;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isActive() {
        return isActive;
    }
}
