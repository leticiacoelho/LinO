package com.example.demo.controller;

import com.example.demo.business.SpendBusiness;
import com.example.demo.model.Spend;
import com.example.demo.model.User;
import com.example.demo.model.Wallet;
import com.example.demo.repository.SpendRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/spend")

@RestController
public class SpendController {

    @Autowired
    private SpendRepository spendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpendBusiness spendBusiness;

    /* Insert new spend. */
    @PostMapping
    public Spend insert (@RequestBody Spend spend) {
        return spendBusiness.save(spend);
    }

    /* List all spend */
    @GetMapping
    public ResponseEntity<List<Spend>> listSpend () {
        return new ResponseEntity<List<Spend>>(
                spendRepository.findAll(), HttpStatus.OK
        );
    }

    /* Find spend by receiver */
    @GetMapping(path = {"/receiver/{id}"})
    public ResponseEntity<List<Spend>> listSpendByReceiver (@PathVariable Long id) {
        return new ResponseEntity<List<Spend>>(
                spendRepository.findByReceiverIdIs(id), HttpStatus.OK
        );
    }

    /* Find spend by payer */
    @GetMapping(path = {"/payer/{id}"})
    public ResponseEntity<List<Spend>> listSpendByPayer (@PathVariable Long id) {
        return new ResponseEntity<List<Spend>>(
                spendRepository.findByPayerIdIs(id), HttpStatus.OK
        );
    }

    /* Delete spend by id */
    @DeleteMapping(path = {"/{id}"})
    public void delete (@PathVariable Long id) {
        spendRepository.findById(id)
                .map(record -> {
                    record.setPayer(null);
                    record.setReceiver(null);
                    record.setUserGroup(null);
                    spendRepository.deleteById(id);
                    return null;
                }).orElse(ResponseEntity.notFound().build());
    }

    /* Update info into the wallet */
    @PutMapping(value = "/{id}")
    public ResponseEntity update (@PathVariable("id") long id,
                                  @RequestBody Spend spend) {
        return spendRepository.findById(id)
                .map(record -> {
                    record.setUserGroup(spend.getUserGroup());
                    record.setPayer(spend.getPayer());
                    record.setReceiver(spend.getReceiver());
                    record.setCategory(spend.getCategory());
                    record.setDate(spend.getDate());
                    record.setName(spend.getName());
                    record.setValue(spend.getValue());
                    Spend updated = spendRepository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    /* Pay . */
    @PutMapping(value = "/payOrReceive/{id}")
    public ResponseEntity payOrReceive (@PathVariable("id") long id,
                               @RequestBody Spend spend) {


        Double walletBalancePayer = spend.getPayer().getWallet().getBalance();
        Double walletBalanceReceiver = spend.getReceiver().getWallet().getBalance();
        Long value = spend.getValue();

        Double finalValuePayer = walletBalancePayer-value;
        Double finalValueReceiver = walletBalanceReceiver+value;

        Wallet payerWallet = spend.getPayer().getWallet();
        Wallet receiverWallet = spend.getReceiver().getWallet();

        User payerUser = spend.getPayer();
        User receiverUser = spend.getReceiver();

        payerWallet.setBalance(finalValuePayer);
        receiverWallet.setBalance(finalValueReceiver);

        return (ResponseEntity) ResponseEntity.ok();
    }
}
