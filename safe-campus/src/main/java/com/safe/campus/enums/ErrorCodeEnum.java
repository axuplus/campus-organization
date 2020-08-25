package com.safe.campus.enums;

public enum ErrorCodeEnum {


    /**
     * Uac 10011041 error code enum.
     */
    GL99990100(9999100, "参数异常"),

    GL404(9999404, "访问路径不存在"),

    GL9999417(9999417, "操作失败"),

    GL99990101(9999101, "token is invalid"),

    GL99990401(9999401, "无访问权限"),

    UAC10011041(10011041, "token已过期,请重新登录"),

    PUB10000001(10000001, "文件上传失败"),
    PUB10000002(10000002, "插入失败"),
    PUB10000003(10000003, "删除失败"),
    PUB10000004(10000004, "文件不能为空"),
    PUB10000005(10000005, "文件类型不支持"),
    PUB10000006(10000006, "文件不能为空"),
    PUB10000007(10000007, "上传失败: 文件大小不能超过10M!"),
    PUB10000008(10000008, "文件名格式不正确, 请使用后缀名为.XLSX的文件"),
    PUB10000009(10000009, "身份证已存在,请确认后导入"),
    PUB10000010(10000010, "部门不存在,请新建后导入"),
    PUB10000011(10000011, "角色不存在,请新建后导入"),
    PUB10000012(10000012, "性别不合法,请确认后导入"),
    PUB10000013(10000013, "年级不存在,请新建后导入"),
    PUB10000014(10000014, "班级不存在,请新建后导入"),
    PUB10000015(10000015, "角色不能为空"),
    PUB10000016(10000016, "楼幢不能为空"),
    PUB10000017(10000017, "暂无权限操作"),
    PUB10000018(10000018, "状态有误,请确认后重新导入"),
    PUB10000019(10000019, "图片命名有误,请检查后重新导入"),
    PUB10000020(10000020, "图片没有对应教师,请确认后导入"),
    PUB10000021(10000021, "有重复学生,请确认后导入"),
    PUB10000022(10000022, "身份证号码不合法"),
    PUB10000023(10000023, "此床位已经有学生了,请检查后导入"),
    PUB10000024(10000024, "学生住校类型有误,请检查后导入"),
    PUB10000025(10000025, "学生年级不存在,请检查后导入"),
    PUB10000026(10000026, "学生班级不存在,请检查后导入"),
    PUB10000027(10000027, "图片没有对应学生,请确认后导入"),
    PUB10000028(10000028, "楼幢不存在"),
    PUB10000029(10000029, "楼层不存在"),
    PUB10000030(10000030, "宿舍不存在"),
    PUB10000031(10000031, "床位不存在"),


    SYS20000001(20000001, "登录失败"),
    SYS20000002(20000002, "账号不存在"),
    SYS20000003(20000003, "密码错误"),
    SYS20000004(20000004, "账号已存在"),
    SYS10011004(10011004, "账号已存在"),
    SYS10011005(10011005, "账号不存在"),
    SYS10011006(10011006, "密码错误");

    private int code;
    private String msg;

    /**
     * Msg string.
     *
     * @return the string
     */
    public String msg() {
        return msg;
    }

    /**
     * Code int.
     *
     * @return the int
     */
    public int code() {
        return code;
    }

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets enum.
     *
     * @param code the code
     * @return the enum
     */
    public static ErrorCodeEnum getEnum(int code) {
        for (ErrorCodeEnum ele : ErrorCodeEnum.values()) {
            if (ele.code() == code) {
                return ele;
            }
        }
        return null;
    }

}
