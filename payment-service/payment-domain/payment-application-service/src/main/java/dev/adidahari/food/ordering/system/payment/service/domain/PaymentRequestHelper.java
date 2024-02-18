package dev.adidahari.food.ordering.system.payment.service.domain;

import dev.adidahari.food.ordering.system.domain.valueobject.CustomerId;
import dev.adidahari.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import dev.adidahari.food.ordering.system.payment.service.domain.entity.CreditEntry;
import dev.adidahari.food.ordering.system.payment.service.domain.entity.CreditHistory;
import dev.adidahari.food.ordering.system.payment.service.domain.entity.Payment;
import dev.adidahari.food.ordering.system.payment.service.domain.event.PaymentEvent;
import dev.adidahari.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import dev.adidahari.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import dev.adidahari.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import dev.adidahari.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import dev.adidahari.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {
    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for order with id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry,
                creditHistories, failureMessages);

        persistDbObject(payment, failureMessages, creditEntry, creditHistories);

        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment rollback event for order with id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository
                .findByOrderId(UUID.fromString(paymentRequest.getOrderId()));

        if (paymentResponse.isEmpty()) {
            log.error("Payment with order id: {} could not be found", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Payment with order id: " +
                    paymentRequest.getOrderId() + " could not be found");
        }

        Payment payment = paymentResponse.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry,
                creditHistories, failureMessages);

        persistDbObject(payment, failureMessages, creditEntry, creditHistories);

        return paymentEvent;
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntry.isEmpty()) {
            log.error("Could not find credit entry for customer with id: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer with id: " +
                    customerId.getValue());
        }

        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistories.isEmpty()) {
            log.error("Could not find credit history for customer with id: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer with id: " +
                    customerId.getValue());
        }

        return creditHistories.get();
    }

    private void persistDbObject(Payment payment, List<String> failureMessages, CreditEntry creditEntry, List<CreditHistory> creditHistories) {

        paymentRepository.save(payment);

        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }
}

























