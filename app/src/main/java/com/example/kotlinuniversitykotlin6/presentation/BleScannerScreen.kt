package com.example.kotlinuniversitykotlin6.presentation

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BleScannerScreen() {
    val context = LocalContext.current
    val viewModel = remember { BleViewModel(context) }

    val devices by viewModel.devices.collectAsState()
    val heartRate by viewModel.heartRate.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()

    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted && !isScanning) {
            viewModel.startScan()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("BLE Heart Rate Monitor", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (permissionState.allPermissionsGranted) {
                if (isScanning) viewModel.stopScan() else viewModel.startScan()
            } else {
                permissionState.launchMultiplePermissionRequest()
            }
        }) {
            Text(if (isScanning) "Остановить сканирование" else "Начать сканирование")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение частоты пульса
        Text(
            text = "Heart Rate: ${heartRate ?: "—"}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Status: $connectionState", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (devices.isEmpty() && !isScanning) {
            Text("No devices found", style = MaterialTheme.typography.bodyLarge)
        } else if (devices.isEmpty() && isScanning) {
            Text("Scanning for devices...", style = MaterialTheme.typography.bodyLarge)
        }

        LazyColumn {
            items(devices) { device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            viewModel.connect(device)
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(device.name ?: "Unnamed device", style = MaterialTheme.typography.titleMedium)
                        Text(device.address, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        if (connectionState == "Connected") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { viewModel.disconnect() }) {
                    Text("Disconnect")
                }
            }
        }
    }
}