package com.renking.xmall.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class TokenConfig {

    public static final String TOKEN_KEY= "renking666ceshikaifagogogo1231231123112312312311231123112311223123223";

    public static final Long TOKEN_EXPIRE_TIME= 60 * 60 * 8L;
}
