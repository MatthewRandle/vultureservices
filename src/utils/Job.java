package utils;

import java.util.Date;

public class Job {
    private String client, quotedParts;
    private int jobNumber, labourTime;
    private Date arrivalDate, returnDate;

    public Job(String client, int jobNumber, int labourTime, Date arrivalDate, Date returnDate) {
        this.client = client;
        this.jobNumber = jobNumber;
        this.labourTime = labourTime;
        this.arrivalDate = arrivalDate;
        this.returnDate = returnDate;
    }

    public Job(String client, int jobNumber, String quotedParts, Date returnDate) {
        this.client = client;
        this.jobNumber = jobNumber;
        this.quotedParts = quotedParts;
        this.returnDate = returnDate;
    }

    public String getClient() {
        return client;
    }
    public void setClient(String client) {
        this.client = client;
    }

    public String getQuotedParts() { return quotedParts; }
    public void setQuotedParts(String quotedParts) { this.quotedParts = quotedParts; }

    public int getJobNumber() {
        return jobNumber;
    }
    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public int getLabourTime() {
        return labourTime;
    }
    public void setLabourTime(int labourTime) {
        this.labourTime = labourTime;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }
    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

}