package ru.kpfu.itis.subscribio.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.kpfu.itis.subscribio.model.OrderStatus;

@Converter
public class OrderStatusJpaConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus status) {
        if (status == null) {
            return null;
        }
        return status.name();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;}
        return OrderStatus.valueOf(value);
    }

}

