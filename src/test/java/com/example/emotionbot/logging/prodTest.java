package com.example.emotionbot.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("prod")
public class prodTest {


    @Test
    @DisplayName("error,info,warn 이 만들어진다")
    public void prod_환경_작동_확인(){
    }

}
