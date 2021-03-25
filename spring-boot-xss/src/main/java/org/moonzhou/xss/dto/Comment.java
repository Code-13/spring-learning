/*
 * Copyright (C), 2002-2021, moon-zhou
 * FileName: Comment.java
 * Author:   moon-zhou
 * Email:    ayimin1989@163.com
 * Date:     2021/3/20 17:51
 * Description: //模块目的、功能描述
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名    修改时间    版本号       描述
 */
package org.moonzhou.xss.dto;

import lombok.Data;

/**
 * 功能描述: 评论内容封装类<br>
 *     存储型XSS示例使用
 *
 * @author moon-zhou
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Data
public class Comment {

    /**
     * 评论内容
     */
    private String content;
}
