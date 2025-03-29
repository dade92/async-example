package experimental

import order.Details
import order.Order
import order.OrderRepository
import user.User
import user.UserRepository
import utils.Logger
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class Experiment(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val executor: ExecutorService
) {

    private fun getOrder(token: String): CompletableFuture<List<Order>> =
        CompletableFuture.supplyAsync(
            {
                Logger.log("Getting orders")
                val orders = orderRepository.retrieveByUserToken(token)
                Logger.log("Getting orders finished")
                orders
            },
            executor
        )

    private fun getUser(token: String): CompletableFuture<User> =
        CompletableFuture.supplyAsync(
            {
                Logger.log("Getting user")
                val user = userRepository.findByToken(token)
                Logger.log("Getting user finished")
                user
            },
            executor
        )

    fun run(token: String): CompletableFuture<Details> {
        Logger.log("Experiment started!")
        return getOrder(token)
            .thenCombine(
                getUser(token)
            ) { orders, user ->
                Logger.log("Building final details...")
                Details(user, orders)
            }
    }

}