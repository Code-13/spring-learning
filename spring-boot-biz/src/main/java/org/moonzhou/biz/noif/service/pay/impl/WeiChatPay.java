package org.moonzhou.biz.noif.service.pay.impl;

import org.moonzhou.biz.noif.annotation.PayCode;
import org.moonzhou.biz.noif.constant.PayEnum;
import org.moonzhou.biz.noif.service.pay.IPay;
import org.springframework.stereotype.Service;

@PayCode(value = PayEnum.WE_CHAT_PAY)
@Service
public class WeiChatPay implements IPay {

    @Override
    public void pay() {
        System.out.println("===发起微信支付===");
    }
}