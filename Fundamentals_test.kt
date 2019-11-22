import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.JsonDeserializer

import java.io.BufferedReader
import java.io.File

import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */


fun main() {
    println("Hello, world!!!")

    //read json from file

    val jsonFile = "./orders.json"
    val bufferedReader: BufferedReader = File(jsonFile).bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    // parse json into obj

    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
            val time = json.asJsonPrimitive.asString
            LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        })
        .create()

//    val gson = GsonBuilder()
//        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
//        .create()

    var orderLsit: List<OrdersAnalyzer.Order> = gson.fromJson(inputString, object : TypeToken<List<OrdersAnalyzer.Order>>() {}.type)

    val ordersAnalyzer = OrdersAnalyzer()

    var result = ordersAnalyzer.totalDailySales(orderLsit)

    println(orderLsit)
    println(result)

}

class OrdersAnalyzer {

    data class Order(val orderId: Int, val creationDate: LocalDateTime, val orderLines: List<OrderLine>)

    data class OrderLine(val productId: Int, val name: String, val quantity: Int, val unitPrice: BigDecimal)

    fun totalDailySales(orders: List<Order>): Map<DayOfWeek, Int> {

        var totalDailySalesMap = sortedMapOf<DayOfWeek, Int>()

        for (order in orders){
            var quantity = totalDailySalesMap.getOrDefault(order.creationDate.dayOfWeek, 0)

            order.orderLines.forEach {
                quantity += it.quantity
            }

            totalDailySalesMap.put(order.creationDate.dayOfWeek, quantity)
        }

        return totalDailySalesMap
    } // fun totalDailySales
} // class OrdersAnalyzer