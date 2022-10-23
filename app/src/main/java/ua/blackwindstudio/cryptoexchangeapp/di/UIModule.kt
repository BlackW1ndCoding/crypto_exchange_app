package ua.blackwindstudio.cryptoexchangeapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ua.blackwindstudio.cryptoexchangeapp.R
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.ResourceProvider
import javax.inject.Singleton

@Module
class UIModule {
    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider<String> {
        val stringIds = intArrayOf(
            R.string.from_to,
            R.string.updated_at,
            R.string.date_time_pattern
        )
        return object: ResourceProvider<String> {
            override val resourcesMap =
                ResourceProvider.mapResources(
                    context.resources,
                    context.resources::getString,
                    *stringIds
                )
        }
    }
}