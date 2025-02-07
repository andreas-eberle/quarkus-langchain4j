package io.quarkiverse.langchain4j.pinecone.runtime;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Represents a response to a Query operation.
 * See the <a href="https://docs.pinecone.io/reference/query">API documentation</a>.
 */
@RegisterForReflection
public class QueryResponse {

    private final List<VectorMatch> matches;

    private String metadata;

    @JsonCreator
    public QueryResponse(List<VectorMatch> matches, String metadata) {
        this.matches = matches;
        this.metadata = metadata;
    }

    public List<VectorMatch> getMatches() {
        return matches;
    }

    public String getMetadata() {
        return metadata;
    }

}
