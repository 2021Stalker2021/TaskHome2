package com.dmdev.taskhome2;

import org.springframework.boot.SpringApplication;

public class TestTaskHome2Application {

    public static void main(String[] args) {
        SpringApplication.from(TaskHome2Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
