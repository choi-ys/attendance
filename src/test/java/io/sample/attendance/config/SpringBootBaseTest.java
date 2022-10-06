package io.sample.attendance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = AutowireMode.ALL)
@Transactional
public class SpringBootBaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    public <T> ResultActions post(String urlTemplate, T body, Object... path) throws Exception {
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

    public ResultActions get(String urlTemplate, Object... path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public <T> ResultActions delete(String urlTemplate, Object... path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public <T> T as(ResultActions resultActions, Class<T> clazz) throws Exception {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), clazz);
    }

    public static String getLocation(ResultActions resultActions) {
        return resultActions.andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
    }
}
