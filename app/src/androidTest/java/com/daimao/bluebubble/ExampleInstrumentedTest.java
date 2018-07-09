package com.daimao.bluebubble;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.daimao.bluebubble.data.model.AccountEntity;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.droidlover.xdroidmvp.kit.Codec;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.daimao.bluebubble", appContext.getPackageName());
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
