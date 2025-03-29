package dev.mbo.t60f.domain.request

import dev.mbo.t60f.domain.company.CompanyRepository
import dev.mbo.t60f.domain.request.dto.FeedbackRequestNewDto
import dev.mbo.t60f.global.AsyncMailSender
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class FeedbackRequestService(
    private val repository: FeedbackRequestRepository,
    private val companyRepository: CompanyRepository,
    private val mailer: AsyncMailSender,
    @Value("\${app.base-url}")
    private val baseUrl: String,
) {

    @Transactional
    fun create(request: FeedbackRequestNewDto): FeedbackRequest {
        val company = companyRepository.findByIdOrNull(request.companyId)
            ?: throw EntityNotFoundException("no company with id ${request.companyId}")
        val created = repository.save(
            FeedbackRequest(
                company = company,
                email = request.email,
            )
        )
        mailer.send(
            to = created.email!!,
            subject = "Feedback Round Token",
            content = """
            Hi ${created.email}!
            
            if you want to start a feedback round, use the following link to get it started:
            
            ${baseUrl}/users?requestId=${created.id}
            
            Yours,
            t60f
            """.trimIndent()
        )
        return created
    }

    fun findById(requestId: UUID): FeedbackRequest {
        return repository.findByIdOrNull(requestId)
            ?: throw EntityNotFoundException("No feedback request with id $requestId")
    }

}