package com.points.demo.service;

import com.points.demo.dto.TransactionDTO;
import com.points.demo.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface TransactionService {
    public Integer addTransaction(TransactionDTO transactionDTO);
    public Map<String, Long>  spendPoints(Long point) throws Exception;
    public Map<String, Long>  allPayerBalances();
}
