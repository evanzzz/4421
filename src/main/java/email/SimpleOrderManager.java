package email;





/**
 * Created by ZWH on 4/20/2014.
 */
//public class SimpleOrderManager implements OrderManager {
//
//    private MailSender mailSender;
//    private SimpleMailMessage templateMessage;
//
//    public void setMailSender(MailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public void setTemplateMessage(SimpleMailMessage templateMessage) {
//        this.templateMessage = templateMessage;
//    }
//
//    public void placeOrder(Order order) {
//
//        // Do the business calculations...
//
//        // Call the collaborators to persist the order...
//
//        // Create a thread safe "copy" of the template message and customize it
//        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
//        msg.setTo(order.getCustomer().getEmailAddress());
//        msg.setText(
//                "Dear " + order.getCustomer().getFirstName()
//                        + order.getCustomer().getLastName()
//                        + ", thank you for placing order. Your order number is "
//                        + order.getOrderNumber());
//        try{
//            this.mailSender.send(msg);
//        }
//        catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
//        }
//    }
//
//}


//public class SimpleOrderManager implements OrderManager {
//
//    private JavaMailSender mailSender;
//
//    public void setMailSender(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public void placeOrder(final Order order) {
//
//        // Do the business calculations...
//
//        // Call the collaborators to persist the order...
//
//        MimeMessagePreparator preparator = new MimeMessagePreparator() {
//
//            public void prepare(MimeMessage mimeMessage) throws Exception {
//
//                mimeMessage.setRecipient(Message.RecipientType.TO,
//                        new InternetAddress(order.getCustomer().getEmailAddress()));
//                mimeMessage.setFrom(new InternetAddress("mail@mycompany.com"));
//                mimeMessage.setText(
//                        "Dear " + order.getCustomer().getFirstName() + " "
//                                + order.getCustomer().getLastName()
//                                + ", thank you for placing order. Your order number is "
//                                + order.getOrderNumber());
//            }
//        };
//
//        try {
//            this.mailSender.send(preparator);
//        }
//        catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
//        }
//    }
//
//}


