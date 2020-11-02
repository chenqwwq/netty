package io.netty.example.echo;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

/**
 * @author chen
 * @date 2020/10/11
 **/
public class LearnHandler extends ChannelDuplexHandler {


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().pipeline().addLast(new Learn2Handler());
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("Fuucckkk");
        super.bind(ctx, localAddress, promise);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("// ================ 渠道已经注册");
        ctx.fireChannelRegistered();
    }
}
