package com.campoe.android.zycle.extension

internal interface Extendable {

    fun extendWith(extensionPoint: AdapterExtension): Extendable

}