package ua.blackwindstudio.cryptoexchangeapp.coin.ui.adapters

import android.content.Context
import android.widget.ArrayAdapter
import ua.blackwindstudio.cryptoexchangeapp.coin.ui.model.UiToSymbol

class ToSymbolSpinnerAdapter(
    context: Context,
    layoutResourceId: Int,
    values: Array<out UiToSymbol>
): ArrayAdapter<UiToSymbol>(
    context,
    layoutResourceId,
    values
)