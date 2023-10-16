package io.quarkiverse.langchain4j.test;

import static dev.langchain4j.model.input.structured.StructuredPromptProcessor.toPrompt;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import io.quarkus.test.QuarkusUnitTest;

/**
 * Copied from {@code dev.langchain4j.model.input.structured.StructuredPromptProcessorTest}
 * Meant to ensure that the Quarkus integration works as expected
 */
class StructuredPromptProcessorTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @StructuredPrompt("Hello, my name is {{name}}")
    static class Greeting {

        private String name;
    }

    @Test
    void test_prompt_with_single_variable() {
        Greeting structuredPrompt = new Greeting();
        structuredPrompt.name = "Klaus";

        Prompt prompt = toPrompt(structuredPrompt);

        assertThat(prompt.text()).isEqualTo("Hello, my name is Klaus");
    }

    @StructuredPrompt("Hello, my name is {{wrapper.name}}")
    static class WrappedGreeting {

        private Wrapper wrapper;

        static class Wrapper {
            private String name;
        }

    }

    @Test
    void test_prompt_with_single_variable_in_wrapper() {
        WrappedGreeting structuredPrompt = new WrappedGreeting();
        WrappedGreeting.Wrapper wrapper = new WrappedGreeting.Wrapper();
        wrapper.name = "Klaus";
        structuredPrompt.wrapper = wrapper;

        Prompt prompt = toPrompt(structuredPrompt);

        assertThat(prompt.text()).isEqualTo("Hello, my name is Klaus");
    }

    @StructuredPrompt({
            "Suggest tasty {{dish}} recipes that can be prepared in {{maxPreparationTime}} minutes.",
            "I have only {{ingredients}} in my fridge.",
    })
    static class SuggestRecipes extends SuggestRecipesSuper {

        private String dish;
        private int maxPreparationTime;
        private List<String> ingredients;
    }

    static class SuggestRecipesSuper {
        private List<String> ingredients;
    }

    @Test
    void test_prompt_with_multiple_variables() {
        SuggestRecipes structuredPrompt = new SuggestRecipes();
        structuredPrompt.dish = "salad";
        structuredPrompt.maxPreparationTime = 5;
        structuredPrompt.ingredients = asList("Tomato", "Cucumber", "Onion");

        Prompt prompt = toPrompt(structuredPrompt);

        assertThat(prompt.text())
                .isEqualTo(
                        "Suggest tasty salad recipes that can be prepared in 5 minutes.\nI have only [Tomato, Cucumber, Onion] in my fridge.");
    }

    @StructuredPrompt("Example of numbers with floating point: {{nDouble}}, {{nFloat}} and whole numbers: {{nInt}}, {{nShort}}, {{nLong}}")
    static class VariousNumbers {

        private double nDouble;
        private float nFloat;
        private int nInt;
        private short nShort;
        private long nLong;
    }

    @Test
    void test_prompt_with_various_number_types() {
        VariousNumbers numbers = new VariousNumbers();
        numbers.nDouble = 17.15;
        numbers.nFloat = 1;
        numbers.nInt = 2;
        numbers.nShort = 10;
        numbers.nLong = 12;

        Prompt prompt = toPrompt(numbers);

        assertThat(prompt.text())
                .isEqualTo("Example of numbers with floating point: 17.15, 1.0 and whole numbers: 2, 10, 12");
    }
}
