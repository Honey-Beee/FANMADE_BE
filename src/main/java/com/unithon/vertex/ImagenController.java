package com.unithon.vertex;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/images")
public class ImagenController {

    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    /**
     * 프롬프트 → PNG 바이트 반환(브라우저에서 바로 미리보기 가능)
     */
    @PostMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generate(
            @RequestParam String prompt,
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) Integer height
    ) throws Exception {
        byte[] png = imagenService.generatePng(prompt, width, height);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=generated.png")
                .body(png);
    }

    /**
     * base64로 바로 받고 싶은 경우
     */
    @PostMapping(value = "/generate/base64", produces = MediaType.TEXT_PLAIN_VALUE)
    public String generateBase64(
            @RequestParam String prompt,
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) Integer height
    ) {
        byte[] png = imagenService.generatePng(prompt, width, height);
        return java.util.Base64.getEncoder().encodeToString(png);
    }
}