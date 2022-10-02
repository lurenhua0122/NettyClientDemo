package cn.com.aiton.reconn;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AppClientReconn {
    private   String ip = "127.0.0.1";
    private   int port = 3333;

    public AppClientReconn(String ip, int port) {
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
                            socketChannel.pipeline().addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new AppClientReconnHandler());
                        }
                    });
            ChannelFuture cf = bs.connect();
            cf.addListener(new ClientReconnListener());

            while(true){
                Thread.sleep(3000);
                SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                String str = df.format(new Date());

                cf.channel().writeAndFlush(Unpooled.copiedBuffer("AppClientReconn"+str, CharsetUtil.UTF_8));

            }
//            cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8));
//            cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new AppClientReconn("127.0.0.1",3333).run();
    }
}
