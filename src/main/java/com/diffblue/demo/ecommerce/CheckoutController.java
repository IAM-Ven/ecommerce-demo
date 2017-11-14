package com.diffblue.demo.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CheckoutController {


  @Autowired
  public CheckoutController() { }


  /**
   * Get the cart data and display it on the customer information page.
   * @param model where to put cart data, session current http session
   * @return Page for the output
   */

  @RequestMapping("/checkout")
  public String viewCheckoutPage(Map<String, Object> model, HttpSession session,
                                 Customer customer) {
    if (session.getAttribute("shoppingCart") != null) {
      Cart shoppingCart = (Cart) session.getAttribute("shoppingCart");
      model.put("cart", shoppingCart);
      return "Checkout";
    } else {
      return "redirect:/cart";
    }
  }

  /**
   * Save customer information page.
   * @param customer - customer object
   * @param bindingResult - object with form validation result
   * @param model - model where to add data to access on view
   * @param session - current http session
   * @return page for the output
   */
  @PostMapping("/checkout")
  public String saveCustomerInformation(@Valid Customer customer, BindingResult bindingResult,
                                        Map<String, Object> model, HttpSession session) {
    if (bindingResult.hasErrors()) {
      Cart shoppingCart = (Cart) session.getAttribute("shoppingCart");
      model.put("cart", shoppingCart);
      return "Checkout";
    } else {
      session.setAttribute("customerInformation", customer);
      return "redirect:/payment";
    }
  }

  /**
   * Get the cart and customer data and display it on the payment page.
   * @param model where to put cart data, session current http session
   * @return Page for the output
   */
  @RequestMapping("/payment")
  public String viewPaymentPage(Map<String, Object> model, HttpSession session, Payment payment) {

    if (session.getAttribute("shoppingCart") != null
        && session.getAttribute("customerInformation") != null) {

      Cart shoppingCart = (Cart) session.getAttribute("shoppingCart");
      Customer customer = (Customer) session.getAttribute("customerInformation");
      model.put("cart", shoppingCart);
      model.put("customer", customer);

      return "Payment";

    } else {
      return "redirect:/checkout";
    }
  }

  /**
   * Validate payment form and make payment.
   * @param payment - payment object
   * @param bindingResult - object with form validation result
   * @param model - model where to add data to access on view
   * @param session - current http session
   * @return page for the output
   */
  @PostMapping("/payment")
  public String makePayment(@Valid Payment payment, BindingResult bindingResult,
                                        Map<String, Object> model, HttpSession session) {
    if (bindingResult.hasErrors()) {

      Cart shoppingCart = (Cart) session.getAttribute("shoppingCart");
      Customer customer = (Customer) session.getAttribute("customerInformation");
      model.put("cart", shoppingCart);
      model.put("customer", customer);

      return "Payment";

    } else {
      Application.log.info(">>>>>>>>>>>>>>> READY FOR PAYMENT!");
      return "redirect:/order-complete";
    }
  }

  @RequestMapping("/order-complete")
  public String viewOrderCompletePage(Map<String, Object> model, HttpSession session) {
    return "OrderComplete";
  }

}
