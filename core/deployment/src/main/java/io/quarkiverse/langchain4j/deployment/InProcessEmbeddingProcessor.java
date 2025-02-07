package io.quarkiverse.langchain4j.deployment;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import org.jboss.jandex.DotName;

import dev.langchain4j.model.embedding.EmbeddingModel;
import io.quarkiverse.langchain4j.deployment.items.InProcessEmbeddingBuildItem;
import io.quarkiverse.langchain4j.runtime.InProcessEmbeddingRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;

/**
 * Generate a local embedding build item for each local embedding model available in the classpath.
 * Note that the user must have the dependency for the model in their pom.xml/build.gradle.
 */
public class InProcessEmbeddingProcessor {

    // https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2
    @BuildStep
    InProcessEmbeddingBuildItem all_minilm_l6_v2_q() {
        if (QuarkusClassLoader
                .isClassPresentAtRuntime("dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel")) {
            return new InProcessEmbeddingBuildItem("all-minilm-l6-v2-q",
                    "dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel",
                    "all-minilm-l6-v2-q.onnx", "bert-vocabulary-en.txt");
        } else {
            return null;
        }
    }

    // https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2
    @BuildStep
    InProcessEmbeddingBuildItem all_minilm_l6_v2() {
        if (QuarkusClassLoader.isClassPresentAtRuntime("dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel")) {
            return new InProcessEmbeddingBuildItem("all-minilm-l6-v2",
                    "dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel",
                    "all-minilm-l6-v2.onnx", "bert-vocabulary-en.txt");
        } else {
            return null;
        }
    }

    // https://huggingface.co/neuralmagic/bge-small-en-v1.5-quant
    @BuildStep
    InProcessEmbeddingBuildItem bge_small_en_q() {
        if (QuarkusClassLoader.isClassPresentAtRuntime("dev.langchain4j.model.embedding.BgeSmallEnQuantizedEmbeddingModel")) {
            return new InProcessEmbeddingBuildItem("bge-small-en-q",
                    "dev.langchain4j.model.embedding.BgeSmallEnQuantizedEmbeddingModel",
                    "bge-small-en-q.onnx", "bert-vocabulary-en.txt");
        } else {
            return null;
        }
    }

    // https://huggingface.co/BAAI/bge-small-en-v1.5
    @BuildStep
    InProcessEmbeddingBuildItem bge_small_en() {
        if (QuarkusClassLoader.isClassPresentAtRuntime("dev.langchain4j.model.embedding.BgeSmallEnEmbeddingModel")) {
            return new InProcessEmbeddingBuildItem("bge-small-en", "dev.langchain4j.model.embedding.BgeSmallEnEmbeddingModel",
                    "bge-small-en.onnx", "bert-vocabulary-en.txt");
        } else {
            return null;
        }
    }

    @BuildStep
    InProcessEmbeddingBuildItem bge_small_zh_q() {
        if (QuarkusClassLoader.isClassPresentAtRuntime("dev.langchain4j.model.embedding.BgeSmallZhQuantizedEmbeddingModel")) {
            return new InProcessEmbeddingBuildItem("bge-small-zh-q",
                    "dev.langchain4j.model.embedding.BgeSmallZhQuantizedEmbeddingModel",
                    "bge-small-zh-q.onnx", "bge-small-zh-vocabulary.txt");
        } else {
            return null;
        }
    }

    // https://huggingface.co/BAAI/bge-small-zh
    @BuildStep
    InProcessEmbeddingBuildItem bge_small_zh() {
        if (QuarkusClassLoader.isClassPresentAtRuntime("dev.langchain4j.model.embedding.BgeSmallZhEmbeddingModel")) {
            return new InProcessEmbeddingBuildItem("bge-small-zh", "dev.langchain4j.model.embedding.BgeSmallZhEmbeddingModel",
                    "bge-small-zh.onnx", "bge-small-zh-vocabulary.txt");
        } else {
            return null;
        }
    }

    // https://huggingface.co/intfloat/e5-small-v2
    @BuildStep
    InProcessEmbeddingBuildItem e5_small_v2_q() {
        if (QuarkusClassLoader.isClassPresentAtRuntime("dev.langchain4j.model.embedding.E5SmallV2QuantizedEmbeddingModel")) {
            return new InProcessEmbeddingBuildItem("e5-small-v2-q",
                    "dev.langchain4j.model.embedding.E5SmallV2QuantizedEmbeddingModel",
                    "e5-small-v2-q.onnx", "bert-vocabulary-en.txt");
        } else {
            return null;
        }
    }

    // https://huggingface.co/intfloat/e5-small-v2
    @BuildStep
    InProcessEmbeddingBuildItem e5_small_v2() {
        if (QuarkusClassLoader.isClassPresentAtRuntime("dev.langchain4j.model.embedding.E5SmallV2EmbeddingModel")) {
            return new InProcessEmbeddingBuildItem("e5-small-v2", "dev.langchain4j.model.embedding.E5SmallV2EmbeddingModel",
                    "e5-small-v2.onnx", "bert-vocabulary-en.txt");
        } else {
            return null;
        }
    }

    // Expose a bean for each in process embedding model
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void exposeInProcessEmbeddingBeans(InProcessEmbeddingRecorder recorder,
            List<InProcessEmbeddingBuildItem> embeddings,
            BuildProducer<SyntheticBeanBuildItem> beanProducer) {

        for (InProcessEmbeddingBuildItem embedding : embeddings) {
            beanProducer.produce(SyntheticBeanBuildItem
                    .configure(DotName.createSimple(embedding.className()))
                    .types(EmbeddingModel.class)
                    .defaultBean()
                    .setRuntimeInit()
                    .scope(ApplicationScoped.class)
                    .supplier(recorder.instantiate(embedding.className()))
                    .done());
        }

    }

}
