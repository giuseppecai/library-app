package net.giuse.biblioteca.member;

import net.giuse.biblioteca.loan.LoanDTO;

import java.time.LocalDate;
import java.util.List;

public class MemberDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private LocalDate registrationDate;
    private Boolean active;
    private List<LoanDTO> loans;

    public MemberDTO() {

    }

    public MemberDTO(Long id, String name, String surname, String email, String phoneNumber, LocalDate registrationDate, Boolean active, List<LoanDTO> loans) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.active = active;
        this.loans = loans;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<LoanDTO> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanDTO> loans) {
        this.loans = loans;
    }
}
