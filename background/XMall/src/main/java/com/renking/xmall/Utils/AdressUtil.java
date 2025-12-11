package com.renking.xmall.Utils;

import com.renking.xmall.Entity.Address;

public class AdressUtil {

    public static String getAddress(Address address){
        String tar = "";
        tar = address.getProvince() + address.getCity() + address.getDistrict() + address.getDetail();
        return tar;
    }
}
