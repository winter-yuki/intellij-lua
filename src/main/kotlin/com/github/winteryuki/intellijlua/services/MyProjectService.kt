package com.github.winteryuki.intellijlua.services

import com.intellij.openapi.project.Project
import com.github.winteryuki.intellijlua.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
