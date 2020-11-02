package com.example.plant_cart.testing;

import java.util.Scanner;

public class UPITesting {
    public void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter details: ");
//        String number = sc.next();
//        validateUPINumber(number);
    }

    public boolean validateUPINumber(String str) {

       if(str.length()>=14)
       {
           if(str.charAt(10)=='@')
           {
               if(str.charAt(0)=='7' || str.charAt(0)=='8' || str.charAt(0)=='9'|| str.charAt(0)=='6')
               {
                   //System.out.println(str + " is a valid UPI number");
                   return true;
               }else{
                   //System.out.println(str + " is an invalid UPI number");
                   return false;
               }
           }else{
               //System.out.println(str + " is an invalid UPI number");
               return false;
           }
       }else{
           //System.out.println(str + " is an invalid UPI number");
           return false;
       }
    }
}
