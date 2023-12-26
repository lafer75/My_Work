package com.example.democlientdiscovery;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class DemoClientDiscoveryApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoClientDiscoveryApplication.class)
                .web(WebApplicationType.SERVLET).run(args);
    }

    private Map<String, String> data = new HashMap<>();

    @GetMapping("/services/{name}")
    public ResponseEntity<String> get(@PathVariable String name) {
        if (data.containsKey(name)) {
            return ResponseEntity.ok("Service name is " + data.get(name));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/services")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestParam("name") String name,@RequestParam("value") String value) {
        data.put(name, value);
    }

    @PutMapping("/services/{name}")
    public ResponseEntity<String> update(@PathVariable String name, @RequestParam String value) {
        if (data.containsKey(name)) {
            data.put(name, value);
            return ResponseEntity.ok("Service updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/services/{name}")
    public ResponseEntity<String> delete(@PathVariable String name) {
        if (data.containsKey(name)) {
            data.remove(name);
            return ResponseEntity.ok("Service deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
