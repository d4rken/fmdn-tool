package eu.darken.fmdn.common.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.fmdn.common.coroutine.DispatcherProvider
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothManager2 @Inject constructor(
    private val manager: BluetoothManager,
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
) {

    val adapter: BluetoothAdapter?
        get() = manager.adapter

    val scanner: BluetoothLeScanner?
        get() = adapter?.bluetoothLeScanner

    val isBluetoothEnabled: Flow<Boolean> = callbackFlow {
        send(manager.adapter?.isEnabled ?: false)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (BluetoothAdapter.ACTION_STATE_CHANGED != intent.action) {
                    log(TAG) { "Unknown BluetoothAdapter action: $intent" }
                    return
                }

                val value = when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
                    BluetoothAdapter.STATE_OFF -> false
                    BluetoothAdapter.STATE_ON -> true
                    else -> false
                }

                trySend(value)
            }
        }
        context.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        awaitClose { context.unregisterReceiver(receiver) }
    }

    companion object {
        private val TAG = logTag("Bluetooth", "Manager2")
    }
}