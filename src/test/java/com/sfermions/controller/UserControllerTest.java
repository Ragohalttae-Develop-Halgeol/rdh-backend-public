package com.sfermions.controller;

import java.util.List;

import org.jboss.jandex.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfermions.dto.user.AddUserRequest;
import com.sfermions.dto.user.UpdateUserRequest;
import com.sfermions.entity.User;
import com.sfermions.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
public class UserControllerTest {
    
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();
        userRepository.deleteAll();
    }

    @DisplayName("addUser: 사용자 추가에 성공한다.")
    @Test
    public void addUser() throws Exception {
        // given
        final String url = "/api/users"; // 올바른 URI 경로로 수정
        final String name = "test_name";
        final AddUserRequest userRequest = new AddUserRequest();

        // 객체 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // when
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<User> users = userRepository.findAll(); // articles -> users로 변경
        
        assertThat(users.size()).isEqualTo(1); // 크기가 1인지 검증
        assertThat(users.get(0).getName()).isEqualTo(name); // getTitle(), getContent() -> getName()으로 수정
    }

    @DisplayName("findAllUsers: 사용자 목록 조회에 성곤한다.")
    @Test
    public void findAllUsers() throws Exception {
        final String url = "/api/users";
        final String name = "test_name";

        userRepository.save(User.builder()
                .name(name)
                .build());

        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));
        
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));
    }
    
    @DisplayName("deleteUser: 사용자 삭제에 성공한다.")
    @Test
    public void deleteUser() throws Exception {
        final String url = "/api/users/{id}";
        final String name = "test_name";

        User savedUser = userRepository.save(User.builder()
                .name(name)
                .build());

        mockMvc.perform(delete(url, savedUser.getId()))
                .andExpect(status().isOk());
        
        List<User> users = userRepository.findAll();

        assertThat(users).isEmpty();
    }

    @DisplayName("updatedUser: 사용자 수정에 성공한다.")
    @Test
    public void updateUser() throws Exception {
        final String url = "/api/users/{id}";
        final String name = "test_name";

        User savedUser = userRepository.save(User.builder()
                .name(name)
                .build());
        
        final String newName = "new_name";

        UpdateUserRequest request = new UpdateUserRequest(newName);

        ResultActions result = mockMvc.perform(put(url, savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk());

        User user = userRepository.findById(savedUser.getId()).get();

        assertThat(user.getName()).isEqualTo(newName);
    }
}