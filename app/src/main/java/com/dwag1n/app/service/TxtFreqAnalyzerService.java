package com.dwag1n.app.service;

import com.dwag1n.app.common.dto.TopFreqWordsDto;
import com.dwag1n.app.common.trie.Trie;
import com.dwag1n.app.exceptions.exception.BizException;
import com.dwag1n.app.service.aws.S3InfrastructureService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.dwag1n.app.exceptions.enums.impl.ExceptionEnum.NO_SUCH_FILE_IN_S3;

/**
 * @author: Duo Wang
 * @version: v1.0
 */

@Service
public class TxtFreqAnalyzerService {
    private final S3InfrastructureService s3InfrastructureService;

    public TxtFreqAnalyzerService(@Qualifier("s3InfrastructureService") S3InfrastructureService s3InfrastructureService) {
        this.s3InfrastructureService = s3InfrastructureService;
    }

    @Cacheable(value = "txtFreq", key = "#txtFileName + #k")
    public List<TopFreqWordsDto> analyzeTxtFreqByTrie(String txtFileName, int k) throws InterruptedException, IOException {
        // 1. Check if the file exists in S3
        if (!s3InfrastructureService.checkIfFileExists(txtFileName)) {
            throw new BizException(NO_SUCH_FILE_IN_S3);
        }
        // 2. Download the file from S3 to local
        String localFilePath = s3InfrastructureService.downloadLargeTextFileFromS3Bucket(txtFileName);
        // 3. Read the file to string
        String fileContent = new String(Files.readAllBytes(Paths.get(localFilePath)));
        // 4. Analyze the frequency of words using Trie
        return analyzeFrequencyByTrie(fileContent, k);
    }
    @Cacheable(value = "txtFreq", key = "#txtFileName + #k")
    public List<TopFreqWordsDto> analyzeTxtFreqByHashMap(String txtFileName, int k) {
        // 1. Check if the file exists in S3
        if (!s3InfrastructureService.checkIfFileExists(txtFileName)) {
            throw new BizException(NO_SUCH_FILE_IN_S3);
        }
        // 2. Load the file content from S3
        String fileContent = s3InfrastructureService.loadLargeTextFileFromS3Bucket(txtFileName);
        // 3. Analyze the frequency of words using HashMap
        return analyzeFrequencyByHashMap(fileContent, k);
    }

    private List<TopFreqWordsDto> analyzeFrequencyByHashMap(String fileContent, int k) {
        Map<String, Integer> wordCount = new HashMap<>();
        Arrays.stream(fileContent.toLowerCase().split("\\P{L}+")) // Split the content into words
                .filter(word -> !word.isEmpty()) // Ignore empty words
                .forEach(word -> wordCount.put(word, wordCount.getOrDefault(word, 0) + 1)); // Count the frequency of each word
        AtomicInteger rank = new AtomicInteger(1); // Initialize rank counter
        return wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) // Sort the words by frequency in descending order
                .limit(k) // Get the top k words
                .map(entry -> TopFreqWordsDto.builder().word(entry.getKey()).frequency(entry.getValue()).rank(rank.getAndIncrement()).build()) // Convert the entry to TopFreqWordsDto and set rank
                .collect(Collectors.toList());
    }

    private List<TopFreqWordsDto> analyzeFrequencyByTrie(String fileContent, int k) {
        Trie trie = new Trie();
        Arrays.stream(fileContent.toLowerCase().split("\\P{L}+")) // Split the content into words
                .filter(word -> !word.isEmpty()) // Ignore empty words
                .forEach(trie::insert); // Insert the word into the trie
        AtomicInteger rank = new AtomicInteger(1); // Initialize rank counter
        return Arrays.stream(fileContent.toLowerCase().split("\\P{L}+")) // Split the content into words
                .filter(word -> !word.isEmpty()) // Ignore empty words
                .distinct() // Remove duplicate words
                .map(word -> TopFreqWordsDto.builder().word(word).frequency(trie.search(word)).rank(rank.getAndIncrement()).build()) // Convert the word to TopFreqWordsDto and set rank
                .sorted((a, b) -> b.getFrequency() - a.getFrequency()) // Sort the words by frequency in descending order
                .limit(k) // Get the top k words
                .collect(Collectors.toList());
    }
}
