package com.shike.snake.service.chatgpt;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.shike.snake.util.HttpUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shike02
 * @version 2023/3/9 10:34 PM.
 */
@Service
public class ChatgptService {
    private static final Gson GSON = new Gson();
    private static final String url = "https://api.openai.com/v1/chat/completions";

    public String getAnswer(List<String> queryContext) {
        String result = null;
        try {
            result = HttpUtils.doPostWithBody(url, buildBody(queryContext), chatgptHeaders());
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    private String buildBody(List<String> contents) {
        if (CollectionUtils.isEmpty(contents)) {
            return null;
        }
        OpenAIBody openAIBody = new OpenAIBody();
        openAIBody.setModel("gpt-3.5-turbo");
        openAIBody.setTemperature(0.7);

        List<OpenAIChat> openAIChatList = Lists.newArrayList();
        for (String content : contents) {
            OpenAIChat openAIChat = new OpenAIChat();
            openAIChat.setRole("user");
            openAIChat.setContent(content);
            openAIChatList.add(openAIChat);
        }

        openAIBody.setMessages(openAIChatList);
        return GSON.toJson(openAIBody);
    }

    private Map<String, String> chatgptHeaders() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "*/*");
        headerMap.put("Accept-Encoding", "gzip, deflate, br");
        headerMap.put("Accept-Connection", "keep-alive");
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", "Bearer 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");
        return headerMap;
    }

    @Getter
    @Setter
    class OpenAIChat {
        private String role;
        private String content;
    }

    @Getter
    @Setter
    class OpenAIBody {
        private String model;
        private Double temperature;
        private List<OpenAIChat> messages;
    }
}