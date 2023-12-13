package com.sfd.thesmartestate.security.certificates;

import com.sfd.thesmartestate.common.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Log4j2
public class SecurityCertificatesManager {
    @Value("${security.certificates.keys.public}")
    private String publicKey;

    @Value("${security.certificates.keys.private}")
    private String privateKey;

    public PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        publicKey = publicKey.replace(Constants.PUBLIC_KEY_START_TEXT, Constants.EMPTY)
                .replace(Constants.PUBLIC_KEY_END_TEXT, Constants.EMPTY);
        return KeyFactory
                .getInstance(Constants.RSA)
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
    }

    public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        privateKey = privateKey.replace(Constants.PRIVATE_KEY_START_TEXT, Constants.EMPTY)
                .replace(Constants.PRIVATE_KEY_END_TEXT, Constants.EMPTY);

        return KeyFactory
                .getInstance(Constants.RSA)
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    }
}
