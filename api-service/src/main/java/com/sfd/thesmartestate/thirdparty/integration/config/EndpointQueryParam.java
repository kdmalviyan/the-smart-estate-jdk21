package com.sfd.thesmartestate.thirdparty.integration.config;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_endpoint_query_params")
@Data
public class EndpointQueryParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "param_key")
    private String paramKey;

    @Column(name = "param_value")
    private String paramValue;

    @Column(name = "encryption_name")
    private String encryptionName;
}
