package org.acme.test;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import dev.langchain4j.model.embedding.BgeSmallEnEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

@Path("/in-process-embedding")
public class InProcessEmbeddingResource {

    @Inject
    BgeSmallEnEmbeddingModel typedModel;

    @Inject
    EmbeddingModel embeddingModel;

    @POST
    public String computeEmbedding(String sentence) {
        var r1 = typedModel.embed(sentence);
        var r2 = embeddingModel.embed(sentence);

        return "bgeSmallEnEmbeddingModel: " + r1.content().dimensions() + "\n" + "embeddingModel: "
                + r2.content().dimensions();
    }

}
