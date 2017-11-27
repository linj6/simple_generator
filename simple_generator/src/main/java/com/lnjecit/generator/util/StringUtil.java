package com.lnjecit.generator.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 */
public final class StringUtil {

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if(str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串非空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 将字符串转换为驼峰命名格式
     * @param before 要转换的字符串
     * @param firstChar2Upper 首字母是否大写
     * @return
     */
    public static String transferToCamel(String before, boolean firstChar2Upper) {
        //不带"_"的字符串,则直接首字母大写后返回
        if (!before.contains("_"))
            return firstChar2Upper ? initCap(before) : before;
        String[] strs = before.split("_");
        StringBuffer after = null;
        if (firstChar2Upper) {
            after = new StringBuffer(initCap(strs[0]));
        } else {
            after = new StringBuffer(strs[0]);
        }
        for (int i=1; i< strs.length; i++) {
            after.append(initCap(strs[i]));
        }
        return after.toString();
    }

    private static String initCap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z')
            ch[0] = (char) (ch[0] - 32);
        return new String(ch);
    }

    /**
     * 字符串首字母小写
     * @param str
     * @return
     */
    public static String firstCharToLower(String str) {
        char[] chars = new char[1];
        chars[0] = str.charAt(0);
        String tempStr = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {//当为字母时，则转换为小写
            return str.replaceFirst(tempStr, new String(tempStr).toLowerCase());
        }
        return str;
    }
}
