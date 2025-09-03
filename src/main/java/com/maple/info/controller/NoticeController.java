package com.maple.info.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.info.vo.NoticeVo;

@RestController
public class NoticeController {

	// application properties에 나중에 옮기기
    private static final String PROJECT_ID = "74071629";
    private static final String PRIVATE_TOKEN = "glpat-NiKXmqc9zb2s42o1cO4Ai286MQp1OmNxeXR2Cw.01.121pukiz8";
    private static final String GITLAB_API_TREE = "https://gitlab.com/api/v4/projects/%s/repository/tree?path=notices&ref=main&private_token=%s";

    @GetMapping("/api/notice/gitlab")
    public List<NoticeVo> getNoticesFromGitlab() {
        List<NoticeVo> todayNotices = new ArrayList<>();
        LocalDate today = LocalDate.now();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // 1️. GitLab에서 파일 트리 가져오기 
            String treeUrl = String.format(GITLAB_API_TREE, PROJECT_ID, PRIVATE_TOKEN);
            HttpGet treeGet = new HttpGet(treeUrl);
            treeGet.addHeader("User-Agent", "Mozilla/5.0");

            try (CloseableHttpResponse treeResponse = client.execute(treeGet)) {
                String treeJson = EntityUtils.toString(treeResponse.getEntity(), "UTF-8");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(treeJson);

                // 2️. blob 타입(파일)만 필터링
                for (JsonNode node : root) {
                    if (!"blob".equals(node.get("type").asText())) continue;

                    String filePath = node.get("path").asText(); // notices/2025-09-01_test1_2025-09-07.md
                    String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                    String[] parts = fileName.split("_");
                    if (parts.length < 3) continue;

                    LocalDate startDate = LocalDate.parse(parts[0]);
                    LocalDate endDate = LocalDate.parse(parts[2].replace(".md", ""));
                    String title = parts[1];

                    if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
                        // 3️. 파일 내용 가져오기
                        String encodedFilePath = filePath.replace("/", "%2F");
                        String fileUrl = String.format(
                                "https://gitlab.com/api/v4/projects/%s/repository/files/%s/raw?ref=main&private_token=%s",
                                PROJECT_ID,
                                encodedFilePath,
                                PRIVATE_TOKEN
                        );

                        HttpGet fileGet = new HttpGet(fileUrl);
                        fileGet.addHeader("User-Agent", "Mozilla/5.0");

                        try (CloseableHttpResponse fileResponse = client.execute(fileGet)) {
                            String content = EntityUtils.toString(fileResponse.getEntity(), "UTF-8");
                            todayNotices.add(new NoticeVo(filePath, title, content));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return todayNotices;
    }
}
