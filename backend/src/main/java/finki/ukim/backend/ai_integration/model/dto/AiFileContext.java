package finki.ukim.backend.ai_integration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiFileContext {
    private String mimeType;  // e.g. "image/png", "image/jpeg"
    private String base64Data; // raw bytes encoded in Base64
}
