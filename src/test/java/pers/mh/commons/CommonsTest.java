package pers.mh.commons;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

public class CommonsTest {

    @Test
    public void testGetHourString() {
        String start = "2021-01-24-00";
        String end = "2021-01-24-05";
        List<String> dates = Commons.getHourString(start, end, "yyyy-MM-dd-HH", "yyyyMMdd/HH");
        Assert.assertEquals(5, dates.size());
        Assert.assertEquals("20210124/01", dates.get(1));
    }

    @Test
    public void testCheckBlankAndReturn() {
        String ret = Commons.checkBlankAndReturn("a", "b");
        Assert.assertEquals("a", ret);

        ret = Commons.checkBlankAndReturn(" ", "b");
        Assert.assertEquals("b", ret);

        ret = Commons.checkBlankAndReturn(null, "b");
        Assert.assertEquals("b", ret);

        ret = Commons.checkBlankAndReturn(null, null);
        Assert.assertNull(ret);

        Integer i = Commons.checkBlankAndReturn("1", 2, Integer::parseInt);
        Assert.assertEquals(new Integer(1), i);

        i = Commons.checkBlankAndReturn("", 2, Integer::parseInt);
        Assert.assertEquals(new Integer(2), i);

        Boolean b = Commons.checkBlankAndReturn("", true, Boolean::parseBoolean);
        Assert.assertTrue(b);
    }


    @Test
    public void test_00() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f0");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertNull(type);
    }

    @Test
    public void test_01() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f1");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_02() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f2");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_03() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f3");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_04() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f4");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_05() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f5");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void test_06() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f6");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_07() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f7");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_08() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f8");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void test_09() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f9");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_10() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f10");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void test_11() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f11");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_12() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f12");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void test_13() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f13");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertNotNull(type);
        Assert.assertEquals("?", type.getTypeName());
    }

    @Test
    public void test_14() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f14");
        Type type = Commons.getTypeArg(f.getGenericType(), null, Map.class, 1);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_15() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f15");
        Type type = Commons.getTypeArg(f.getGenericType(), null, Map.class, 1);
        Assert.assertEquals(Integer.class, type);
    }

    @Test
    public void test_16() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f16");
        Type type = Commons.getTypeArg(f.getGenericType(), null, Iterable.class, 0);
        Assert.assertEquals(Byte.class, type);
    }

    @Test
    public void test_17() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f17");
        Type type = Commons.getTypeArg(f.getGenericType(), null, Iterable.class, 0);
        Assert.assertEquals(Byte.class, type);
    }

    @Test
    public void test_18() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f18");
        Type type = Commons.getTypeArg(f.getGenericType(), null, Iterable.class, 0);
        Assert.assertEquals(Byte.class, type);
    }

    @Test
    public void test_19() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f19");
        Type type = Commons.getTypeArg(f.getGenericType(), null, Iterable.class, 0);
        Assert.assertEquals(Byte.class, type);
    }

    @Test
    public void test_20() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f20");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertTrue(type instanceof TypeVariable);
    }

    @Test
    public void test_21() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f21");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertTrue(type instanceof ParameterizedType);
        Type nested = Commons.getTypeArg(type, null, List.class, 0);
        Assert.assertEquals(String.class, nested);
    }

    @Test
    public void test_22() throws NoSuchFieldException {
        Field f = Pojo.class.getField("f22");
        Type type = Commons.getTypeArg(f.getGenericType(), null, List.class, 0);
        Assert.assertEquals(List.class, type);
    }


    public static class Pojo {
        public Object f0;
        public List<Integer> f1;
        public ArrayList<Integer> f2;
        public C1 f3;
        public C2<Integer> f4;
        public C3<Integer> f5; // notice, this is not an integer list, it's a string list
        public C4<Integer> f6;
        public C5<String, Integer> f7;
        public C5<String, String> f8;
        public C6<String, Integer> f9;
        public C6<String, String> f10;
        public C7<String, Integer> f11;
        public C7<String, String> f12;
        public C7<String, ?> f13;
        public Map<String, Integer> f14;
        public HashMap<String, Integer> f15;
        public Iterable<Byte> f16;
        public X1 f17;
        public X2<Byte> f18;
        public X3 f19;
        public List f20;
        public List<List<String>> f21;
        public List<List> f22;
    }

    private interface A<E> {
    }

    private static class B<T> extends AbstractList<T> {
        @Override
        public T get(int index) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    public static class C1 extends B<Integer> {
    }

    public static class C2<T> extends B<T> {
    }

    public static class C3<T> extends B<String> {
    }

    public static class C4<T> extends B<Integer> {
    }

    public static class C5<T1, T2> extends B<T2> {
    }

    public static class C6<T1, T2> implements List<T2> {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<T2> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(T2 t2) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends T2> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends T2> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public T2 get(int index) {
            return null;
        }

        @Override
        public T2 set(int index, T2 element) {
            return null;
        }

        @Override
        public void add(int index, T2 element) {

        }

        @Override
        public T2 remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<T2> listIterator() {
            return null;
        }

        @Override
        public ListIterator<T2> listIterator(int index) {
            return null;
        }

        @Override
        public List<T2> subList(int fromIndex, int toIndex) {
            return null;
        }
    }

    public static class C7<T1, T2> implements List<T2>, A<T1> {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<T2> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(T2 t2) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends T2> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends T2> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public T2 get(int index) {
            return null;
        }

        @Override
        public T2 set(int index, T2 element) {
            return null;
        }

        @Override
        public void add(int index, T2 element) {

        }

        @Override
        public T2 remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<T2> listIterator() {
            return null;
        }

        @Override
        public ListIterator<T2> listIterator(int index) {
            return null;
        }

        @Override
        public List<T2> subList(int fromIndex, int toIndex) {
            return null;
        }
    }

    interface X1 extends Iterable<Byte> {
    }

    interface X2<T> extends Iterable<T> {
    }

    interface X3 extends X1 {
    }
}
