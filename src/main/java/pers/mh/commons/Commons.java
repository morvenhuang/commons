package pers.mh.commons;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Commons {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Commons.class);
    private static final Random random = new Random();
    /**
     * Help method from parsing command line arguments into a Map<String, String>,
     * the argument is in 'key=value' format.
     */
    public static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            String[] arr = arg.split("=", 2);
            if (arr.length == 2 && !"".equals(arr[1])) {
                map.put(arr[0], arr[1].trim());
            } else {
                logger.warn("Invalid argument, {}, ignored", arg);
            }
        }
        return map;
    }

    /**
     * In case we need to stick to some specific time, for example, x:05:30, x:10:30, x:15:30,
     * the method will calculate the initial delay for any given date time. Time unit: second
     */
    public static long delay(LocalDateTime ldt, int step, int seconds) {
        long ret;
        LocalDateTime now = ldt.truncatedTo(ChronoUnit.SECONDS);
        int minute = now.getMinute();
        int mod = minute % step;
        if (mod == 0 && now.getSecond() <= seconds) {
            ret = seconds - now.getSecond();
        } else {
            LocalDateTime x = now.plusMinutes(step - mod).withSecond(seconds);
            ret = now.until(x, ChronoUnit.SECONDS);
        }
        logger.warn("-------------------- Schedule Initial Delay: {} seconds --------------------", ret);
        return ret;
    }

    /**
     * Convert Iterable to List
     */
    public static <T> List<T> convertIterable2List(Iterable<T> src) {
        if (src == null) {
            logger.warn("The iterable is null.");
            return null;
        }
        List<T> ret = new ArrayList<>();
        for (T t : src) {
            ret.add(t);
        }
        return ret;
    }

    /**
     * Join a Iterable into one single String
     */
    public static <T> String concatIterable(Iterable<T> iterable, String delimiter, String replaceNullWith) {
        if (iterable == null) {
            logger.warn("The iterable is null.");
            return null;
        }
        if (delimiter == null) {
            logger.warn("The delimiter is null.");
            return null;
        }
        if (replaceNullWith == null) {
            logger.warn("The parameter 'replaceNullWith' is null.");
            return null;
        }
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (T t : iterable) {
            String str = t == null ? replaceNullWith : t.toString();
            if (first) {
                sb.append(str);
                first = false;
            } else {
                sb.append(delimiter + str);
            }
        }
        return sb.toString();
    }

    /**
     * Convert List of T1 to List of T2
     */
    public static <T> List<T> convertList(List<Object> from, Function<Object, T> converter) {
        List<T> to = from.stream().map(item -> converter.apply(item)).collect(Collectors.toList());
        return to;
    }

    /**
     * Convert List of T1 to List of T2
     */
    public static <T> List<T> convertList(List<Object> from, Class<T> clazz) {
        List<T> to = convertList(from, clazz::cast);
        return to;
    }

    /**
     * Convert List of T1 to List of String
     */
    public static List<String> convertList2StringList(List<Object> from) {
        List<String> to = from.stream().map(item -> item.toString()).collect(Collectors.toList());
        return to;
    }

    /**
     * If the type of the given field is parameterized, get the type argument according to
     * the given index. For example, it will return Integer.class if the type of the field
     * is List<Integer>
     * @param f field
     * @param index type argument index
     * @return return the type argument, or null if the type of the given field is not
     * parameterized, or the type argument is not an instance of Class.
     */
    public static Class<?> getGenericType(Field f, int index) {
        if (!(f.getGenericType() instanceof ParameterizedType)) {
            logger.warn("Not parameterized type, ignored");
            return null;
        }
        ParameterizedType pt = (ParameterizedType) f.getGenericType();
        if (pt.getActualTypeArguments()[index] instanceof Class) {
            return (Class<?>) pt.getActualTypeArguments()[index];
        } else {
            logger.warn("Wildcard type, ignored");
            return null;
        }
    }

    /**
     * If the returnn type of the given method is parameterized, get the type argument according to
     * the given index. For example, it will return Integer.class if the return type of the method
     * is {@literal List<Integer>}
     * @param m method
     * @param index type argument index
     * @return return the type argument, or null if the return type of the given method is not
     * parameterized, or the type argument is not an instance of Class.
     */
    public static Class<?> getGenericType(Method m, int index) {
        if (!(m.getGenericReturnType() instanceof ParameterizedType)) {
            logger.warn("Not parameterized type, ignored");
            return null;
        }
        ParameterizedType pt = (ParameterizedType) m.getGenericReturnType();
        if (pt.getActualTypeArguments()[index] instanceof Class) {
            return (Class<?>) pt.getActualTypeArguments()[index];
        } else {
            logger.warn("Wildcard type, ignored");
            return null;
        }
    }

    /**
     * Check if the given type is an instance of given generic target class,
     * if so, return the type argument according to the given index, otherwise,
     * return null. Usage, getTypeArg(field.getGenericType(), null, List.class, 0),
     * or, getTypeArg(field.getGenericType(), null, Map.class, 1), etc.
     * @param type a type to check
     * @param typeVar2RealType a map mapping type variable to real type
     * @param targetClazz generic target class
     * @param targetTypeArgIndex type argument index
     * @return type argument
     */
    public static Type getTypeArg(Type type, Map<TypeVariable<?>, Type> typeVar2RealType,
                                  Class<?> targetClazz, int targetTypeArgIndex) {
        if (targetClazz == null) {
            throw new RuntimeException("Missing target class");
        }
        if (targetTypeArgIndex < 0) {
            throw new RuntimeException("Invalid target type argument index");
        }
        if (type == null || type.equals(Object.class)) {
            return null;
        }
        Class<?> raw;
        Map<TypeVariable<?>, Type> map = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            raw = (Class<?>) pt.getRawType();
            if (!targetClazz.isAssignableFrom(raw)) {
                return null;
            }
            map = new HashMap<>();
            for (int i = 0; i < pt.getActualTypeArguments().length; i++) {
                TypeVariable<?> var = raw.getTypeParameters()[i];
                Type arg = pt.getActualTypeArguments()[i];
                Type real = arg;
                if (arg instanceof TypeVariable) {
                    TypeVariable<?> tv = (TypeVariable<?>) arg;
                    if (typeVar2RealType != null && typeVar2RealType.containsKey(tv)) {
                        real = typeVar2RealType.get(tv);
                    }
                }
                if (raw.equals(targetClazz) && i == targetTypeArgIndex) {
                    return real;
                }
                map.put(var, real);
            }
        } else if (type instanceof Class) {
            raw = (Class<?>) type;
            if (!targetClazz.isAssignableFrom(raw)) {
                return null;
            }
        } else {
            return null; // ?
        }
        //
        for (Type parent : getGenericParents(raw)) {
            Type ret = getTypeArg(parent, map, targetClazz, targetTypeArgIndex);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    /**
     * Get generic interfaces and the generic superclass of the given class.
     * Returns a list containing implemented generic interfaces and the generic superclass if present.
     *
     * @param clazz the class whose generic parents to retrieve
     * @return list of generic parent types; empty if none
     */
    public static List<Type> getGenericParents(Class<?> clazz) {
        List<Type> parents = new ArrayList<>();
        if (clazz.getGenericInterfaces().length > 0) {
            parents = new ArrayList<>(Arrays.asList(clazz.getGenericInterfaces()));
        }
        if (clazz.getGenericSuperclass() != null) {
            parents.add(clazz.getGenericSuperclass());
        }
        return parents;
    }

    /**
     * Check if a String is blank (null, empty string, or space(s))
     */
    public static boolean isBlankString(String s) {
        if (s == null || s.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * Check if a String is empty (null, or empty string)
     */
    public static boolean isEmptyString(String s) {
        if (s == null || s.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * Split a String to a Set according to given delimiter
     */
    public static <T> Set<T> splitString2Set(String s, String delimiter, Function<String, T> converter) {
        return splitString2Set(s, delimiter, converter, false);
    }

    /**
     * Split a String to a Set according to given delimiter
     */
    public static <T> Set<T> splitString2Set(String s, String delimiter, Function<String, T> converter, boolean trim) {
        if (s == null) {
            logger.warn("The string is null.");
            return null;
        }
        if (delimiter == null) {
            logger.warn("The delimiter is null.");
            return null;
        }
        if (converter == null) {
            logger.warn("The converter is null.");
            return null;
        }
        Set<T> set = new HashSet<>();
        String[] split = s.split(delimiter);
        for (String each : split) {
            if (trim) {
                each = each.trim();
            }
            set.add(converter.apply(each));
        }
        return set;
    }

    /**
     * Split a String to a String Set according to given delimiter
     */
    public static Set<String> splitString2StringSet(String s, String delimiter, boolean trim) {
        return splitString2Set(s, delimiter, x -> x, trim);
    }

    /**
     * Split a String to a String Set according to given delimiter
     */
    public static Set<String> splitString2StringSet(String s, String delimiter) {
        return splitString2Set(s, delimiter, x -> x);
    }

    /**
     * Split a String to a Integer Set according to given delimiter
     */
    public static Set<Integer> splitString2IntSet(String s, String delimiter) {
        return splitString2Set(s, delimiter, Integer::parseInt, true);
    }

    /**
     * Split a String to a Long Set according to given delimiter
     */
    public static Set<Long> splitString2LongSet(String s, String delimiter) {
        return splitString2Set(s, delimiter, Long::parseLong, true);
    }

    /**
     * Split a String to a Double Set according to given delimiter
     */
    public static Set<Double> splitString2DoubleSet(String s, String delimiter) {
        return splitString2Set(s, delimiter, Double::parseDouble, true);
    }

    /**
     * Split a String to a List according to given delimiter
     */
    public static <T> List<T> splitString2List(String s, String delimiter, Function<String, T> converter) {
        return splitString2List(s, delimiter, converter, false);
    }

    /**
     * Split a String to a List according to given delimiter
     */
    public static <T> List<T> splitString2List(String s, String delimiter, Function<String, T> converter, boolean trim) {
        if (s == null) {
            logger.warn("The string is null.");
            return null;
        }
        if (delimiter == null) {
            logger.warn("The delimiter is null.");
            return null;
        }
        if (converter == null) {
            logger.warn("The converter is null.");
            return null;
        }
        List<T> list = new ArrayList<>();
        String[] split = s.split(delimiter);
        for (String each : split) {
            if (trim) {
                each = each.trim();
            }
            list.add(converter.apply(each));
        }
        return list;
    }

    /**
     * Split a String to a String List according to given delimiter
     */
    public static List<String> splitString2StringList(String s, String delimiter) {
        return splitString2List(s, delimiter, x -> x);
    }

    /**
     * Split a String to a String List according to given delimiter
     */
    public static List<String> splitString2StringList(String s, String delimiter, boolean trim) {
        return splitString2List(s, delimiter, x -> x, trim);
    }

    /**
     * Split a String to a Integer List according to given delimiter
     */
    public static List<Integer> splitString2IntList(String s, String delimiter) {
        return splitString2List(s, delimiter, Integer::parseInt, true);
    }

    /**
     * Split a String to a Long List according to given delimiter
     */
    public static List<Long> splitString2LongList(String s, String delimiter) {
        return splitString2List(s, delimiter, Long::parseLong, true);
    }

    /**
     * Split a String to a Double List according to given delimiter
     */
    public static List<Double> splitString2DoubleList(String s, String delimiter) {
        return splitString2List(s, delimiter, Double::parseDouble, true);
    }

    /**
     * Read a file, return a List of lines
     */
    public static List<String> readFileByLine(String path) {
        List<String> list = new ArrayList<String>();
        try (FileReader reader = new FileReader(path); BufferedReader br = new BufferedReader(reader)) {
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Read a file, return a List of lines
     */
    public static List<String> readFileByLine(InputStream is) {
        List<String> list = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Right pad with space
     */
    public static String rightPad(String src, int len) {
        return rightPad(src, len, ' ');
    }

    /**
     * Right pad with given char
     */
    public static String rightPad(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] chars = new char[len];
        System.arraycopy(src.toCharArray(), 0, chars, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            chars[i] = ch;
        }
        return new String(chars);
    }

    /**
     * Left pad with space
     */
    public static String leftPad(String src, int len) {
        return leftPad(src, len, ' ');
    }

    /**
     * Left pad with given char
     */
    public static String leftPad(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] chars = new char[len];
        System.arraycopy(src.toCharArray(), 0, chars, diff, src.length());
        for (int i = 0; i < diff; i++) {
            chars[i] = ch;
        }
        return new String(chars);
    }

    /**
     * Check if any element in the array is null
     */
    public static boolean anyNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generate hour ticks between start (inclusive) and end (exclusive) using pattern strings.
     *
     * @param start start datetime string
     * @param end end datetime string
     * @param inFmt input datetime pattern (e.g., yyyy-MM-dd HH:mm:ss)
     * @param outFmt output datetime pattern
     * @return list of hour strings from start up to but excluding end
     * @throws RuntimeException if any parameter is null or start is after end
     */
    public static List<String> getHourString(String start, String end, String inFmt, String outFmt) {
        if (anyNull(start, end, inFmt, outFmt)) {
            throw new RuntimeException("Pre-check failed, some parameter is null");
        }
        return getHourString(start, end, DateTimeFormatter.ofPattern(inFmt), DateTimeFormatter.ofPattern(outFmt));
    }

    /**
     * Generate hour ticks between start (inclusive) and end (exclusive) using DateTimeFormatter.
     *
     * @param start start datetime string
     * @param end end datetime string
     * @param inFmt input formatter
     * @param outFmt output formatter
     * @return list of hour strings from start up to but excluding end
     * @throws RuntimeException if any parameter is null or start is after end
     */
    public static List<String> getHourString(String start, String end, DateTimeFormatter inFmt, DateTimeFormatter outFmt) {
        if (anyNull(start, end, inFmt, outFmt)) {
            throw new RuntimeException("Pre-check failed, some parameter is null");
        }
        LocalDateTime dtStart = LocalDateTime.parse(start, inFmt).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime dtEnd = LocalDateTime.parse(end, inFmt).truncatedTo(ChronoUnit.HOURS);
        if (dtStart.compareTo(dtEnd) > 0) {
            throw new RuntimeException("Start date should not be larger than end date");
        }
        List<String> ret = new ArrayList<>();
        while (dtStart.compareTo(dtEnd) < 0) {
            ret.add(dtStart.format(outFmt));
            dtStart = dtStart.plusHours(1);
        }
        return ret;
    }

    /**
     * Return default value if the given value is blank (null, empty string, or space(s)),
     * otherwise, return the value itself.
     */
    public static <T> T checkBlankAndReturn(String value, T ifBlank, Function<String, T> converter) {
        if (isBlankString(value)) {
            return ifBlank;
        }
        if (converter == null) {
            throw new RuntimeException("The converter is null.");
        }
        return converter.apply(value);
    }

    /**
     * Return default value if the given value is blank (null, empty string, or space(s)),
     * otherwise, return the value itself.
     */
    public static String checkBlankAndReturn(String value, String ifBlank) {
        if (isBlankString(value)) {
            return ifBlank;
        }
        return value;
    }

    /**
     * Return default value if the given value is null,
     * otherwise, return the value itself.
     */
    public static String checkNullAndReturn(String value, String ifBlank) {
        if (value == null) {
            return ifBlank;
        }
        return value;
    }

    /**
     * Throw exception if the given value is null.
     */
    public static <T> void checkNullAndThrow(T value, String errorMsg) {
        if (value == null) {
            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * Throw exception if the given string is empty (null or zero length).
     */
    public static void checkEmptyAndThrow(String value, String errorMsg) {
        if (isEmptyString(value)) {
            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * Throw exception if the given string is blank (null, zero length, or whitespace only).
     */
    public static void checkBlankAndThrow(String value, String errorMsg) {
        if (isBlankString(value)) {
            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * Get rid of the heading and tailing char, if thing goes wrong, return default value.
     */
    public static String strip(String value, String defVal) {
        if (isBlankString(value) || value.length() < 3) {
            return defVal;
        }
        return value.substring(1, value.length() - 1);
    }

    /**
     * Get rid of the heading and tailing char, and do the convertion,
     * if thing goes wrong, return default value.
     */
    public static <T> T strip(String value, Function<String, T> converter, T defVal) {
        if (isBlankString(value) || value.length() < 3) {
            return defVal;
        }
        if (converter == null) {
            throw new RuntimeException("The converter is null.");
        }
        String s = value.substring(1, value.length() - 1);
        return converter.apply(s);
    }

    /**
     * Throw exception if the given value is blank (null, empty string, or space(s)),
     * otherwise, return the value itself.
     */
    public static <T> T checkBlankAndReturnOrThrow(String value, Function<String, T> converter, String errorMsg) {
        if (isBlankString(value)) {
            throw new RuntimeException(errorMsg);
        }
        if (converter == null) {
            throw new RuntimeException("The converter is null.");
        }
        return converter.apply(value);
    }

    /**
     * Throw exception if the given value is blank (null, empty string, or space(s)),
     * otherwise, return the value itself.
     */
    public static String checkBlankAndReturnOrThrow(String value, String errorMsg) {
        if (isBlankString(value)) {
            throw new RuntimeException(errorMsg);
        }
        return value;
    }

    /**
     * Throw exception if the given value is null, otherwise, return the value itself.
     */
    public static String checkNullAndReturnOrThrow(String value, String errorMsg) {
        if (value == null) {
            throw new RuntimeException(errorMsg);
        }
        return value;
    }

    /**
     * Check if the class is primitive/primitive-wrapper/string
     */
    public static boolean isPrimitiveLike(Class<?> clz) {
        return clz.isPrimitive()
                || Double.class.equals(clz)
                || Float.class.equals(clz)
                || Long.class.equals(clz)
                || Integer.class.equals(clz)
                || Short.class.equals(clz)
                || Character.class.equals(clz)
                || Byte.class.equals(clz)
                || Boolean.class.equals(clz)
                || String.class.equals(clz);
    }

    /**
     * Count lines of a string
     */
    public static int countLines(String s){
        String[] lines = s.split("\r\n|\r|\n");
        return  lines.length;
    }

    /**
     * Get the max width of a multi-line string
     */
    public static int getStringWidth(String s) {
        String[] lines = s.split("\r\n|\r|\n");
        int width = 0;
        for (String line : lines) {
            if (line.length() > width) {
                width = line.length();
            }
        }
        return width;
    }

    /**
     * Split the range [0, max] into batches of the given size.
     *
     * @param max inclusive upper bound
     * @param batchSize size of each batch
     * @return list of batches, each containing numbers as strings
     */
    public static List<List<String>> splitRangeToBatches(Long max, int batchSize) {
        return splitRangeToBatches(0L, max, batchSize);
    }

    /**
     * Split the range [min, max] into batches of the given size.
     *
     * @param min inclusive lower bound
     * @param max inclusive upper bound
     * @param batchSize size of each batch
     * @return list of batches, each containing numbers as strings
     */
    public static List<List<String>> splitRangeToBatches(Long min, Long max, int batchSize) {
        List<List<String>> batchList = new ArrayList<>();
        List<String> currentBatch = new ArrayList<>();

        for (long i = min; i <= max; i++) {
            currentBatch.add(String.valueOf(i));

            if (currentBatch.size() == batchSize) {
                batchList.add(new ArrayList<>(currentBatch));
                currentBatch.clear();
            }
        }

        if (!currentBatch.isEmpty()) {
            batchList.add(currentBatch);
        }

        return batchList;
    }

    /**
     * Compute the SHA-256 digest of the input string and return its hex representation.
     *
     * @param input input string
     * @return hex-encoded SHA-256 digest
     * @throws RuntimeException if the SHA-256 algorithm is unavailable
     */
    public static String getSha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return convertBytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a byte array to a lowercase hexadecimal string.
     *
     * @param hash byte array to convert
     * @return hex string
     */
    private static String convertBytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Generate a normalized pseudo-random vector of length d with values in [0, 1).
     *
     * Note: randomness is based on a shared Random instance.
     *
     * @param d dimension of the vector
     * @return normalized vector
     */
    public static List<Double> genPseudoVector(int d) {
        List<Double> vector = new ArrayList<>(d);
        for (int i = 0; i < d; i++) {
            vector.add(random.nextDouble());
        }
        return normalize(vector);
    }

    /**
     * Normalize the vector to unit L2 norm.
     * If the vector is null/empty or the norm is near zero, return the original vector.
     *
     * @param vector input vector
     * @return normalized vector or original when normalization is not applicable
     */
    private static List<Double> normalize(List<Double> vector) {
        if (vector == null || vector.isEmpty()) return vector;
        double norm = 0.0;
        for (Double v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);

        if (norm < 1e-10) return vector;

        List<Double> normalized = new ArrayList<>(vector.size());
        for (Double v : vector) {
            normalized.add(v / norm);
        }
        return normalized;
    }

    /**
     * Convert a string to a number using the provided function.
     * Return the default value when the input is blank or parsing fails.
     *
     * @param s string to convert
     * @param function converter function (e.g., Integer::valueOf)
     * @param defaultVal default value when input is blank or cannot be parsed
     * @param <T> target number type
     * @return converted value or the default value
     */
    public static <T> T convertString2Number(String s, Function<String, T> function, T defaultVal) {
        if (isBlankString(s)) {
            return defaultVal;
        }
        try {
            return function.apply(s);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
