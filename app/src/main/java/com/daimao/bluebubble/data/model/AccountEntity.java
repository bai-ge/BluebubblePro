package com.daimao.bluebubble.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by baige on 2018/6/22.
 */

public class AccountEntity implements Parcelable {
    int id;
    boolean isLock;
    String logoPath;
    String title;
    String account;
    String password;
    String groupName;
    String url;
    long creatTime;
    String remark;


    public AccountEntity() {

    }

    public AccountEntity(Parcel in) {
        id = in.readInt();
        isLock = in.readByte() != 0;
        logoPath = in.readString();
        title = in.readString();
        account = in.readString();
        password = in.readString();
        groupName = in.readString();
        url = in.readString();
        creatTime = in.readLong();
        remark = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (isLock ? 1 : 0));
        dest.writeString(logoPath);
        dest.writeString(title);
        dest.writeString(account);
        dest.writeString(password);
        dest.writeString(groupName);
        dest.writeString(url);
        dest.writeLong(creatTime);
        dest.writeString(remark);
    }

    public static final Creator<AccountEntity> CREATOR = new Creator<AccountEntity>() {

        @Override
        public AccountEntity createFromParcel(Parcel in) {
            return new AccountEntity(in);
        }

        @Override
        public AccountEntity[] newArray(int size) {

            return new AccountEntity[size];
        }
    };

    public static AccountEntity newFromQRCode(String qrcode) {
        AccountEntity accountEntity = null;
        Gson gson = new Gson();
        Account account = gson.fromJson(qrcode, Account.class);
        if (account != null) {
            accountEntity = account.toAccountEntity();
        }
        return accountEntity;
    }
    //get and set

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", isLock=" + isLock +
                ", logoPath='" + logoPath + '\'' +
                ", title='" + title + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", groupName='" + groupName + '\'' +
                ", url='" + url + '\'' +
                ", creatTime=" + creatTime +
                ", remark='" + remark + '\'' +
                '}';
    }

    public static class Account {
        String T;
        String A;
        String P;
        String U;
        String G;
        String R;

        public AccountEntity toAccountEntity() {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setTitle(getT());
            accountEntity.setAccount(getA());
            accountEntity.setPassword(getP());
            accountEntity.setUrl(getU());
            accountEntity.setGroupName(getG());
            accountEntity.setRemark(getR());
            return accountEntity;
        }

        public String getT() {
            return T;
        }

        public void setT(String t) {
            T = t;
        }

        public String getA() {
            return A;
        }

        public void setA(String a) {
            A = a;
        }

        public String getP() {
            return P;
        }

        public void setP(String p) {
            P = p;
        }

        public String getU() {
            return U;
        }

        public void setU(String u) {
            U = u;
        }

        public String getG() {
            return G;
        }

        public void setG(String g) {
            G = g;
        }

        public String getR() {
            return R;
        }

        public void setR(String r) {
            R = r;
        }
    }
}
