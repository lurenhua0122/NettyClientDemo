package cn.com.aiton.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class AppClientHello {
    private   String ip = "127.0.0.1";
    private   int port = 3333;

    public AppClientHello(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    public void run() throws  Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bs = new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(ip,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HelloHandler());
                        }
                    });
            ChannelFuture cf = bs.connect().sync();

            cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8));
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new AppClientHello("127.0.0.1",3333).run();
    }
}
