/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.sbt.jschool.session2;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class OutputFormatter {
    private PrintStream out;

    public OutputFormatter(PrintStream out) {
        this.out = out;
    }

    public void output(String[] names, Object[][] data) {
        int[] columnWidths = getColumnWidth(names, data);
        String splitter = data.length > 0 ? getHorizontalLine(columnWidths) : getHorizontalLineByHeader(names);
        out.println(splitter);
        String line = "|";
        if (data.length > 0) {
            for (int i = 0; i < names.length; i++) {
                line += centerString(columnWidths[i], names[i]);
                line += "|";
            }
        } else {
            for (int i = 0; i < names.length; i++) {
                line += centerString(names[i].length(), names[i]);
                line += "|";
            }
        }
        out.println(line);
        out.println(splitter);

        String date;
        for (int row = 0; row < data.length; row++) {
            line = "|";
            for (int column = 0; column < data[0].length; column++) {
                Object object = data[row][column];
                if (object instanceof Integer) {
                    for (int j = 0; j < columnWidths[column] - getNumberNiceLook((Integer) object).length(); j++) {
                        line += " ";
                    }
                    line += getNumberNiceLook((Integer) object);
                }
                if (object instanceof Date) {
                    date = new SimpleDateFormat("dd.MM.yyyy").format(object);
                    //date = date.substring(0, 6) + (Integer.parseInt(date.substring(6, 10)) - 1900);
                    line += date;
                }
                if (object instanceof Double) {
                    for (int j = 0; j < columnWidths[column] - getMoneyNiceLook((Double) object).length(); j++) {
                        line += " ";
                    }
                    line += getMoneyNiceLook((Double) object);
                }
                if (object instanceof String) {
                    line += object;
                    for (int i = 0; i < columnWidths[column] - (((String) object).length()); i++) {
                        line += " ";
                    }
                }
                if (object == null) {
                    if (getClassName(data, column) == "java.lang.String") {
                        line += "-";
                        for (int i = 0; i < columnWidths[column] - 1; i++) {
                            line += " ";
                        }
                    }
                    if (getClassName(data, column) == "java.lang.Integer") {
                        for (int i = 0; i < columnWidths[column] - 1; i++) {
                            line += " ";
                        }
                        line += "-";
                    }
                }
                line += "|";
            }
            out.println(line);
            out.println(splitter);
        }
    }

    private Object getClassName(Object[][] data, int column) {
        String name = null;
        int i = 0;
        while (name == null) {
            name = data[i][column].getClass().getName();
        }
        return name;
    }

    private String getNumberNiceLook(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(number);
    }

    private String getMoneyNiceLook(double money) {
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
        return decimalFormat.format(money);
    }

    private String getHorizontalLine(int[] columnWidths) {
        String line = "+";
        for (int i = 0; i < columnWidths.length; i++) {
            for (int j = 0; j < columnWidths[i]; j++) {
                line += "-";
            }
            line += "+";
        }
        return line;
    }

    private int[] getColumnWidth(String[] names, Object[][] data) {
        int[] columnWidths = new int[names.length];
        for (int column = 0; column < names.length; column++) {
            int width = names[column].length();
            int buf;
            for (int i = 0; i < data.length; i++) {
                if (data[i][column] instanceof Integer) {
                    buf = getNumberNiceLook((Integer) data[i][column]).length();
                } else if (data[i][column] instanceof Double) {
                    buf = getMoneyNiceLook((Double) data[i][column]).length();
                } else {
                    buf = String.valueOf(data[i][column]).length();
                }
                if (data[i][column] == null) {
                    buf = 1;
                }
                if (buf > width) {
                    width = buf;
                }
            }
            if (data.length > 0 && data[0][column] instanceof Date) {
                width = 10;
            }
            columnWidths[column] = width;
        }
        return columnWidths;
    }

    private String getHorizontalLineByHeader(String[] names) {
        String line = "+";
        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < names[i].length(); j++) {
                line += "-";
            }
            line += "+";
        }
        return line;
    }

    private String centerString(int columnWidth, String string) {
        String result = "";
        int startPos = (int) Math.round(columnWidth / 2. - string.length() / 2. - .5);
        for (int i = 0; i < startPos; i++) {
            result += " ";
        }
        result += string;
        int preLength = result.length();
        for (int i = 0; i < columnWidth - preLength; i++) {
            result += " ";
        }
        return result;
    }
}
