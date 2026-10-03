package com.arbdevai.quranvip.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import com.arbdevai.quranvip.data.model.City
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepository(private val context: Context, private val prayerRepository: PrayerRepository) {
    @SuppressLint("MissingPermission")
    suspend fun detectCurrentCity(): Pair<City, String>? = withTimeoutOrNull(25_000) {
        val location = currentLocation() ?: return@withTimeoutOrNull null
        val address = address(location) ?: return@withTimeoutOrNull null
        if (!address.countryCode.equals("ID", true)) return@withTimeoutOrNull null
        val zone = CityMatching.zone(address.adminArea.orEmpty()) ?: return@withTimeoutOrNull null
        val cities = prayerRepository.getCities()
        val name = address.subAdminArea ?: address.locality ?: return@withTimeoutOrNull null
        val city = CityMatching.match(name, cities) ?: return@withTimeoutOrNull null
        city to zone
    }

    @SuppressLint("MissingPermission")
    private suspend fun currentLocation(): Location? = suspendCancellableCoroutine { cont ->
        val token = CancellationTokenSource()
        cont.invokeOnCancellation { token.cancel() }
        LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, token.token)
            .addOnSuccessListener { if (cont.isActive) cont.resume(it) }
            .addOnFailureListener { if (cont.isActive) cont.resumeWithException(it) }
    }

    @Suppress("DEPRECATION")
    private suspend fun address(location: Location): Address? = withContext(Dispatchers.IO) {
        if (!Geocoder.isPresent()) return@withContext null
        val geocoder = Geocoder(context, Locale("id", "ID"))
        if (Build.VERSION.SDK_INT >= 33) {
            suspendCancellableCoroutine { cont ->
                geocoder.getFromLocation(location.latitude, location.longitude, 1, object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        if (cont.isActive) cont.resume(addresses.firstOrNull())
                    }
                    override fun onError(errorMessage: String?) {
                        if (cont.isActive) cont.resume(null)
                    }
                })
            }
        } else geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
    }
}
