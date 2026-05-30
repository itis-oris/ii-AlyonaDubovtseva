package ru.kpfu.itis.subscribio.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.subscribio.model.OrderStatus;

@Component("orderStatusText")
public class OrderStatusTextConverter implements Converter<OrderStatus, String> {

    @Override
    public String convert(OrderStatus status) {
        if (status == null) {
            return "Неизвестно";
        }
        return switch (status) {
            case CREATED -> "Создан";
            case PAID -> "Оплачен";
            case KEY_SENT -> "Ключи отправлены";
            case CANCELLED -> "Отменён";
        };
    }

}