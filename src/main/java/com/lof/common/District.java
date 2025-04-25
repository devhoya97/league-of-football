package com.lof.common;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

public enum District {
    GANGNAM,
    GANGDONG,
    GANGBUK,
    GANGSEO,
    GWANAK,
    GWANGJIN,
    GURO,
    GEUMCHEON,
    NOWON,
    DOBONG,
    DONGDAEMUN,
    DONGJAK,
    MAPO,
    SEODAEMUN,
    SEOCHO,
    SEONGDONG,
    SEONGBUK,
    SONGPA,
    YANGCHEON,
    YEONGDEUNGPO,
    YONGSAN,
    EUNPYEONG,
    JONGNO,
    JUNG,
    JUNGRANG;

    @JsonCreator
    public static District from(String input) {
        return Arrays.stream(District.values())
                .filter(d -> d.name().equals(input))
                .findFirst()
                .orElseThrow(() -> new BizException(ErrorCode.INVALID_DISTRICT));
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
