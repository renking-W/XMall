package com.renking.xmall.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class TokenConfig {

    private static final String TOKEN_KEY= "renking666ceshikaifagogogo";

    private static final Long TOKEN_EXPIRE_TIME= 60 * 60 * 8L;
}
