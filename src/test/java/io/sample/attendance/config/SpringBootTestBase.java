package io.sample.attendance.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = AutowireMode.ALL)
@AutoConfigureRestDocs
@Transactional
public class SpringBootTestBase {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EntityManager entityManager;

    public <T> T executeWithPersistContextClear(Supplier<T> supplier) {
        try {
            return supplier.get();
        } finally {
            entityManager.clear();
        }
    }

    @SneakyThrows
    public <T> ResultActions post(String urlTemplate, T body, Object... path) {
        return mockMvc.perform(MockMvcRequestBuilders.post(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body))
        );
    }

    public ResultActions get(String urlTemplate) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public ResultActions get(String urlTemplate, Object path) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.get(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public ResultActions get(String urlTemplate, MultiValueMap<String, String> paramMap) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
            .params(paramMap)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public ResultActions delete(String urlTemplate, Object... path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public <T> T as(ResultActions resultActions, Class<T> clazz) throws Exception {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), clazz);
    }

    public <T> T as(ResultActions resultActions, TypeReference<T> type) throws Exception {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), type);
    }
}
