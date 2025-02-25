package com.lof.waitingqueue.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.lof.auth.domain.TokenType;
import com.lof.global.ControllerTest;

class WaitingQueueControllerTest extends ControllerTest {

    @Test
    @DisplayName("대기열 입장에 성공하면, 입장한 대기열의 id를 반환한다.")
    void joinWaitingQueue() throws Exception {
        // given
        String accessToken = "accessToken";
        when(tokenParser.parseMemberId(accessToken)).thenReturn(1L);
        when(tokenParser.parseTokenType(accessToken)).thenReturn(TokenType.ACCESS);
        when(queueService.join(1L)).thenReturn(1L);

        // when & then
        mockMvc.perform(post("/queue/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("queueId", is(1)));
    }
}
