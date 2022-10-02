package cn.com.aiton.monitor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

@ChannelHandler.Sharable
public class AppServerMonitorHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bf = (ByteBuf) msg;
        System.out.println(bf.toString(CharsetUtil.UTF_8));
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String str = df.format(new Date());
        ctx.writeAndFlush(Unpooled.copiedBuffer("这个是服务端的channelRead方法"+str, CharsetUtil.UTF_8));
        ctx.writeAndFlush(Unpooled.copiedBuffer("receviced your msg",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incon = ctx.channel();
        System.out.println("客户端新增："+incon.remoteAddress());
        channels.add(incon);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incon = ctx.channel();
        System.out.println("客户端移除："+incon.remoteAddress());
        channels.remove(incon);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incon = ctx.channel();
        System.out.println(incon.remoteAddress());
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String str = df.format(new Date());
        ctx.writeAndFlush(Unpooled.copiedBuffer("这个是服务端的channelActive方法"+str, CharsetUtil.UTF_8));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incon = ctx.channel();
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String str = df.format(new Date());
        System.out.println("客户端掉线："+incon.remoteAddress()+" \t"+str);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("channelInactive"+str, CharsetUtil.UTF_8));
    }

    //    通道数组，保存所有注册到EventLoop的通道
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
