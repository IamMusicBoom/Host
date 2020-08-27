// ILockScreenImpl.aidl
package com.optima.plugin.host;

// Declare any non-default types here with import statements

interface ILockScreenImpl {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

      void setUseLockScreen(String pluginName,int priority);
}
