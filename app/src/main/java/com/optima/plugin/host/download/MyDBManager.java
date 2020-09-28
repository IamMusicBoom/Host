package com.optima.plugin.host.download;

import com.optima.plugin.host.download.module.PluginInfoModule;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.pluginUtils.P_Context;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * create by wma
 * on 2020/9/28 0028
 */
public class MyDBManager {
    String TAG = MyDBManager.class.getSimpleName();
    DbManager db;
    DbManager.DaoConfig daoConfig;

    public MyDBManager() {

        daoConfig = new DbManager.DaoConfig()
                .setDbName("wma.db")
//                .setDbDir(P_Context.getContext().getExternalFilesDir("databases"))
                .setDbVersion(2)
                .setAllowTransaction(true)
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Logger.d(TAG, "onTableCreated: ");
                    }
                })
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) throws DbException {
                        Logger.d(TAG, "onDbOpened: ");
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) throws DbException {
                        Logger.d(TAG, "onUpgrade: oldVersion = " + oldVersion + " newVersion = " + newVersion);
                        db.addColumn(PluginInfoModule.class, "id2");
                    }
                });
        try {
            db = x.getDb(daoConfig);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public DbManager getDb() {
        return db;
    }

    public void setDb(DbManager db) {
        this.db = db;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    public void setDaoConfig(DbManager.DaoConfig daoConfig) {
        this.daoConfig = daoConfig;
    }
}
