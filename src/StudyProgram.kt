class StudyProgram( name: String, link: String) {
    private var studyProgramName: String
    private var studyProgramLink: String
    private lateinit var courses: ArrayList<Course>

    init{
        studyProgramName = name
        studyProgramLink = link
    }

}