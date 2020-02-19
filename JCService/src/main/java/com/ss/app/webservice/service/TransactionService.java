package com.ss.app.webservice.service;


import java.util.Map;

public interface TransactionService {

	Map callFunction(Map<String,Object> data);
	Map orderItem(String data);
	Map voidOrder(String data);
	Map paymentInvoice(String data);
	Map voidPaymentInvoice(String data);
	String testjfin();
}

