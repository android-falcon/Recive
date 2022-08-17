package com.example.sendrecive.Models;

public class VendorInfo {
   private String accName;
   private String accNumber;
   private  int select;

   public int getSelect() {
      return select;
   }

   public void setSelect(int select) {
      this.select = select;
   }

   public VendorInfo() {
   }

   public String getAccName() {
      return accName;
   }

   public void setAccName(String accName) {
      this.accName = accName;
   }

   public String getAccNumber() {
      return accNumber;
   }

   public void setAccNumber(String accNumber) {
      this.accNumber = accNumber;
   }
}
