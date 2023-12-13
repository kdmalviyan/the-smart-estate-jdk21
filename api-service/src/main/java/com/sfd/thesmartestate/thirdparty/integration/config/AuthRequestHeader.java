package com.sfd.thesmartestate.thirdparty.integration.config;


import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_endpoint_request_headers")
@Data
public class AuthRequestHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "header_key")
    private String headerKey;

    @Column(name = "header_value")
    private String headerValue;
}
