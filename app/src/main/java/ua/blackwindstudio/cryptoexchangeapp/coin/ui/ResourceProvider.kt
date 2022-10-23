package ua.blackwindstudio.cryptoexchangeapp.coin.ui

import android.content.res.Resources

interface ResourceProvider<T> {
    val resourcesMap: Map<String, T>

    companion object {
        fun <T> mapResources(
            resources: Resources,
            getMethod: (Int) -> T,
            vararg resourceId: Int,
        ): Map<String, T> {
            if (resourceId.isEmpty()) return emptyMap()

            return resourceId.associate { id ->
                resources.getResourceEntryName(id) to getMethod(id)
            }
        }
    }
}