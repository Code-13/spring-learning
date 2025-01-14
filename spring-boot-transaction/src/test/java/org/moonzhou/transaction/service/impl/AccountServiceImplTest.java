package org.moonzhou.transaction.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.moonzhou.transaction.constant.ConditionEnum;
import org.moonzhou.transaction.constant.ExceptionHandleEnum;
import org.moonzhou.transaction.constant.RollbackExceptionEnum;
import org.moonzhou.transaction.entity.Account;
import org.moonzhou.transaction.param.AccountParam;
import org.moonzhou.transaction.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    private IAccountService accountService;

    @BeforeEach
    void beforeTest() {
        boolean deleteAll = accountService.deleteAll();
        log.info("before test class, delete data: {}.", deleteAll);
    }

    /**
     * 测试方法无事务注解，正常和异常均可以保存
     */
    @Order(1)
    @Test
    void saveAccountWithoutTransAction() {
        // 正常情况，正常保存
        AccountParam normalAccountParam = new AccountParam("001", "moon", 18, "ayimin1989@163.com", "Financial member");
        Long normalSave = accountService.saveAccountWithoutTransAction(normalAccountParam, ConditionEnum.NORMAL);
        assertTrue(normalSave > 0L);

        // 运行时异常，但是因为没有添加事务注解，依然可以正常保存
        AccountParam exceptionAccountParam = new AccountParam("002", "moon", 19, "ayimin1989@163.com", "Bank member");
        assertThrows(RuntimeException.class, () -> {
            accountService.saveAccountWithoutTransAction(exceptionAccountParam, ConditionEnum.RUNTIME_EXCEPTION);
        });
        // 断言保存后有数据
        Account exceptionAccount = accountService.getOneByParam(exceptionAccountParam);
        assertNotNull(exceptionAccount);
    }

    /**
     * 测试方法有事务注解，正常可以保存，异常不能保存
     */
    @Order(2)
    @Test
    void saveAccountTransAction() {
        // 正常情况，正常保存
        AccountParam normalAccountParam = new AccountParam("001", "moon", 18, "ayimin1989@163.com", "Financial member");
        Long normalSave = accountService.saveAccountTransAction(normalAccountParam, ConditionEnum.NORMAL);
        assertTrue(normalSave > 0L);

        // 运行时异常，但是因为添加事务注解，则无法正常保存
        AccountParam exceptionAccountParam = new AccountParam("002", "moon", 19, "ayimin1989@163.com", "Bank member");
        assertThrows(RuntimeException.class, () -> {
            accountService.saveAccountTransAction(exceptionAccountParam, ConditionEnum.RUNTIME_EXCEPTION);
        });
        // 断言保存后无数据
        Account exceptionAccount = accountService.getOneByParam(exceptionAccountParam);
        assertNull(exceptionAccount);
    }

    /**
     * 测试方法有事务注解，如果运行时异常被catch之后，未继续向上抛运行时异常，则不会回滚事务，数据可以被保存
     */
    @Order(3)
    @Test
    void saveAccountIncorrectTransAction() {
        // 保存数据，遇到异常不继续往上抛，异常不回滚，数据入库
        AccountParam normalAccountParam = new AccountParam("001", "moon", 18, "ayimin1989@163.com", "Financial member");
        accountService.saveAccountIncorrectTransAction(normalAccountParam, ExceptionHandleEnum.LOG);
        // 断言数据已入库
        Account normalAccount = accountService.getOneByParam(normalAccountParam);
        assertNotNull(normalAccount);

        // 保存数据，捕获异常之后，继续往上抛异常
        AccountParam exceptionAccountParam = new AccountParam("002", "moon", 19, "ayimin1989@163.com", "Bank member");
        assertThrows(RuntimeException.class, () -> {
            accountService.saveAccountIncorrectTransAction(exceptionAccountParam, ExceptionHandleEnum.THROW_RUNTIME_EXCEPTION);
        });
        // 断言事务回滚，数据未入库
        Account exceptionAccount = accountService.getOneByParam(exceptionAccountParam);
        assertNull(exceptionAccount);
    }

    @Order(4)
    @Test
    void saveAccountRollbackBizException() {
        // 保存数据，抛出AccountBiz1RuntimeException，且符合rollback里配置的具体异常，会回滚，数据不保存
        AccountParam biz1ExceptionAccountParam = new AccountParam("001", "moon", 18, "ayimin1989@163.com", "Financial member");
        accountService.saveAccountRollbackBizException(biz1ExceptionAccountParam, RollbackExceptionEnum.ACCOUNT_BIZ_ONE_EXCEPTION);
        // 断言数据未入库
        Account biz1ExceptionAccount = accountService.getOneByParam(biz1ExceptionAccountParam);

        // 保存数据，抛出AccountBiz2RuntimeException，不在rollback异常里，不会回滚，数据存在
        AccountParam biz2ExceptionAccountParam = new AccountParam("002", "moon", 19, "ayimin1989@163.com", "Bank member");
    }

    @Order(5)
    @Test
    void saveAccountInnerPublicMethod() {
        AccountParam saveInnerAccountParam = new AccountParam("001", "moon", 18, "ayimin1989@163.com", "Financial member");
        accountService.saveAccountInnerPublicMethod(saveInnerAccountParam, ConditionEnum.NORMAL);
        List<Account> normalList = accountService.getListByParam(saveInnerAccountParam);
        assertEquals(2, normalList.size());

        // 运行时异常，因为是通过内部方法调用了加注解的公共方法（入口方法无事务注解），所以事务不会生效，抛出异常时原方法和被调用的方法数据都会保存下来。
        AccountParam exceptionAccountParam = new AccountParam("002", "moon", 19, "ayimin1989@163.com", "Bank member");
        assertThrows(RuntimeException.class, () -> {
            accountService.saveAccountInnerPublicMethod(exceptionAccountParam, ConditionEnum.RUNTIME_EXCEPTION);
        });
        List<Account> exceptionList = accountService.getListByParam(exceptionAccountParam);
        assertEquals(2, exceptionList.size());
    }

    @Order(6)
    @Test
    void saveAccountTransactionInnerPublicMethod() {
        AccountParam saveInnerAccountParam = new AccountParam("001", "moon", 18, "ayimin1989@163.com", "Financial member");
        accountService.saveAccountTransactionInnerPublicMethod(saveInnerAccountParam, ConditionEnum.NORMAL);
        List<Account> normalList = accountService.getListByParam(saveInnerAccountParam);
        assertEquals(2, normalList.size());

        // 运行时异常，因为是通过内部方法调用了加注解的公共方法（入口方法有事务注解），所以事务生效，抛出异常时原方法和被调用的方法不会保存下来。此处是事务传播特性。
        AccountParam exceptionAccountParam = new AccountParam("002", "moon", 19, "ayimin1989@163.com", "Bank member");
        assertThrows(RuntimeException.class, () -> {
            accountService.saveAccountTransactionInnerPublicMethod(exceptionAccountParam, ConditionEnum.RUNTIME_EXCEPTION);
        });
        List<Account> exceptionList = accountService.getListByParam(exceptionAccountParam);
        assertEquals(0, exceptionList.size());
    }
}