package dev.mbo.t60f.domain.request

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FeedbackRequestRepository : JpaRepository<FeedbackRequest, UUID>