package com.whereIsMyMoney.service;

import com.whereIsMyMoney.dao.BillDao;
import com.whereIsMyMoney.dao.PurchaseDao;
import com.whereIsMyMoney.dao.ShopDao;
import com.whereIsMyMoney.dataModel.Bill;
import com.whereIsMyMoney.dataModel.Purchase;
import com.whereIsMyMoney.dataModel.Shop;
import com.whereIsMyMoney.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    @Autowired
    BillDao billDao;

    @Autowired
    ShopDao shopDao;

    @Autowired
    PurchaseDao purchaseDao;

    public List<Bill> getAll(){
        return billDao.findAll();
    }

    public Bill getOne(int id){
        Bill theBill = billDao.getOne(id);
        return theBill;
    }

    public List<Bill> getByUserId(int id){
        return billDao.findByUserId(id);
    }

    public void add(Bill theBill){
        setShopToBill(theBill);
        theBill.setSum(sumCalculator(theBill));
        billDao.save(theBill);
    }

    public void update(Bill theBill){
        setShopToBill(theBill);
        theBill.setSum(sumCalculator(theBill));
        billDao.save(theBill);
    }

    public void delete(Bill theBill){
        onDeleteAction(theBill);
        billDao.delete(theBill);
    }
    public void delete(int id){
        onDeleteAction(getOne(id));
        billDao.delete(id);
    }
    public boolean exists(int id){
        return billDao.exists(id);
    }

    private void setShopToBill(Bill theBill){
        String shopName = theBill.getShop().getName();
        if(shopDao.findByName(shopName) == null){
            throw new DataNotFoundException("Not found shop with name " + shopName);
        }
        Shop shop = shopDao.findByName(shopName);
        theBill.setShop(shop);
    }

    private void onDeleteAction(Bill theBill){
        List<Purchase> purchases = purchaseDao.findByBillId(theBill.getId());
        purchaseDao.delete(purchases);
    }

    private int sumCalculator(Bill theBill){
        int sum = 0;
        List<Purchase> purchases = purchaseDao.findByBillId(theBill.getId());
        for(Purchase p : purchases){
            sum += p.getSum();
        }
        return sum;
    }
}
