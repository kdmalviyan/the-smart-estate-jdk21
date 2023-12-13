package com.sfd.thesmartestate.importdata.services;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class RawLeadInput {
    @CsvBindByName
    private String customerEmail;
    @CsvBindByName
    private String project;
    @CsvBindByName
    private String remark;
    @CsvBindByName
    private String phone;
    @CsvBindByName
    private String customerName;
}