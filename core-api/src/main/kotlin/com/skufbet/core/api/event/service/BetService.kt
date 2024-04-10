package com.skufbet.core.api.event.service

import com.skufbet.core.api.event.dao.BetDao
import com.skufbet.skufdb.id.IdGenerator
import org.springframework.stereotype.Service

@Service
class BetService(
    private val betIdGenerator: IdGenerator<Int>,
    private val betDao: BetDao
) {

}
