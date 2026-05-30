package ru.kpfu.itis.subscribio.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.subscribio.model.Category;
import ru.kpfu.itis.subscribio.model.Role;
import ru.kpfu.itis.subscribio.model.SubscriptionProduct;
import ru.kpfu.itis.subscribio.model.User;
import ru.kpfu.itis.subscribio.repository.CategoryRepository;
import ru.kpfu.itis.subscribio.repository.RoleRepository;
import ru.kpfu.itis.subscribio.repository.SubscriptionProductRepository;
import ru.kpfu.itis.subscribio.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final SubscriptionProductRepository productRepository;

    @Override
    public void run(String... args) {
        Role userRole = createRoleIfNotExists("ROLE_USER");
        Role adminRole = createRoleIfNotExists("ROLE_ADMIN");
        if (!userRepository.existsByEmail("admin@subscribio.ru")) {
            User admin = new User();
            admin.setEmail("admin@subscribio.ru");
            admin.setFullName("Администратор");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getRoles().add(userRole);
            admin.getRoles().add(adminRole);
            userRepository.save(admin);


        }
        Category music = createCategoryIfNotExists("Музыка", "Музыкальные сервисы");
        Category ai = createCategoryIfNotExists("Нейросети", "AI-сервисы и помощники");
        Category video = createCategoryIfNotExists("Видео", "Онлайн-кинотеатры и видео-сервисы");
        createProductIfNotExists(
                "Spotify Premium Gift Card",
                "Подарочная карта для оплаты Spotify Premium. Код отправляется на email после оплаты.",
                "1 месяц",
                "/images/spotify.png",
                new java.math.BigDecimal("899"),
                music

        );
        createProductIfNotExists(
                "Apple Music Gift Card",
                "Подарочная карта для Apple Music. Подходит для пополнения аккаунта.",
                "1 месяц",
                "/images/apple-music.png",
                new java.math.BigDecimal("799"),
                music
        );


        createProductIfNotExists(
                "ChatGPT Plus Gift Card",
                "Цифровой подарочный код для оплаты доступа к AI-сервису.",
                "1 месяц",
                "/images/chatgpt.png",
                new java.math.BigDecimal("2490"),
                ai

        );
        createProductIfNotExists(
                "Netflix Gift Card",
                "Подарочная карта для оплаты видео-сервиса.",
                "1 месяц",
                "/images/netflix.png",
                new java.math.BigDecimal("1290"),
                video
        );
    }
    private Role createRoleIfNotExists(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    return roleRepository.save(role);
                });
    }
    private Category createCategoryIfNotExists(String name, String description) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(name);
                    category.setDescription(description);
                    return categoryRepository.save(category);


                } );
    }

    private void createProductIfNotExists(String title, String description, String durationLabel, String imageUrl, java.math.BigDecimal priceRub, Category category) {
        boolean exists = productRepository.findAll()
                .stream()
                .anyMatch(product -> product.getTitle().equals(title));
        if (!exists) {
            SubscriptionProduct product = new SubscriptionProduct();
            product.setTitle(title);
            product.setDescription(description);
            product.setDurationLabel(durationLabel);
            product.setImageUrl(imageUrl);
            product.setPriceRub(priceRub);
            product.setCategory(category);
            product.setActive(true);
            productRepository.save(product);
        }


    }


}