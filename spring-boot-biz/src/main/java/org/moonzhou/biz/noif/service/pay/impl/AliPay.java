package org.moonzhou.biz.noif.service.pay.impl;

import org.moonzhou.biz.noif.annotation.PayCode;
import org.moonzhou.biz.noif.constant.PayEnum;
import org.moonzhou.biz.noif.service.pay.IPay;
import org.springframework.stereotype.Service;

@PayCode(value = PayEnum.ALI_PAY)
@Service
public class AliPay implements IPay {
    @Override
    public void pay() {
        System.out.println("===发起支付宝支付===");
    }
}