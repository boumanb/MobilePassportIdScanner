package Models.Entities;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by billy on 24-5-2017.
 */

public class MRTDRecord implements Serializable, Comparable<MRTDRecord> {
    private int id;
    private String firstName;
    private String lastName;
    private String issuer;
    private String nationality;
    private String dateOfBirth;
    private String documentNumber;
    private String sex;
    private String documentCode;
    private String dateOfExpiry;
    private String opt1;
    private String opt2;
    private String mrzText;
    private Date dateTimeStamp;
    private String note;

    public MRTDRecord() {
    }

    public MRTDRecord(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public MRTDRecord(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public MRTDRecord(String firstName, String lastName, String issuer, String nationality, String dateOfBirth, String documentNumber, String sex, String documentCode, String dateOfExpiry, String opt1, String opt2, String mrzText, Date dateTimeStamp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.issuer = issuer;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.documentNumber = documentNumber;
        this.sex = sex;
        this.documentCode = documentCode;
        this.dateOfExpiry = dateOfExpiry;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.mrzText = mrzText;
        this.dateTimeStamp = dateTimeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        nationality = nationality;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getMrzText() {
        return mrzText;
    }

    public void setMrzText(String mrzText) {
        this.mrzText = mrzText;
    }

    public Date getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(Date dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "MRTDRecord{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", issuer='" + issuer + '\'' +
                ", Nationality='" + nationality + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", documentCode='" + documentCode + '\'' +
                ", dateOfExpiry='" + dateOfExpiry + '\'' +
                ", opt1='" + opt1 + '\'' +
                ", opt2='" + opt2 + '\'' +
                ", mrzText='" + mrzText + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull MRTDRecord o) {
        return o.getDateTimeStamp().compareTo(getDateTimeStamp());
    }
}
