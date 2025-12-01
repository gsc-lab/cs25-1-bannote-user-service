package com.bannote.userservice.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 한글 초성 검색을 위한 유틸리티 클래스
 */
public class KoreanSearchUtils {

    // 초성 유니코드 범위 매핑
    private static final Map<Character, String> CHOSUNG_RANGE = new HashMap<>();

    static {
        CHOSUNG_RANGE.put('ㄱ', "[가-깋]");
        CHOSUNG_RANGE.put('ㄲ', "[까-낗]");
        CHOSUNG_RANGE.put('ㄴ', "[나-닣]");
        CHOSUNG_RANGE.put('ㄷ', "[다-딯]");
        CHOSUNG_RANGE.put('ㄸ', "[따-띻]");
        CHOSUNG_RANGE.put('ㄹ', "[라-맇]");
        CHOSUNG_RANGE.put('ㅁ', "[마-밓]");
        CHOSUNG_RANGE.put('ㅂ', "[바-빻]");
        CHOSUNG_RANGE.put('ㅃ', "[빠-삫]");
        CHOSUNG_RANGE.put('ㅅ', "[사-싷]");
        CHOSUNG_RANGE.put('ㅆ', "[싸-앃]");
        CHOSUNG_RANGE.put('ㅇ', "[아-잏]");
        CHOSUNG_RANGE.put('ㅈ', "[자-짛]");
        CHOSUNG_RANGE.put('ㅉ', "[짜-찧]");
        CHOSUNG_RANGE.put('ㅊ', "[차-칳]");
        CHOSUNG_RANGE.put('ㅋ', "[카-킣]");
        CHOSUNG_RANGE.put('ㅌ', "[타-팋]");
        CHOSUNG_RANGE.put('ㅍ', "[파-핗]");
        CHOSUNG_RANGE.put('ㅎ', "[하-힣]");
    }

    /**
     * 초성인지 확인
     */
    private static boolean isChosung(char c) {
        return CHOSUNG_RANGE.containsKey(c);
    }

    /**
     * 완성된 한글인지 확인
     */
    private static boolean isCompleteHangul(char c) {
        return c >= '가' && c <= '힣';
    }

    /**
     * 검색어를 MySQL REGEXP 패턴으로 변환
     * 예: "김ㅅ" → "김[사-싷].*"
     * 예: "ㄱㅅㄷ" → "[가-깋][사-싷][다-딯].*"
     * 예: "김서" → "김서.*"
     */
    public static String toRegexPattern(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return ".*";
        }

        StringBuilder pattern = new StringBuilder();

        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);

            if (isChosung(c)) {
                // 초성이면 해당 초성 범위로 변환
                pattern.append(CHOSUNG_RANGE.get(c));
            } else if (isCompleteHangul(c)) {
                // 완성된 한글이면 그대로 사용
                pattern.append(c);
            } else {
                // 영문, 숫자 등은 그대로 사용
                pattern.append(c);
            }
        }

        return pattern.toString();
    }

    /**
     * 이름 검색을 위한 MySQL REGEXP 패턴 생성
     * 이름의 어느 부분에서든 매칭되도록 함
     * 예: "ㅅㄷ" 검색 시 "김서동"도 매칭되도록 (중간 매칭 허용)
     */
    public static String toNameSearchPattern(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return ".*";
        }

        String corePattern = toRegexPattern(keyword);

        // 이름 중간에 있어도 매칭되도록 앞뒤에 .*를 추가
        return ".*" + corePattern + ".*";
    }

    /**
     * 이름 시작 부분만 검색하는 패턴 생성
     * 예: "김ㅅ" 검색 시 "김서동"은 매칭, "이김서"는 미매칭
     */
    public static String toNamePrefixPattern(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return ".*";
        }

        String corePattern = toRegexPattern(keyword);

        // 이름 시작 부분에서만 매칭
        return "^" + corePattern + ".*";
    }
}
