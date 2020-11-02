package io.netty.example.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author chen
 * @date 2020/10/27
 **/
public class Learn2Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("这里是Leader2Handler");
        super.channelRegistered(ctx);
    }
}
