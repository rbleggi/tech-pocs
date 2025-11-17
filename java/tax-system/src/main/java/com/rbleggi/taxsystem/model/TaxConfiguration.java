package com.rbleggi.taxsystem.model;

import java.util.Map;

public record TaxConfiguration(String state, int year, Map<String, Double> rates) {}
