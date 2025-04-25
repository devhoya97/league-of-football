package com.lof.game.domain;

import java.util.List;

public enum TeamColor {
    BLUE,
    RED;

    public TeamColor getOpposite() {
        if (this == BLUE) {
            return RED;
        }
        return BLUE;
    }
}
