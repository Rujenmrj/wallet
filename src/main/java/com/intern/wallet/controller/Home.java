package com.intern.wallet.controller;

import com.intern.wallet.shared.ResponseCodes;
import com.intern.wallet.shared.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class Home {
    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping(){
        log.info("received ping");
        return ResponseEntity.ok(new ApiResponse<>(true, ResponseCodes.SUCCESS.getMessageKey(),"pong"));
    }
}
