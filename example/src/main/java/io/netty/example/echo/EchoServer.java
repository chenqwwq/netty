/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.SocketAddress;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Echoes back any received data from a client.
 */
public final class EchoServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Netty服务端的初始化流程基本上就是创建关键的ServerSocketChannel，并初始化一些通用组件，比如EventLoopGroup，ServerBootstrap等
        // 另外的需要将ServerSocketChannel注册到bossGroup中，并开启accept，设置SelectorKey

        // Configure the server.
        // doubt: EventLoop，EventLoopGroup，EventExecutor等类之间的关系
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final EchoServerHandler serverHandler = new EchoServerHandler();
        try {
            // 创建并配置SeverBootStrap
            // 构造函数啥事没干
            ServerBootstrap b = new ServerBootstrap();
            // 指定两个线程组,Netty是以何种Reactor的形式运行的在此时就决定了
            b.group(bossGroup, workerGroup)
                    // 服务端的Channel类型
                    // 切换协议类型以及Nio,Bio就在这里修改
                    // TCP & NIO - NioSocketChannel
                    // UDP & NIO - NioDatagramChannel 等等...
                    .channel(NioServerSocketChannel.class)
                    // 服务端Channel的配置
                    // 常用的还有SO_KEEPALIVE,SO_SNDBUF,SO_RCVBUF等
                    .option(ChannelOption.SO_BACKLOG, 100)
                    // 服务端的Handler
                    // doubt: 服务端Handler的处理逻辑
                    // 搜了下网上说是handler仅仅在初始化时执行一次
                    // childHandler会在客户端连接后执行
                    .handler(new ChannelInitializer<ServerSocketChannel>() {
                        @Override
                        protected void initChannel(ServerSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler());
                            ch.pipeline().addLast(new LearnHandler());
                        }
                    })
                    // 指定了ChannelInitializer
                    // 如何根据ChannelInitializer生成ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(serverHandler);
                        }
                    });

            // 以上流程之后ServerBootStrap已经完全创建好了

            // Start the server.
            // 同步绑定端口
            /**
             * @see io.netty.bootstrap.AbstractBootstrap#doBind(SocketAddress)
             */
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
