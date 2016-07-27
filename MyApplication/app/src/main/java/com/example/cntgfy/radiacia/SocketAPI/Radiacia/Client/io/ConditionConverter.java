package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Cntgfy on 21.07.2016.
 */
public class ConditionConverter {
    private static Condition c = Condition.Object;
    private static Condition[] conditions = Condition.values();

    /**
     * Возвращает Condition по переданному значению
     *
     * @param i переданное значение 0<=i<128
     * @return Condition соответствующий значению i
     */
    public static Condition condition(int i) {
        if (i >= 0 & i < conditions.length) {
            return conditions[i];
        } else {
            return Condition.Unknown;
        }
    }

    public static int anInt(Condition condition) {
        for (int i = 0; i < conditions.length; i++) {
            if (conditions[i] == condition) return i;
        }

        return Condition.Unknown.ordinal();
    }
}
