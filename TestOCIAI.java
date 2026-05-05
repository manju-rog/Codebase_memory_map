import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.*;
import com.oracle.bmc.generativeaiinference.requests.ChatRequest;
import com.oracle.bmc.generativeaiinference.responses.ChatResponse;

import java.util.ArrayList;
import java.util.List;

public class TestOCIAI {
    
    public static void main(String[] args) {
        System.out.println("=== Testing OCI GenAI ===\n");
        
        try {
            // Configuration
            String configFile = "./OCI_ApiKey/config";
            String compartmentId = "ocid1.compartment.oc1..aaaaaaaacqxr3j2qbhntidg2s32gb67lv7ydpbfkvcazqvnjfsaxj3vdxr7a";
            String endpoint = "https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com";
            String modelId = "ocid1.generativeaimodel.oc1.iad.amaaaaaask7dceyah6tjdejjashngznsylutuhhvufukzb2g2ls54g2flsfq";
            
            System.out.println("1. Loading OCI config from: " + configFile);
            ConfigFileReader.ConfigFile config = ConfigFileReader.parse(configFile, "DEFAULT");
            AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);
            System.out.println("   ✓ Config loaded\n");
            
            System.out.println("2. Creating OCI GenAI client...");
            GenerativeAiInferenceClient client = GenerativeAiInferenceClient.builder()
                .endpoint(endpoint)
                .build(provider);
            System.out.println("   ✓ Client created\n");
            
            // Test prompt
            String testPrompt = "What is Spring Boot? Answer in one sentence.";
            System.out.println("3. Sending prompt: \"" + testPrompt + "\"");
            
            // Create message
            List<ChatContent> contentList = new ArrayList<>();
            contentList.add(TextContent.builder().text(testPrompt).build());
            
            List<Message> messages = new ArrayList<>();
            messages.add(UserMessage.builder()
                .content(contentList)
                .build());
            
            // Create chat request
            ChatDetails chatDetails = ChatDetails.builder()
                .servingMode(OnDemandServingMode.builder()
                    .modelId(modelId)
                    .build())
                .compartmentId(compartmentId)
                .chatRequest(GenericChatRequest.builder()
                    .messages(messages)
                    .maxTokens(500)
                    .temperature(0.7)
                    .build())
                .build();
            
            ChatRequest request = ChatRequest.builder()
                .chatDetails(chatDetails)
                .build();
            
            System.out.println("   ✓ Request prepared\n");
            
            System.out.println("4. Calling OCI GenAI API...");
            ChatResponse response = client.chat(request);
            System.out.println("   ✓ Response received\n");
            
            // Extract answer
            if (response.getChatResult() != null && 
                response.getChatResult().getChatResponse() != null) {
                
                BaseChatResponse chatResponse = response.getChatResult().getChatResponse();
                
                if (chatResponse instanceof GenericChatResponse) {
                    GenericChatResponse genericResponse = (GenericChatResponse) chatResponse;
                    if (genericResponse.getChoices() != null && !genericResponse.getChoices().isEmpty()) {
                        ChatChoice choice = genericResponse.getChoices().get(0);
                        if (choice.getMessage() != null && choice.getMessage() instanceof AssistantMessage) {
                            List<ChatContent> contents = ((AssistantMessage) choice.getMessage()).getContent();
                            if (contents != null && !contents.isEmpty() && contents.get(0) instanceof TextContent) {
                                String answer = ((TextContent) contents.get(0)).getText();
                                
                                System.out.println("=== SUCCESS! ===");
                                System.out.println("\nAI Answer:");
                                System.out.println(answer);
                                System.out.println("\n================\n");
                                return;
                            }
                        }
                    }
                }
            }
            
            System.out.println("Could not extract answer from response");
            
        } catch (Exception e) {
            System.err.println("\n=== ERROR ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
