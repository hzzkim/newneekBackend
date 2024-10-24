package com.newneek.crawling;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class RelativeTimeParser {

    public LocalDateTime parse(String relativeTime) {
        int amount;

        // 상대 시간에서 숫자 부분을 추출
        if (relativeTime.contains("분 전")) {
            amount = convertNumberToInt(relativeTime.replace("분 전", "").trim());
            return LocalDateTime.now().minusMinutes(amount);
        } else if (relativeTime.contains("시간 전")) {
            amount = convertNumberToInt(relativeTime.replace("시간 전", "").trim());
            return LocalDateTime.now().minusHours(amount);
        } else if (relativeTime.contains("일 전")) {
            amount = convertNumberToInt(relativeTime.replace("일 전", "").trim());
            return LocalDateTime.now().minusDays(amount);
        } else if (relativeTime.contains("달 전")) {
            amount = convertNumberToInt(relativeTime.replace("달 전", "").trim());
            return LocalDateTime.now().minusMonths(amount);
        } else if (relativeTime.contains("하루 전")) {
            return LocalDateTime.now().minusHours(24); // 하루 전을 24시간으로 처리
        } else {
            throw new IllegalArgumentException("지원하지 않는 형식입니다: " + relativeTime);
        }
    }

    private int convertNumberToInt(String text) {
        // 한글 숫자 처리
        if (text.contains("한")) return 1;
        if (text.contains("두")) return 2;
        if (text.contains("세")) return 3;
        if (text.contains("네")) return 4;
        if (text.contains("다섯")) return 5;
        if (text.contains("여섯")) return 6;
        if (text.contains("일곱")) return 7;
        if (text.contains("여덟")) return 8;
        if (text.contains("아홉")) return 9;
        if (text.contains("열")) return 10;

        // 숫자형태 처리
        String numberPart = text.replaceAll("[^0-9]", ""); // 숫자 이외의 문자 제거
        if (!numberPart.isEmpty()) {
            return Integer.parseInt(numberPart); // 숫자로 변환
        }

        throw new IllegalArgumentException("지원하지 않는 숫자: " + text);
    }
}
