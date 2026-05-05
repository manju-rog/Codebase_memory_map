package com.manju.repolens.controller;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.*;
import com.oracle.bmc.generativeaiinference.requests.ChatRequest;
import com.oracle.bmc.generativeaiinference.responses.ChatResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/simple")
    public Map<String, String> testSimple(@RequestParam(defaultValue = "hi how are you") String prompt) {
        Map<String, String> result = new HashMap<>();
        
        try {
            // OCI Configuration
            String configFile = "./OCI_ApiKey/config";
            String compartmentId = "ocid1.compartment.oc1..aaaaaaaacqxr3j2qbhntidg2s32gb67lv7ydpbfkvcazqvnjfsaxj3vdxr7a";
            String endpoint = "https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com";
            String modelId = "openai.gpt-4.1";
            
            // Load config
            ConfigFileReader.ConfigFile config = ConfigFileReader.parse(configFile, "DEFAULT");
            AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);
            
            // Create client
            GenerativeAiInferenceClient client = GenerativeAiInferenceClient.builder()
                .endpoint(endpoint)
                .build(provider);
            
            // Create message
            List<ChatContent> contentList = new ArrayList<>();
            contentList.add(TextContent.builder().text(prompt).build());
            
            List<Message> messages = new ArrayList<>();
            messages.add(UserMessage.builder().content(contentList).build());
            
            // Create request
            ChatDetails chatDetails = ChatDetails.builder()
                .servingMode(OnDemandServingMode.builder().modelId(modelId).build())
                .compartmentId(compartmentId)
                .chatRequest(GenericChatRequest.builder()
                    .messages(messages)
                    .maxTokens(200)
                    .temperature(0.7)
                    .build())
                .build();
            
            ChatRequest request = ChatRequest.builder().chatDetails(chatDetails).build();
            
            // Call API
            ChatResponse response = client.chat(request);
            
            // Extract answer
            if (response.getChatResult() != null && response.getChatResult().getChatResponse() != null) {
                BaseChatResponse chatResponse = response.getChatResult().getChatResponse();
                
                if (chatResponse instanceof GenericChatResponse) {
                    GenericChatResponse genericResponse = (GenericChatResponse) chatResponse;
                    if (genericResponse.getChoices() != null && !genericResponse.getChoices().isEmpty()) {
                        ChatChoice choice = genericResponse.getChoices().get(0);
                        if (choice.getMessage() != null && choice.getMessage() instanceof AssistantMessage) {
                            List<ChatContent> contents = ((AssistantMessage) choice.getMessage()).getContent();
                            if (contents != null && !contents.isEmpty() && contents.get(0) instanceof TextContent) {
                                String answer = ((TextContent) contents.get(0)).getText();
                                
                                result.put("status", "success");
                                result.put("prompt", prompt);
                                result.put("answer", answer);
                                result.put("model", modelId);
                                return result;
                            }
                        }
                    }
                }
            }
            
            result.put("status", "error");
            result.put("message", "Could not extract answer from response");
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            result.put("error", e.getClass().getSimpleName());
        }
        
        return result;
    }
}
