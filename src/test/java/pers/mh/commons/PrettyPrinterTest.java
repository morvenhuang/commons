package pers.mh.commons;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PrettyPrinterTest {

    @Test
    public void testBasic() {
        PrettyPrinter pp = new PrettyPrinter("1", "2", "3");
        String a = pp.buildString();
        System.out.println(a);
        String x = pp.newRow("1", "", "3").newRow("22", "333", "8888").newRow("7", null, null).buildString();
        System.out.println(x);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyInput() {
        new PrettyPrinter();
    }

    @Test(expected = RuntimeException.class)
    public void testColCount() {
        PrettyPrinter pp = new PrettyPrinter("1");
        pp.newRow("2", "3");
    }

    @Test(expected = RuntimeException.class)
    public void testMapNull() {
        Map<Object, Object> m = null;
        new PrettyPrinter(m);
    }

    @Test
    public void testMap() {
        Map<Object, Object> m = new HashMap<>();
        m.put("a", null);
        m.put("b", "c");
        PrettyPrinter pp = new PrettyPrinter(m);
        System.out.println(pp.buildString());
    }

    @Test
    public void testMultiLineString() {
        PrettyPrinter pp = new PrettyPrinter("foobar", "foobar");
        pp.newRow("x", "xxxxxxxxxxxxx");
        pp.newRow("dummy", "");
        pp.newRow("john", "This method can be used to join multiple \nstrings\n which are not \nyet in form of col");
        System.out.println(pp.buildString());
    }
}
