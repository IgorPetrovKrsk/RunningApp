package igor.petrov.core.connectivity.data.di

import igor.petrov.core.connectivity.data.WearNodeDiscovery
import igor.petrov.core.connectivity.data.messaging.WearMessagingClient
import igor.petrov.core.connectivity.domain.NodeDiscovery
import igor.petrov.core.connectivity.domain.messaging.MessagingClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreConnectivityDataModule = module {
    singleOf(::WearNodeDiscovery).bind<NodeDiscovery>()
    singleOf(::WearMessagingClient).bind<MessagingClient>()
}