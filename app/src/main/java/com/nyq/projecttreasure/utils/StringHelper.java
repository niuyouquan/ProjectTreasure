package com.nyq.projecttreasure.utils;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc:(字符串帮助类)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:16/9/8 16:35
 */
public class StringHelper {


    static boolean flag = false;
    static String regex = "";


    private StringHelper() {
        //私有化构造方法 隐藏对象
    }

    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     * @return 判断后的字符串
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }

    /**
     * 过滤HTML标签
     *
     * @param html
     * @return
     */
    public static String dealHtml(String html) {
        Pattern pt = Pattern.compile("<[^>]*>");
        String content = html.replaceAll(pt.pattern(), "");
        String result = content;

        if (content.indexOf("&ldquo;") > -1) {
            result = content.replaceAll("&ldquo;", "“");
        }
        if (content.indexOf("&rdquo;") > -1) {
            result = content.replaceAll("&rdquo;", "”");
        }

        if (content.indexOf("&lsquo;") > -1) {
            result = content.replaceAll("&lsquo;", "’");
        }
        if (content.indexOf("&rsquo;") > -1) {
            result = content.replaceAll("&rsquo;", "‘");
        }

        if (content.indexOf("&sbquo;") > -1) {
            result = content.replaceAll("&sbquo;", "，");
        }

        if (content.indexOf("&quot;") > -1) {
            result = content.replaceAll("&quot;", "\"");
        }
        if (content.indexOf("&amp;") > -1) {
            result = content.replaceAll("&amp;", "&");
        }
        if (content.indexOf("&lt;") > -1) {
            result = content.replaceAll("&lt;", "<");
        }
        if (content.indexOf("&gt;") > -1) {
            result = content.replaceAll("&gt;", ">");
        }
        if (content.indexOf("&nbsp;") > -1) {
            result = content.replaceAll("&nbsp;", "");
        }
        return result;
    }

    /**
     * 处理正文中的特殊字符
     *
     * @param str
     * @return
     */
    public static String dealSpecial(String str) {
        char[] charArr = str.toCharArray();
        int[] pos = new int[100];
        for (int i = 0; i < pos.length; i++)
            pos[i] = -1;
        int j = 0;
        for (int i = 0; i < charArr.length; i++) {
            if ((int) charArr[i] == 10 || (int) charArr[i] == 13) {
                pos[j++] = i;
            }
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < pos.length; i++) {
            if (pos[i] != -1) {
                sb.insert(pos[i] + i * (5 - 1), "<br/>");
                sb.replace(pos[i] + i * (5 - 1) + 5, pos[i] + i * (5 - 1) + 5
                        + 1, "");
            }
        }
        return sb.toString();
    }

    /**
     * 根据传入的正则表达式数组里的内容，判断传入的字符串是否符合正则表达式的要求
     *
     * @param checkedStr -- 传入的要被检查的字符串
     * @param regStr     -- 要匹配的正则表达式数组
     * @return -- 如果符合正则表达式的要求，返回true;如果不符合正则表达式的要求，返回false
     */
    public static Boolean ifMeetRequire(final String checkedStr,
                                        final String[] regStr) {
        Boolean res = false;
        for (String s : regStr) {
            if (checkedStr.matches(s)) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * @param obj
     * @return
     */
    public static String convertToString(Object obj) {
        if (obj == null)
            return "";
        String str = obj.toString().trim();
        if ("null".equals(str) || "NULL".equals(str))
            return "";
        return str;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 方法描述 : 电信手机号码校验
     * 133、149、153、173、177、180、181、189、199
     *
     * @param phone
     * @return
     */
    public static boolean phoneCheck(String phone) {
        String regxpForHtml = "^((1[35]3)|(149)|(17[37])|(18[019])|(199]))\\d{8}$"; // 电信手机校验
        Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }

    public static boolean check(String str, String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
            LogUtil.info("StringHelper", e);
        }
        return flag;
    }

    /**
     * 验证手机号码
     * 匹配所有的手机号，不区分哪个运营商，不考虑卫星通信、物联网等特殊号段
     * 移动号码段:134 【0-8】、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、 198 【0-9】
     * 联通号码段:130、131、132、145、155、156、175 、176、185、186、 166 【0-9】
     * 电信号码段:133、149、153、173、177、180、181、189、199 【0-9】
     *
     * @param cellphone
     * @return
     */
    public static boolean checkCellphone(String cellphone) {
        String regex = "^((134[0-8]\\d{7})|((13[^4])|(14[5-9])|(15[^4])|(16[6])|(17[0-8])|(18[0-9])|(19[8,9]))\\d{8})$";
        return check(cellphone, regex);
    }

    /**
     * 固话号码，支持400 或 800开头
     *
     * @param telephone
     * @return
     */
    public static boolean checkTelephone(String telephone) {
        String regex = "^(400|800)([0-9\\\\-]{7,10})|(([0-9]{4}|[0-9]{3})(-| )?)?([0-9]{7,8})((-| |转)*([0-9]{1,4}))?$";
        return check(telephone, regex);
    }



    /**
     *要求：
     密码长度最少8位
     大写字母，小写字母，数字，特殊符号必须四选三
     大写+小写，大写+数字，大写+特殊符号，小写+数字，小写+特殊符号，数字+特殊符号
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        String regex = "^(?=.*[a-zA-Z0-9].*)(?=.*[a-zA-Z\\\\W].*)(?=.*[0-9\\\\W].*).{6,24}$";
        return check(password, regex);
    }
    /**
     * 残疾证号校验
     *
     * @param disabilityertificateNo
     * @return
     */
    public static boolean disabilityertificateNo(String disabilityertificateNo) {
        String regex = "^[0-9]{17}[0-9xX][1-7][1-4]([B|b][1-8]){0,1}$";
        return check(disabilityertificateNo, regex);
    }

    /**
     * 验证就诊卡号6-24位的数字
     *
     * @param visitCard
     * @return
     */
    public static boolean checkVisitCard(String visitCard) {
        String regex = "^[0-9]{6,24}$";
        return check(visitCard, regex);
    }

    /**
     * @param str
     * @return
     * @Title: ignoreNullStr
     * @Description: DESC(传来的值忽略空值)
     */
    public static String ignoreNullStr(String str) {
        if (isNullOrEmpty(str)) {
            return "--";
        } else {
            return str;
        }
    }

    /**
     * DESC(将字符串转化为字符串数组)
     *
     * @param tempStr
     * @return
     */
    public static String[] convertStrToArray(String tempStr) {
        String[] temp = null;
        if (isNotBlank(tempStr)) {
            temp = tempStr.split(",");
        }
        return temp;
    }

    /**
     * DESC(判断连个字符串是否相等)
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isEquals(String str1, String str2) {
        return str1.equals(str2);
    }

    /**
     * 实际替换动作
     *
     * @param username username
     * @param regular  正则
     * @return
     */
    private static String replaceAction(String username, String regular) {
        return username.replaceAll(regular, "*");
    }

    /**
     * 身份证号替换，保留前四位和后四位
     * <p>
     * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param idCard 身份证号
     * @return
     */
    public static String idCardReplaceWithStar(String idCard) {

        if (idCard.isEmpty() || idCard == null) {
            return "";
        } else {
            return replaceAction(idCard, "(?<=\\d{4})\\d(?=\\d{4})");
        }
    }

    /**
     * 身份证号替换，保留前14位
     * <p>
     * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param idCard 身份证号
     * @return
     */
    public static String idCardReplaceWithStarLaster4(String idCard) {

        if ( idCard == null ||idCard.isEmpty()) {
            return "";
        } else {
            if(idCard.length()>4){
                return idCard.substring(0,idCard.length()-4)+"****";
            }else {
                return "";
            }

        }
    }

    /**
     * 手机号替换，保留前3位和后四位
     * <p>
     * 如果手机号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param phone 手机号
     * @return
     */
    public static String phoneReplaceWithStar(String phone) {

        if (phone.isEmpty() || phone == null) {
            return "";
        } else {
            return replaceAction(phone, "(?<=\\d{3})\\d(?=\\d{4})");
        }
    }

    /**
     * 银行卡替换，保留后四位
     * <p>
     * 如果银行卡号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param bankCard 银行卡号
     * @return
     */
    public static String bankCardReplaceWithStar(String bankCard) {

        if (bankCard.isEmpty() || bankCard == null) {
            return null;
        } else {
            return replaceAction(bankCard, "(?<=\\d{0})\\d(?=\\d{4})");
        }
    }

    /**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     *
     * @param userName 用户名
     * @return 替换后的用户名
     */
    public static String userNameReplaceWithStar(String userName) {
        String userNameAfterReplaced = "";

        if (userName == null) {
            userName = "";
        }

        int nameLength = userName.length();

        if (nameLength <= 1) {
            userNameAfterReplaced = "*";
        } else if (nameLength == 2) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\S{0})\\S(?=\\S{1})");
        } else if (nameLength >= 3 && nameLength <= 6) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\S{1})\\S(?=\\S{1})");
        } else if (nameLength == 7) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\S{1})\\S(?=\\S{2})");
        } else if (nameLength == 8) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\S{2})\\S(?=\\S{2})");
        } else if (nameLength == 9) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\S{2})\\S(?=\\S{3})");
        } else if (nameLength == 10) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\S{3})\\S(?=\\S{3})");
        } else if (nameLength >= 11) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\S{3})\\S(?=\\S{4})");
        }

        return userNameAfterReplaced;

    }

    public static String getSexString(String sex) {
        String sexString = "";
        if ("1".equals(sex)) {
            sexString = "男";
        } else {
            sexString = "女";
        }
        return sexString;
    }

    public static String getSexInt(String sex) {
        String sexInt = "";
        if ("男".equals(sex)) {
            sexInt = "1";
        } else {
            sexInt = "2";
        }
        return sexInt;
    }

    public static String strBlankReplace(String old, String reStr) {
        if (isNotBlank(old)) {
            return old;
        } else {
            return reStr;
        }

    }

    //去除换行和空格
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 正则校验中国人的姓名 中间可以包含.
     *
     * @param userName
     * @return
     */
    public static boolean checkChinaName(String userName) {
        String regex = "^[\\u4e00-\\u9fa5]+([·|•][\\u4e00-\\u9fa5]+)*$";
        return check(userName, regex);
    }

    /**
     * 对double数据进行取精度.
     * <p>
     * For example: <br>
     * double value = 100.345678; <br>
     * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br>
     * ret为100.3457 <br>
     *
     * @param value        double数据.
     * @param scale        精度位数(保留的小数位数).
     * @param roundingMode BigDecimal.ROUND_UP BigDecimal.ROUND_DOWN BigDecimal.ROUND_CEILING BigDecimal.ROUND_FLOOR
     *                     BigDecimal.ROUND_HALF_UP BigDecimal.ROUND_HALF_DOWN BigDecimal.ROUND_HALF_EVEN
     *                     精度取值方式.
     * @return 精度计算后的数据.
     */
    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

}
