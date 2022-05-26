package com.nowcoder.community.entity;

public class PassWordChange {
   private String old_pass;
   private String new_pass;
   private String new_pass2;

    public String getOld_pass() {
        return old_pass;
    }

    public void setOld_pass(String old_pass) {
        this.old_pass = old_pass;
    }

    public String getNew_pass() {
        return new_pass;
    }

    public void setNew_pass(String new_pass) {
        this.new_pass = new_pass;
    }

    public String getNew_pass2() {
        return new_pass2;
    }

    public void setNew_pass2(String new_pass2) {
        this.new_pass2 = new_pass2;
    }

    @Override
    public String toString() {
        return "PassWordChange{" +
                "old_pass='" + old_pass + '\'' +
                ", new_pass='" + new_pass + '\'' +
                ", new_pass2='" + new_pass2 + '\'' +
                '}';
    }
}
