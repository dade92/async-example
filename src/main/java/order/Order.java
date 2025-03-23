package order;

import java.math.BigDecimal;

public record Order(
    String id,
    BigDecimal price
) {
}
