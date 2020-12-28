package com.sms.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 数据加解密
 */
public class Encrypt {

    private static final String DES_ALGORITHM = "DES/CBC/PKCS5Padding";
    private static final String DES_KEY = "zjxxt861";
    private static final String DES_IV = "86159637";

    /**
     * 加密 String 明文输入 ,String 密文输出
     */
    public static String encode(String strMing) {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        BASE64Encoder base64En = new BASE64Encoder();
        try {
            byteMing = strMing.getBytes("UTF-8");
            byteMi = Encrypt.encryptByte(byteMing);
            strMi = base64En.encode(byteMi);
            strMi = strMi.replaceAll("\r", "");
            strMi = strMi.replaceAll("\n", "");
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            base64En = null;
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    /**
     * 解密 以 String 密文输入 ,String 明文输出
     *
     * @param strMi
     * @return
     */
    public static String decode(String strMi) {
        BASE64Decoder base64De = new BASE64Decoder();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try {
            byteMi = base64De.decodeBuffer(strMi);
            byteMing = Encrypt.decryptByte(byteMi);
            strMing = new String(byteMing, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            base64De = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }

    /**
     * 加密以 byte[] 明文输入 ,byte[] 密文输出
     *
     * @param byteS
     * @return
     */
    public static byte[] encryptByte(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            IvParameterSpec iv = new IvParameterSpec(DES_IV.getBytes());
            cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 解密以 byte[] 密文输入 , 以 byte[] 明文输出
     *
     * @param byteD
     * @return
     */
    public static byte[] decryptByte(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            DESKeySpec desKeySpec = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(DES_IV.getBytes());
            cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }

    public static void main(String[] args) {

//        SaSmsReqBean bean = new SaSmsReqBean();
//        bean.setAction("smsToPerson");
//        bean.setSchoolName("杭州实验学校11");
////		bean.setClassName("灵和班123");
//        bean.setStudentName("张加加");
//        bean.setPhone("17691073197");
//        bean.setContent("[测试短信]短信稽核测试个人1");
//        bean.setSenderName("个人发送");
//        bean.setSendTime("2018-7-10 22:07:30");
//        bean.setToken("9EA0B79BA9B73BD09BF539602C59E55050E14EB3ACE709BEDD5F4213");
//        bean.setSourceMobile("10657061845123456");
//
//		SaSmsReqBean tbean = new SaSmsReqBean();
//		tbean.setAction("smsToTeacher");
//		tbean.setSchoolName("杭州实验学校");
//		tbean.setTeacherName("张喜林");
//		tbean.setPhone("17691073197");
//		tbean.setContent("[测试短信]短信稽核测试，教师发送");
//		tbean.setSenderName("教师发送");
//		tbean.setToken("9EA0B79BA9B73BD09BF539602C59E55050E14EB3ACE709BEDD5F4213");
//		tbean.setSourceMobile("10657061845123456");
//
//        SaMultReqBean mbean = new SaMultReqBean();
//        mbean.setAction("multToPerson");
//        List<SaMultBean> list = new ArrayList<SaMultBean>();
//        for (int i = 0; i < 1; i++) {
//            SaMultBean bean1 = new SaMultBean();
//            bean1.setSchoolName("杭州实验学校");
//            bean1.setClassName("灵和班");
//            bean1.setStudentName("张加加");
//            bean1.setPhone("17691073197");
//            bean1.setContent("[测试短信]短信稽核测试，个人批量发送");
//            list.add(bean1);
//        }
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校1");
//			bean1.setClassName("灵和班");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("17691073197");
//			bean1.setContent("[测试短信]短信稽核测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("灵和班1");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("17691073197");
//			bean1.setContent("[测试短信]短信稽核测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("灵和班");
//			bean1.setStudentName("张加加1");
//			bean1.setPhone("17691073197");
//			bean1.setContent("[测试短信]短信稽核测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("灵和班");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("17691073198");
//			bean1.setContent("[测试短信]短信稽核测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("灵和班");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("17691073197");
//			bean1.setContent("[测试短信]短信稽核测试，个人批量发送法轮功");
//			list.add(bean1);
//		}
//        mbean.setMultList(list);
//        mbean.setSenderName("个人批量发送");
//        mbean.setSendTime("2018-5-24 23:07:32");
//        mbean.setToken("9EA0B79BA9B73BD09BF539602C59E55050E14EB3ACE709BEDD5F4213");
//        mbean.setSourceMobile("10657061845123456");
//
//		SaMultReqBean mtbean = new SaMultReqBean();
//		mtbean.setAction("multToTeacher");
//		List<SaMultBean> mtlist = new ArrayList<SaMultBean>();
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setTeacherName("张林林");
//			bean1.setPhone("17691073197");
//			bean1.setContent("[测试短信]短信稽核测试，教师批量发送");
//			mtlist.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校1");
//			bean1.setTeacherName("张林林");
//			bean1.setPhone("17691073197");
//			bean1.setContent("[测试短信]短信稽核测试，教师批量发送");
//			mtlist.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setTeacherName("张林林1");
//			bean1.setPhone("17691073197");
//			bean1.setContent("[测试短信]短信稽核测试，教师批量发送");
//			mtlist.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setTeacherName("张林林");
//			bean1.setPhone("17691073198");
//			bean1.setContent("[测试短信]短信稽核测试，教师批量发送");
//			mtlist.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setTeacherName("张林林");
//			bean1.setPhone("17691073197");
//			bean1.setContent("[测试短信]短信稽核测试，教师批量发送法轮功");
//			mtlist.add(bean1);
//		}
//		mtbean.setMultList(mtlist);
//		mtbean.setSenderName("教师批量发送");
//		mtbean.setSendTime("2018-5-24 23:07:33");
//		mtbean.setToken("B875552D4D6B1A326B2531A36C3FD2447386858D5F1C729CDD12B1D6");
//		mtbean.setSourceMobile("10657061859234567");

//		SaSmsReqBean bean = new SaSmsReqBean();
//		bean.setAction("smsToPerson");
//		bean.setSchoolName("杭州实验学校");
//		bean.setClassName("2013毕业班");
//		bean.setStudentName("张加加");
//		bean.setPhone("17691073197");
//		bean.setContent("[测试短信]万众物联短信稽核测试");
//		bean.setToken("01C830629437D9CE9E732CEB9BD441408A96172E499A24689AA386CB");
//		bean.setSourceMobile("10657061847123456");
//
//		SaMultReqBean mbean = new SaMultReqBean();
//		mbean.setAction("multToPerson");
//		List<SaMultBean> list = new ArrayList<SaMultBean>();
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("2013毕业班");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("18629193941");
//			bean1.setContent("[测试短信]短信稽核新服务器测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校2");
//			bean1.setClassName("2013毕业班");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("18629193941");
//			bean1.setContent("[测试短信]短信稽核新服务器测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("2013毕业班2");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("18629193941");
//			bean1.setContent("[测试短信]短信稽核新服务器测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("2013毕业班");
//			bean1.setStudentName("张加加2");
//			bean1.setPhone("18629193941");
//			bean1.setContent("[测试短信]短信稽核新服务器测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("2013毕业班");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("18629193942");
//			bean1.setContent("[测试短信]短信稽核新服务器测试，个人批量发送");
//			list.add(bean1);
//		}
//		for (int i = 0; i < 1; i++) {
//			SaMultBean bean1 = new SaMultBean();
//			bean1.setSchoolName("杭州实验学校");
//			bean1.setClassName("2013毕业班");
//			bean1.setStudentName("张加加");
//			bean1.setPhone("18629193941");
//			bean1.setContent("[测试短信]短信稽核新服务器测试，个人批量发送法轮功");
//			list.add(bean1);
//		}
//		mbean.setMultList(list);
//		mbean.setToken("9CB3009D932D5D99DB18A612F8A22619D385E066A181A87BB8394B0A");
//		mbean.setSourceMobile("10657061833123456");

        //System.out.println(Encrypt.encode(JSonUtil.wrapperToJsonObject(bean)));
//		System.out.println(Encrypt.encode(JSonUtil.wrapperToJsonObject(tbean)));
//        System.out.println(Encrypt.encode(JSonUtil.wrapperToJsonObject(mbean)));
////		System.out.println(Encrypt.encode(JSonUtil.wrapperToJsonObject(mtbean)));
//        System.out.println(Encrypt.encode("{\"result_code\":1,\"serialNo\":\"1498814892973\",\"result_msg\":\"稽核成功\"}"));
//
//        System.out.println("-------------------------------------------------");
//        System.out.println(Encrypt.decode("/9Y6tsHUHs48fyfVZb/3YoLqOjHZ2YNS3BO7fTunskuMLVvX4WEmkAB8BPgu/QE108oHGDPXJLVx058rVbwgMA=="));

//        SaMultReqBean mbean = new SaMultReqBean();
//        mbean.setAction("multToPerson");
//        List<SaMultBean> list = new ArrayList<SaMultBean>();
        //for (int i = 0; i < 1; i++) {
        //	SaMultBean bean1 = new SaMultBean();
        //	bean1.setSchoolName("杭州实验学校");
        //	bean1.setClassName("灵和班");
        //	bean1.setStudentName("张加加");
        //	bean1.setPhone("17691073197");
        //	bean1.setContent("[测试短信]短信稽核测试，个人批量发送");
        //	list.add(bean1);
        //}
//		mbean.setMultList(list);
//		mbean.setToken("9CB3009D932D5D99DB18A612F8A22619D385E066A181A87BB8394B0A");
//		mbean.setSourceMobile("10657061833123456");

        String ss = "{\"token\": \"MZJTHCAEAIHCYUDJ44GLORXTR1RAJ2F7SEPCLRH34DPPYUE6WC5ETRMS\",\"content\": \"杨立波给个人下发短信测试\",\"phone\": \"13991919119\",\"senderName\": \"钱丽英\",\"schoolName\": \"绍兴中小学智安校园\",\"studentName\": \"杨立波\",\"action\": \"smsToPerson\",\"className\": \"校园测试 \",\"sourceMobile\": \"1065706182212345678\"}";
        System.out.println(Encrypt.encode(ss));

        System.out.println("-------------------------------------------------");
        String s = "oHB+KhhZ6XJxEAzkDVLTdQ5WiqK2DKfiEJhNAAbI+IkivWbKWfxm4+vaKUSt/OtbJ7nlYrOJsGik\n" +
                "dhsrkYNDGuTXOU+jF9I9W6Ww9R5Rpb++poGxV5EI7ejonofkWWboq9Z+1Id+U1ZEiHv7OU5yV3dx\n" +
                "cRWaJu4wgvdUB7UtcJxNk8oeEAbA+/PjvdfMSRb1D3hhIwnTzdt2EcyVylLA/UQ/A5sGr72ijMkW\n" +
                "/mVPH1qXbdRWnKGJio8r1+Fj00zsqUhDOH9L7af6S41DiOw0TOYUxx/3hCNz+AnNLXmlONgDeLir\n" +
                "p64YXfNb4izKNHx6B3DwbLy5EoDESPIrUlGZX++9UT9NGuNBGrlyDv80L8p0XeDctPF1lL3slFdL\n" +
                "9onjDv011vuF6PlG6MEiajZlnA==";
        //System.out.println(Encrypt.decode("/9Y6tsHUHs48fyfVZb/3YoLqOjHZ2YNS3BO7fTunskuMLVvX4WEmkAB8BPgu/QE108oHGDPXJLVx058rVbwgMA=="));
        System.out.println("解密内容>>>>>>>"+Encrypt.decode(s));

//        SaSmsReqBean bean = new SaSmsReqBean();
//        bean.setAction("smsToPersonByStuName");
//        bean.setSchoolName("杭州实验学校");
//		bean.setClassName("巡检高一(8)班");
//        bean.setStudentName("沈璐");
//        bean.setContent("[测试短信]短信稽核测试个人1");
//        bean.setSenderName("个人发送");
//        bean.setSendTime("2020-12-23 11:07:30");
//        bean.setToken("UVTQVLHJZW6X6BOSHSZ0TQVL2MVQ60MQDLVLU5HSYRANU95312LVQ5FQ");
//        bean.setSourceMobile("10657061840123456");
//        System.out.println("smsToPersonByStuName>>>>>>>>>>"+Encrypt.encode(JSonUtil.wrapperToJsonObject(bean)));
    }


}
