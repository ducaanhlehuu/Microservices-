package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDTO;

public interface IAccountService {

    void createAccount(CustomerDTO customerDTO);
    public CustomerDTO fetchData(String mobileNumber);

    public boolean updateAccount(CustomerDTO customerDTO);

    boolean deleteAccount(String mobileNumber);

}
