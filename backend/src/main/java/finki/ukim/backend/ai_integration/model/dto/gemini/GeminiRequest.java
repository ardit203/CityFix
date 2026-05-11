package finki.ukim.backend.ai_integration.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequest {

    private List<Content> contents;
    private GenerationConfig generationConfig;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String role;
        private List<Part> parts;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Part {
        private String text;

        @JsonProperty("inline_data")
        private InlineData inlineData;

        // Text-only part constructor
        public Part(String text) {
            this.text = text;
        }

        // Image part constructor
        public Part(InlineData inlineData) {
            this.inlineData = inlineData;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InlineData {
        @JsonProperty("mime_type")
        private String mimeType;
        private String data; // Base64 encoded
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerationConfig {
        @JsonProperty("response_mime_type")
        private String responseMimeType;
    }
}
