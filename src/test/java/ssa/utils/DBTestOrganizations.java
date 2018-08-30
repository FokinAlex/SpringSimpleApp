package ssa.utils;

import ssa.models.entities.Organization;

public enum  DBTestOrganizations {
    ORGANIZATION_1("Test organization #1", "Active test organization with two offices", "100", "100", "Test address #1", "100", true),
    ORGANIZATION_2("Test organization #2", "Active test organization with one office", "200", "200", "Test address #2", "200", true),
    ORGANIZATION_3("Test organization #3", "Not active test organization without offices", "300", "300", "Test address #3", "300", false);

    public static final String ORGANIZATION_NEW_NAME = "Organization new name";
    public static final String ORGANIZATION_NEW_FULLNAME = "Organization new fullname";
    public static final String ORGANIZATION_NEW_INN = "999";
    public static final String ORGANIZATION_NEW_KPP = "999";
    public static final String ORGANIZATION_NEW_ADDRESS = "Organization new address";
    public static final String ORGANIZATION_NEW_PHONE = "999";

    private final String name;
    private final String fullName;
    private final String inn;
    private final String kpp;
    private final String address;
    private final String phone;
    private final boolean isActive;

    DBTestOrganizations(String name,
                        String fullName,
                        String inn,
                        String kpp,
                        String address,
                        String phone,
                        boolean isActive) {
        this.name = name;
        this.fullName = fullName;
        this.inn = inn;
        this.kpp = kpp;
        this.address = address;
        this.phone = phone;
        this.isActive = isActive;
    }


    public Organization get() {
        Organization organization = new Organization();
        organization.setName(this.name);
        organization.setFullName(this.fullName);
        organization.setInn(this.inn);
        organization.setKpp(this.kpp);
        organization.setAddress(this.address);
        organization.setPhone(this.phone);
        organization.setIsActive(this.isActive);
        return organization;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getInn() {
        return inn;
    }

    public String getKpp() {
        return kpp;
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
