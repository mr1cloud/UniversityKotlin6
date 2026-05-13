package com.example.kotlinuniversitykotlin6.data

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class BleRepository(context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val adapter = bluetoothManager.adapter

    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices: StateFlow<List<BluetoothDevice>> = _devices

    private val _heartRate = MutableStateFlow<String?>(null)  // "Heart Rate: 72 bpm"
    val heartRate: StateFlow<String?> = _heartRate

    private val _connectionState = MutableStateFlow("Disconnected")
    val connectionState: StateFlow<String> = _connectionState

    private var currentGatt: BluetoothGatt? = null

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            if (!device.name.isNullOrBlank()) {
                println("Найдено устройство: ${device.name} - ${device.address}")

                val current = _devices.value.toMutableList()
                if (current.none { it.address == device.address }) {
                    current.add(device)
                    _devices.value = current.toList()
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            _isScanning.value = false
            println("Сканирование провалилось: errorCode = $errorCode")
        }
    }

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
        if (!adapter.isEnabled) return
        val scanner = adapter.bluetoothLeScanner ?: return

        _devices.value = emptyList()  // очищаем список

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanner.startScan(null, settings, scanCallback)
        _isScanning.value = true
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        adapter.bluetoothLeScanner?.stopScan(scanCallback)
        _isScanning.value = false
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(device: BluetoothDevice) {
        stopScan()
        currentGatt = device.connectGatt(null, false, gattCallback)
        _connectionState.value = "Connecting"
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        currentGatt?.disconnect()
        currentGatt?.close()
        currentGatt = null
        _connectionState.value = "Disconnected"
        _heartRate.value = null
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                _connectionState.value = "Connected"
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                _connectionState.value = "Disconnected"
                _heartRate.value = null
            }
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            println("onServicesDiscovered: status = $status")

            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(HEART_RATE_SERVICE_UUID)
                if (service == null) {
                    println("Heart Rate Service НЕ НАЙДЕН!")
                    return
                }

                val characteristic = service.getCharacteristic(HEART_RATE_MEASUREMENT_UUID)
                if (characteristic == null) {
                    println("Heart Rate Measurement НЕ НАЙДЕНА!")
                    return
                }

                // Включаем уведомления
                gatt.setCharacteristicNotification(characteristic, true)

                val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
                if (descriptor != null) {
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(descriptor)
                    println("Запись CCC-дескриптора отправлена")
                }
            } else {
                println("Обнаружение сервисов провалилось: status = $status")
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            println("onCharacteristicChanged: uuid = ${characteristic.uuid}")
            if (characteristic.uuid == HEART_RATE_MEASUREMENT_UUID) {
                parseHeartRateData(characteristic)
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            println("onDescriptorWrite: status = $status, uuid = ${descriptor.uuid}")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                println("Дескриптор CCC успешно записан — уведомления должны работать")
            } else {
                println("Ошибка записи дескриптора: $status")
            }
        }

        private fun parseHeartRateData(characteristic: BluetoothGattCharacteristic) {
            val value = characteristic.value ?: run {
                println("Значение характеристики пустое (null)")
                return
            }

            if (value.isEmpty()) {
                println("Значение характеристики пустое (0 байт)")
                return
            }

            println("Получено значение (hex): ${value.joinToString(" ") { "%02x".format(it) }}")
            println("Длина: ${value.size} байт")

            // Парсинг Heart Rate Measurement согласно спецификации
            val flags = value[0].toInt() and 0xFF
            val isHeartRate16Bit = (flags and 0x01) != 0
            val heartRateValue: Int

            var offset = 1

            if (isHeartRate16Bit) {
                // 16-bit heart rate value
                heartRateValue = ((value[offset + 1].toInt() and 0xFF) shl 8) or (value[offset].toInt() and 0xFF)
                offset += 2
            } else {
                // 8-bit heart rate value
                heartRateValue = value[offset].toInt() and 0xFF
                offset += 1
            }

            val hrText = "$heartRateValue bpm"
            _heartRate.value = hrText

            println("Частота пульса: $hrText")

            // Опционально: парсинг дополнительных полей (энергия, интервалы RR)
            val isSensorContactDetected = (flags and 0x02) != 0
            val isSensorContactSupported = (flags and 0x04) != 0
            val isEnergyExpendedPresent = (flags and 0x08) != 0
            val isRRIntervalPresent = (flags and 0x10) != 0

            if (isSensorContactSupported) {
                if (isSensorContactDetected) {
                    println("Датчик контакта с кожей: обнаружен")
                } else {
                    println("Датчик контакта с кожей: не обнаружен")
                }
            }

            if (isEnergyExpendedPresent && value.size >= offset + 2) {
                val energyExpended = ((value[offset + 1].toInt() and 0xFF) shl 8) or (value[offset].toInt() and 0xFF)
                println("Энергозатраты: $energyExpended кДж")
                offset += 2
            }

            if (isRRIntervalPresent) {
                val remainingBytes = value.size - offset
                val rrIntervalsCount = remainingBytes / 2
                val rrIntervals = mutableListOf<Int>()

                for (i in 0 until rrIntervalsCount) {
                    val pos = offset + (i * 2)
                    if (pos + 1 < value.size) {
                        val rrInterval = ((value[pos + 1].toInt() and 0xFF) shl 8) or (value[pos].toInt() and 0xFF)
                        rrIntervals.add(rrInterval)
                        println("RR интервал ${i + 1}: $rrInterval мс")
                    }
                }
            }
        }
    }

    companion object {
        private val HEART_RATE_SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")
        private val HEART_RATE_MEASUREMENT_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
        private val CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }
}