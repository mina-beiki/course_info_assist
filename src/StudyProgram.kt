class StudyProgram( name: String, link: String) {
    var studyProgramName: String = name
    var studyProgramLink: String = link
    lateinit var courses: ArrayList<Course>

    fun setCoursesList(coursesList: ArrayList<Course>){
        courses = coursesList
    }



}