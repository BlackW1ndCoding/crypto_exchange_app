package ua.blackwindstudio.cryptoexchangeapp.coin.ui

import android.content.res.Resources

interface ResourceProvider<T> {
    val resourcesMap: Map<String, T>

    companion object {
        /**
         * Factory method to map resource ids to their values
         * @param resources resources class from Context.
         * @param getMethod method from resources class to use to load resources.
         * @param resourceId ids of resources to load into ResourceProvider.
         */
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