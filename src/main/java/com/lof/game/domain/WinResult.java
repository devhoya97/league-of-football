package com.lof.game.domain;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

public enum WinResult {
    BLUE,
    RED,
    DRAW,
    NOT_DETERMINED;

    public TeamColor toTeamColor() {
        if (this == BLUE) {
            return TeamColor.BLUE;
        }
        if (this == RED) {
            return TeamColor.RED;
        }
        throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR); // 매번 ErrorCode 만들어주는거 너무 별론데
    }
}
