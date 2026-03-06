package com.rbleggi.imageclassification;

import java.util.*;
import java.util.stream.Collectors;

record ImageFeatures(
    double redAverage,
    double greenAverage,
    double blueAverage,
    double brightness,
    double contrast,
    double saturation
) {}

record Image(String id, String name, ImageFeatures features, String actualCategory) {}

record Classification(String imageId, String predictedCategory, double confidence, String method) {}

sealed interface ClassificationStrategy permits ThresholdStrategy, KNNStrategy, NeuralNetworkStrategy {
    Classification classify(Image image, List<Image> trainingSet);
}

final class ThresholdStrategy implements ClassificationStrategy {
    @Override
    public Classification classify(Image image, List<Image> trainingSet) {
        ImageFeatures f = image.features();

        double greenScore = f.greenAverage() - (f.redAverage() + f.blueAverage()) / 2;
        double blueScore = f.blueAverage() - (f.redAverage() + f.greenAverage()) / 2;
        double brownScore = (f.redAverage() + f.greenAverage()) / 2 - f.blueAverage();

        String category;
        double confidence;

        if (greenScore > 0.3 && f.saturation() > 0.5) {
            category = "Arara";
            confidence = 0.75 + greenScore * 0.1;
        } else if (blueScore > 0.2 && f.brightness() > 0.6) {
            category = "Arara";
            confidence = 0.70 + blueScore * 0.15;
        } else if (brownScore > 0.3 && f.saturation() < 0.4) {
            category = "Capivara";
            confidence = 0.78 + brownScore * 0.08;
        } else if (f.brightness() < 0.4 && f.contrast() > 0.6) {
            category = "Onca";
            confidence = 0.72 + f.contrast() * 0.1;
        } else {
            category = "Capivara";
            confidence = 0.60;
        }

        return new Classification(image.id(), category, Math.min(confidence, 1.0), "Threshold-based");
    }
}

final class KNNStrategy implements ClassificationStrategy {
    private final int k;

    public KNNStrategy(int k) {
        this.k = k;
    }

    @Override
    public Classification classify(Image image, List<Image> trainingSet) {
        record Distance(Image img, double distance) {}

        List<Distance> distances = trainingSet.stream()
            .map(trainImg -> new Distance(trainImg, calculateDistance(image.features(), trainImg.features())))
            .sorted(Comparator.comparingDouble(Distance::distance))
            .limit(k)
            .toList();

        Map<String, Long> votes = distances.stream()
            .collect(Collectors.groupingBy(d -> d.img().actualCategory(), Collectors.counting()));

        String predicted = votes.entrySet().stream()
            .max(Comparator.comparingLong(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse("Capivara");

        double confidence = votes.getOrDefault(predicted, 0L) / (double) k;

        return new Classification(image.id(), predicted, confidence, "KNN-k" + k);
    }

    private double calculateDistance(ImageFeatures f1, ImageFeatures f2) {
        return Math.sqrt(
            Math.pow(f1.redAverage() - f2.redAverage(), 2) +
            Math.pow(f1.greenAverage() - f2.greenAverage(), 2) +
            Math.pow(f1.blueAverage() - f2.blueAverage(), 2) +
            Math.pow(f1.brightness() - f2.brightness(), 2) +
            Math.pow(f1.contrast() - f2.contrast(), 2) +
            Math.pow(f1.saturation() - f2.saturation(), 2)
        );
    }
}

final class NeuralNetworkStrategy implements ClassificationStrategy {
    private final Map<String, double[]> weights;

    public NeuralNetworkStrategy() {
        this.weights = Map.of(
            "Arara", new double[]{0.3, 0.8, 0.5, 0.6, 0.4, 0.7},
            "Onca", new double[]{0.6, 0.4, 0.3, 0.3, 0.8, 0.5},
            "Capivara", new double[]{0.5, 0.5, 0.4, 0.5, 0.5, 0.3}
        );
    }

    @Override
    public Classification classify(Image image, List<Image> trainingSet) {
        ImageFeatures f = image.features();
        double[] inputs = new double[]{
            f.redAverage(), f.greenAverage(), f.blueAverage(),
            f.brightness(), f.contrast(), f.saturation()
        };

        Map<String, Double> scores = new HashMap<>();
        for (Map.Entry<String, double[]> entry : weights.entrySet()) {
            double score = 0.0;
            for (int i = 0; i < inputs.length; i++) {
                score += inputs[i] * entry.getValue()[i];
            }
            scores.put(entry.getKey(), sigmoid(score));
        }

        String predicted = scores.entrySet().stream()
            .max(Comparator.comparingDouble(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse("Capivara");

        double confidence = scores.get(predicted);

        return new Classification(image.id(), predicted, confidence, "Neural-Network");
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
}

class ImageClassificationSystem {
    private final ClassificationStrategy strategy;

    public ImageClassificationSystem(ClassificationStrategy strategy) {
        this.strategy = strategy;
    }

    public Classification classify(Image image, List<Image> trainingSet) {
        return strategy.classify(image, trainingSet);
    }

    public List<Classification> classifyBatch(List<Image> images, List<Image> trainingSet) {
        return images.stream()
            .map(img -> classify(img, trainingSet))
            .toList();
    }

    public double calculateAccuracy(List<Image> testSet, List<Image> trainingSet) {
        List<Classification> results = classifyBatch(testSet, trainingSet);
        long correct = 0;
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).predictedCategory().equals(testSet.get(i).actualCategory())) {
                correct++;
            }
        }
        return (double) correct / testSet.size();
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Image Classification");
    }
}
