package com.rbleggi.logisticpricing.model

data class FreightInfo(
    val volume: Double,
    val size: Double,
    val distance: Double,
    val transportType: TransportType
)
