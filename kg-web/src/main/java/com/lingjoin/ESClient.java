package com.lingjoin;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class ESClient {



    @Value("${es.host.name}")
    private String esHostName;

    @Value("${es.host.port}")
    private Integer port;

    @Bean
    public RestHighLevelClient getEsClient(){
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(esHostName, port)));
    }



}
