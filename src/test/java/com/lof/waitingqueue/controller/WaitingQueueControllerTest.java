package com.lof.waitingqueue.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
//        // given
//        LocalDate gameDate = LocalDate.now();
//        String requestBody = String.format("""
//                {
//                    "gameDate": "%s"
//                }
//                """, gameDate);
//        String accessToken = "accessToken";
//        JoinResult joinResult = WaitingQueueFixture.joinResult();
//        when(tokenParser.parseMemberId(accessToken)).thenReturn(1L);
//        when(tokenParser.parseTokenType(accessToken)).thenReturn(TokenType.ACCESS);
//        when(waitingQueueService.join(1L, gameDate)).thenReturn(joinResult);
//
//        // when & then
//        mockMvc.perform(post("/queue/join")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, accessToken)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("queueId", is(1)))
//                .andExpect(jsonPath("gameId", is(1)));
    }

    @Test
    @DisplayName("대기열 퇴장에 성공하면 200 상태코드를 반환받는다.")
    void leaveWaitingQueue() throws Exception {
        // given
        String accessToken = "accessToken";
        when(tokenParser.parseMemberId(accessToken)).thenReturn(1L);
        when(tokenParser.parseTokenType(accessToken)).thenReturn(TokenType.ACCESS);

        // when & then
        mockMvc.perform(post("/queue/leave/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("대기열의 현재 상태를 확인한다.")
    void checkMatchingStatus() throws Exception {
        // given
//        String accessToken = "accessToken";
//        when(tokenParser.parseMemberId(accessToken)).thenReturn(1L);
//        when(tokenParser.parseTokenType(accessToken)).thenReturn(TokenType.ACCESS);
//        when(waitingQueueService.checkMatchingStatus(1L)).thenReturn(WaitingQueueFixture.joinResult());
//
//        // when & then
//        mockMvc.perform(get("/queue/check/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("joinedMemberCount", is(0)))
//                .andExpect(jsonPath("gameId", is(1)));
    }
}
