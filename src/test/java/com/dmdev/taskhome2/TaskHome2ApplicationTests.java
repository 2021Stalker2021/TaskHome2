package com.dmdev.taskhome2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TaskHome2ApplicationTests {

    @Test
    void contextLoads() {
    }

}
