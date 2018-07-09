package com.daimao.bluebubble.data.source.local;

import com.daimao.bluebubble.data.source.local.dbupdate.Upgrade;
import com.daimao.bluebubble.data.source.local.dbupdate.VersionCode;

import java.util.LinkedHashSet;
import java.util.Set;

public class VersionFactory {

    static Set<String> list = new LinkedHashSet<>();

    static {
//        list.add("com.daimao.bluebubble.data.source.local.dbupdate.VersionSecond");


    }

    /**
     * 得到当前数据库版本
     *
     * @return
     */
    public static int getCurrentDBVersion() {
        return list.size() + 1;
    }

    /**
     * 根据数据库版本号获取对象
     *
     * @param i
     * @return upgrade
     */
    public static Upgrade getUpgrade(int i) {
        Upgrade upgrade = null;
//	List<Class<?>> list = ClassUtil.getClasses("com.upsoft.ep.app.module.dbupdate");
        if (null != list && list.size() > 0) {
            try {
                for (String className : list) {
                    Class<?> cls = null;
                    cls = Class.forName(className);
                    if (Upgrade.class == cls.getSuperclass()) {
                        VersionCode versionCode = cls.getAnnotation(VersionCode.class);
                        if (null == versionCode) {
                            throw new IllegalStateException(cls.getName() + "类必须使用VersionCode类注解");
                        } else {
                            if (i == versionCode.value()) {
                                upgrade = (Upgrade) cls.newInstance();
                                break;
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new IllegalStateException("没有找到类名,请检查list里面添加的类名是否正确！");
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return upgrade;
    }

}
