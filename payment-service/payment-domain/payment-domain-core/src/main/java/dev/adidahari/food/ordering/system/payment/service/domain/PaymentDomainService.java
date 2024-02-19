package dev.adidahari.food.ordering.system.payment.service.domain;

import dev.adidahari.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import dev.adidahari.food.ordering.system.payment.service.domain.entity.CreditEntry;
import dev.adidahari.food.ordering.system.payment.service.domain.entity.CreditHistory;
import dev.adidahari.food.ordering.system.payment.service.domain.entity.Payment;
import dev.adidahari.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import dev.adidahari.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import dev.adidahari.food.ordering.system.payment.service.domain.event.PaymentEvent;
import dev.adidahari.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;

import java.util.List;

public interface PaymentDomainService {
    PaymentEvent validateAndInitiatePayment(Payment payment,
                                            CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages, DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher, DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                          CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories,
                                          List<String> failureMessages, DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher, DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);

}
