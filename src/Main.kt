import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException
import java.util.*
import javax.swing.text.Document
import kotlin.collections.ArrayList
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


fun main() {

    println("Welcome to your Course Assistant!")
    var doc: org.jsoup.nodes.Document = Jsoup.parse("https://www.google.com/")
    println("Connecting ...")
    println()


    try {
        doc = Jsoup.connect("https://ufind.univie.ac.at/en/vvz.html").userAgent("Firefox").get()
    } catch (e: Exception) {
        println("Network error occurred! Try again.")
    }

    println("Successfully connected!")
    println()
    //find all study programs:
    val fieldsElements: Elements = doc.select("h2.icon-usse-info")
    val fields = ArrayList<String>()
    for (e in fieldsElements) {
        fields.add(e.text())
    }
    var ctr: Int = 0
    for (str in fields) {
        if (ctr < 36)
            println("$ctr - $str")
        else if (ctr == 45 || ctr == 46)
            println("${ctr+3} - $str")
        else
            println(str)
        ctr++;
    }


    //print(doc.body())
}