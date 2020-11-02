package com.example.plant_cart.testing;

import java.util.Scanner;

public class cardtesting {

    public void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //validateCreditCardNumber("12345678903555");
//        String imei = "012850003580200";
//        validateCreditCardNumber(imei);
        System.out.println("Enter Card Number: ");
        String number = sc.next();
        validateCreditCardNumber(number);
    }

    public boolean validateCreditCardNumber(String str) {

        if (str.length()==0){
            return false;
        }
        else {
            int[] ints = new int[str.length()];
            for (int i = 0; i < str.length(); i++) {
                ints[i] = Integer.parseInt(str.substring(i, i + 1));
            }
            for (int i = ints.length - 2; i >= 0; i = i - 2) {
                int j = ints[i];
                j = j * 2;
                if (j > 9) {
                    j = j % 10 + 1;
                }
                ints[i] = j;
            }
            int sum = 0;
            for (int i = 0; i < ints.length; i++) {
                sum += ints[i];
            }
            if (str.equals(null)){
                return false;
            }else{
                if (sum % 10 == 0) {
//            System.out.println(str + " is a valid credit card number");
                    return true;

                } else {
//            System.out.println(str + " is an invalid credit card number");
                    return false;
                }
            }
        }

    }

}