package com.optima.plugin.host.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.optima.plugin.repluginlib.ICommonAidlInterface;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.module.User;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wma
 * on 2020/9/2 0002
 */
public class CommonAIDLService extends Service {
    String TAG = CommonAIDLService.class.getSimpleName();
    List<User> mUsers = new ArrayList<>();

    @Override
    public void onCreate() {
        mUsers.add(new User("user1", 11));
        mUsers.add(new User("user2", 12));
        mUsers.add(new User("user3", 13));
        mUsers.add(new User("user4", 14));
        mUsers.add(new User("user5", 15));
        Logger.d(TAG, "onCreate: " + mUsers.contains(mUsers.get(1)));
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CommonBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class CommonBinder extends ICommonAidlInterface.Stub{

        @Override
        public void say(String word) throws RemoteException {
            Logger.d(TAG, "say: word = " + word);
        }


        @Override
        public void setUser(User user, int position) throws RemoteException {
            mUsers.set(position, user);
            for (int i = 0; i < mUsers.size(); i++) {
                User user1 = mUsers.get(i);
                Logger.d(TAG, "setUser: " + user1.getName() + " " + user1.getAge());
            }
        }

        @Override
        public void addUser(User user) throws RemoteException {
            Logger.d(TAG, "addUser: 是否包含" + mUsers.contains(user));
            if (!mUsers.contains(user)) {
                mUsers.add(user);
                for (int i = 0; i < mUsers.size(); i++) {
                    User user1 = mUsers.get(i);
                    Logger.d(TAG, "addUser: " + user1.getName() + " " + user1.getAge());
                }
            }
        }

        @Override
        public List<User> getUsers() throws RemoteException {
            return mUsers;
        }


        @Override
        public User getUser(int position) throws RemoteException {
            return mUsers.get(position);
        }
    }
}
