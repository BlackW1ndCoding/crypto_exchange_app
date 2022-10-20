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
    fun provideResourceProvider(context: Context): ResourceProvider {
        return object: ResourceProvider {
            override val stringsMap: Map<String, String> = listOf(
                R.string.from_to,
                R.string.updated_at,
                R.string.date_time_pattern
            ).associate {
                context.resources.getResourceName(it)
                    .removePrefix(RESOURCE_NAME_PREFIX) to context.getString(it)
            }
        }
    }

    companion object {
        private const val RESOURCE_NAME_PREFIX = "ua.blackwindstudio.cryptoexchangeapp:string/"
    }
}