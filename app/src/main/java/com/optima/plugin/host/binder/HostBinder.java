package com.optima.plugin.host.binder;

import android.os.RemoteException;

import com.optima.plugin.host.IHostImpl;
import com.optima.plugin.host.module.User;
import com.optima.plugin.repluginlib.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wma
 * on 2020/8/27 0027
 */
public class HostBinder extends IHostImpl.Stub {
    User user;
    List<User> users = new ArrayList<>();
    final String TAG = HostBinder.class.getSimpleName();

    @Override
    public void say(String word) throws RemoteException {
        Logger.d(TAG, "say: word = " + word);
    }

    @Override
    public void setUser(User user, int postion) throws RemoteException {
        this.user = user;
        Logger.d(TAG, "setUser: name = " + user.getName() + " age = " + user.getAge());
        users.set(postion,user);
    }

    @Override
    public void addUser(User user) throws RemoteException {
        Logger.d(TAG, "setUser: name = " + user.getName() + " age = " + user.getAge());
        users.add(user);
    }

    @Override
    public List<User> getUsers() throws RemoteException {
        return users;
    }

    @Override
    public User getUser(int position) throws RemoteException {
        return user;
    }
}
