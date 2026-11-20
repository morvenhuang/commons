package pers.mh.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PrettyPrinter {

    private List<String[]> table = new ArrayList<>();
    private int[] colWidth;
    private int colCount = 0;

    @SuppressWarnings("unused")
    private PrettyPrinter() {
        //
    }

    private void init(String... values) {
        // it's a empty array (not null) if we don't provide any value when calling this ctor
        if (values.length == 0) {
            throw new RuntimeException("Input is empty");
        }
        this.colCount = values.length;
        this.colWidth = new int[this.colCount]; // init
        //
        for (int i = 0; i < this.colCount; i++) {
            values[i] = values[i] == null ? "(NULL)" : values[i];
            this.colWidth[i] = Commons.getStringWidth(values[i]);
        }
        this.table.add(values);
    }

    public PrettyPrinter(Map<Object, Object> map) {
        if (map == null || map.size() == 0) {
            throw new RuntimeException("Input is empty");
        }
        int index = 0;
        for (Entry<Object, Object> kv : map.entrySet()) {
            String k = kv.getKey() == null ? null : kv.getKey().toString();
            String v = kv.getValue() == null ? null : kv.getValue().toString();
            if (index == 0) {
                init(k, v);
            } else {
                newRow(k, v);
            }
            index++;
        }
    }

    public PrettyPrinter(String... values) {
        init(values);
    }

    public PrettyPrinter newRow(String... values) {
        if (this.colCount != values.length) {
            throw new RuntimeException("Rows do not have same field number");
        }
        for (int i = 0; i < this.colCount; i++) {
            values[i] = values[i] == null ? "(NULL)" : values[i];
            int width = Commons.getStringWidth(values[i]);
            if (width > this.colWidth[i]) {
                this.colWidth[i] = width;
            }
        }
        this.table.add(values);
        return this;
    }

    public String buildString() {
        StringBuffer border = new StringBuffer("+");
        for (int i : colWidth) {
            border.append(Commons.rightPad("", i + 1, '-') + "+");
        }
        //
        StringBuffer buf = new StringBuffer();
        String sep = System.lineSeparator();
        buf.append(sep);
        buf.append(border.toString() + sep);
        for (String[] srow : table) {
            // for multi-line string
            List<String[]> list = new ArrayList<>();
            int height = 0;
            for (String s : srow) {
                String[] lines = s.split("\r\n|\r|\n");
                if (lines.length > height) {
                    height = lines.length;
                }
                list.add(lines);
            }
            for (int h = 0; h < height; h++) {
                StringBuffer sbrow = new StringBuffer("|");
                for (int i = 0; i < this.colCount; i++) {
                    String content = "";
                    if (h < list.get(i).length) {
                        content = list.get(i)[h];
                    }
                    sbrow.append(Commons.rightPad(content, this.colWidth[i] + 1) + "|");
                }
                buf.append(sbrow.toString() + sep);
            }
            buf.append(border.toString() + sep);
        }
        buf.append(sep);
        return buf.toString();
    }

    public int getColCount() {
        return colCount;
    }
}
