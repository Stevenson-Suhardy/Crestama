package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class SalesReportForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name = "submissionDate")
    private Date submissionDate;

    @Column(name = "startActivityDate")
    private Date startActivityDate;

    @Column(name = "endActivityDate")
    private Date endActivityDate;

    @Column(name = "activityType")
    private String activityType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "companyType_id", referencedColumnName = "id")
    private CompanyType companyType;

    @Column(name = "companyName")
    private String companyName;

    @Column(name = "streetAddress")
    private String streetAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @Column(name = "contactPersonName")
    private String contactPersonName;

    @Column(name = "contactPersonPhone")
    private String contactPersonPhone;

    @Column(name = "detailedActivity")
    private String detailedActivity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prospect_id", referencedColumnName = "id")
    private Prospect prospect;

    @Column(name = "comments")
    private String comments;

    public SalesReportForm() {}

    public SalesReportForm(Date submissionDate, Date startActivityDate, Date endActivityDate, String activityType,
                           CompanyType companyType, String companyName, String streetAddress, City city,
                           String contactPersonName, String contactPersonPhone, String detailedActivity,
                           Prospect prospect, String comments) {
        this.submissionDate = submissionDate;
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

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Date getStartActivityDate() {
        return startActivityDate;
    }

    public void setStartActivityDate(Date startActivityDate) {
        this.startActivityDate = startActivityDate;
    }

    public Date getEndActivityDate() {
        return endActivityDate;
    }

    public void setEndActivityDate(Date endActivityDate) {
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
