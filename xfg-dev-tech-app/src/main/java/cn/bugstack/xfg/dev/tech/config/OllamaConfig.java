package cn.bugstack.xfg.dev.tech.config;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class OllamaConfig {

    @Bean
    public OllamaApi ollamaApi(@Value("${spring.ai.ollama.base-url}") String baseUrl) {
        return new OllamaApi(baseUrl);
    }

    @Bean
    public OllamaChatClient ollamaChatClient(OllamaApi ollamaApi) {
        return new OllamaChatClient(ollamaApi);
    }

    /**
     * 创建一个 TokenTextSplitter 实例，它可能用于将文本按令牌（Token）进行分割。在自然语言处理中，将文本分割成令牌是常见的预处理步骤。
     * @return
     */
    @Bean
    public TokenTextSplitter tokenTextSplitter(){
        return new TokenTextSplitter();
    }

    /**
     * 创建一个 SimpleVectorStore 实例，用于存储向量数据。
     * 步骤：
     * 创建一个 OllamaEmbeddingClient 实例，该客户端依赖于 OllamaApi，用于从 Ollama 服务获取文本的嵌入向量。
     * 使用 withDefaultOptions 方法设置默认选项，指定使用 nomic-embed-text 模型。
     * @param ollamaApi
     * @return
     */
    @Bean
    public SimpleVectorStore simpleVectorStore(OllamaApi ollamaApi){
        OllamaEmbeddingClient ollamaEmbeddingClient = new OllamaEmbeddingClient(ollamaApi);
        ollamaEmbeddingClient.withDefaultOptions(OllamaOptions.create().withModel("nomic-embed-text"));
        return new SimpleVectorStore(ollamaEmbeddingClient);
    }

    /**
     * 功能：创建一个 PgVectorStore 实例，用于将向量数据存储到 PostgreSQL 数据库中。
     * 参数：
     * ollamaApi：依赖于 OllamaApi，用于创建 OllamaEmbeddingClient 以获取文本的嵌入向量。
     * jdbcTemplate：Spring 的 JdbcTemplate 用于与 PostgreSQL 数据库进行交互。
     * @param ollamaApi
     * @param jdbcTemplate
     * @return
     */
    @Bean
    public PgVectorStore pgVectorStore(OllamaApi ollamaApi, JdbcTemplate jdbcTemplate){
        OllamaEmbeddingClient ollamaEmbeddingClient = new OllamaEmbeddingClient(ollamaApi);
        ollamaEmbeddingClient.withDefaultOptions(OllamaOptions.create().withModel("nomic-embed-text"));
        return new PgVectorStore(jdbcTemplate, ollamaEmbeddingClient);
    }

}
