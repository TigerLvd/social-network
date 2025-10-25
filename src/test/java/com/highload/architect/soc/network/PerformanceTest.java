package com.highload.architect.soc.network;

import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PerformanceTest extends BaseIntegrationTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void shouldHandleConcurrentUserCreation() throws Exception {
        // Given
        int numberOfUsers = 100;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // When: Create users concurrently
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < numberOfUsers; i++) {
            final int userId = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                UserInfo user = new UserInfo();
                user.setFirstName("User" + userId);
                user.setSecondName("Test");
                user.setBirthdate(java.time.LocalDate.of(2024 - (20 + (userId % 50)), 1, 1));
                user.setBiography("Test biography " + userId);
                user.setCity("Test City " + userId);
                userInfoRepository.save(user);
            }, executor);
            futures.add(future);
        }

        // Wait for all operations to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Then
        assertThat(userInfoRepository.count()).isEqualTo(numberOfUsers);
        System.out.println("Created " + numberOfUsers + " users in " + duration + "ms");
        assertThat(duration).isLessThan(5000); // Should complete within 5 seconds
    }

    @Test
    void shouldHandleBulkUserSearch() throws Exception {
        // Given: Create many users with similar names
        List<UserInfo> users = IntStream.range(0, 50)
                .mapToObj(i -> {
                    UserInfo user = new UserInfo();
                    user.setFirstName("John");
                    user.setSecondName("Doe" + i);
                    user.setBirthdate(java.time.LocalDate.of(2024 - (20 + (i % 30)), 1, 1));
                    user.setBiography("Test biography " + i);
                    user.setCity("Test City " + i);
                    return user;
                })
                .toList();

        userInfoRepository.saveAll(users);

        // When: Search for users with firstName="John"
        long startTime = System.currentTimeMillis();
        
        List<UserInfo> foundUsers = userInfoRepository.findByFirstNameAndSecondName("John", "Doe0");
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Then
        assertThat(foundUsers).hasSize(1);
        assertThat(foundUsers.get(0).getFirstName()).isEqualTo("John");
        assertThat(foundUsers.get(0).getSecondName()).isEqualTo("Doe0");
        System.out.println("Search completed in " + duration + "ms");
        assertThat(duration).isLessThan(1000); // Should complete within 1 second
    }

    @Test
    void shouldHandleConcurrentReads() throws Exception {
        // Given: Create test data
        UserInfo user = new UserInfo();
        user.setFirstName("John");
        user.setSecondName("Doe");
        user.setBirthdate(java.time.LocalDate.of(1994, 1, 1));
        user.setBiography("Test biography");
        user.setCity("Test City");
        UserInfo savedUser = userInfoRepository.save(user);

        // When: Perform concurrent reads
        int numberOfReads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfReads; i++) {
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                return userInfoRepository.findById(savedUser.getId()).isPresent();
            }, executor);
            futures.add(future);
        }

        // Wait for all reads to complete
        List<Boolean> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Then
        assertThat(results).allMatch(result -> result);
        System.out.println("Performed " + numberOfReads + " reads in " + duration + "ms");
        assertThat(duration).isLessThan(2000); // Should complete within 2 seconds
    }
}
