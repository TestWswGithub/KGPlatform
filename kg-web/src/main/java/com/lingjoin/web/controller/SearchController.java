package com.lingjoin.web.controller;

import com.alibaba.fastjson.JSON;
import com.lingjoin.common.anonation.LoginRequired;
import org.apache.http.HttpHost;
import org.elasticsearch.Build;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = {"null","http://172.16.1.184:8080"},allowCredentials = "true")
public class SearchController {


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @RequestMapping(value = "/content", produces = {"application/json;charset=UTF-8"})
    @LoginRequired
    public String search(String content,
                         @RequestParam(value = "createDate", required = false) String createDate,
                         @RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize) {


        return null;

    }

    /**
     * 创建索引(IndexAPI)
     */

//    public static void main(String[] args) throws IOException {
//
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(new HttpHost("172.16.1.140", 9200, "http")));
//
//        CreateIndexRequest request = new CreateIndexRequest("upload_doc");
//
//        request.settings(Settings.builder()
//                .put("index.number_of_shards", 3)
//                .put("index.number_of_replicas", 1)
//        );
//
//        HashMap<String, Object> textType = new HashMap<>();
//        textType.put("type","text");
//        textType.put("analyzer","ik_max_word");
//        HashMap<String, Object> dateType = new HashMap<>();
//        dateType.put("type","date");
//        dateType.put("format","yyyy-MM-dd HH:mm:ss||epoch_millis");
//        HashMap<String, Object> properties = new HashMap<>();
//        properties.put("uuid",textType);
//        properties.put("content",textType);
//        properties.put("title",textType);
//        properties.put("author",textType);
//        properties.put("date",dateType);
//        HashMap<String, Object> mapping = new HashMap<>();
//        mapping.put("properties",properties);
//        request.mapping(mapping);
//        System.out.println(JSON.toJSONString(mapping));
//        CreateIndexResponse createIndexResponse = client.indices().create(request,RequestOptions.DEFAULT);
//        boolean acknowledged = createIndexResponse.isAcknowledged();
//        System.out.println(acknowledged);
//        client.close();
//    }

    /**
     * 创建Document(DocumentAPI)
     */

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("172.16.1.140", 9200, "http")));



//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("content", "爱杰伦，爱周杰伦");
//        jsonMap.put("title", "我是周杰伦");
//        jsonMap.put("author", "周杰伦");
//        jsonMap.put("date", new Date());
//        IndexRequest indexRequest = new IndexRequest("posts")
//                .id(new Date().toString()).source(jsonMap);
//
//        IndexResponse index = client.index(indexRequest, RequestOptions.DEFAULT);
//        System.out.println(index);


        String queryTerm = "杰伦";

        SearchRequest request = new SearchRequest("doc");



        //总的SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

/**
 * matchQuery：会将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到。
 *
 * termQuery：不会对搜索词进行分词处理，而是作为一个整体与目标字段进行匹配，若完全匹配，则可查询到。
 */


        searchSourceBuilder.fetchSource(new String[]{"author", "title", "content", "date"}, new String[]{});
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(queryTerm,"author", "title", "content"));
        request.source(searchSourceBuilder);

        //起始页默认是0
//        searchSourceBuilder.from(0);
//        searchSourceBuilder.size(1);
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("author"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("content"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.sort("date", SortOrder.ASC);

        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        hits.forEach(new Consumer<SearchHit>() {
            @Override
            public void accept(SearchHit searchHit) {

            }
        });

        System.out.println(search);
        client.close();


    }


}
