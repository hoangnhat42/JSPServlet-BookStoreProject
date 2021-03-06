package com.bookstore.dao;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bookstore.entity.Book;
import com.bookstore.entity.BookOrder;
import com.bookstore.entity.Customer;
import com.bookstore.entity.OrderDetail;

public class OrderDAOTest {
	private static OrderDAO orderDAO;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		orderDAO = new OrderDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		orderDAO.close();
	}

	
	
	
	@Test
	public void testCreateBookOrder() {
		BookOrder order = new BookOrder();
		Customer customer = new Customer();
		customer.setCustomerId(12);
		
		order.setCustomer(customer);
		order.setFirstname("Jennie");
		order.setLastname("Fer");
		order.setPhone("12345566");
		order.setAddressLine1("123 South St");
		order.setAddressLine2("Clifton Park");
		order.setCity("New York");
		order.setState("New York");
		order.setCountry("US");
		order.setPaymentMethod("paypal");
		order.setZipcode("123566");
		
		Set<OrderDetail> orderDetails = new HashSet<>();
		OrderDetail orderDetail = new OrderDetail();
		
		Book book = new Book(34);
		orderDetail.setBook(book);
		orderDetail.setQuantity(2);
		orderDetail.setSubtotal(68.0f);
		orderDetail.setBookOrder(order);
		
		orderDetails.add(orderDetail);
		
		order.setOrderDetails(orderDetails);
		order.setTax(6.8f);
		order.setShippingFee(2.0f);
		order.setSubtotal(68.0f);
		order.setTotal(76.8f);
		
		BookOrder savedOrder = orderDAO.create(order);
		assertNotNull(savedOrder);
		
	}

	@Test
	public void testUpdateBookOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet() {
		Integer orderId = 36;
		BookOrder order = orderDAO.get(orderId);
		
		assertEquals(1, order.getOrderDetails().size());
	}

	@Test
	public void testGetIntegerInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteOrder() {
		int orderId = 26;
		orderDAO.delete(orderId);
		
		BookOrder order = orderDAO.get(orderId);
		
		assertNull(order);
	}

	@Test
	public void testListAll() {

		List<BookOrder> listOrders = orderDAO.listAll();
		
		for (BookOrder order : listOrders) {
			System.out.println(order.getOrderId() + " - " + order.getCustomer().getFullname()
					+ " - " + order.getTotal() + " - " + order.getStatus());
			for (OrderDetail detail : order.getOrderDetails()) {
				Book book = detail.getBook();
				int quantity = detail.getQuantity();
				float subtotal = detail.getSubtotal();
				System.out.println("\t" + book.getTitle() + " - " + quantity + " - " + subtotal);
			}
		}
		
		assertTrue(listOrders.size() > 0);
	}

	@Test
	public void testUpdateBookOrderShippingAddress() {
		Integer orderId = 9;
		BookOrder order = orderDAO.get(orderId);
		order.setAddressLine1("New Shipping Address");
		
		orderDAO.update(order);
		
		BookOrder updateOrder = orderDAO.get(orderId);
		
		assertEquals(order.getAddressLine1(), updateOrder.getAddressLine1());
	}
	
	@Test
	public void testUpdateBookOrderDetail() {
		Integer orderId = 36;
		BookOrder order = orderDAO.get(orderId);
		
		Iterator<OrderDetail> iterator = order.getOrderDetails().iterator();
		
		while (iterator.hasNext()) {
			OrderDetail orderDetail = iterator.next();
			if (orderDetail.getBook().getBookId() == 40) {
				orderDetail.setQuantity(3);
				orderDetail.setSubtotal(102);
			}
		}
		
		orderDAO.update(order);
		
		BookOrder updateOrder = orderDAO.get(orderId);
		
		iterator = order.getOrderDetails().iterator();
		
		int expectedQuantity = 3;
		float expectedSubtotal = 102;
		int actualQuantity = 0;
		float actualSubtotal = 0;
		
		while (iterator.hasNext()) {
			OrderDetail orderDetail = iterator.next();
			if (orderDetail.getBook().getBookId() == 40) {
				actualQuantity = orderDetail.getQuantity();
				actualSubtotal = orderDetail.getSubtotal();
			}
		}
		
		assertEquals(expectedQuantity, actualQuantity);
		assertEquals(expectedSubtotal, actualSubtotal, 0.0f);
	}
	
	@Test
	public void testCount() {
		long totalOrders = orderDAO.count();
		assertEquals(3, totalOrders);
	}

	@Test
	public void testListByCustomerNoOrders() {
		Integer customerId = 99;
		List<BookOrder> listOrders = orderDAO.listByCustomer(customerId);
		
		assertTrue(listOrders.isEmpty());
	}
	
	@Test
	public void testListByCustomerHaveOrders() {
		Integer customerId = 16;
		List<BookOrder> listOrders = orderDAO.listByCustomer(customerId);
		
		assertTrue(listOrders.size() > 0);
	}

	@Test
	public void testGetByIdAndCustomerNull() {
		Integer orderId = 10;
		Integer customerId = 99;
		
		BookOrder order = orderDAO.get(orderId, customerId);
		
		assertNull(order);
	}
	
	@Test
	public void testGetByIdAndCustomerNotNull() {
		Integer orderId = 30;
		Integer customerId = 16;
		
		BookOrder order = orderDAO.get(orderId, customerId);
		
		assertNotNull(order);
	}
	
	@Test
	public void testListMostRecentSales() {
		List<BookOrder> recentOrders = orderDAO.listMostRecentSales();
		
		assertEquals(3, recentOrders.size());
	}

}
