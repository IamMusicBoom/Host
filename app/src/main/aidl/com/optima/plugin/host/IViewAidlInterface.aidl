// IViewAidlInterface.aidl
package com.optima.plugin.host;

// Declare any non-default types here with import statements

interface IViewAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
void onClick(int resId);
void onLongClick(int resId);
void setText(int resId,String text);
void startAnim(int resId);

}
