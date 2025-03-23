package experimental

import order.Details
import order.Order
import order.OrderRepository
import user.User
import user.UserRepository
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class Experiment(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val executor: ExecutorService
) {

    private fun getOrder(id: String): CompletableFuture<List<Order>> =
        CompletableFuture.supplyAsync({
            println("Getting orders")
            val orders = orderRepository.retrieveByUserToken(id)
            println("Getting orders finished")
            orders
        }, executor)

    private fun getUser(token: String): CompletableFuture<User> =
        CompletableFuture.supplyAsync({
            println("Getting user")
            val user = userRepository.findByToken(token)
            println("Getting user finished")
            user
        }, executor)

    fun run() {
        println("Experiment started!")
        getOrder("XXX")
            .thenCombine(
                getUser("token")
            ) { orders, user -> Details(user, orders) }
            .thenRun { executor.shutdown() }
    }

}