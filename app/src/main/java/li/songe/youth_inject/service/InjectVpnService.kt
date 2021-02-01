package li.songe.youth_inject.service

import android.content.pm.PackageManager
import android.net.VpnService
import android.os.ParcelFileDescriptor

class InjectVpnService : VpnService() {
    private lateinit var localTunnel: ParcelFileDescriptor
    override fun onCreate() {
        super.onCreate()
        val appPackages = arrayOf(
            "com.android.chrome",
            "com.google.android.youtube",
        )
        val builder = Builder()
        for (appPackage in appPackages) {
            try {
                packageManager.getPackageInfo(appPackage, 0)
                builder.addAllowedApplication(appPackage)
            } catch (e: PackageManager.NameNotFoundException) {
                // The app isn't installed.
            }
        }
        localTunnel = builder
            .addAddress("192.168.2.2", 24)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("192.168.1.1")
            .establish()!!
        localTunnel
    }
}