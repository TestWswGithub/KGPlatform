package com.lingjoin.common.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
    @Value("${docker.service.addr}")
    private String dockerServiveAddr;

    public String getDockerClient() {
        return dockerClient;
    }

    public void setDockerClient(String dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Value("${docker.client}")
    private String dockerClient;

    public String getDockerServiveAddr() {
        return dockerServiveAddr;
    }

    public void setDockerServiveAddr(String dockerServiveAddr) {
        this.dockerServiveAddr = dockerServiveAddr;
    }
}
