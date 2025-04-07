package dev.mbo.t60f.global

import dev.mbo.t60f.global.git.GitInfo
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class GlobalCommitVersion(private val gitInfo: GitInfo) {

    @ModelAttribute("commitVersion")
    fun commitVersion(): String = gitInfo.commitFull.takeLast(8)
}