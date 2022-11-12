package ua.blackwindstudio.cryptoexchangeapp.coin.data.network

sealed class CoinError {
    sealed class RemoteError: CoinError() {
        object IOError: RemoteError()
    }
}
