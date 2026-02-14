package com.rbleggi.realestate;

import java.util.*;
import java.util.stream.Collectors;

record Property(
    String id,
    String city,
    String neighborhood,
    double area,
    int bedrooms,
    int bathrooms,
    int parkingSpaces,
    int age,
    double actualPrice
) {}

record PricePrediction(String propertyId, double predictedPrice, double confidence, String method) {}

sealed interface PredictionStrategy permits LinearRegressionStrategy, PolynomialRegressionStrategy, KNNRegressionStrategy {
    PricePrediction predict(Property property, List<Property> trainingData);
}

final class LinearRegressionStrategy implements PredictionStrategy {
    private final Map<String, Double> cityMultipliers;
    private final Map<String, Double> neighborhoodMultipliers;

    public LinearRegressionStrategy() {
        this.cityMultipliers = Map.of(
            "Sao Paulo", 1.5,
            "Curitiba", 1.2,
            "Belo Horizonte", 1.1
        );
        this.neighborhoodMultipliers = Map.of(
            "Jardins", 1.8, "Batel", 1.6, "Savassi", 1.5,
            "Vila Mariana", 1.4, "Centro", 1.0, "Zona Sul", 1.3
        );
    }

    @Override
    public PricePrediction predict(Property property, List<Property> trainingData) {
        double basePrice = 5000.0;

        double areaComponent = property.area() * 3000;
        double bedroomComponent = property.bedrooms() * 50000;
        double bathroomComponent = property.bathrooms() * 30000;
        double parkingComponent = property.parkingSpaces() * 40000;
        double ageComponent = -property.age() * 2000;

        double cityMultiplier = cityMultipliers.getOrDefault(property.city(), 1.0);
        double neighborhoodMultiplier = neighborhoodMultipliers.getOrDefault(property.neighborhood(), 1.0);

        double price = (basePrice + areaComponent + bedroomComponent + bathroomComponent +
                       parkingComponent + ageComponent) * cityMultiplier * neighborhoodMultiplier;

        return new PricePrediction(property.id(), Math.max(price, 100000), 0.75, "Linear-Regression");
    }
}

final class PolynomialRegressionStrategy implements PredictionStrategy {
    @Override
    public PricePrediction predict(Property property, List<Property> trainingData) {
        double basePrice = 8000.0;

        double areaSquared = Math.pow(property.area(), 1.5) * 200;
        double bedroomSquared = Math.pow(property.bedrooms(), 2) * 25000;
        double bathroomSquared = Math.pow(property.bathrooms(), 1.8) * 20000;
        double parkingSquared = Math.pow(property.parkingSpaces(), 1.5) * 30000;
        double ageSquared = -Math.pow(property.age(), 1.2) * 1500;

        double locationBonus = 0.0;
        if (property.city().equals("Sao Paulo") && property.neighborhood().equals("Jardins")) {
            locationBonus = 300000;
        } else if (property.city().equals("Curitiba") && property.neighborhood().equals("Batel")) {
            locationBonus = 200000;
        } else if (property.city().equals("Belo Horizonte") && property.neighborhood().equals("Savassi")) {
            locationBonus = 150000;
        }

        double price = basePrice + areaSquared + bedroomSquared + bathroomSquared +
                      parkingSquared + ageSquared + locationBonus;

        return new PricePrediction(property.id(), Math.max(price, 120000), 0.82, "Polynomial-Regression");
    }
}

final class KNNRegressionStrategy implements PredictionStrategy {
    private final int k;

    public KNNRegressionStrategy(int k) {
        this.k = k;
    }

    @Override
    public PricePrediction predict(Property property, List<Property> trainingData) {
        record Distance(Property prop, double distance) {}

        List<Distance> distances = trainingData.stream()
            .map(trainProp -> new Distance(trainProp, calculateDistance(property, trainProp)))
            .sorted(Comparator.comparingDouble(Distance::distance))
            .limit(k)
            .toList();

        double avgPrice = distances.stream()
            .mapToDouble(d -> d.prop().actualPrice())
            .average()
            .orElse(200000.0);

        double maxDistance = distances.stream()
            .mapToDouble(Distance::distance)
            .max()
            .orElse(1.0);

        double confidence = 0.65 + (1.0 / (1.0 + maxDistance)) * 0.2;

        return new PricePrediction(property.id(), avgPrice, Math.min(confidence, 0.95), "KNN-k" + k);
    }

    private double calculateDistance(Property p1, Property p2) {
        double cityMatch = p1.city().equals(p2.city()) ? 0 : 1;
        double neighborhoodMatch = p1.neighborhood().equals(p2.neighborhood()) ? 0 : 1;

        return Math.sqrt(
            Math.pow((p1.area() - p2.area()) / 100.0, 2) +
            Math.pow(p1.bedrooms() - p2.bedrooms(), 2) +
            Math.pow(p1.bathrooms() - p2.bathrooms(), 2) +
            Math.pow(p1.parkingSpaces() - p2.parkingSpaces(), 2) +
            Math.pow((p1.age() - p2.age()) / 10.0, 2) +
            cityMatch * 2 +
            neighborhoodMatch * 1.5
        );
    }
}

class RealEstatePricingSystem {
    private final PredictionStrategy strategy;

    public RealEstatePricingSystem(PredictionStrategy strategy) {
        this.strategy = strategy;
    }

    public PricePrediction predictPrice(Property property, List<Property> trainingData) {
        return strategy.predict(property, trainingData);
    }

    public List<PricePrediction> predictBatch(List<Property> properties, List<Property> trainingData) {
        return properties.stream()
            .map(prop -> predictPrice(prop, trainingData))
            .toList();
    }

    public double calculateMeanAbsoluteError(List<Property> testSet, List<Property> trainingData) {
        return testSet.stream()
            .mapToDouble(prop -> {
                PricePrediction prediction = predictPrice(prop, trainingData);
                return Math.abs(prediction.predictedPrice() - prop.actualPrice());
            })
            .average()
            .orElse(0.0);
    }

    public double calculateAccuracyPercentage(List<Property> testSet, List<Property> trainingData, double threshold) {
        long accurate = testSet.stream()
            .filter(prop -> {
                PricePrediction prediction = predictPrice(prop, trainingData);
                double error = Math.abs(prediction.predictedPrice() - prop.actualPrice());
                return (error / prop.actualPrice()) <= threshold;
            })
            .count();
        return (double) accurate / testSet.size() * 100;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Property> trainingData = List.of(
            new Property("p1", "Sao Paulo", "Jardins", 120, 3, 2, 2, 5, 950000),
            new Property("p2", "Sao Paulo", "Vila Mariana", 85, 2, 1, 1, 10, 620000),
            new Property("p3", "Curitiba", "Batel", 100, 3, 2, 2, 3, 720000),
            new Property("p4", "Curitiba", "Centro", 70, 2, 1, 1, 15, 380000),
            new Property("p5", "Belo Horizonte", "Savassi", 95, 2, 2, 1, 8, 580000),
            new Property("p6", "Belo Horizonte", "Zona Sul", 110, 3, 2, 2, 6, 650000)
        );

        Property testProperty = new Property(
            "test1", "Sao Paulo", "Jardins", 110, 3, 2, 2, 7, 900000
        );

        System.out.println("=== Sistema de Predicao de Precos de Imoveis ===\n");
        System.out.println("Imovel de Teste:");
        System.out.printf("  Cidade: %s, Bairro: %s%n", testProperty.city(), testProperty.neighborhood());
        System.out.printf("  Area: %.0f m², Quartos: %d, Banheiros: %d, Vagas: %d%n",
            testProperty.area(), testProperty.bedrooms(), testProperty.bathrooms(), testProperty.parkingSpaces());
        System.out.printf("  Idade: %d anos%n", testProperty.age());
        System.out.printf("  Preco Real: R$ %.2f%n%n", testProperty.actualPrice());

        System.out.println("Estrategia: Regressao Linear");
        RealEstatePricingSystem linearSystem = new RealEstatePricingSystem(new LinearRegressionStrategy());
        PricePrediction linearResult = linearSystem.predictPrice(testProperty, trainingData);
        System.out.printf("  Preco Previsto: R$ %.2f%n", linearResult.predictedPrice());
        System.out.printf("  Confianca: %.2f%n", linearResult.confidence());
        System.out.printf("  Erro: R$ %.2f (%.1f%%)%n",
            Math.abs(linearResult.predictedPrice() - testProperty.actualPrice()),
            Math.abs(linearResult.predictedPrice() - testProperty.actualPrice()) / testProperty.actualPrice() * 100);

        System.out.println("\nEstrategia: Regressao Polinomial");
        RealEstatePricingSystem polySystem = new RealEstatePricingSystem(new PolynomialRegressionStrategy());
        PricePrediction polyResult = polySystem.predictPrice(testProperty, trainingData);
        System.out.printf("  Preco Previsto: R$ %.2f%n", polyResult.predictedPrice());
        System.out.printf("  Confianca: %.2f%n", polyResult.confidence());
        System.out.printf("  Erro: R$ %.2f (%.1f%%)%n",
            Math.abs(polyResult.predictedPrice() - testProperty.actualPrice()),
            Math.abs(polyResult.predictedPrice() - testProperty.actualPrice()) / testProperty.actualPrice() * 100);

        System.out.println("\nEstrategia: KNN (k=3)");
        RealEstatePricingSystem knnSystem = new RealEstatePricingSystem(new KNNRegressionStrategy(3));
        PricePrediction knnResult = knnSystem.predictPrice(testProperty, trainingData);
        System.out.printf("  Preco Previsto: R$ %.2f%n", knnResult.predictedPrice());
        System.out.printf("  Confianca: %.2f%n", knnResult.confidence());
        System.out.printf("  Erro: R$ %.2f (%.1f%%)%n",
            Math.abs(knnResult.predictedPrice() - testProperty.actualPrice()),
            Math.abs(knnResult.predictedPrice() - testProperty.actualPrice()) / testProperty.actualPrice() * 100);

        System.out.println("\nAcuracia com Limiar de 20%:");
        List<Property> testSet = List.of(testProperty);
        System.out.printf("  Linear: %.1f%%%n", linearSystem.calculateAccuracyPercentage(testSet, trainingData, 0.20));
        System.out.printf("  Polinomial: %.1f%%%n", polySystem.calculateAccuracyPercentage(testSet, trainingData, 0.20));
        System.out.printf("  KNN: %.1f%%%n", knnSystem.calculateAccuracyPercentage(testSet, trainingData, 0.20));
    }
}
