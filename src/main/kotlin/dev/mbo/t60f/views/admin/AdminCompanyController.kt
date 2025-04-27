package dev.mbo.t60f.views.admin

import dev.mbo.kotlinencryption.Encryptor
import dev.mbo.logging.logger
import dev.mbo.t60f.domain.company.Company
import dev.mbo.t60f.domain.company.CompanyRepository
import dev.mbo.t60f.domain.company.CompanyService
import dev.mbo.t60f.views.admin.dto.EditCompanyDto
import dev.mbo.t60f.views.admin.dto.NewCompanyDto
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID

@Controller
@RequestMapping("/admin/companies")
class AdminCompanyController(
    private val companyRepository: CompanyRepository,
    private val companyService: CompanyService,
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

    @GetMapping("/new")
    fun newCompany(): String = "admin/company_new"

    @GetMapping("/{companyId}/edit")
    fun editCompany(
        @PathVariable companyId: UUID,
        model: ModelMap
    ): String {
        val company = companyRepository.findByIdOrNull(companyId)
            ?: throw IllegalArgumentException("Unknown company")
        model.addAttribute("companyId", companyId)
        model.addAttribute("company", EditCompanyDto.from(company))
        return "admin/company_edit"
    }

    @PostMapping("/{companyId}/edit")
    fun updateCompany(
        @PathVariable companyId: UUID,
        @ModelAttribute company: EditCompanyDto,
        model: RedirectAttributes
    ): RedirectView {
        companyService.updateCompany(companyId, company)
        model.addFlashAttribute("message", "company updated: ${company.name}")
        return RedirectView("/admin/companies")
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
                syncBaseUrl = dto.miteBaseUrl,
                syncApiKey = encKey,
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