package hello.world.button.Utils;

import java.util.regex.Pattern;

public class Regex {
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    public static final String REGEX_Area_code_telephone ="^[0][1-9]{2,3}[0-9]{5,10}$";
    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
    //校验固定电话
    public static boolean isArea_code_telephone(String mobile) {
        return Pattern.matches(REGEX_Area_code_telephone, mobile);
    }
}
