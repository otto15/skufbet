package com.skufbet.balance.service

import com.skufbet.balance.client.billing.BillingClient
import com.skufbet.balance.client.billing.dto.PaymentCreationRequest
import com.skufbet.balance.client.billing.dto.PaymentCreationResponse
import com.skufbet.balance.client.userprofile.ConflictBalanceException
import com.skufbet.balance.client.userprofile.NotFoundException
import com.skufbet.balance.client.userprofile.UpdateUserBalanceRequestTo
import com.skufbet.balance.client.userprofile.UserProfileClient
import com.skufbet.balance.dao.BalanceOperationDao
import com.skufbet.balance.domain.BalanceOperation
import com.skufbet.balance.domain.BalanceOperationType
import com.skufbet.balance.dto.BalanceOperationCreationTo
import com.skufbet.balance.dto.CallbackRequestTo
import com.skufbet.balance.dto.CallbackRequestTo.Status.FAILED
import com.skufbet.balance.dto.CallbackRequestTo.Status.SUCCESS
import com.skufbet.balance.service.command.BalanceOperationCreateCommand
import com.skufbet.balance.service.command.UpdatePaymentTokenCommand
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class BalanceOperationService(
    private val balanceOperationDao: BalanceOperationDao,
    private val billingClient: BillingClient,
    private val userProfileClient: UserProfileClient,
    @Value("\${balance.url}")
    private val balanceUrl: String,
) {
    @Transactional
    fun createWithdrawal(command: BalanceOperationCreateCommand): BalanceOperationCreationTo {
        try {
            userProfileClient.withdrawFromBalance(command.userProfileId, UpdateUserBalanceRequestTo(command.amount))
        } catch (e: ConflictBalanceException) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Unsufficient funds on the balance for userProfileId: ${command.userProfileId}",
            )
        } catch (e: NotFoundException) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User profile not found for userProfileId: ${command.userProfileId}",
            )
        }

        val balanceOperation = BalanceOperation(
            UUID.randomUUID(),
            null,
            command.userProfileId,
            command.amount,
            BalanceOperationType.WITHDRAWAL,
            BalanceOperation.Status.NOT_STARTED,
        )

        return create(balanceOperation)
    }

    @Transactional
    fun createDeposit(command: BalanceOperationCreateCommand): BalanceOperationCreationTo {
        return create(
            BalanceOperation(
                UUID.randomUUID(),
                null,
                command.userProfileId,
                command.amount,
                BalanceOperationType.DEPOSIT,
                BalanceOperation.Status.NOT_STARTED,
            ),
        )
    }

    @Transactional
    fun updatePaymentToken(updatePaymentTokenCommand: UpdatePaymentTokenCommand) {
        val balanceOperation: BalanceOperation =
            balanceOperationDao.selectBy(updatePaymentTokenCommand.paymentToken) ?: throw IllegalStateException(
                "No purchase found for paymentToken: ${updatePaymentTokenCommand.paymentToken}"
            )

        when (balanceOperation.type) {
            BalanceOperationType.WITHDRAWAL -> recordWithdrawalResult(
                balanceOperation,
                updatePaymentTokenCommand.status
            )

            BalanceOperationType.DEPOSIT -> recordDepositResult(
                balanceOperation,
                updatePaymentTokenCommand.status
            )
        }
    }

    private fun create(balanceOperation: BalanceOperation): BalanceOperationCreationTo {
        balanceOperationDao.insert(balanceOperation)

        val paymentCreationResponse: PaymentCreationResponse = billingClient.createPayment(
            PaymentCreationRequest(
                balanceOperation.userProfileId,
                balanceOperation.amount,
                balanceUrl
            )
        )

        val updatedBalanceOperation = balanceOperation.copy(
            paymentToken = paymentCreationResponse.paymentToken,
            status = BalanceOperation.Status.STARTED
        )
        balanceOperationDao.updatePaymentTokenAndStatus(updatedBalanceOperation)

        return BalanceOperationCreationTo(updatedBalanceOperation.id, paymentCreationResponse.formUrl)
    }

    private fun recordWithdrawalResult(balanceOperation: BalanceOperation, status: CallbackRequestTo.Status) {
        when (status) {
            SUCCESS -> {
                val updatedBalanceOperation = balanceOperation.copy(
                    status = BalanceOperation.Status.PROCESSED
                )
                balanceOperationDao.updateStatus(updatedBalanceOperation)
            }

            FAILED -> {
                val updatedBalanceOperation = balanceOperation.copy(
                    status = BalanceOperation.Status.FAILED
                )

                userProfileClient.depositToBalance(
                    balanceOperation.userProfileId,
                    UpdateUserBalanceRequestTo(balanceOperation.amount)
                )

                balanceOperationDao.updateStatus(updatedBalanceOperation)
            }
        }
    }

    private fun recordDepositResult(balanceOperation: BalanceOperation, status: CallbackRequestTo.Status) {
        when (status) {
            SUCCESS -> {
                val updatedBalanceOperation = balanceOperation.copy(
                    status = BalanceOperation.Status.PROCESSED
                )

                userProfileClient.depositToBalance(
                    balanceOperation.userProfileId,
                    UpdateUserBalanceRequestTo(balanceOperation.amount)
                )

                balanceOperationDao.updateStatus(updatedBalanceOperation)
            }

            FAILED -> {
                val updatedBalanceOperation = balanceOperation.copy(
                    status = BalanceOperation.Status.FAILED
                )

                balanceOperationDao.updateStatus(updatedBalanceOperation)
            }
        }
    }
}
