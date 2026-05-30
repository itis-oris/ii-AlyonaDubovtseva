package ru.kpfu.itis.subscribio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.subscribio.model.Order;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendActivationKeys(Order order, String activationKeys) {
        try {
            log.info("Отправка ключей по заказу {} на email {}",
                    order.getOrderNumber(), order.getUser().getEmail());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getUser().getEmail());
            message.setSubject("Ключи активации по заказу " + order.getOrderNumber());
            message.setText("""
                Здравствуйте, %s!

                Ваш заказ %s успешно обработан.

                Купленные подписки:
                %s

                Ключи активации:

                %s

                Инструкция:
                1. Найдите нужную подписку в списке ключей.
                2. Скопируйте ключ активации.
                3. Активируйте его на сайте соответствующего сервиса.

                Если возникнут вопросы, обратитесь в поддержку Subscribio.

                Спасибо за покупку!
                """.formatted(
                    order.getUser().getFullName(),
                    order.getOrderNumber(),
                    buildItemsText(order),
                    activationKeys
            ));
            mailSender.send(message);
            log.info("Ключи по заказу {} успешно отправлены", order.getOrderNumber());
        } catch (Exception e) {
            log.error("Ошибка при отправке ключей по заказу {}", order.getOrderNumber(), e);
            throw new IllegalStateException("Не удалось отправить письмо пользователю", e);

        }



    }

    private String buildItemsText(Order order) {
        StringBuilder builder = new StringBuilder();
        order.getItems().forEach(item -> {
            builder.append("- ")
                    .append(item.getProductTitle())
                    .append(", количество: ")
                    .append(item.getQuantity())
                    .append("\n");
        });
        return builder.toString();
    }


}