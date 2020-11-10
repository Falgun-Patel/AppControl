import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.util.Log
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface.UsbReadCallback
import java.util.*

/**
 * This class allows to send HDMI CEC commands through Pulse-Eight's USB-CEC adapter.
 * Command syntax is inspired by libCEC (https://github.com/Pulse-Eight/libcec).
 *
 * It requires UsbSerial library (https://github.com/felHR85/UsbSerial)
 *
 * IMPORTANT: the provided [UsbDevice] must have been granted permission.
 *
 */
class UsbCecConnectionn(device: UsbDevice?, usbConnection: UsbDeviceConnection?) :
    UsbReadCallback {
    private enum class MsgCode {
        MSGCODE_NOTHING, MSGCODE_PING, MSGCODE_TIMEOUT_ERROR, MSGCODE_HIGH_ERROR, MSGCODE_LOW_ERROR, MSGCODE_FRAME_START, MSGCODE_FRAME_DATA, MSGCODE_RECEIVE_FAILED, MSGCODE_COMMAND_ACCEPTED, MSGCODE_COMMAND_REJECTED, MSGCODE_SET_ACK_MASK, MSGCODE_TRANSMIT, MSGCODE_TRANSMIT_EOM, MSGCODE_TRANSMIT_IDLETIME, MSGCODE_TRANSMIT_ACK_POLARITY, MSGCODE_TRANSMIT_LINE_TIMEOUT, MSGCODE_TRANSMIT_SUCCEEDED, MSGCODE_TRANSMIT_FAILED_LINE, MSGCODE_TRANSMIT_FAILED_ACK, MSGCODE_TRANSMIT_FAILED_TIMEOUT_DATA, MSGCODE_TRANSMIT_FAILED_TIMEOUT_LINE, MSGCODE_FIRMWARE_VERSION, MSGCODE_START_BOOTLOADER, MSGCODE_GET_BUILDDATE, MSGCODE_SET_CONTROLLED, MSGCODE_GET_AUTO_ENABLED, MSGCODE_SET_AUTO_ENABLED, MSGCODE_GET_DEFAULT_LOGICAL_ADDRESS, MSGCODE_SET_DEFAULT_LOGICAL_ADDRESS, MSGCODE_GET_LOGICAL_ADDRESS_MASK, MSGCODE_SET_LOGICAL_ADDRESS_MASK, MSGCODE_GET_PHYSICAL_ADDRESS, MSGCODE_SET_PHYSICAL_ADDRESS, MSGCODE_GET_DEVICE_TYPE, MSGCODE_SET_DEVICE_TYPE, MSGCODE_GET_HDMI_VERSION, MSGCODE_SET_HDMI_VERSION, MSGCODE_GET_OSD_NAME, MSGCODE_SET_OSD_NAME, MSGCODE_WRITE_EEPROM, MSGCODE_GET_ADAPTER_TYPE, MSGCODE_SET_ACTIVE_SOURCE, MSGCODE_UNKNOWN
    }

    private val usbSerialDevice: UsbSerialDevice
    private val currentPacket: MutableList<Byte> = ArrayList()
    fun switchTvOn() {
        sendCommand(MsgCode.MSGCODE_SET_CONTROLLED, 1)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_ACK_POLARITY, 0)
        sendCommand(MsgCode.MSGCODE_TRANSMIT, 16)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_EOM, 4)
    }

    fun switchTvOff() {
        sendCommand(MsgCode.MSGCODE_SET_CONTROLLED, 1)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_ACK_POLARITY, 0)
        sendCommand(MsgCode.MSGCODE_TRANSMIT, 16)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_EOM, 54)
    }

    fun activeSource() {
        sendCommand(MsgCode.MSGCODE_SET_CONTROLLED, 1)
        // marking the adapter as active source
        sendCommand(MsgCode.MSGCODE_SET_ACTIVE_SOURCE, 1)
        // powering on 'TV'
        sendCommand(MsgCode.MSGCODE_TRANSMIT_ACK_POLARITY, 0)
        sendCommand(MsgCode.MSGCODE_TRANSMIT, 16)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_EOM, 4)
        // active source
        sendCommand(MsgCode.MSGCODE_TRANSMIT_ACK_POLARITY, 1)
        sendCommand(MsgCode.MSGCODE_TRANSMIT, 31)
        sendCommand(MsgCode.MSGCODE_TRANSMIT, 130)
        sendCommand(MsgCode.MSGCODE_TRANSMIT, 16)
        sendCommand(MsgCode.MSGCODE_TRANSMIT_EOM, 0)
    }

    override fun onReceivedData(bytes: ByteArray) {
        Log.v(LOG_TAG, "onReceivedData: " + bytes.contentToString())
        for (b in bytes) {
            currentPacket.add(b)
            if (b == MSG_END) {
                val packetBytes = ByteArray(currentPacket.size)
                for (i in packetBytes.indices) {
                    packetBytes[i] = currentPacket[i]
                }
                onReceivedPacket(packetBytes)
                currentPacket.clear()
            }
        }
    }

    private fun onReceivedPacket(bytes: ByteArray) {
        Log.d(LOG_TAG, "onReceivedPacket: " + Arrays.toString(bytes))
        if (bytes.size <= 2) {
            Log.w(LOG_TAG, "Invalid response: " + Arrays.toString(bytes))
        } else {
            val msgCode = getMsgCode(bytes[1])
            val params = ByteArray(bytes.size - 3)
            System.arraycopy(bytes, 2, params, 0, params.size)
            onReceivedPacket(msgCode, params)
        }
    }

    private fun onReceivedPacket(msgCode: MsgCode, params: ByteArray) {
        Log.d(LOG_TAG, "onReceivedPacket: " + msgCode + ", " + Arrays.toString(params))
        when (msgCode) {
            MsgCode.MSGCODE_COMMAND_ACCEPTED -> Log.d(
                LOG_TAG, "ACCEPTED " + getMsgCode(
                    params[0]
                )
            )
            MsgCode.MSGCODE_COMMAND_REJECTED -> Log.w(
                LOG_TAG, "REJECTED: " + getMsgCode(
                    params[0]
                )
            )
            MsgCode.MSGCODE_FIRMWARE_VERSION -> {
                val firmwareVersion: Int = 255 * (params[0] /*and 0xFF*/) + (params[1]/* and 0xFF*/)
                Log.i(
                    LOG_TAG,
                    "Firmware version is $firmwareVersion"
                )
            }
            MsgCode.MSGCODE_GET_OSD_NAME -> {
                val osdName = String(params)
                Log.i(LOG_TAG, "OSD name is '$osdName'")
            }
        }
    }

    private fun sendCommand(code: MsgCode, param: Int) {
        sendCommand(code, byteArrayOf(param.toByte()))
    }

    private fun sendCommand(code: MsgCode, params: ByteArray = ByteArray(0)) {
        Log.i(LOG_TAG, "sendCommand: " + code + ", " + Arrays.toString(params))
        sendCommand(code.ordinal.toByte(), params)
    }

    private fun sendCommand(command: Byte, params: ByteArray) {
        Log.d(LOG_TAG, "sendCommand: " + command + ", " + Arrays.toString(params))
        val data = ByteArray(3 + params.size)
        data[0] = MSG_START
        data[1] = command
        System.arraycopy(params, 0, data, 2, params.size)
        data[data.size - 1] = MSG_END
        sendData(data)
    }

    private fun sendData(data: ByteArray) {
        Log.v(LOG_TAG, "sendData: " + Arrays.toString(data))
        usbSerialDevice.write(data)
    }

    companion object {
        private val LOG_TAG = UsbCecConnectionn::class.java.simpleName
        private const val MSG_START = 255.toByte()
        private const val MSG_END = 254.toByte()
        private fun getMsgCode(b: Byte): MsgCode {
            val i: Int = /*b and*/ 0xFF // unsigned int
            return if (i < MsgCode.values().size) {
                MsgCode.values()[i]
            } else {
                MsgCode.MSGCODE_UNKNOWN
            }
        }
    }

    init {
        usbSerialDevice = UsbSerialDevice.createUsbSerialDevice(device, usbConnection)
        usbSerialDevice.open()
        usbSerialDevice.read(this)

        // Optional, just attempt to get some information
        sendCommand(MsgCode.MSGCODE_FIRMWARE_VERSION)
        sendCommand(MsgCode.MSGCODE_SET_CONTROLLED, 1)
        sendCommand(MsgCode.MSGCODE_GET_OSD_NAME)
    }
}