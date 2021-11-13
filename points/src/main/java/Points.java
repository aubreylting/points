package main.java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Points {
    public Map<String, Long> payerAccountsInfo;
    public Queue<Pay> pq;
    Long total;

    public Points(){
        // store the user with the total points exist in the account
        payerAccountsInfo = new HashMap<>();
        // store the transaction with timestamp increasing order
        pq = new PriorityQueue<Pay>((a,b) ->a.getTimeStamp().compareTo((b.getTimeStamp())));
        // assume the accumulated point is from negative infinite to positive infinite total points
        total = 0L;
    }

    /*
        * Add the given transaction data into the payerAccountsInfo
        * Assume transaction data have been valid
        * which is "payer: DANNON, points: 1000, timestamp: "2020-11-02T14:00:00Z"
        * @ param : transaction
     */

    public void addTransaction(String transaction) throws ParseException{
        String[] data = transaction.split(",");
        String payer = data[0].split(":")[1];
        Integer point =Integer.valueOf(data[1].split(": ")[1]);
        String timeStampString = data[2].split(": ")[1];
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.US);
        Date timeStamp = df.parse(timeStampString);

        // add user and total points into account info
        payerAccountsInfo.put(payer,payerAccountsInfo.getOrDefault(payer, 0L)+point);
        // add the transaction into queue
        pq.add(new Pay(payer, point, timeStamp));
        total += point;
    }

    /*
        * Accounting team require that the point earned most previously should be redeemed first
        * The total point spent should smaller than the total point earned
        * Point can not be spent if there is no enough points in the user's account
        * User can not speed negative points
        * * @ param : point
     */

    public void spendPoints(int spend){
        if(spend <= 0) {
            System.out.println("The points spent should be larger than 0");
            return;
        }

        if(total <  spend) {
            System.out.println("The points entered is larger than the total points earned. Please enter again");
            return;
        }

        Map<String, Long> spentMap = new HashMap<>();
        while(!pq.isEmpty() && spend > 0){
            Pay current = pq.poll();
            Long removable = Math.min(spend, Math.min(current.getPoints(), payerAccountsInfo.get(current.getPayer())));
            if(current.getPoints() > removable){
                current.setPoints(current.getPoints() - removable);
                pq.add(current);
            }
            total -= removable;
            spend -= removable;
            payerAccountsInfo.put(current.getPayer(),payerAccountsInfo.get(current.getPayer()) - removable);
            spentMap.put(current.getPayer(), spentMap.getOrDefault(current.getPayer(), 0L) + removable);
        }

        for(String payer : spentMap.keySet()) {
            System.out.println("payer:" + payer + ", points: -" + String.valueOf(spentMap.get(payer)));
        }
    }

    /*
        * Print out all payer point balances
        * @param: none
     */

    public void allPayerBalances(){
        for(String payer : payerAccountsInfo.keySet()) {
            System.out.println("payer:" + payer + ", points: " + String.valueOf(payerAccountsInfo.get(payer)));
        }
    }


    public static void main(String[] args) throws ParseException {
        List<String> transactions = new ArrayList<String>();
        transactions.add("payer: DANNON, points: 300, timestamp: 2020-10-31T10:00:00Z");
        transactions.add("payer: UNILEVER, points: 200, timestamp: 2020-10-31T11:00:00Z");
        transactions.add("payer: DANNON, points: -200, timestamp: 2020-10-31T15:00:00Z");
        transactions.add("payer: MILLER COORS, points: 10000, timestamp: 2020-11-01T14:00:00Z");
        transactions.add("payer: DANNON, points: 1000, timestamp: 2020-11-02T14:00:00Z");

        Points points = new Points();
        for(String transaction :transactions){
            points.addTransaction(transaction);
        }
        System.out.println("-------Spend 0 points-------");
        points.spendPoints(0);
        System.out.println("----------------------------");
        points.allPayerBalances();

        System.out.println("-------Spend 5000 points----");

        points.spendPoints(5000);
        System.out.println("----------------------------");
        points.allPayerBalances();

        System.out.println("-------Spend 10000 points----");
        points.spendPoints(10000);
        System.out.println("-----------------------------");
        points.allPayerBalances();

    }
}
