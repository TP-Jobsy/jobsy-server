package com.example.jobsyserver.util;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public final class ConfirmationCodeGenerator {

    public static String generateNumericCode(int length) {
        int max = (int) Math.pow(10, length);
        int code = ThreadLocalRandom.current().nextInt(0, max);
        return String.format("%0" + length + "d", code);
    }

}
