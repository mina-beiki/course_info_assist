import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

fun main() {

    val scanner = Scanner(System.`in`)
    println("Welcome to your Course Assistant!")
    var doc: org.jsoup.nodes.Document = Jsoup.parse("https://www.google.com/")
    println("Connecting ...")
    println()

    //connect to the main webpage:
    try {
        doc = Jsoup.connect("https://ufind.univie.ac.at/en/vvz.html").userAgent("Firefox").get()
    } catch (e: Exception) {
        println("Network error occurred! Try again.")
        exitProcess(-1)
    }

    println("Successfully connected!")
    println()

    //find all fields:
    val fieldsElements: Elements = doc.select("h2.icon-usse-info")
    val fields = ArrayList<String>()
    for (e in fieldsElements) {
        fields.add(e.text())
    }
    //print all fields with new indices and without their own numbers:
    var ctr: Int = 0
    for (str in fields) {
        if (ctr < 36)
            println("$ctr - $str")
        //exceptions for after 36
        else if (ctr == 45 || ctr == 6) {
            println("$ctr - $str")
        } else {
            //omit the index at the first:
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

    //find all study programs for a field:
    val studyPrograms: Elements = doc.select(":containsOwn($fieldName)")
    val studyProgram: Element = studyPrograms[0]
    val siblings = studyProgram.parent()?.siblingElements()
    //arraylist of study program elements
    var sProgramList: ArrayList<StudyProgram> = ArrayList()

    //save study programs' info in study programs' array list and print all of them:
    ctr = 0
    if (siblings != null) {
        for (e in siblings) {
            println("$ctr - ${e.text()}")
            val sProgram = StudyProgram(e.text(), e.children()[0].attr("abs:href"))
            sProgramList.add(sProgram)
            ctr++
        }
    }

    //choose study program:
    println("Choose your study program: (Enter index number)")
    val sProgramNumber: Int = scanner.nextInt()
    println("Chosen study program = ${sProgramList.get(sProgramNumber).studyProgramName}")
    println(sProgramList.get(sProgramNumber).studyProgramLink)

    //choose the task to do (the second one is the studo project!):
    println("Choose the task you want to do: (Enter index number)")
    println("1. View all events for a course")
    println("2. View next 10 events")
    val index: Int = scanner.nextInt()


    //connect to the study program webpage:
    try {
        doc = Jsoup.connect(sProgramList.get(sProgramNumber).studyProgramLink).userAgent("Firefox").get()
    } catch (e: Exception) {
        println("Network error occurred! Try again.")
        exitProcess(-1)
    }


    //find all courses in that study program and save them into courses and coursesList:
    var courses: Elements = doc.getElementsByClass("list course level2")
    var coursesList: ArrayList<Course> = ArrayList()

    //there are multiple levels which include courses, so check each of them to save all:
    if (doc.getElementsByClass("list course level4").size != 0) {
        for (e in doc.getElementsByClass("list course level4")) {
            courses.add(e)
        }
    }
    if (doc.getElementsByClass("list course level3").size != 0) {
        for (e in doc.getElementsByClass("list course level3")) {
            courses.add(e)
        }
    }
    if (doc.getElementsByClass("list course level2").size != 0) {
        for (e in doc.getElementsByClass("list course level2")) {
            courses.add(e)
        }
    }
    if (doc.getElementsByClass("list course level1").size != 0) {
        for (e in doc.getElementsByClass("list course level1")) {
            courses.add(e)
        }
    }

    //if task 1 is chosen:
    if (index == 1) {
        println("Successfully loaded!")
        println()

        //if there are no courses available for this study program:
        if (courses.size == 0) {
            println("There are no courses for this study program!")
            exitProcess(-1)
        }

        //complete courses' objects list:
        ctr = 0
        for (e in courses) {
            println("$ctr - ${e.text()}")
            val c: Course = Course(e.text(), e.children().select("a.what").attr("abs:href"))
            coursesList.add(c)
            ctr++
        }
        sProgramList.get(sProgramNumber).setCoursesList(coursesList)

        //choose course:
        println()
        println("Choose your course: (Enter index number)")
        val courseNumber: Int = scanner.nextInt()
        var course: Course = sProgramList.get(sProgramNumber).courses.get(courseNumber)
        println("Chosen course's name = ${course.courseLink}")
        println("Chosen course's link = ${course.courseName}")

        //load all events for the chosen course, connect to the course webpage:
        println()
        println("Loading events ...")
        try {
            doc = Jsoup.connect(course.courseLink).userAgent("Firefox").get()
        } catch (e: Exception) {
            println("Network error occurred! Try again.")
            exitProcess(-1)
        }
        println("Successfully loaded!")

        //print all events:
        val eventsParent: Elements = doc.select("div.usse-id-group")
        val children: Elements = eventsParent[0].children()
        var events: ArrayList<String> = ArrayList()

        //to find all events in the course and save all of them:
        for (e in children) {
            if (e.getElementsByClass("event line future").size > 0 || e.getElementsByClass("event line next").size > 0) {
                for (e2 in e.getElementsByClass("event line next")) {
                    events.add(e2.text())
                }
                for (e2 in e.getElementsByClass("event line future")) {
                    events.add(e2.text())
                }
            }
        }

        //if there are no events available for this course:
        if (events.size == 0) {
            println("Currently no class schedule is known!")
            exitProcess(-1)
        }

        //print all events:
        for (eventStr in events) {
            println(eventStr)
        }

        //if task 2 is chosen:
    } else if (index == 2) {

        //save all courses for the chosen study program into coursesList:
        ctr = 0
        for (e in courses) {
            val c: Course = Course(e.text(), e.children().select("a.what").attr("abs:href"))
            coursesList.add(c)
            ctr++
        }
        sProgramList.get(sProgramNumber).setCoursesList(coursesList)

        /*query over each course to find all events for each of them, in order to do this, make a thread for each of them
        to make sure it is not taking a lot of time.*/

        var threadsCtr: Int = 0
        for (c in coursesList) {
            Thread(Runnable {
                //connect to that course webpage:
                try {
                    doc = Jsoup.connect(c.courseLink).userAgent("Firefox").get()
                } catch (e: Exception) {
                    c.setEventString("")
                }
                val children: Elements = doc.select("div.usse-id-courselong")[0].children()
                for (e in children) {
                    if (e.getElementsByClass("summary groups").size > 0) {

                        val strSize: Int = e.getElementsByClass("summary groups")[0].text().length
                        //if it doesn't have any events of if it contains multiple groups:
                        if (strSize == 0 || strSize > 500) {
                            c.setEventString("")
                        } else {
                            c.setEventString(e.getElementsByClass("summary groups")[0].text())
                        }
                        //extract date and time for that course:
                        c.generateDate()
                        break
                    }
                }
                threadsCtr++
            }).start()
        }

        //if all threads have been finished:
        while (threadsCtr != coursesList.size) {
            print("$threadsCtr out of ${coursesList.size} loaded")
            Thread.sleep(1000)
            print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b")
        }

        //when threads are completed:
        println("Loaded all events successfully!")


        var datesList: ArrayList<String> = ArrayList()

        //check if there are no events:
        if (coursesList.size == 0) {
            println("No upcoming events!")
        } else {
            //save all dates into a list:
            for (c in coursesList) {
                if (c.date.length > 0) {
                    datesList.add(c.date)
                }
            }

            //use date time formatter to convert them to a specified format and then sort them:
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val result = datesList.sortedByDescending {
                LocalDate.parse(it, dateTimeFormatter)
            }

            //if we have more than 10 dates (because we want the first 10 events):
            if (result.size >= 10) {
                //iterate from the last element because we want the newest time first:
                for (i in 9 downTo 0) {
                    println(result[i])
                }
            } else {
                for (i in result.size - 1 downTo 0) {
                    println(result[i])
                }
            }
        }
    }
}