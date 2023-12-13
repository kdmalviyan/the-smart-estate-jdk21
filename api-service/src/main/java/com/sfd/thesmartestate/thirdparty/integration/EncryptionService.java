package com.sfd.thesmartestate.thirdparty.integration;

import com.sfd.thesmartestate.thirdparty.integration.config.EndpointQueryParam;
import org.hibernate.boot.beanvalidation.IntegrationException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class EncryptionService {

    private EncryptionService() {
        throw new IllegalStateException("Constructor error");
    }

    public static String apply(EndpointQueryParam queryParam) {
        String response = queryParam.getParamValue();
        String encryptionName = queryParam.getEncryptionName();
        if (Objects.isNull(encryptionName)
                || Objects.equals("", encryptionName.trim())) {
            return response;
        }
        if ("HMAC_EPOCH_TIME".equals(encryptionName)) {
            try {
                response = calculateHmacWithEpocTime(queryParam.getParamValue());
            } catch (NoSuchAlgorithmException
                     | InvalidKeyException e) {
                throw new IntegrationException(e.getMessage());
            }
        } else {
            throw new IntegrationException("Unable to find encryption name.");
        }
        return response;
    }

    private static String calculateHmacWithEpocTime(String value) throws NoSuchAlgorithmException,
             InvalidKeyException {
        Mac algorithName = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(value.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        algorithName.init(secretKey);
        LocalDateTime localDateTime = LocalDateTime.parse("2019-11-15T13:15:30");
        //LocalDateTime to epoch milliseconds
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        long timeInMillis = instant.toEpochMilli();
        return byteArrayToHex(algorithName.doFinal(String.valueOf(timeInMillis).getBytes(StandardCharsets.UTF_8)));
    }


    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

}
