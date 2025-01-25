package dev.mbo.t60f.domain.company

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CompanyRepository : JpaRepository<Company, UUID>