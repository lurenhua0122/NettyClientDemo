package cn.com.aiton.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class AppServerHello {
    private   String ip = "127.0.0.1";
    private   int port = 3333;

    public AppServerHello(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    public void run() throws  Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new AppServerHelloHandler());
                        }
                    });
            ChannelFuture cf = bs.bind().sync();
            System.out.println("启动："+cf.channel().localAddress());
//            cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8));
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new AppServerHello("127.0.0.1",3333).run();
    }
}
