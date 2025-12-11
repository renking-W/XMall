package com.renking.xmall.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BaseEntity implements Serializable {

    private Integer id;    // id
    private Integer userId;        // 用户id
    private String recipientName;   // 收件人姓名
    private String recipientPhone;  // 收件人电话
    private String province;        // 省
    private String city;            // 市
    private String district;        // 区
    private String detail;          // 详细地址
    private Integer isDefault;      // 是否默认
}
