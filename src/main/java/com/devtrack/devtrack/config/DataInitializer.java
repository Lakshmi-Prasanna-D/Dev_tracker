package com.devtrack.devtrack.config;

import com.devtrack.devtrack.entity.*;
import com.devtrack.devtrack.enums.*;
import com.devtrack.devtrack.repository.*;
import com.devtrack.devtrack.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final GoalRepository goalRepository;
    private final DsaProblemRepository dsaProblemRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMilestoneRepository milestoneRepository;
    private final ReminderRepository reminderRepository;
    private final ActivityRepository activityRepository;
    private final UserService userService;

    public DataInitializer(GoalRepository goalRepository,
                           DsaProblemRepository dsaProblemRepository,
                           ProjectRepository projectRepository,
                           ProjectMilestoneRepository milestoneRepository,
                           ReminderRepository reminderRepository,
                           ActivityRepository activityRepository,
                           UserService userService) {
        this.goalRepository = goalRepository;
        this.dsaProblemRepository = dsaProblemRepository;
        this.projectRepository = projectRepository;
        this.milestoneRepository = milestoneRepository;
        this.reminderRepository = reminderRepository;
        this.activityRepository = activityRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Ensure default users exist
        User alex = userService.createOrUpdateUser("alex", "alex@devtrack.com", "password123", "Alex Developer", Role.ROLE_USER);
        User sarah = userService.createOrUpdateUser("sarah", "sarah@devtrack.com", "password123", "Sarah Engineer", Role.ROLE_USER);
        userService.createOrUpdateUser("admin", "admin@devtrack.com", "admin123", "DevTrack Admin", Role.ROLE_ADMIN);

        if (goalRepository.count() > 0) {
            logger.info("Database already populated with data. Skipping initial seeding.");
            return;
        }

        logger.info("Seeding realistic sample data for DevTrack Platform (User: alex)...");
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // 1. Seed Goals for Alex
        Goal g1 = new Goal("Master Java 21 Concurrency & Virtual Threads",
                "Deep dive into Structured Concurrency, Virtual Threads, and CompletableFuture benchmarks.",
                GoalCategory.JAVA, today.plusDays(10), GoalStatus.IN_PROGRESS);
        g1.setUser(alex);
        goalRepository.save(g1);

        Goal g2 = new Goal("Complete Blind 75 DSA Sheet",
                "Solve top 75 frequently asked LeetCode interview problems across dynamic programming and graphs.",
                GoalCategory.DSA, today.plusDays(25), GoalStatus.IN_PROGRESS);
        g2.setUser(alex);
        goalRepository.save(g2);

        Goal g3 = new Goal("Build Production Full-Stack Spring Boot 4 + MySQL App",
                "Architect and deploy a SaaS developer growth platform with layered architecture and responsive UI.",
                GoalCategory.SPRING_BOOT, today.minusDays(2), GoalStatus.COMPLETED);
        g3.setCompletedDate(now.minusDays(2));
        g3.setUser(alex);
        goalRepository.save(g3);

        Goal g4 = new Goal("Advanced SQL Optimization & Indexing",
                "Study B-Tree indexes, query execution plans (EXPLAIN ANALYZE), and database partitioning.",
                GoalCategory.SQL, today.plusDays(14), GoalStatus.PENDING);
        g4.setUser(alex);
        goalRepository.save(g4);

        Goal g5 = new Goal("System Design: Distributed Cache & Rate Limiter",
                "Design scalable distributed systems using Redis, Token Bucket algorithms, and CAP theorem trade-offs.",
                GoalCategory.FULL_STACK, today.plusDays(30), GoalStatus.PENDING);
        g5.setUser(alex);
        goalRepository.save(g5);

        Goal g6 = new Goal("Dockerize & Deploy Cloud Services",
                "Containerize Spring Boot applications using Multi-stage Dockerfiles and deploy to container platforms.",
                GoalCategory.PROJECTS, today.minusDays(5), GoalStatus.COMPLETED);
        g6.setCompletedDate(now.minusDays(5));
        g6.setUser(alex);
        goalRepository.save(g6);

        // Starter Goal for Sarah (multi-tenant proof)
        Goal sg1 = new Goal("Learn Rust & WebAssembly Systems",
                "Explore memory safety without GC and compile high-performance modules for the browser.",
                GoalCategory.OTHER, today.plusDays(20), GoalStatus.IN_PROGRESS);
        sg1.setUser(sarah);
        goalRepository.save(sg1);

        // 2. Seed DSA Problems for Alex
        DsaProblem p1 = new DsaProblem("Two Sum", Platform.LEETCODE, "https://leetcode.com/problems/two-sum/",
                DsaTopic.ARRAYS, Difficulty.EASY, DsaStatus.SOLVED, "Used HashMap for O(n) one-pass solution.");
        p1.setAttemptedDate(today.minusDays(10));
        p1.setSolvedDate(today.minusDays(10));
        p1.setUser(alex);
        dsaProblemRepository.save(p1);

        DsaProblem p2 = new DsaProblem("Longest Substring Without Repeating Characters", Platform.LEETCODE,
                "https://leetcode.com/problems/longest-substring-without-repeating-characters/",
                DsaTopic.SLIDING_WINDOW, Difficulty.MEDIUM, DsaStatus.SOLVED, "Sliding window with character index map. O(n) time, O(min(m, n)) space.");
        p2.setAttemptedDate(today.minusDays(8));
        p2.setSolvedDate(today.minusDays(8));
        p2.setUser(alex);
        dsaProblemRepository.save(p2);

        DsaProblem p3 = new DsaProblem("Trapping Rain Water", Platform.LEETCODE,
                "https://leetcode.com/problems/trapping-rain-water/",
                DsaTopic.TWO_POINTERS, Difficulty.HARD, DsaStatus.SOLVED, "Classic two-pointer approach maintaining maxLeft and maxRight pointers.");
        p3.setAttemptedDate(today.minusDays(6));
        p3.setSolvedDate(today.minusDays(6));
        p3.setUser(alex);
        dsaProblemRepository.save(p3);

        DsaProblem p4 = new DsaProblem("LRU Cache", Platform.LEETCODE,
                "https://leetcode.com/problems/lru-cache/",
                DsaTopic.HASHING, Difficulty.MEDIUM, DsaStatus.SOLVED, "Doubly Linked List + HashMap for O(1) get and put operations.");
        p4.setAttemptedDate(today.minusDays(4));
        p4.setSolvedDate(today.minusDays(4));
        p4.setUser(alex);
        dsaProblemRepository.save(p4);

        DsaProblem p5 = new DsaProblem("Lowest Common Ancestor of a Binary Tree", Platform.LEETCODE,
                "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/",
                DsaTopic.TREES, Difficulty.MEDIUM, DsaStatus.SOLVED, "Post-order tree traversal returning non-null nodes.");
        p5.setAttemptedDate(today.minusDays(2));
        p5.setSolvedDate(today.minusDays(2));
        p5.setUser(alex);
        dsaProblemRepository.save(p5);

        DsaProblem p6 = new DsaProblem("Coin Change", Platform.LEETCODE,
                "https://leetcode.com/problems/coin-change/",
                DsaTopic.DYNAMIC_PROGRAMMING, Difficulty.MEDIUM, DsaStatus.SOLVED, "Bottom-up tabulation dp[i] = min(dp[i], dp[i - coin] + 1).");
        p6.setAttemptedDate(today.minusDays(1));
        p6.setSolvedDate(today.minusDays(1));
        p6.setUser(alex);
        dsaProblemRepository.save(p6);

        DsaProblem p7 = new DsaProblem("Course Schedule (Cycle Detection)", Platform.LEETCODE,
                "https://leetcode.com/problems/course-schedule/",
                DsaTopic.GRAPHS, Difficulty.MEDIUM, DsaStatus.SOLVED, "Kahn's Topological Sort using in-degrees queue.");
        p7.setAttemptedDate(today);
        p7.setSolvedDate(today);
        p7.setUser(alex);
        dsaProblemRepository.save(p7);

        DsaProblem p8 = new DsaProblem("Merge k Sorted Lists", Platform.LEETCODE,
                "https://leetcode.com/problems/merge-k-sorted-lists/",
                DsaTopic.LINKED_LISTS, Difficulty.HARD, DsaStatus.ATTEMPTED, "Used Min-Heap PriorityQueue. Need to optimize memory allocation.");
        p8.setAttemptedDate(today);
        p8.setUser(alex);
        dsaProblemRepository.save(p8);

        DsaProblem p9 = new DsaProblem("Word Break", Platform.LEETCODE,
                "https://leetcode.com/problems/word-break/",
                DsaTopic.DYNAMIC_PROGRAMMING, Difficulty.MEDIUM, DsaStatus.REVISIT, "Dynamic programming with Trie or HashSet lookup. Review again before interview.");
        p9.setAttemptedDate(today.minusDays(3));
        p9.setUser(alex);
        dsaProblemRepository.save(p9);

        // 3. Seed Projects & Milestones for Alex
        Project proj1 = new Project("DevTrack - Developer Growth Platform",
                "Full-stack productivity suite with goal management, DSA tracking, analytics, and coding streaks.",
                "Java 21, Spring Boot, MySQL, Thymeleaf, Bootstrap 5, Chart.js",
                "https://github.com/developer/devtrack",
                "http://localhost:8085",
                today.minusDays(15),
                today.plusDays(5),
                ProjectStatus.IN_PROGRESS,
                80);
        proj1.setUser(alex);
        projectRepository.save(proj1);

        ProjectMilestone m1 = new ProjectMilestone("Database Schema & JPA Entities", "Design and configure MySQL schema with entities", today.minusDays(12), true, proj1);
        ProjectMilestone m2 = new ProjectMilestone("Service Layer & Streak Engine", "Implement streak algorithms and dynamic statistics", today.minusDays(7), true, proj1);
        ProjectMilestone m3 = new ProjectMilestone("Interactive UI & Chart.js Analytics", "Build responsive dashboard and analytics views", today.minusDays(2), true, proj1);
        ProjectMilestone m4 = new ProjectMilestone("Production Verification & Testing", "Execute end-to-end user testing and QA verification", today.plusDays(4), false, proj1);
        milestoneRepository.save(m1);
        milestoneRepository.save(m2);
        milestoneRepository.save(m3);
        milestoneRepository.save(m4);

        Project proj2 = new Project("FinStream - High-Frequency Trading Core",
                "Low-latency algorithmic order matching engine built with concurrent collections and Zero-GC buffers.",
                "Java 21, Aeron IPC, Chronicle Queue, Disruptor, Docker",
                "https://github.com/developer/finstream",
                null,
                today.minusDays(30),
                today.plusDays(45),
                ProjectStatus.IN_PROGRESS,
                45);
        proj2.setUser(alex);
        projectRepository.save(proj2);

        ProjectMilestone fm1 = new ProjectMilestone("LMAX Disruptor Ring Buffer Engine", "Benchmark ring buffer event processing", today.minusDays(15), true, proj2);
        ProjectMilestone fm2 = new ProjectMilestone("Limit Order Book Matching", "Implement price-time priority order matching", today.plusDays(10), false, proj2);
        milestoneRepository.save(fm1);
        milestoneRepository.save(fm2);

        Project proj3 = new Project("CloudVault - Distributed File Sync Service",
                "Resilient cloud storage service with deduplication, chunked upload, and client encryption.",
                "Spring Boot, AWS S3, Redis, PostgreSQL, React",
                "https://github.com/developer/cloudvault",
                "https://cloudvault-demo.app",
                today.minusDays(60),
                today.minusDays(10),
                ProjectStatus.COMPLETED,
                100);
        proj3.setUser(alex);
        projectRepository.save(proj3);

        // 4. Seed Reminders for Alex
        Reminder r1 = new Reminder("Review Graph Topological Sort Algorithms",
                "Practice Kahn's algorithm and DFS cycle detection before mock interview.",
                today.plusDays(1).atTime(18, 0),
                ReminderStatus.PENDING, g2, null);
        r1.setUser(alex);
        reminderRepository.save(r1);

        Reminder r2 = new Reminder("Sprint Review: DevTrack Platform",
                "Verify dashboard statistics, responsive layout, and streak calculations.",
                today.plusDays(2).atTime(10, 30),
                ReminderStatus.PENDING, null, proj1);
        r2.setUser(alex);
        reminderRepository.save(r2);

        Reminder r3 = new Reminder("Submit Weekly DSA Contest Solution",
                "Participate in weekly LeetCode contest and review problem editor discussions.",
                today.plusDays(4).atTime(20, 0),
                ReminderStatus.PENDING, g2, null);
        r3.setUser(alex);
        reminderRepository.save(r3);

        // 5. Seed Historical Activities for Alex (10-day consecutive streak)
        for (int i = 9; i >= 0; i--) {
            LocalDateTime dayTime = now.minusDays(i).minusHours(2);
            switch (i) {
                case 9 -> activityRepository.save(new Activity(alex, ActivityType.GOAL_CREATED, "Created new learning goal: Master Java 21 Concurrency", "GOAL", g1.getId(), dayTime));
                case 8 -> activityRepository.save(new Activity(alex, ActivityType.DSA_SOLVED, "Solved Medium problem: Longest Substring Without Repeating Characters", "DSA", p2.getId(), dayTime));
                case 7 -> activityRepository.save(new Activity(alex, ActivityType.PROJECT_CREATED, "Created project: DevTrack - Developer Growth Platform", "PROJECT", proj1.getId(), dayTime));
                case 6 -> activityRepository.save(new Activity(alex, ActivityType.DSA_SOLVED, "Solved Hard problem: Trapping Rain Water", "DSA", p3.getId(), dayTime));
                case 5 -> activityRepository.save(new Activity(alex, ActivityType.MILESTONE_COMPLETED, "Completed milestone: Database Schema & JPA Entities", "MILESTONE", m1.getId(), dayTime));
                case 4 -> activityRepository.save(new Activity(alex, ActivityType.DSA_SOLVED, "Solved Medium problem: LRU Cache", "DSA", p4.getId(), dayTime));
                case 3 -> activityRepository.save(new Activity(alex, ActivityType.DSA_ATTEMPTED, "Attempted Medium problem: Word Break", "DSA", p9.getId(), dayTime));
                case 2 -> activityRepository.save(new Activity(alex, ActivityType.GOAL_COMPLETED, "Completed goal: Build Production Full-Stack Spring Boot 4 + MySQL App", "GOAL", g3.getId(), dayTime));
                case 1 -> activityRepository.save(new Activity(alex, ActivityType.DSA_SOLVED, "Solved Medium problem: Coin Change", "DSA", p6.getId(), dayTime));
                case 0 -> activityRepository.save(new Activity(alex, ActivityType.DSA_SOLVED, "Solved Medium problem: Course Schedule (Cycle Detection)", "DSA", p7.getId(), dayTime));
            }
        }

        logger.info("Sample data successfully initialized for user 'alex'! Verified consecutive streak established.");
    }
}
