package com.iflytek.petwisdombackend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Value("${aliyun.api-key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://dashscope.aliyuncs.com/api/v1")
            .build();

    // AI问答
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> params) {
        String question = params.get("question");
        Map<String, Object> result = new HashMap<>();

        try {
            String response = webClient.post()
                    .uri("/services/aigc/text-generation/generation")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(JSON.toJSONString(Map.of(
                            "model", "qwen-turbo",
                            "input", Map.of(
                                    "messages", new Object[]{
                                            Map.of("role", "system", "content", "你是宠物万事通智能助手，专门回答宠物相关问题。"),
                                            Map.of("role", "user", "content", question)
                                    }
                            ),
                            "parameters", Map.of("result_format", "message")
                    )))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONObject json = JSON.parseObject(response);
            String answer = json.getJSONObject("output")
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            result.put("code", 200);
            result.put("data", answer);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "AI服务调用失败");
            result.put("data", getFallbackAnswer(question));
        }
        return result;
    }

    // AI图片识别（Base64方式）- 返回JSON格式
    @PostMapping("/recognize")
    public Map<String, Object> recognize(@RequestBody Map<String, String> params) {
        String base64Image = params.get("image");
        Map<String, Object> result = new HashMap<>();

        try {
            // 修改提示词，要求 AI 返回严格的 JSON 格式
            String prompt = "请分析这张宠物便便图片，严格按照以下JSON格式返回结果，只返回JSON，不要返回其他任何内容：\n" +
                    "{\n" +
                    "  \"consistency\": \"正常/偏软/偏硬/腹泻\",\n" +
                    "  \"color\": \"黄褐色/黑色/红色/灰色\",\n" +
                    "  \"score\": 85,\n" +
                    "  \"analysis\": \"简要分析（30字以内）\",\n" +
                    "  \"advice\": \"护理建议（20字以内）\"\n" +
                    "}\n" +
                    "如果图片不是便便，consistency填'无法识别'，color填'无法识别'，score填0，analysis描述图片内容。";

            String response = webClient.post()
                    .uri("/services/aigc/multimodal-generation/generation")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(JSON.toJSONString(Map.of(
                            "model", "qwen-vl-plus",
                            "input", Map.of(
                                    "messages", new Object[]{
                                            Map.of("role", "user", "content", new Object[]{
                                                    Map.of("type", "image", "image", base64Image),
                                                    Map.of("type", "text", "text", prompt)
                                            })
                                    }
                            ),
                            "parameters", Map.of("result_format", "message")
                    )))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONObject json = JSON.parseObject(response);
            String analysis = json.getJSONObject("output")
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            // 尝试提取 JSON
            String jsonStr = extractJSON(analysis);
            result.put("code", 200);
            result.put("data", jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "识别失败");
            result.put("data", "{\"consistency\":\"正常\",\"score\":85,\"analysis\":\"便便形态正常\",\"advice\":\"保持当前饮食\"}");
        }
        return result;
    }

    // 从 AI 返回文本中提取 JSON
    private String extractJSON(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start != -1 && end != -1 && start < end) {
            return text.substring(start, end + 1);
        }
        return "{\"analysis\":\"" + text.replace("\"", "\\\"") + "\"}";
    }

    // 降级回答
    private String getFallbackAnswer(String question) {
        if (question.contains("疫苗")) {
            return "狗狗疫苗通常每年接种一次。核心疫苗包括狂犬疫苗、犬瘟热/细小病毒疫苗。建议咨询兽医制定个性化接种计划。";
        } else if (question.contains("呕吐")) {
            return "猫咪偶尔吐毛球是正常的。如果频繁呕吐、伴随精神萎靡，建议及时就医。";
        } else if (question.contains("巧克力")) {
            return "绝对不能！巧克力对狗狗有毒，即使少量也可能引起呕吐、腹泻，严重时会抽搐甚至死亡。";
        }
        return "感谢您的提问！建议您咨询专业兽医获取更详细的信息。";
    }
}