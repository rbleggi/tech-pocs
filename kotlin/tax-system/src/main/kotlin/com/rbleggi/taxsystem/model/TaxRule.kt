package com.rbleggi.taxsystem.model

import com.rbleggi.taxsystem.service.strategy.TaxStrategy

data class TaxRule(
    val product: String,
    val state: String,
    val strategy: TaxStrategy
)
