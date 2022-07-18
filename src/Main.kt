import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


fun main() {

    val scanner = Scanner(System.`in`)
    println("Welcome to your Course Assistant!")
    var doc: org.jsoup.nodes.Document = Jsoup.parse("https://www.google.com/")
    println("Connecting ...")
    println()


    try {
        doc = Jsoup.connect("https://ufind.univie.ac.at/en/vvz.html").userAgent("Firefox").get()
    } catch (e: Exception) {
        println("Network error occurred! Try again.")
        exitProcess(-1)
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
        else if (ctr == 45 || ctr == 6) {
            println("$ctr - $str")
        } else {
            val trimmedStr: String = str.substring(5, str.length)
            println("$ctr - $trimmedStr")
        }

        ctr++
    }

    //choose field:
    println()
    println("Choose your field: (Enter index number)")
    val fieldNumber: Int = scanner.nextInt()
    val fieldName: String = fields.get(fieldNumber)

    println("$fieldName's study programs:")

    //find th study program element:
    val studyPrograms: Elements = doc.select(":containsOwn($fieldName)")
    val studyProgram: Element = studyPrograms[0]
    val siblings = studyProgram.parent()?.siblingElements()

    var sProgramList: ArrayList<StudyProgram> = ArrayList()
    ctr = 0
    if (siblings != null) {
        for (e in siblings) {
            println("$ctr - ${e.text()}")
            //println(e.children()[0].attr("abs:href"))
            val sProgram = StudyProgram(e.text(),e.children()[0].attr("abs:href"))
            sProgramList.add(sProgram)
            ctr ++
        }
    }

    //choose course:
    println("Choose your study program: (Enter index number)")
    val sProgramNumber: Int = scanner.nextInt()
    println("Chosen study program = ${sProgramList.get(sProgramNumber).studyProgramName}")
    println(sProgramList.get(sProgramNumber).studyProgramLink)

    println("Loading ...")
    try {
        doc = Jsoup.connect(sProgramList.get(sProgramNumber).studyProgramLink).userAgent("Firefox").get()
    } catch (e: Exception) {
        println("Network error occurred! Try again.")
        exitProcess(-1)
    }

    println("Successfully loaded!")
    println()
    //list courses:


    var courses: Elements  = doc.getElementsByClass("list course level2")
    var coursesList : ArrayList<Course> = ArrayList()

    if(doc.getElementsByClass("list course level4").size != 0){
        for(e in doc.getElementsByClass("list course level4")){
            courses.add(e)
        }
    }
    if(doc.getElementsByClass("list course level3").size != 0){
        for(e in doc.getElementsByClass("list course level3")){
            courses.add(e)
        }
    }
    if(doc.getElementsByClass("list course level2").size != 0){
        for(e in doc.getElementsByClass("list course level2")){
            courses.add(e)
        }
    }
    if(doc.getElementsByClass("list course level1").size != 0){
        for(e in doc.getElementsByClass("list course level1")){
            courses.add(e)
        }
    }

    //println(courses.size)
    ctr = 0
    for(e in courses){
        println("$ctr - ${e.text()}")
        //println(e.children().select("a.what").attr("abs:href"))
        val c: Course = Course(e.text(),e.children().select("a.what").attr("abs:href"))
        coursesList.add(c)
        ctr ++
    }
    sProgramList.get(sProgramNumber).setCoursesList(coursesList)

    //choose course:

    println()
    println("Choose your course: (Enter index number)")
    val courseNumber: Int = scanner.nextInt()
    var course: Course = sProgramList.get(sProgramNumber).courses.get(courseNumber)
    println("Chosen course's name = ${course.courseLink}")
    println("Chosen course's link = ${course.courseName}")
    //print(doc.body())

    println()
    println("Loading events ...")
    try {
        doc = Jsoup.connect(course.courseLink).userAgent("Firefox").get()
    } catch (e: Exception) {
        println("Network error occurred! Try again.")
        exitProcess(-1)
    }

    println("Successfully loaded!")


    //print events:
    val eventsParent: Elements = doc.select("div.usse-id-group")
    val children: Elements = eventsParent[0].children()
    var events: ArrayList<String> = ArrayList()


    for(e in children){
        if(e.getElementsByClass("event line future").size>0 || e.getElementsByClass("event line next").size>0){
            for(e2 in e.getElementsByClass("event line next")){
                events.add(e2.text())
            }
            for(e2 in e.getElementsByClass("event line future")) {
                events.add(e2.text())
            }
        }
    }

    for(eventStr in events){
        println(eventStr)
    }

    //println(doc.body())

}