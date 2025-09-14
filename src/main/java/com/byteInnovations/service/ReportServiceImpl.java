package com.byteInnovations.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.byteInnovations.controller.HomeController;
import com.byteInnovations.model.OrderClass;
import com.byteInnovations.model.Purchase;
import com.byteInnovations.model.Report;
import com.byteInnovations.repository.OrderRepository;
import com.byteInnovations.repository.PurchaseRepository;
import com.byteInnovations.repository.ReportRepository;
import com.byteInnovations.repository.UserSessionRepository;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository repo;
	@Autowired
	private OrderRepository orepo;
	@Autowired
	private PurchaseRepository prepo;

	@Override
	public List<Report> findByDesc(String desc) {

		List<Report> report = repo.findByDesc(desc);
		return report;
	}

	@Override
	public List<OrderClass> findOrdersBetweenDates(LocalDate fromdate, LocalDate toDate) {

		return orepo.findOrdersBetweenDates(fromdate, toDate);
	}

	@Override
	public List<OrderClass> findOrdersBetweenDatesGroupByProduct(LocalDate fromdate, LocalDate toDate) {
		List<OrderClass> orders = new ArrayList<OrderClass>();
		List<Object[]> results = orepo.findOrdersBetweenDatesGroupByProduct(fromdate, toDate);
		for (Object[] row : results) {
			OrderClass order = new OrderClass();
			order.setProductName((String) row[0]);
			order.setCategory((String) row[1]);
			order.setSubCategory((String) row[2]);
			order.setQuantity(((Long) row[3]).intValue());
			order.setPrice((BigDecimal) row[4]);
			//System.out.println(row[0]+" "+row[1]+" "+row[2]+" "+row[3]+" "+row[4]+" ");
			orders.add(order);
		}
		return orders;
	}

	@Override
	public List<Purchase> findPurcheasesBetweenDates(LocalDate fromdate, LocalDate todate) {

		List<Object[]> results = prepo.findPurchasesBetweenDates(fromdate, todate);

		List<Purchase> purchases = new ArrayList<Purchase>();

		for (Object[] row : results) {
			Purchase purchase = new Purchase();
			purchase.setProduct((String) row[0]);
			purchase.setQuantity((String) row[1]);
			purchase.setCost((Double) row[2]);
			purchase.setPurchaseDate((LocalDate) row[3]);

			purchases.add(purchase);
		}

		return purchases;

	}

}
