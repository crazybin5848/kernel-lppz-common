package com.lppz.dubbo.micro;


public interface TransactionCompensationService {

	Boolean doCompensation(String exception);
}
