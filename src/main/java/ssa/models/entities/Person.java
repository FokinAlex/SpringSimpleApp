package ssa.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "firstname", length = 50)
    private String firstName;

    @Column(name = "secondname", length = 50)
    private String secondName;

    @Column(name = "middlename", length = 50)
    private String middleName;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "docdate")
    private Date docDate;

    @Column(name = "identified")
    private boolean isIdentified;

    @Column(name = "off_id", insertable = false, updatable = false)
    private Long officeId;

    @Column(name = "doc_id", insertable = false, updatable = false)
    private Byte docCode;

    @Column(name = "cs_id", insertable = false, updatable = false)
    private Short citizenshipCode;

    @ManyToOne
    @JoinColumn(name = "off_id")
    private Office office;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "cs_id")
    private Citizenship citizenship;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public boolean isIdentified() {
        return isIdentified;
    }

    public void setIsIdentified(boolean isIdentified) {
        this.isIdentified = isIdentified;
    }

    public Byte getDocCode() {
        return docCode;
    }

    public void setDocCode(Byte docCode) {
        this.docCode = docCode;
    }

    public Short getCitizenshipCode() {
        return citizenshipCode;
    }

    public void setCitizenshipCode(Short citizenshipCode) {
        this.citizenshipCode = citizenshipCode;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Citizenship getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(Citizenship citizenship) {
        this.citizenship = citizenship;
    }
}
