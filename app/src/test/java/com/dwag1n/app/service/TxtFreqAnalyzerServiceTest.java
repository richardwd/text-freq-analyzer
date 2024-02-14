package com.dwag1n.app.service;

import com.dwag1n.app.common.dto.TopFreqWordsDto;
import com.dwag1n.app.exceptions.exception.BizException;
import com.dwag1n.app.service.aws.S3InfrastructureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author: Duo Wang
 * @createDate: 2/14/2024 - 2:41 PM
 * @version: v1.0
 */
class TxtFreqAnalyzerServiceTest {

    @Mock
    private S3InfrastructureService s3InfrastructureService;

    private TxtFreqAnalyzerService txtFreqAnalyzerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        txtFreqAnalyzerService = new TxtFreqAnalyzerService(s3InfrastructureService);
    }

    @Test
    @DisplayName("analyzeTxtFreqWithNormalFile: should throw BizException when file does not exist in S3")
    void bizExceptionWhenFileDoesNotExistInS3() {
        String txtFileName = "test.txt";
        int k = 10;

        when(s3InfrastructureService.checkIfFileExists(txtFileName)).thenReturn(false);
        assertThrows(BizException.class, () -> txtFreqAnalyzerService.analyzeTxtFreqByTrie(txtFileName, k));
    }

    @Test
    @DisplayName("analyzeTxtFreqWithLargeFile: should return correct result when file exists and can be downloaded and read successfully")
    void shouldReturnCorrectResultWhenFileExistsAndCanBeDownloadedAndReadSuccessfully() throws InterruptedException, IOException {
        // Mock S3InfrastructureService behavior
        String txtFileName = "example.txt";
        String fileContent = "You and me and you and him."; // and: 3, you: 2, me: 1, him: 1
        when(s3InfrastructureService.checkIfFileExists(txtFileName)).thenReturn(true);
        when(s3InfrastructureService.downloadLargeTextFileFromS3Bucket(txtFileName)).thenReturn("localFilePath");

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class);
             MockedStatic<Paths> mockedPaths = Mockito.mockStatic(Paths.class)) { // Mock Paths.get and Files.readAllBytes in the same context
            mockedPaths.when(() -> Paths.get(anyString())).thenReturn(mock(Path.class));
            mockedFiles.when(() -> Files.readAllBytes(any())).thenReturn(fileContent.getBytes());
            // Call the method
            List<TopFreqWordsDto> result = txtFreqAnalyzerService.analyzeTxtFreqByTrie(txtFileName, 5);
            verify(s3InfrastructureService, times(1)).checkIfFileExists(txtFileName);
            verify(s3InfrastructureService, times(1)).downloadLargeTextFileFromS3Bucket(txtFileName);
            TopFreqWordsDto top1 = TopFreqWordsDto.builder().word("and").frequency(3).rank(1).build();
            TopFreqWordsDto top2 = TopFreqWordsDto.builder().word("you").frequency(2).rank(2).build();
            TopFreqWordsDto top3 = TopFreqWordsDto.builder().word("me").frequency(1).rank(3).build();
            TopFreqWordsDto top4 = TopFreqWordsDto.builder().word("him").frequency(1).rank(4).build();
            assertEquals(List.of(top1, top2, top3, top4), result);
        }

    }

}