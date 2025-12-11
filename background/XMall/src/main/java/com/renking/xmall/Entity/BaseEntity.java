package com.renking.xmall.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseEntity {
    //更新时间
    private LocalDateTime updatedAt;
    //创建时间
    private LocalDateTime createdAt;
}
