package com.onscreen.watcher.utils.whiletrue;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HdmiHelper {

    public HdmiHelper(Context context) {
        init(context);
    }

    public void init(Context context) {

        try {

            //Interface Callback Proxy
            Class<?> hotplugEventListenerClass = Class.forName("android.hardware.hdmi.HdmiControlManager$HotplugEventListener");
            Class<?> vendorCommandListenerClass = Class.forName("android.hardware.hdmi.HdmiControlManager$VendorCommandListener");
            Class<?> oneTouchPlayCallbackClass = Class.forName("android.hardware.hdmi.HdmiPlaybackClient$OneTouchPlayCallback");
            Class<?> displayStatusCallbackClass = Class.forName("android.hardware.hdmi.HdmiPlaybackClient$DisplayStatusCallback");

            Object interfaceOneTouchPlaybackCallback = Proxy.newProxyInstance(oneTouchPlayCallbackClass.getClassLoader(),
                    new Class<?>[]{oneTouchPlayCallbackClass}, new callbackProxyListener());

            Object interfaceHotplugEventCallback = Proxy.newProxyInstance(hotplugEventListenerClass.getClassLoader(),
                    new Class<?>[]{hotplugEventListenerClass}, new callbackProxyListener());

            Object interfaceDisplayStatusCallbackClass = Proxy.newProxyInstance(displayStatusCallbackClass.getClassLoader(),
                    new Class<?>[]{displayStatusCallbackClass}, new callbackProxyListener());


            Method m = context.getClass().getMethod("getSystemService", String.class);
            Object obj_HdmiControlManager = m.invoke(context, (Object) "hdmi_control");

            Log.d("HdmiHelper", "obj: " + obj_HdmiControlManager + " | " + obj_HdmiControlManager.getClass());

            for (Method method : obj_HdmiControlManager.getClass().getMethods()) {
                Log.d("HdmiHelper", "   method: " + method.getName());
            }


            Method method_addHotplugEventListener = obj_HdmiControlManager.getClass().getMethod("addHotplugEventListener", hotplugEventListenerClass);
            method_addHotplugEventListener.invoke(obj_HdmiControlManager, interfaceHotplugEventCallback);


            Method m2 = obj_HdmiControlManager.getClass().getMethod("getPlaybackClient");
            Object obj_HdmiPlaybackClient = m2.invoke(obj_HdmiControlManager);
            Log.d("HdmiHelper", "obj_HdmiPlaybackClient: " + obj_HdmiPlaybackClient + " | " + obj_HdmiPlaybackClient.getClass());

            Method method_oneTouchPlay = obj_HdmiPlaybackClient.getClass().getMethod("oneTouchPlay", oneTouchPlayCallbackClass);
            method_oneTouchPlay.invoke(obj_HdmiPlaybackClient, interfaceOneTouchPlaybackCallback);


            Method method_queryDisplayStatus = obj_HdmiPlaybackClient.getClass().getMethod("queryDisplayStatus", displayStatusCallbackClass);

            method_queryDisplayStatus.invoke(obj_HdmiPlaybackClient, interfaceDisplayStatusCallbackClass);

            Method method_getActiveSource = obj_HdmiPlaybackClient.getClass().getMethod("getActiveSource");
            Log.d("HdmiHelper", "getActiveSource: " + method_getActiveSource.invoke(obj_HdmiPlaybackClient));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class callbackProxyListener implements java.lang.reflect.InvocationHandler {

        public callbackProxyListener() {

        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            try {
                Log.d("HdmiHelper", "Start method " + method.getName() + " | " + proxy.getClass() + " | " + method.getDeclaringClass());

                if (args != null) {

                    // Prints the method being invoked
                    for (int i = 0; i != args.length; i++) {
                        Log.d("HdmiHelper", "  - Arg(" + i + "): " + args[i].toString());
                    }

                }

                if (method.getName().equals("onReceived")) {

                    if (args.length == 1) {
                        onReceived(args[0]);
                    }/* else if (args.length == 3) {
                        onReceived((int) args[0], BytesUtil.toByteArray(args[1]), (boolean) args[2]);
                    }*/


                } else if (method.getName().equals("onComplete")) {
                    onComplete((int) args[0]);
                } else if (method.getName().equals("toString")) {
                    return this.toString();
                } else {
                    return method.invoke(this, args);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        void onComplete(int result) {
            Log.d("HdmiHelper", "onComplete: " + result);
        }

        void onReceived(Object event) {

            Class eventClass = event.getClass();

            Log.d("HdmiHelper", "onReceived(1): " + event.toString() + " | " + eventClass);

            try {
                Method method_getPort = eventClass.getMethod("getPort");
                Method method_isConnected = eventClass.getMethod("isConnected");
                Method method_describeContents = eventClass.getMethod("describeContents");

                Log.d("HdmiHelper", "    - " + method_getPort.invoke(event) + " | " + method_isConnected.invoke(event) + " | " + method_describeContents.invoke(event));

            } catch (Exception e) {

                e.printStackTrace();

            }


        }


        void onReceived(int srcAddress, byte[] params, boolean hasVendorId) {
            Log.d("HdmiHelper", "onReceived(3): " + srcAddress + " | " + params + " | " + hasVendorId);
        }


    }
}
