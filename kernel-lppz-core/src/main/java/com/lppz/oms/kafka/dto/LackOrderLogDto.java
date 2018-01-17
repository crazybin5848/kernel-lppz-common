 package com.lppz.oms.kafka.dto;
 
 import java.text.SimpleDateFormat;
 import java.util.Date;
 
 public class LackOrderLogDto extends MyPageDto
 {
   private String mergeorderid;
   private String seq;
   private String orderid;
   private String orderlineid;
   private String werehousecode;
   private String productcode;
   private String productname;
   private String mergeflag;
   private String receivetime;
   private String issuccess;
   private String message;
 
   public String getMergeorderid()
   {
     return this.mergeorderid;
   }
 
   public void setMergeorderid(String mergeorderid)
   {
     this.mergeorderid = mergeorderid;
   }
 
   public String getSeq()
   {
     return this.seq;
   }
 
   public void setSeq(String seq)
   {
     this.seq = seq;
   }
 
   public String getOrderid()
   {
     return this.orderid;
   }
 
   public void setOrderid(String orderid)
   {
     this.orderid = orderid;
   }
 
   public String getOrderlineid()
   {
     return this.orderlineid;
   }
 
   public void setOrderlineid(String orderlineid)
   {
     this.orderlineid = orderlineid;
   }
 
   public String getWerehousecode()
   {
     return this.werehousecode;
   }
 
   public void setWerehousecode(String werehousecode)
   {
     this.werehousecode = werehousecode;
   }
 
   public String getProductcode()
   {
     return this.productcode;
   }
 
   public void setProductcode(String productcode)
   {
     this.productcode = productcode;
   }
 
   public String getProductname()
   {
     return this.productname;
   }
 
   public void setProductname(String productname)
   {
    this.productname = productname;
   }
 
   public String getMergeflag()
   {
     return this.mergeflag;
   }
 
   public void setMergeflag(String mergeflag)
   {
     this.mergeflag = mergeflag;
   }
 
   public String getReceivetime()
   {
     return this.receivetime;
   }
 
   public void setReceivetime(String receivetime)
   {
     this.receivetime = receivetime;
   }
 
   public String getIssuccess()
   {
     return this.issuccess;
   }
 
   public void setIssuccess(String issuccess)
   {
     this.issuccess = issuccess;
   }
 
   public String getMessage()
   {
     return this.message;
   }
 
   public void setMessage(String message)
   {
     this.message = message; }
 
   public void buildTime() {
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     try {
       if (this.receivetime != null)
         this.receivetime = format.format(new Date(Long.parseLong(this.receivetime)));
     }
     catch (NumberFormatException e) {
     }
   }
 
   public String toString() {
     StringBuilder builder = new StringBuilder();
     builder.append("LackOrderLog [mergeorderid=");
     builder.append(this.mergeorderid);
     builder.append(", seq=");
     builder.append(this.seq);
     builder.append(", orderid=");
     builder.append(this.orderid);
     builder.append(", orderlineid=");
     builder.append(this.orderlineid);
     builder.append(", werehousecode=");
     builder.append(this.werehousecode);
     builder.append(", productcode=");
     builder.append(this.productcode);
     builder.append(", productname=");
     builder.append(this.productname);
     builder.append(", mergeflag=");
     builder.append(this.mergeflag);
     builder.append(", receivetime=");
     builder.append(this.receivetime);
     builder.append(", issuccess=");
     builder.append(this.issuccess);
     builder.append(", message=");
     builder.append(this.message);
     builder.append("]");
     return builder.toString();
   }
 }