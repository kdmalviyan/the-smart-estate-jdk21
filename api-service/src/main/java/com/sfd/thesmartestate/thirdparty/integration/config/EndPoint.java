package com.sfd.thesmartestate.thirdparty.integration.config;

import com.sfd.thesmartestate.persistence.HttpMethodEnumConverter;
import com.sfd.thesmartestate.thirdparty.exceptions.ThirdPartyExceptions;
import com.sfd.thesmartestate.thirdparty.integration.AuthenticationType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.http.HttpMethod;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="tb_third_party_endpoints")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")
public class EndPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "auth_type")
    @Enumerated(EnumType.STRING)
    private AuthenticationType authType;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "token_url")
    private String tokenUrl;

    private HttpMethod accessTokenMethod;

    private String accessTokenUsernameFieldName;

    private String accessTokenPasswordFieldName;

    private String accessTokenResponseFieldName;

    @Column(name = "http_method")
    @Convert(converter = HttpMethodEnumConverter.class)
    private HttpMethod httpMethod;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private boolean isActive;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<AuthRequestHeader> headers;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<LeadFieldsMapper> fieldsMappers;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<EndpointQueryParam> params;

    public void validate() {
        if(Objects.isNull(authType)) {
            throw new ThirdPartyExceptions("You must provide authentication type.");
        }
        switch (this.authType) {
            case BASIC:
                if(Objects.isNull(this.username) || Objects.isNull(this.password)) {
                    throw new ThirdPartyExceptions("User name password must be present");
                }
                break;
            case TOKEN_BASED:
                if(Objects.isNull(this.tokenUrl) || Objects.equals("", this.tokenUrl.trim())) {
                    throw new ThirdPartyExceptions("Token Url must be present must be present");
                }
                break;
            default:
                break;
        }

        if(Objects.isNull(url) || Objects.equals("", url) || !(url.startsWith("http") || url.startsWith("https"))) {
            throw new ThirdPartyExceptions("Endpoint url must be present");
        }
    }
}
