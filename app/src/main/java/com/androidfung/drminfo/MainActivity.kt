package com.androidfung.drminfo

import android.annotation.SuppressLint
import android.media.MediaDrm
import android.media.MediaDrmException
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Box
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.androidfung.drminfo.ui.DRMInfoTheme
import com.androidfung.drminfo.ui.typography
import java.util.*

class MainActivity : AppCompatActivity() {
    val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
    val PLAYREADY_UUID = UUID(-0x65fb0f8667bfbd7aL, -0x546d19a41f77a06bL)
    val CLEARKEY_UUID = UUID(0x1077EFECC0B24D02L, -0x531cc3e1ad1d04b5L)

    var widevineDrm: MediaDrm? = null
    var playreadyDrm: MediaDrm? = null
    var clearkeyDrm: MediaDrm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        widevineDrm = getMediaDrm(WIDEVINE_UUID)
        playreadyDrm = getMediaDrm(PLAYREADY_UUID)
        clearkeyDrm = getMediaDrm(CLEARKEY_UUID)

        setContent {
            DRMInfoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = Color(0xFFEEEEEE)) {
                    ScrollableColumn(modifier = Modifier.fillMaxSize()) {
                        DeviceInfo()

                        clearkeyDrm?.let {
                            DrmInfo(mediaDrm = it)
                        }

                        widevineDrm?.let {
                            DrmInfo(mediaDrm = it)
                        }

                        playreadyDrm?.let {
                            DrmInfo(mediaDrm = it)
                        }

                    }
                }
            }
        }
    }
}

private fun getMediaDrm(uuid: UUID): MediaDrm? {
    return if (MediaDrm.isCryptoSchemeSupported(uuid)) {
        try {
            MediaDrm(uuid)
        } catch (ignored: MediaDrmException) {
            null
        } catch (ignored: RuntimeException) {
            null
        }
    } else {
        null
    }
}

private fun getPropertyString(mediaDrm: MediaDrm, key: String): String? {
    return try {
        return mediaDrm.getPropertyString(key)
    } catch (e: RuntimeException) {
        null
    } catch (e: MediaDrmException) {
        null
    }
}

@SuppressLint("WrongConstant")
@Composable
fun DrmInfo(mediaDrm: MediaDrm) {
    val vendor = getPropertyString(mediaDrm, MediaDrm.PROPERTY_VENDOR)
    val description = getPropertyString(mediaDrm, MediaDrm.PROPERTY_DESCRIPTION)
    val securityLevel = getPropertyString(mediaDrm, "securityLevel")
    Box(modifier = Modifier.padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {

                description?.let { d ->
                    Text(d, style = typography.h6)
                    Spacer(Modifier.preferredHeight(8.dp))
                }

                vendor?.let { v ->
                    Text("Vendor", style = typography.caption)
                    Text(v, style = typography.body2)
                    Spacer(Modifier.preferredHeight(8.dp))
                }

                securityLevel?.let { sl ->
                    Text("Security Level", style = typography.caption)
                    Text(sl, style = typography.body2)
                    Spacer(Modifier.preferredHeight(8.dp))
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceInfo() {
    Box(modifier = Modifier.padding(16.dp)) {

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(Build.MANUFACTURER + " " + Build.MODEL, style = typography.h6)
                Spacer(Modifier.preferredHeight(8.dp))

                Text("Brand", style = typography.caption)
                Text(Build.BRAND, style = typography.body2)
                Spacer(Modifier.preferredHeight(8.dp))

                Text("Hardware", style = typography.caption)
                Text(Build.HARDWARE, style = typography.body2)
                Spacer(Modifier.preferredHeight(8.dp))

                Text("Build", style = typography.caption)
                Text(Build.DISPLAY, style = typography.body2)
                Spacer(Modifier.preferredHeight(8.dp))

                Text("type", style = typography.caption)
                Text(Build.TYPE, style = typography.body2)
                Spacer(Modifier.preferredHeight(8.dp))

                Text("Fingerprint", style = typography.caption)
                Text(Build.FINGERPRINT, style = typography.body2)
                Spacer(Modifier.preferredHeight(8.dp))

                Text("SDK Level", style = typography.caption)
                Text(Build.VERSION.SDK_INT.toString(), style = typography.body2)
                Spacer(Modifier.preferredHeight(8.dp))

                Text("Supported ABI", style = typography.caption)
                Text(Build.SUPPORTED_ABIS.joinToString(), style = typography.body2)
                Spacer(Modifier.preferredHeight(8.dp))
            }
        }
    }
}
