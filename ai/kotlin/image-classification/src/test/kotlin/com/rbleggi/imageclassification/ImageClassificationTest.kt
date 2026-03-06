package com.rbleggi.imageclassification

import kotlin.test.*

class ImageClassificationTest {

    private val trainingSet = listOf(
        Image("t1", "Onca 1", listOf(0.9, 0.8, 0.2, 0.1), Category.Onca),
        Image("t2", "Onca 2", listOf(0.85, 0.75, 0.25, 0.15), Category.Onca),
        Image("t3", "Arara 1", listOf(0.2, 0.3, 0.9, 0.8), Category.Arara),
        Image("t4", "Arara 2", listOf(0.25, 0.35, 0.85, 0.75), Category.Arara),
        Image("t5", "Capivara 1", listOf(0.5, 0.6, 0.4, 0.5), Category.Capivara)
    )

    private val testImage = Image("test", "Test", listOf(0.88, 0.78, 0.22, 0.12), Category.Onca)

    @Test
    fun `ThresholdStrategy classifies image correctly`() {
        val thresholds = mapOf(
            Category.Onca to listOf(0.9, 0.8, 0.2, 0.1),
            Category.Arara to listOf(0.2, 0.3, 0.9, 0.8),
            Category.Capivara to listOf(0.5, 0.6, 0.4, 0.5)
        )
        val strategy = ThresholdStrategy(thresholds)
        val result = strategy.classify(testImage)

        assertEquals(Category.Onca, result.category)
        assertTrue(result.confidence > 0.0)
    }

    @Test
    fun `ThresholdStrategy provides confidence score`() {
        val thresholds = mapOf(
            Category.Onca to listOf(0.9, 0.8, 0.2, 0.1)
        )
        val strategy = ThresholdStrategy(thresholds)
        val result = strategy.classify(testImage)

        assertTrue(result.confidence >= 0.0 && result.confidence <= 1.0)
        assertTrue(result.details.contains("Threshold matching"))
    }

    @Test
    fun `KNNStrategy classifies based on nearest neighbors`() {
        val strategy = KNNStrategy(trainingSet, k = 3)
        val result = strategy.classify(testImage)

        assertEquals(Category.Onca, result.category)
        assertTrue(result.confidence > 0.0)
    }

    @Test
    fun `KNNStrategy respects k parameter`() {
        val strategy = KNNStrategy(trainingSet, k = 1)
        val result = strategy.classify(testImage)

        assertTrue(result.details.contains("k=1"))
        assertTrue(result.confidence >= 0.0 && result.confidence <= 1.0)
    }

    @Test
    fun `KNNStrategy uses euclidean distance`() {
        val oncaImage = Image("onca", "Onca", listOf(0.9, 0.8, 0.2, 0.1), Category.Onca)
        val strategy = KNNStrategy(trainingSet, k = 3)
        val result = strategy.classify(oncaImage)

        assertEquals(Category.Onca, result.category)
    }

    @Test
    fun `NeuralNetworkLikeStrategy classifies with weights`() {
        val weights = mapOf(
            Category.Onca to listOf(0.8, 0.7, -0.3, -0.2),
            Category.Arara to listOf(-0.3, -0.2, 0.8, 0.7),
            Category.Capivara to listOf(0.1, 0.2, 0.1, 0.2)
        )
        val strategy = NeuralNetworkLikeStrategy(weights)
        val result = strategy.classify(testImage)

        assertNotNull(result.category)
        assertTrue(result.confidence > 0.0)
    }

    @Test
    fun `NeuralNetworkLikeStrategy uses sigmoid activation`() {
        val weights = mapOf(
            Category.Onca to listOf(1.0, 1.0, 1.0, 1.0)
        )
        val strategy = NeuralNetworkLikeStrategy(weights)
        val result = strategy.classify(testImage)

        assertTrue(result.confidence > 0.0 && result.confidence < 1.0)
        assertTrue(result.details.contains("Rede neural"))
    }

    @Test
    fun `ImageClassifier classifies single image`() {
        val classifier = ImageClassifier(KNNStrategy(trainingSet, k = 3))
        val result = classifier.classify(testImage)

        assertEquals(Category.Onca, result.category)
    }

    @Test
    fun `ImageClassifier classifies batch of images`() {
        val classifier = ImageClassifier(KNNStrategy(trainingSet, k = 3))
        val images = listOf(testImage, testImage)
        val results = classifier.classifyBatch(images)

        assertEquals(2, results.size)
        assertTrue(results.all { it.category == Category.Onca })
    }

    @Test
    fun `ImageClassifier calculates accuracy`() {
        val classifier = ImageClassifier(KNNStrategy(trainingSet, k = 3))
        val testImages = listOf(
            Image("t1", "Test1", listOf(0.9, 0.8, 0.2, 0.1), Category.Onca),
            Image("t2", "Test2", listOf(0.2, 0.3, 0.9, 0.8), Category.Arara)
        )
        val accuracy = classifier.accuracy(testImages)

        assertTrue(accuracy >= 0.0 && accuracy <= 1.0)
    }

    @Test
    fun `Image data class stores features correctly`() {
        val image = Image("id", "name", listOf(1.0, 2.0, 3.0), Category.Onca)

        assertEquals("id", image.id)
        assertEquals("name", image.name)
        assertEquals(3, image.features.size)
        assertEquals(Category.Onca, image.actualCategory)
    }

    @Test
    fun `ClassificationResult contains required fields`() {
        val result = ClassificationResult(Category.Onca, 0.95, "test details")

        assertEquals(Category.Onca, result.category)
        assertEquals(0.95, result.confidence)
        assertEquals("test details", result.details)
    }

    @Test
    fun `Category enum has all Brazilian options`() {
        assertNotNull(Category.Onca)
        assertNotNull(Category.Arara)
        assertNotNull(Category.Capivara)
        assertNotNull(Category.PaoDeQueijo)
        assertNotNull(Category.Feijoada)
        assertNotNull(Category.Acai)
    }

    @Test
    fun `ThresholdStrategy handles multiple categories`() {
        val thresholds = mapOf(
            Category.Onca to listOf(0.9, 0.8, 0.2, 0.1),
            Category.Arara to listOf(0.2, 0.3, 0.9, 0.8),
            Category.Capivara to listOf(0.5, 0.6, 0.4, 0.5),
            Category.PaoDeQueijo to listOf(0.7, 0.7, 0.7, 0.7)
        )
        val strategy = ThresholdStrategy(thresholds)
        val result = strategy.classify(testImage)

        assertNotNull(result.category)
    }

    @Test
    fun `KNNStrategy with k=1 returns single neighbor category`() {
        val strategy = KNNStrategy(trainingSet, k = 1)
        val result = strategy.classify(testImage)

        assertTrue(result.confidence == 1.0)
    }

    @Test
    fun `NeuralNetworkLikeStrategy handles negative weights`() {
        val weights = mapOf(
            Category.Onca to listOf(-1.0, -1.0, -1.0, -1.0)
        )
        val strategy = NeuralNetworkLikeStrategy(weights)
        val result = strategy.classify(testImage)

        assertTrue(result.confidence > 0.0)
    }

    @Test
    fun `ImageClassifier accuracy is 100 percent for perfect predictions`() {
        val perfectTraining = listOf(testImage)
        val classifier = ImageClassifier(KNNStrategy(perfectTraining, k = 1))
        val accuracy = classifier.accuracy(listOf(testImage))

        assertEquals(1.0, accuracy)
    }
}
