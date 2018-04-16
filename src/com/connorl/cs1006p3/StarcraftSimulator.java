package com.connorl.cs1006p3;

import java.util.Arrays;

public class StarcraftSimulator {
    public static void main(String[] args) {
        Constructable construct = Constructable.DARK_SHRINE;
        Arrays.stream(construct.getDependencies()).forEach(System.out::println);
    }
}
