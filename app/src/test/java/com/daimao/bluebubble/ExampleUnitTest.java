package com.daimao.bluebubble;

import com.daimao.bluebubble.data.model.AccountEntity;
import com.google.gson.Gson;

import org.junit.Test;

import cn.droidlover.xdroidmvp.kit.Codec;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
        AccountEntity.Account account = new AccountEntity.Account();
        account.setT("标题");
        account.setA("账号");
        account.setP("密码");
        account.setU("网址");
        account.setG("分组");
        account.setR("备注");
        Gson gson = new Gson();
//        gson.toJson(account);
        System.out.println(gson.toJson(account));
    }

    @Test
    public void des() {
        Gson gson = new Gson();
        AccountEntity.Account account = new AccountEntity.Account();
        account.setT("标题");
        account.setA("账号");
        account.setP("密码");
        account.setU("网址");
        account.setG("分组");
        account.setR("备注");
        String text = gson.toJson(account);
        try {
            String key = Codec.DES.initKey();
            System.out.println("key = "+ key);

            System.out.println("加密前："+text);

            byte[] buf = Codec.DES.encrypt(text.getBytes(), key);

            String data = Codec.BASE64.encodeToString(buf);
            System.out.println("加密后："+data);

            buf = Codec.DES.decrypt(buf, key);

            String deStr = new String(buf, "utf-8");
            System.out.println("解密后："+deStr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}