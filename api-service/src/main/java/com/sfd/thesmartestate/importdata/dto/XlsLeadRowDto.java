package com.sfd.thesmartestate.importdata.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class XlsLeadRowDto {

    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAlternatePhone;
    private String size;
    private String budget;
    private String leadSource;
    private String leadType;
    private String leadStatus;
    private String projectName;
    private String comment;
    private String assignTo;
    private int rownum;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XlsLeadRowDto)) return false;
        XlsLeadRowDto that = (XlsLeadRowDto) o;
        return Objects.equals(getCustomerPhone(), that.getCustomerPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerPhone());
    }
}
