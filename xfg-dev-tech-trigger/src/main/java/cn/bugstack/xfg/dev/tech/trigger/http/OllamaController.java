package cn.bugstack.xfg.dev.tech.trigger.http;

import cn.bugstack.xfg.dev.tech.api.IAiService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
//import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/ollama/")
public class OllamaController implements IAiService {
    @Resource
    private OllamaChatClient chatClient;

//    /**
//     * curl http://localhost:8090/api/v1/ollama/generate?model=deepseek-r1:1.5b&message=1+1
//     */
//    @RequestMapping(value = "generate", method = RequestMethod.GET)
//    @Override
//    public ChatResponse generate(@RequestParam String model, @RequestParam String message) {
//        return chatClient.call(new Prompt(
//                message,
//                OpenAiChatOptions.builder()
//                        .withModel(model)
//                        .build()
//        ));
//    }

    /**
     * curl http://localhost:8090/api/v1/ollama/generate_stream?model=deepseek-r1:1.5b&message=1+1
     */
    @RequestMapping(value = "generate_stream", method = RequestMethod.GET)
    public Flux<ChatResponse> generateStream(@RequestParam String model, @RequestParam String message) {
        return chatClient.stream(new Prompt(
                message,
                OllamaOptions.create()
                        .withModel(model)
        ));
    }
}
