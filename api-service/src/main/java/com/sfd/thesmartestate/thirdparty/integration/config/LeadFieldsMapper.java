package com.sfd.thesmartestate.thirdparty.integration.config;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_endpoint_fields_mappings")
@Data
public class LeadFieldsMapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "prop_key")
    private String propKey;

    @Column(name = "prop_value")
    private String propValue;
}
