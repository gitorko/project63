package com.demo.project63;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner sendData(KieContainer kContainer) {
        return args -> {
            //Simple
            KieSession kieSession1 = kContainer.newKieSession();
            Product p1 = new Product();
            p1.setType("desktop");
            p1.setRegions(Collections.emptyMap());
            p1.setManufacturers(Collections.emptyList());
            kieSession1.insert(p1);
            kieSession1.fireAllRules();
            kieSession1.dispose();
            log.info("Discount on {} is {}", p1.getType(), p1.getDiscount());

            //Iterates a Map.
            KieSession kieSession2 = kContainer.newKieSession();
            Product p2 = new Product();
            p2.setType("laptop");
            Map<String, String> r2 = new HashMap<>();
            r2.put("region1", "A");
            r2.put("region2", "B");
            r2.put("region3", "C");
            p2.setRegions(r2);
            p2.setManufacturers(Collections.emptyList());
            kieSession2.insert(p2);
            kieSession2.fireAllRules();
            kieSession2.dispose();
            log.info("Discount on {} is {}", p2.getType(), p2.getDiscount());

            //Iterates List
            KieSession kieSession3 = kContainer.newKieSession();
            Product p3 = new Product();
            p3.setType("keyboard");
            p3.setRegions(Collections.emptyMap());
            p3.setManufacturers(Arrays.asList("Company1", "Company2"));
            kieSession3.insert(p3);
            kieSession3.fireAllRules();
            kieSession3.dispose();
            log.info("Discount on {} is {}", p3.getType(), p3.getDiscount());
        };
    }

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("product-discount.drl"));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

}
