package io.nekohasekai.fe

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.meowcat.notaichi.NoTaiChi

class XposedInit : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            XposedHelpers.findAndHookMethod(Application::class.java, "onCreate", object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        NoTaiChi.checkTC(param)
                    }
                })
        }

        XposedBridge.hookAllMethods(SensorManager::class.java,"registerListener", object : XC_MethodHook() {

            override fun beforeHookedMethod(param: MethodHookParam) {

                if (param.args[1] is Sensor) {

                    val sensor = param.args[1] as Sensor

                    if (sensor.type in arrayOf(
                            Sensor.TYPE_ALL,
                            Sensor.TYPE_ACCELEROMETER,
                            Sensor.TYPE_ACCELEROMETER_UNCALIBRATED,
                            Sensor.TYPE_GYROSCOPE
                        )) {

                        param.result = true

                    }

                } else if (param.args[1] is Int) {

                    val sensors = param.args[1] as Int

                    if (sensors and SensorManager.SENSOR_ACCELEROMETER != 0) {

                        param.result = true

                    }

                }

            }

        })

    }

}