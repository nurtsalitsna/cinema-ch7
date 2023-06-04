package com.spring.binar.challenge_5.service.implementation;

import com.spring.binar.challenge_5.controller.rest.PaymentController;
import com.spring.binar.challenge_5.dto.PaymentRequestDTO;
import com.spring.binar.challenge_5.dto.PaymentResponseDTO;
import com.spring.binar.challenge_5.exception.PaymentErrorException;
import com.spring.binar.challenge_5.models.Invoice;
import com.spring.binar.challenge_5.models.Payment;
import com.spring.binar.challenge_5.models.Seat;
import com.spring.binar.challenge_5.repos.*;
import com.spring.binar.challenge_5.repos.CostumerRepository;
import com.spring.binar.challenge_5.repos.PaymentRepository;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.FileNotFoundException;
import java.util.*;

import com.spring.binar.challenge_5.repos.ScheduleRepository;
import com.spring.binar.challenge_5.repos.StaffRepository;
import com.spring.binar.challenge_5.service.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOG = LogManager.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final ScheduleRepository scheduleRepository;
    private final CostumerRepository costumerRepository;
    private final StaffRepository staffRepository;
    private final SeatReservedRepository seatReservedRepository;
    private final SeatRepository seatRepository;

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    @Override
    public List<PaymentResponseDTO> findAll() {
        var payments = paymentRepository.findAll();

        if(payments.isEmpty()) return new ArrayList<>();

        var responses = payments.stream().map(payment -> {
            var seatsReserved = seatReservedRepository.findSeatsByPaymentPaymentId(payment.getPaymentId());
            var seats = seatsReserved.stream().map(it -> seatRepository.findById(it.getSeat().getSeatId()).orElseThrow()).toList();
            return payment.toPaymentResponseDTO(seats);
        }).toList();

        LOG.info("Current time: {}", new Date().getTime());
        LOG.info("Response: {}",responses);

        return responses;
    }

    @Override
    public PaymentResponseDTO findById(int id) {
        var payment = paymentRepository.findById(id).orElseThrow();

        var seatReserved = seatReservedRepository.findSeatsByPaymentPaymentId(payment.getPaymentId());
        var seats = seatReserved.stream().map(it -> seatRepository.findById(it.getSeat().getSeatId()).orElseThrow()).toList();

        return payment.toPaymentResponseDTO(seats);
    }

    @Override
    public JasperPrint exportReport(int id) throws JRException, FileNotFoundException {
        String file = ResourceUtils.getFile("classpath:challenge5payment.jrxml").getAbsolutePath();
        JasperReport jasperReport = JasperCompileManager.compileReport(file);
        List<Invoice> dataList = new ArrayList<>();
        Payment payment = paymentRepository.findById(id).orElseThrow();
        Invoice invoice = payment.toInvoice();
        dataList.add(invoice);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList);
        Map<String, Object> parameters = new HashMap<>();
        return JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);
    }

    @Override
    public PaymentResponseDTO save(PaymentRequestDTO request) {
        if(request.getAmount() <= 0 || request.getStaffId() <= 0
                || request.getScheduleId() <= 0 || request.getSeatIds().isEmpty())
            throw new PaymentErrorException("Invalid Payment");

        var schedule    = scheduleRepository.findById(request.getScheduleId()).orElseThrow(() -> new PaymentErrorException("Schedule not found."));
        var staff       = staffRepository.findById(request.getStaffId()).orElseThrow(() -> new PaymentErrorException("Staff not found."));
        var costumer    = costumerRepository.findById(request.getCostumerId()).orElseThrow(() -> new PaymentErrorException("Costumer not found."));
        // check seats if exist
        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        LOG.info("seats : {}",seats);
        if(seats.isEmpty()) throw new PaymentErrorException("Seat not Available");

        var isSeatsInOneStudio = seats.stream().allMatch(seat ->
                seat.getStudio().getStudioId() == schedule.getStudio().getStudioId()
        );

        if(!isSeatsInOneStudio) throw new PaymentErrorException("Seat selected is not in one studio");

        if(request.getAmount() < schedule.getPrice()) {
            var minMoney = schedule.getPrice() - request.getAmount();
            throw new PaymentErrorException("Not enough money to continue the payment: -" + minMoney );
        }

        var payment = Payment.builder()
                .paymentDate(new Date().getTime())
                .schedule(schedule)
                .staff(staff)
                .costumer(costumer)
                .amount(request.getAmount())
                .build();

        payment = paymentRepository.save(payment);

        var seatReserved = payment.toSeatReserved(seats);

        seatReserved.forEach(it -> LOG.info(it.toString()));

        // save each seat to db
        seatReserved.forEach(seatReservedRepository::save);


        return payment.toPaymentResponseDTO(seats);
    }

    @Override
    public PaymentResponseDTO update(PaymentRequestDTO updatedPayment) {
        var payment = paymentRepository.findById(updatedPayment.getPaymentId()).orElseThrow();

        List<Seat> seats = seatRepository.findAllById(updatedPayment.getSeatIds());
        LOG.info("seats : {}",seats);
        if(seats.isEmpty()) throw new PaymentErrorException("Seat not Available");

        var schedule    = scheduleRepository.findById(updatedPayment.getScheduleId()).orElseThrow(() -> new PaymentErrorException("Schedule not found."));
        var staff       = staffRepository.findById(updatedPayment.getStaffId()).orElseThrow(() -> new PaymentErrorException("Staff not found."));
        var costumer    = costumerRepository.findById(updatedPayment.getCostumerId()).orElseThrow(() -> new PaymentErrorException("Costumer not found."));

        if(updatedPayment.getAmount() < schedule.getPrice()) {
            var minMoney = schedule.getPrice() - updatedPayment.getAmount();
            throw new PaymentErrorException("Not enough money to continue the payment: -" + minMoney );
        }

        payment.setSchedule(schedule);
        payment.setStaff(staff);
        payment.setCostumer(costumer);
        payment.setPaymentDate(new Date().getTime());
        payment.setAmount(updatedPayment.getAmount());

        payment = paymentRepository.save(payment);

        seatReservedRepository.removeAllByPaymentPaymentId(payment.getPaymentId());
        var seatReserved = payment.toSeatReserved(seats);
        seatReservedRepository.saveAll(seatReserved);


        return payment.toPaymentResponseDTO(seats);
    }

    @Override
    public void delete(int id) {
        var result = paymentRepository.findById(id);
        if(result.isEmpty()) throw new PaymentErrorException("No Payment found");

        paymentRepository.delete(result.get());
    }
}
