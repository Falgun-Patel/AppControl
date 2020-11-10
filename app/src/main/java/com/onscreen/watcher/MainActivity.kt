package com.onscreen.watcher

import UsbCecConnectionn
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import com.onscreen.watcher.base.BaseActivity
import com.onscreen.watcher.utils.hdmi.UsbService
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class MainActivity : BaseActivity() {
    lateinit var usbManager: UsbManager
    lateinit var device: UsbDevice
    lateinit var usbService: UsbService
    private val MSG_START = 255.toByte()
    private val MSG_END = 254.toByte()

    var deviceManger: DevicePolicyManager? = null
    var compName: ComponentName? = null
    var RESULT_ENABLE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()


        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
//        findSerialPortDevice()


        btn_tvconnect.setOnClickListener {


            val process =
                Runtime.getRuntime().exec("chmod 222 /sys/class/cec/cmd")
            printResults(process)
            Log.e("xxxxx", "xxxxxxxxxx")
        }

        btn_tvconnectOff.setOnClickListener{

//            val process =
//                Runtime.getRuntime().exec("ls -ll /sys/class/cec/cmd")
//            printResults(process)
//            Log.e("xxxxx", "xxxxxxxxxx")
        }

//        btn_finddevice.setOnClickListener{
//            val process =
//                Runtime.getRuntime().exec("install /sdcard/dd.apk")
//            printResults(process)
//            Log.e("xxxxx", "xxxxxxxxxx")
//        }

    }


    fun switchTvOn() {
        sendCommand(MsgCode.MSGCODE_SET_CONTROLLED, 1)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_ACK_POLARITY, 0)
        sendCommand(MsgCode.MSGCODE_TRANSMIT, 16)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_EOM, 4)
    }

    fun switchTvOff() {
        sendCommand(MsgCode.MSGCODE_SET_CONTROLLED, 1)
//        sendCommand(MsgCode.MSGCODE_TRANSMIT_ACK_POLARITY, 0)
        sendCommand(MsgCode.MSGCODE_TRANSMIT, 16)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_EOM, 54)
    }

    private fun sendCommand(code: MsgCode, param: Int) {
        sendCommand(code, byteArrayOf(param.toByte()))
    }

    private fun sendCommand(code: MsgCode, params: ByteArray = ByteArray(0)) {
        Log.i("LOG_TAG", "sendCommand: " + code + ", " + params.contentToString())
        sendCommand(code.ordinal.toByte(), params)
    }

    private fun sendCommand(command: Byte, params: ByteArray) {
        Log.d("LOG_TAG", "sendCommand: " + command + ", " + params.contentToString())
        val data = ByteArray(3 + params.size)
        data[0] = MSG_START
        data[1] = command
        System.arraycopy(params, 0, data, 2, params.size)
        data[data.size - 1] = MSG_END

        if (usbService != null) { // if UsbService was correctly binded, Send data
            Toast.makeText(this, "parth command write", Toast.LENGTH_SHORT).show()
            usbService.write(data)
        }
    }

    private enum class MsgCode {
        MSGCODE_NOTHING, MSGCODE_PING, MSGCODE_TIMEOUT_ERROR, MSGCODE_HIGH_ERROR, MSGCODE_LOW_ERROR,
        MSGCODE_FRAME_START, MSGCODE_FRAME_DATA, MSGCODE_RECEIVE_FAILED, MSGCODE_COMMAND_ACCEPTED,
        MSGCODE_COMMAND_REJECTED, MSGCODE_SET_ACK_MASK, MSGCODE_TRANSMIT, MSGCODE_TRANSMIT_EOM,
        MSGCODE_TRANSMIT_IDLETIME, MSGCODE_TRANSMIT_ACK_POLARITY, MSGCODE_TRANSMIT_LINE_TIMEOUT,
        MSGCODE_TRANSMIT_SUCCEEDED, MSGCODE_TRANSMIT_FAILED_LINE, MSGCODE_TRANSMIT_FAILED_ACK,
        MSGCODE_TRANSMIT_FAILED_TIMEOUT_DATA, MSGCODE_TRANSMIT_FAILED_TIMEOUT_LINE,
        MSGCODE_FIRMWARE_VERSION, MSGCODE_START_BOOTLOADER, MSGCODE_GET_BUILDDATE,
        MSGCODE_SET_CONTROLLED, MSGCODE_GET_AUTO_ENABLED, MSGCODE_SET_AUTO_ENABLED,
        MSGCODE_GET_DEFAULT_LOGICAL_ADDRESS, MSGCODE_SET_DEFAULT_LOGICAL_ADDRESS,
        MSGCODE_GET_LOGICAL_ADDRESS_MASK, MSGCODE_SET_LOGICAL_ADDRESS_MASK,
        MSGCODE_GET_PHYSICAL_ADDRESS, MSGCODE_SET_PHYSICAL_ADDRESS, MSGCODE_GET_DEVICE_TYPE,
        MSGCODE_SET_DEVICE_TYPE, MSGCODE_GET_HDMI_VERSION, MSGCODE_SET_HDMI_VERSION,
        MSGCODE_GET_OSD_NAME, MSGCODE_SET_OSD_NAME, MSGCODE_WRITE_EEPROM, MSGCODE_GET_ADAPTER_TYPE,
        MSGCODE_SET_ACTIVE_SOURCE, MSGCODE_UNKNOWN
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (device != null) {
            var usbCecConnection = UsbCecConnectionn(device, usbManager.openDevice(device))
            usbCecConnection.switchTvOff()
        }
        when (requestCode) {
            RESULT_ENABLE -> {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Disable", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(
                        applicationContext, "Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    @Throws(IOException::class)
    fun printResults(process: Process) {
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var line: String? = ""
        while (reader.readLine().also { line = it } != null) {
            println("xxxxxxx")
            println(line)
        }
    }
}