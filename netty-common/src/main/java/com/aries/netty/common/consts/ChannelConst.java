package com.aries.netty.common.consts;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created with IntelliJ IDEA.
 * User: daozhang
 * Time: 2019/6/23
 * Description:
 */
public interface ChannelConst {

    ByteBuf DELIMITER = Unpooled.copiedBuffer("_$_$".getBytes());
}
