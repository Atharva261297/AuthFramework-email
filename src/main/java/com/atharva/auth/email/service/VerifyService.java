package com.atharva.auth.email.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class VerifyService {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private Map<String, String> uuidCodeMap = new HashMap<>();
    private Map<String, String> uuidIdMap = new HashMap<>();
    private Map<String, String> uuidTypeMap = new HashMap<>();

    String createUuid(String id) throws NoSuchAlgorithmException {
        String uuid = UUID.randomUUID().toString();
        uuidIdMap.put(uuid, id);
        uuidCodeMap.put(uuid, getCode());
        return uuid;
    }

    String createUuidType(String id, String type) throws NoSuchAlgorithmException {
        String uuid = UUID.randomUUID().toString();
        uuidIdMap.put(uuid, id);
        uuidCodeMap.put(uuid, getCode());
        uuidTypeMap.put(uuid, type);
        return uuid;
    }

    String createCode(String uuid) {
        return uuidCodeMap.getOrDefault(uuid, StringUtils.EMPTY);
    }

    public String verifyCode(String uuid, String code) {
        String codeFromMap = uuidCodeMap.getOrDefault(uuid, StringUtils.EMPTY);
        if (codeFromMap.equals(code)) {
            uuidCodeMap.remove(uuid, code);
            return uuidIdMap.remove(uuid);
        } else {
            return StringUtils.EMPTY;
        }
    }

    public String verifyCodeType(String uuid, String code) {
        String codeFromMap = uuidCodeMap.getOrDefault(uuid, StringUtils.EMPTY);
        if (codeFromMap.equals(code)) {
            uuidCodeMap.remove(uuid, code);
            return uuidIdMap.remove(uuid) + ":" + uuidTypeMap.remove(uuid);
        } else {
            return StringUtils.EMPTY;
        }
    }

    private String getCode() throws NoSuchAlgorithmException {
        final SecureRandom random = new SecureRandom();
        String stringBuilder;
        final byte[] code = new byte[6];
        random.nextBytes(code);
        stringBuilder = IntStream.range(0, 6).mapToObj(i -> String.valueOf(ALPHA_NUMERIC_STRING.charAt(Math.abs(code[i] % 36)))).collect(Collectors.joining());
        return stringBuilder;
    }
}
