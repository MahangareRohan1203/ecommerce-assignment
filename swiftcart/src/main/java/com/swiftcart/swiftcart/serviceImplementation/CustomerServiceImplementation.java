package com.swiftcart.swiftcart.serviceImplementation;

import com.razorpay.Order;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.swiftcart.swiftcart.entity.*;
import com.swiftcart.swiftcart.enums.OrderStatus;
import com.swiftcart.swiftcart.enums.PaymentStatus;
import com.swiftcart.swiftcart.exception.CartException;
import com.swiftcart.swiftcart.exception.CustomerException;
import com.swiftcart.swiftcart.repository.*;
import com.swiftcart.swiftcart.request.CartItemRequest;
import com.swiftcart.swiftcart.service.CustomerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImplementation implements CustomerService {


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Customer addNewCustomer(Customer customer) throws CustomerException {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent())
            throw new CustomerException("Email Already Exists");

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        Cart cart = new Cart();
        customer = customerRepository.save(customer);
        cart.setCustomer(customer);
        cart = cartRepository.save(cart);

        customer.setCart(cart);
        customer = customerRepository.save(customer);
        return customer;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with given id"));
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }


    @Override
    public Cart getCart(String email) {
        Customer existed = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));
        double total = getCartTotal(existed.getCart().getCartItemList());
        existed.getCart().setTotal(total);
        return existed.getCart();
    }

    public double getCartTotal(List<CartItem> list) {
        double ans = 0;
        for (CartItem a : list) {
            double temp = 0;
            checkAvailability(a.getProduct().getProductId(), a.getColor(), a.getSize(), a.getQuantity());
            for (Availability b : a.getProduct().getAvailabilityList()) {
                if (b.getColor().equals(a.getColor()) && b.getSize().equals(a.getSize())) {
                    temp = b.getPriceOfEach();
                }
            }
            ans += (temp * a.getQuantity());
        }
        return ans;

    }

    public double checkAvailability(long productId, String color, String size, long quantity) {
        Availability isAvailable = availabilityRepository.findByProduct_productIdAndColorAndSize(productId, color, size);
        if (isAvailable == null) throw new RuntimeException("Product with given color and size is not available");
        if (isAvailable.getQuantity() < quantity)
            throw new RuntimeException("available quantity of products is: " + isAvailable.getQuantity());

        return isAvailable.getPriceOfEach();
    }

    @Override
    public CartItem addProductToCart(String email, CartItemRequest cartItem) throws CartException {
        Customer existed = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));
        Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(() -> new RuntimeException("Product Not found"));
        // check availability with existing
        double priceOfEach = checkAvailability(product.getProductId(), cartItem.getColor(), cartItem.getSize().toString(), cartItem.getQuantity());

        CartItem cartItem1 = cartItemRepository.findByCart_cartIdAndProduct_productIdAndColorAndSize(existed.getCart().getCartId(), product.getProductId(), cartItem.getColor(), cartItem.getSize().toString());
        if (cartItem1 == null) {
            cartItem1 = new CartItem();
            cartItem1.setCart(existed.getCart());
            cartItem1.setProduct(product);
            cartItem1.setSize(cartItem.getSize().toString());
            cartItem1.setColor(cartItem.getColor());
            cartItem1.setQuantity(cartItem.getQuantity());
            cartItem1.setPriceOfEach(priceOfEach);
            cartItem1.setPrice(priceOfEach * cartItem.getQuantity());
            cartItem1 = cartItemRepository.save(cartItem1);
            return cartItem1;
        } else {
            // add to existing one
            cartItem1.setQuantity(cartItem.getQuantity() + cartItem1.getQuantity());
            cartItem1 = cartItemRepository.save(cartItem1);
            return cartItem1;
        }
    }

    @Override
    public CartItem updateCartItem(String email, CartItemRequest cartItemRequest, long cartItemId) throws CartException {
        Customer existed = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));
        CartItem existedCartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new CartException("Cart Item not found with given id"));
        Product product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(() -> new RuntimeException("Product Not found"));

        // check availability
        double priceOfEach = checkAvailability(product.getProductId(), cartItemRequest.getColor(), cartItemRequest.getSize().toString(), cartItemRequest.getQuantity());

        // check if entry exist with given color, size, product,
        CartItem cartItem1 = cartItemRepository.findByCart_cartIdAndProduct_productIdAndColorAndSize(existed.getCart().getCartId(), product.getProductId(), cartItemRequest.getColor(), cartItemRequest.getSize().toString());

        if (cartItem1 != null) {
            // TODO: DELETE THIS ENTRY => completed
            deleteCartItem(email, cartItemId);
            return addProductToCart(email, cartItemRequest);
        }

        existedCartItem.setQuantity(cartItemRequest.getQuantity());
        existedCartItem.setSize(cartItemRequest.getSize().toString());
        existedCartItem.setColor(cartItemRequest.getColor());
        existedCartItem.setPriceOfEach(priceOfEach);
        existedCartItem.setPrice(cartItemRequest.getQuantity() * priceOfEach);
        existedCartItem = cartItemRepository.save(existedCartItem);
        return existedCartItem;
    }

    @Override
    public String deleteCartItem(String email, long id) throws CartException {
        Customer existed = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));

        CartItem item = cartItemRepository.findById(id).orElseThrow(() -> new CartException("cart item not found with given id"));

        if (existed.getCart().getCartId() != item.getCart().getCartId())
            throw new CartException("You are trying to remove item from another's cart ");

        cartItemRepository.deleteById(id);
        String message = "Item removed from cart successfully";
        return message;
    }

    @Override
    @Transactional
    public Orders createNewOrder(String email, Address address) throws CartException, CustomerException {
        Customer existed = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));
        if (existed.getCart().getCartItemList().size() == 0)
            throw new CartException("No items found in cart try to add products and try again");

        Orders order = new Orders();
        order.setCustomer(existed);
        order.setOrderDate(Date.valueOf(LocalDate.now()));
        order.setOrderStatus(OrderStatus.PAYMENT_PENDING);

        // adding delivery date
        Calendar c = Calendar.getInstance();
        c.setTime(Date.valueOf(LocalDate.now()));
        c.add(Calendar.DAY_OF_YEAR, 6);
        order.setExpectedDeliveryDate(new Date(c.getTimeInMillis()));

        order.setTotalPrice(getCartTotal(existed.getCart().getCartItemList()));

        // find and fix address then proceed
        if (address.getAddressId() == null) {
            // save address
            address.setCustomer(existed);
            address = addressRepository.save(address);
        } else {
            // check and respond
            Address existedAddress = addressRepository.findById(address.getAddressId()).orElseThrow(() -> new CustomerException("Address not found with given id"));
            if (existedAddress.getCustomer().getCustomerId() != existed.getCustomerId())
                throw new CustomerException("This address belong to other user");
            address = existedAddress;
        }

        order.setAddress(address);
        order = orderRepository.save(order);

        for (CartItem d : existed.getCart().getCartItemList()) {
            OrderItem o = new OrderItem();
            o.setOrder(order);
            o.setProduct(d.getProduct());
            o.setProductCount(d.getQuantity());
            o.setPrice(d.getPrice());
            o.setPriceOfEach(d.getPriceOfEach());
            o = orderItemRepository.save(o);
            order.getOrderList().add(o);
            orderRepository.save(order);
        }

        for (CartItem d : existed.getCart().getCartItemList()) {
            cartItemRepository.deleteById(d.getCartItemId());
        }

        return order;
    }

    @Override
    public List<Orders> findAllOrders(String email) throws CustomerException {
        Customer existed = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Orders> list = orderRepository.findByCustomer_CustomerId(existed.getCustomerId());
        return list;
    }

    @Override
    public Orders findOrderById(String email, long id) throws CustomerException {
        Customer existed = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));
        Orders order = orderRepository.findById(id).orElseThrow(() -> new CustomerException("Order not found with given order id"));
        if (existed.getCustomerId() != order.getCustomer().getCustomerId())
            throw new CustomerException("This order belongs to another person");
        return order;

    }


    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String secret;

    @Override
    public String getPaymentLink(String email, long id) throws CustomerException {
        Customer existed = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Orders order = orderRepository.findById(id).orElseThrow(() -> new CustomerException("Order not found with given order id"));

        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, secret);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalPrice() * 100);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", existed.getFirstName());
            customer.put("email", existed.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            notify.put("sms", true);

            paymentLinkRequest.put("notify", notify);


            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");
            String orderId = paymentLink.get("order_id");
            System.out.println("PAYMENT_LINK_ID " + paymentLinkId);
            System.out.println("PAYMENT_LINK_URL " + paymentLinkUrl);
            System.out.println("ORDER ID" + orderId);
            System.out.println(paymentLink);


            Payment payment = new Payment();
            payment.setPaymentID(paymentLinkId);
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setAmount(order.getTotalPrice());
            payment.setOrder(order);
            paymentRepository.save(payment);

            return paymentLinkUrl;
        } catch (RazorpayException r) {
            throw new CustomerException(r.getMessage());
        }
    }

    @Override
    public void checkPaymentStatus(String id) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, secret);
//            JSONObject paymentLinkRequest = new JSONObject();
//            Order o = razorpayClient.orders.fetch("order_NeqAWr84k5bS78");
//            System.out.println(Optional.ofNullable(o.get("status")));
// TODO: Try to do with PaymentLinkId which is already have
        } catch (RazorpayException r) {
            System.out.println("ERROR MESSAGE - ------ --" + r.getMessage());
        }
    }
}

