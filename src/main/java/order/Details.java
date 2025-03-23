package order;

import user.User;

import java.util.List;

public record Details(
    User user,
    List<Order> orders
) {
}
