package io.quarkiverse.langchain4j.samples;

import java.util.function.Supplier;

import jakarta.inject.Inject;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

public class MySmallMemoryProvider implements Supplier<ChatMemoryProvider> {

    @Inject
    ChatMemoryStore store;

    @Override
    public ChatMemoryProvider get() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(store)
                .build();
    }
}
