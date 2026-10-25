package com.howtodoinjava.orderdesk.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsOrder() throws Exception {
        String body = """
                {"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":10.00}]}
                """;
        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(21.60));
    }

    @Test
    void returns400ForMissingCustomer() throws Exception {
        String body = """
                {"customerId":"","lines":[{"sku":"BOOK","quantity":1,"unitPrice":5}]}
                """;
        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returns404ForUnknownOrder() throws Exception {
        mockMvc.perform(get("/orders/999")).andExpect(status().isNotFound());
    }
}
