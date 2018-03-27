package ru.sbt.jschool.session2;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        OutputFormatter outputFormatter = new OutputFormatter(System.out);
        String[] names = {"#", "Date", "Money", "Text"};
        Object[][] data = {
                {1, new Date(2018, 2, 8), 34.2, "Something"},
                {2999, new Date(2018, 2, 9), 72., "Lols"},
                {null, new Date(2018, 3, 2), 234235.2, "Smth"}
        };
        outputFormatter.output(names, data);
    }
}
