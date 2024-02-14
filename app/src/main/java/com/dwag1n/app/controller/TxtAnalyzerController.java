package com.dwag1n.app.controller;

import com.dwag1n.app.common.dto.TopFreqRequestDto;
import com.dwag1n.app.common.dto.TopFreqWordsDto;
import com.dwag1n.app.service.TxtFreqAnalyzerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/txt-analyzer")
@Tag(name = "TxtAnalyzerController", description = "The TxtAnalyzerController API")
public class TxtAnalyzerController {
    private final TxtFreqAnalyzerService txtFreqAnalyzerService;

    public TxtAnalyzerController(TxtFreqAnalyzerService txtFreqAnalyzerService) {
        this.txtFreqAnalyzerService = txtFreqAnalyzerService;
    }

    // --------------GetMapping--------------
    @Operation(summary = "Analyze the frequency of words in a txt file with large size in GetMapping method",
               description = "Analyze the frequency of words in a txt file with large size in GetMapping method",
               tags = {"TxtAnalyzerController"})
    @RolesAllowed({"ADMIN"})
    @GetMapping(value = "/get/largeFile", produces = "application/json")
    public ResponseEntity<List<TopFreqWordsDto>> analyzeTxtFreqWithLargeFile(@RequestParam String txtFileName,
                                                                             @RequestParam int k) throws InterruptedException, IOException {
        return ResponseEntity.ok(txtFreqAnalyzerService.analyzeTxtFreqByTrie(txtFileName, k));
    }

    @Operation(summary = "Analyze the frequency of words in a txt file with normal size in GetMapping method",
               description = "Analyze the frequency of words in a txt file with normal size in GetMapping method",
               tags = {"TxtAnalyzerController"})
    @RolesAllowed({"USER"})
    @GetMapping(value = "/get/normalFile", produces = "application/json")
    public ResponseEntity<List<TopFreqWordsDto>> analyzeTxtFreqWithNormalFile(@RequestParam String txtFileName,
                                                                              @RequestParam int k) {
        return ResponseEntity.ok(txtFreqAnalyzerService.analyzeTxtFreqByHashMap(txtFileName, k));
    }

    // --------------PostMapping--------------
    @Operation(summary = "Analyze the frequency of words in a txt file with large size in PostMapping method",
               description = "Analyze the frequency of words in a txt file with large size in PostMapping method",
               tags = {"TxtAnalyzerController"})
    @RolesAllowed({"ADMIN"})
    @PostMapping(value = "/post/largeFile", produces = "application/json")
    public ResponseEntity<List<TopFreqWordsDto>> analyzeTxtFreqWithLargeFile(@RequestBody TopFreqRequestDto requestDto) throws InterruptedException, IOException {
        return ResponseEntity.ok(txtFreqAnalyzerService.analyzeTxtFreqByTrie(requestDto.getTxtFileName(), requestDto.getK()));
    }

    @Operation(summary = "Analyze the frequency of words in a txt file with normal size in PostMapping method",
               description = "Analyze the frequency of words in a txt file with normal size in PostMapping method",
               tags = {"TxtAnalyzerController"})
    @RolesAllowed({"USER"})
    @PostMapping(value = "/post/normalFile", produces = "application/json")
    public ResponseEntity<List<TopFreqWordsDto>> analyzeTxtFreqWithNormalFile(@RequestBody TopFreqRequestDto requestDto) {
        return ResponseEntity.ok(txtFreqAnalyzerService.analyzeTxtFreqByHashMap(requestDto.getTxtFileName(), requestDto.getK()));
    }
}
