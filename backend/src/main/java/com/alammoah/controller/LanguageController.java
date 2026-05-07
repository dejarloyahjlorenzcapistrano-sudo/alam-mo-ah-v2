package com.alammoah.controller;

import com.alammoah.service.SyntaxDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LanguageController {

    private final SyntaxDataService syntaxDataService;

    @GetMapping("/languages")
    public ResponseEntity<?> getAllLanguages() {
        return ResponseEntity.ok(syntaxDataService.getAllLanguages());
    }

    @GetMapping("/languages/{id}")
    public ResponseEntity<?> getLanguage(@PathVariable String id) {
        var lang = syntaxDataService.getLanguage(id);
        if (lang == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(lang);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "") String q) {
        return ResponseEntity.ok(syntaxDataService.search(q));
    }
}
