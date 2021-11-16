package com.points.demo.service;

import com.points.demo.dto.TransactionDTO;
import com.points.demo.entity.Transaction;
import com.points.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service(value ="transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Integer addTransaction(TransactionDTO transactionDTO){
        Transaction transactionEntity = new Transaction();
        transactionEntity.setPayer(transactionDTO.getPayer());
        transactionEntity.setPoints(transactionDTO.getPoints());
        transactionEntity.setTimestamps(transactionDTO.getTimestamps());
        Transaction newTransaction = transactionRepository.save(transactionEntity);
        return newTransaction.getTransactionId();
    }

    @Override
    public Map<String, Long> spendPoints(Long point) throws Exception {
        Iterable<Transaction> transactionList= transactionRepository.findAll();
        Queue<Transaction> pq = new PriorityQueue<Transaction>((a,b) ->a.getTimestamps().compareTo((b.getTimestamps())));
        Long total = 0L;
        Map<String, Long> payerAccountsInfo = allPayerBalances();
        for(String payer : payerAccountsInfo.keySet()){
            total += payerAccountsInfo.get(payer);
        }
        if(point <= 0) {
            throw new Exception("The points spent should be larger than 0");
        }

        if(total <  point) {
            throw new Exception("The points entered is larger than the total points earned. Please enter again");
        }

        for(Transaction transaction : transactionList){
            pq.add(transaction);
        }

        Map<String, Long> spentMap = new HashMap<>();
        while(!pq.isEmpty() && point > 0){
            Transaction current = pq.poll();
            Long removable = Math.min(point, Math.min(current.getPoints(), payerAccountsInfo.get(current.getPayer())));
            total -= removable;
            point -= removable;
            addTransaction(new TransactionDTO(current.getPayer(), -removable, new Date()));
            spentMap.put(current.getPayer(), spentMap.getOrDefault(current.getPayer(), 0L) + removable);
        }


        return spentMap;
    }

    @Override
    public Map<String, Long> allPayerBalances() {
        Iterable<Transaction> transactionList= transactionRepository.findAll();
        Map<String, Long> payerAccountsInfo = new HashMap<>();
        for(Transaction transaction : transactionList){
            if(!payerAccountsInfo.containsKey(transaction.getPayer())){
                payerAccountsInfo.put(transaction.getPayer(),0L);
            }
            payerAccountsInfo.put(transaction.getPayer(),payerAccountsInfo.get(transaction.getPayer())+transaction.getPoints());
        }
        return payerAccountsInfo;

    }
}
