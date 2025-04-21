package dev.mbo.t60f.views

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Controller
class IndexController {

    @GetMapping(path = ["", "/", "/index", "/index.html"])
    fun index(
        @RequestParam(required = false) companyId: UUID?,
        model: ModelMap,
    ): String {
        if (null != companyId) {
            model.addAttribute("companyId", companyId)
        }
        return "index"
    }

}