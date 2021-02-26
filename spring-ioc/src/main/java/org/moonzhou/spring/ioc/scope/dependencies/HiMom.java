/*
 * Copyright (C), 2002-2021, moon-zhou
 * FileName: HiMom.java
 * Author:   moon-zhou
 * Email:    ayimin1989@163.com
 * Date:     2021/2/26 17:43
 * Description: //模块目的、功能描述
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名    修改时间    版本号       描述
 */
package org.moonzhou.spring.ioc.scope.dependencies;

/**
 * 功能描述:<br>
 *
 * @author moon-zhou
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class HiMom {

    private Performer performer;

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    public Performer getPerformer() {
        return performer;
    }

    @Override
    public String toString() {
        return "HiMom{" +
                "performer=" + performer +
                '}';
    }
}
