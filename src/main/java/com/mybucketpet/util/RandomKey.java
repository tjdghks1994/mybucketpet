package com.mybucketpet.util;

import java.util.Random;

public class RandomKey {
    private static final Random rnd = new Random();
    /**
     * 랜덤 값 생성 (8자리)
     * */
    public static String createKey() {
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) { // 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }
}
