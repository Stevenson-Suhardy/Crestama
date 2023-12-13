package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class SalesReportForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name = "submissionDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Submission Date is required.")
    private LocalDateTime submissionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull(message = "User is required.")
    private User user;

    @Column(name = "startActivityDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Start Activity Date is required.")
    private LocalDateTime startActivityDate;

    @Column(name = "endActivityDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "End Activity Date is required.")
    private LocalDateTime endActivityDate;

    @Column(name = "activityType")
    @NotNull(message = "Activity Type is required.")
    @NotEmpty(message = "Activity Type is required.")
    private String activityType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "companyType_id", referencedColumnName = "id")
    @NotNull(message = "Company Type is required.")
    private CompanyType companyType;

    @Column(name = "companyName")
    @NotNull(message = "Company Name is required.")
    @NotEmpty(message = "Company Name is required.")
    private String companyName;

    @Column(name = "streetAddress")
    @NotNull(message = "Street Address is required.")
    @NotEmpty(message = "Street Address is required.")
    private String streetAddress;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @NotNull(message = "City is required.")
    private City city;

    @Column(name = "contactPersonName")
    @NotNull(message = "Contact Person Name is required.")
    @NotEmpty(message = "Contact Person Name is required.")
    private String contactPersonName;

    @Column(name = "contactPersonPhone")
    @NotNull(message = "Contact Person Phone Number is required.")
    @NotEmpty(message = "Contact Person Phone Number is required.")
    private String contactPersonPhone;

    @Column(name = "detailedActivity")
    @NotNull(message = "Detailed Activity is required.")
    @NotEmpty(message = "Detailed Activity is required.")
    private String detailedActivity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prospect_id", referencedColumnName = "id")
    @NotNull(message = "Prospect is required.")
    private Prospect prospect;

    @Column(name = "comments")
    private String comments;

    public SalesReportForm() {}


    public SalesReportForm(User user, LocalDateTime startActivityDate, LocalDateTime endActivityDate, String activityType,
                           CompanyType companyType, String companyName, String streetAddress, City city,
                           String contactPersonName, String contactPersonPhone, String detailedActivity,
                           Prospect prospect, String comments) throws Exception {

        this.submissionDate = LocalDateTime.now();
        this.user = user;
        this.startActivityDate = startActivityDate;
        this.endActivityDate = endActivityDate;
        this.activityType = activityType;
        this.companyType = companyType;
        this.companyName = companyName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.contactPersonName = contactPersonName;
        this.contactPersonPhone = contactPersonPhone;
        this.detailedActivity = detailedActivity;
        this.prospect = prospect;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStartActivityDate() {
        return startActivityDate;
    }

    public void setStartActivityDate(LocalDateTime startActivityDate) {
        this.startActivityDate = startActivityDate;
    }

    public LocalDateTime getEndActivityDate() {
        return endActivityDate;
    }

    public void setEndActivityDate(LocalDateTime endActivityDate) throws Exception {
        this.endActivityDate = endActivityDate;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getDetailedActivity() {
        return detailedActivity;
    }

    public void setDetailedActivity(String detailedActivity) {
        this.detailedActivity = detailedActivity;
    }

    public Prospect getProspect() {
        return prospect;
    }

    public void setProspect(Prospect prospect) {
        this.prospect = prospect;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
