package org.example.expert;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.expert.domain.user.dto.response.UserSimpleResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class ExpertApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    @Rollback(false)
    void sampleDataUser() {
        for (int idx = 812778; idx < 1000000; idx++) {
            String random = RandomStringUtils.randomAlphanumeric(10);
            String nickname = "[user " + idx + "]" + random;
            userRepository.save(User.builder()
                    .email(nickname + "@gmail.com")
                    .password(passwordEncoder.encode("1234"))
                    .nickname(nickname)
                    .userRole(UserRole.USER)
                    .build());
        }
    }

    @RepeatedTest(value = 50)
    void findUser() {
        String nickname = "[user 999999]a9EEHZ7KDg";
        User user = userRepository.findByNickname(nickname);
    }

    @RepeatedTest(value = 50)
    void findUser2() {
        String nickname = "[user 999999]a9EEHZ7KDg";
        UserSimpleResponse user = userRepository.findByMinimumColumnNickName(nickname);
    }
}
