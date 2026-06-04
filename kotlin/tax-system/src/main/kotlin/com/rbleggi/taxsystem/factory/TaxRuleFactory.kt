package com.rbleggi.taxsystem.factory

import com.rbleggi.taxsystem.model.TaxRule


interface TaxRuleFactory {
    fun rules(): List<TaxRule>
}
