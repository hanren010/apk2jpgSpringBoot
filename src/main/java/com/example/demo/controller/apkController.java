package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class apkController {

    @PostMapping("/processApk")
    public ResponseEntity<String> processApk(@RequestParam String filePath) throws IOException {
        getDex.extractDexFiles(filePath, "E:\\eswa\\pytorch_flask_service\\tempDEX");
        getManifest.extractManifest(filePath, "E:\\eswa\\pytorch_flask_service\\tempManifest");
        start.startDEX("E:\\eswa\\pytorch_flask_service\\tempDEX");
        write.writeManifest("E:\\eswa\\pytorch_flask_service\\tempManifest");
        imageMerge.merge("E:\\eswa\\pytorch_flask_service\\tempDEX\\dex.jpg","E:\\eswa\\pytorch_flask_service\\tempManifest\\manifest.jpg","E:\\eswa\\pytorch_flask_service");
        return ResponseEntity.ok("Processing complete");
    }
}
