package com.github.yedp.ez.common.util;


import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * @author yedp
 * date 2021-01-30 17:30:07
 *
 **/
public class StringUtils {
    private static final String CHARSET = "UTF-8";
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    //表情包表正则达式
    public static final Pattern EMOJI_PATTERN = Pattern.compile("[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);


    /**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is
     * not empty and not null and not whitespace
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * <p>Checks if a CharSequence array has a element is empty ("") or null.</p>
     *
     * @param sss the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty
     */
    public static boolean isNotBlankAll(String... sss) {
        for (String s : sss) {
            if (s == null || isBlank(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * <p>Checks if a CharSequence array has a element is empty ("") or null.</p>
     *
     * @param sss the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty
     */
    public static boolean isNotEmptyAll(String... sss) {
        for (String s : sss) {
            if (s == null || isEmpty(s)) {
                return false;
            }
        }
        return true;
    }


    /**
     * <p>Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters.</p>
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * <pre>
     * StringUtils.equals(null, null)   = true
     * StringUtils.equals(null, "abc")  = false
     * StringUtils.equals("abc", null)  = false
     * StringUtils.equals("abc", "abc") = true
     * StringUtils.equals("abc", "ABC") = false
     * </pre>
     *
     * @param cs1 the first CharSequence, may be {@code null}
     * @param cs2 the second CharSequence, may be {@code null}
     * @return {@code true} if the CharSequences are equal (case-sensitive), or both {@code null}
     * @see Object#equals(Object)
     * @since 3.0 Changed signature from equals(String, String) to equals(CharSequence, CharSequence)
     */
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        return regionMatches(cs1, false, 0, cs2, 0, Math.max(cs1.length(), cs2.length()));
    }

    /**
     * Green implementation of regionMatches.
     *
     * @param cs         the {@code CharSequence} to be processed
     * @param ignoreCase whether or not to be case insensitive
     * @param thisStart  the index to start on the {@code cs} CharSequence
     * @param substring  the {@code CharSequence} to be looked for
     * @param start      the index to start on the {@code substring} CharSequence
     * @param length     character length of the region
     * @return whether the region matched
     */
    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        } else {
            int index1 = thisStart;
            int index2 = start;
            int tmpLen = length;

            while (tmpLen-- > 0) {
                char c1 = cs.charAt(index1++);
                char c2 = substring.charAt(index2++);

                if (c1 == c2) {
                    continue;
                }

                if (!ignoreCase) {
                    return false;
                }

                // The same check as in String.regionMatches():
                if (Character.toUpperCase(c1) != Character.toUpperCase(c2)
                        && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * String长度确保不超长
     *
     * @param str 字符串
     * @param len 长度
     * @return 字符串
     */
    public static String lengthEnsure(String str, int len) {
        if (isEmpty(str) || len < 0) {
            return str;
        }
        return str.length() <= len ? str : str.substring(0, len);
    }



    public static byte[] strToBytes(String data) throws UnsupportedEncodingException {
        return strToBytes(data, CHARSET);
    }

    public static byte[] strToBytes(String data, String charset) throws UnsupportedEncodingException {
        return data.getBytes(charset);
    }

    public static String bytesToStr(byte[] bytes) throws UnsupportedEncodingException {
        return bytesToStr(bytes, CHARSET);
    }

    public static String bytesToStr(byte[] bytes, String charset) throws UnsupportedEncodingException {
        return new String(bytes, charset);
    }


    /**
     * <p>Checks if a CharSequence is has emoji.</p>
     * <pre>
     * StringUtils.hasEmoji("🍑")      = true
     * StringUtils.hasEmoji("🎂")        = true
     * StringUtils.hasEmoji("🧚‍♀")       = true
     * StringUtils.hasEmoji("bob")     = false
     * StringUtils.hasEmoji("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     */
    public static boolean hasEmoji(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        return EMOJI_PATTERN.matcher(cs).find();
    }

    /**
     * <p>remove all emoji from  String .</p>
     * <pre>
     * StringUtils.removeEmojis("A🍑")      = A
     * StringUtils.removeEmojis("A🎂B")        = AB
     * StringUtils.removeEmojis("🧚A‍♀")       = A
     * StringUtils.removeEmojis("bob")     = bob
     * StringUtils.removeEmojis("  bob  ") =   bob
     * </pre>
     *
     * @param str the String to be remove emojis
     * @return String without emoji
     */
    public static String removeEmojis(final String str) {
        if (isBlank(str)) {
            return str;
        }
        return EMOJI_PATTERN.matcher(str).replaceAll("").trim();
    }

}
