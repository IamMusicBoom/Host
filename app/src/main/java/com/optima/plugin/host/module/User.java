package com.optima.plugin.host.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * create by wma
 * on 2020/8/19 0019
 */
public class User implements Parcelable {

    private String name;
    private int age;


    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    //从序列化后的对象中创建原始对象
    protected User(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        //从序列化后的对象中创建原始对象
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        //指定长度的原始对象数组
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //返回当前对象的内容描述。如果含有文件描述符，返回1，否则返回0，几乎所有情况都返回0
    @Override
    public int describeContents() {
        return 0;
    }

    //将当前对象写入序列化结构中，其flags标识有两种（1|0）。
    //为1时标识当前对象需要作为返回值返回，不能立即释放资源，几乎所有情况下都为0.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }


    public void readFromParcel(Parcel in){
        this.name = in.readString();
        this.age = in.readInt();
    }

}
