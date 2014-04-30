//package com.springapp.mvc;
//
///**
// * Created by ZWH on 4/20/2014.
// */
//
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.format.annotation.*;
//import org.springframework.stereotype.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.validation.*;
//
//@Controller
//@RequestMapping("/appointments")
//public class AppointmentsController {
//
//    private final AppointmentBook appointmentBook;
//
//    @Autowired
//    public AppointmentsController(AppointmentBook appointmentBook) {
//        this.appointmentBook = appointmentBook;
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public Map<String, Appointment> get() {
//        return appointmentBook.getAppointmentsForToday();
//    }
//
//    @RequestMapping(value="/{day}", method = RequestMethod.GET)
//    public Map<String, Appointment> getForDay(@PathVariable @DateTimeFormat(iso=ISO.DATE) Date day, Model model) {
//        return appointmentBook.getAppointmentsForDay(day);
//    }
//
//    @RequestMapping(value="/new", method = RequestMethod.GET)
//    public AppointmentForm getNewForm() {
//        return new AppointmentForm();
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public String add(@Valid AppointmentForm appointment, BindingResult result) {
//        if (result.hasErrors()) {
//            return "appointments/new";
//        }
//        appointmentBook.addAppointment(appointment);
//        return "redirect:/appointments";
//    }
//}