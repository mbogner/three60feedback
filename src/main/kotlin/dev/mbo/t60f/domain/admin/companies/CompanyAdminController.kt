package dev.mbo.t60f.domain.admin.companies

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.company.Company
import dev.mbo.t60f.domain.company.CompanyRepository
import dev.mbo.t60f.global.Encryptor
import jakarta.transaction.Transactional
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
@RequestMapping("/admin/companies")
class CompanyAdminController(
    private val companyRepository: CompanyRepository,
    private val encryptor: Encryptor,
) {

    private val log = logger()

    @GetMapping
    fun companies(model: ModelMap): String {
        val companies = companyRepository.findAll()
        companies.sortWith { c1, c2 -> c1.createdAt!!.compareTo(c2.createdAt!!) }
        model.addAttribute("companies", companies)
        return "admin/companies"
    }

    @Transactional
    @PostMapping
    fun create(
        @ModelAttribute dto: NewCompanyDto,
        model: RedirectAttributes
    ): RedirectView {
        model.addFlashAttribute("message", "company created")
        val encKey = encryptor.encrypt(dto.miteApiKey)
        companyRepository.save(
            Company(
                name = dto.name,
                domains = dto.domains
                    .split(",")
                    .map { it.trim().lowercase() }
                    .toSet(),
                miteBaseUrl = dto.miteBaseUrl,
                miteApiKey = encKey,
            )
        )
        return RedirectView("/admin/companies")
    }

    @Transactional
    @PostMapping("/{id}/delete")
    fun delete(
        @PathVariable id: UUID,
        model: RedirectAttributes
    ): RedirectView {
        log.info("deleting company {}", id)
        companyRepository.deleteById(id)
        model.addFlashAttribute("message", "company deleted")
        return RedirectView("/admin/companies")
    }

}