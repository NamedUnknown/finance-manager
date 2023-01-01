package com.dev.financemanager.utils;

import com.dev.financemanager.dto.request.AuthenticationRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class Base64TokenUtils {

    private final Charset STANDARD_CHARSET = StandardCharsets.UTF_8;
    private final Base64.Encoder ENCODER = Base64.getEncoder();
    private final Base64.Decoder DECODER = Base64.getDecoder();

    public String decode(String stringToDecode) {
        return new String(DECODER.decode(stringToDecode.getBytes()), STANDARD_CHARSET);
    }

    public String encode(String stringToEncode) {
        return new String(ENCODER.encode(stringToEncode.getBytes()), STANDARD_CHARSET);
    }

    public AuthenticationRequest fetchCredentials(String header) throws IOException {
        if (!header.contains("Bearer")) {
            return null;
        }
        header = header.replace("Bearer", "").trim();
        if (header.equals("")) {
            return null;
        }
        String[] credentialsArray = decode(header).split(":");
        for (int i = 0; i < credentialsArray.length; i ++) {
            credentialsArray[i] = credentialsArray[i].trim();
        }
        return new AuthenticationRequest(credentialsArray[0], credentialsArray[1]);
    }
}
