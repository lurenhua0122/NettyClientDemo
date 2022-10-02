package cn.com.aiton.reconn;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClientReconnListener implements ChannelFutureListener {
    private AppClientReconn appClientReconn  = new AppClientReconn("127.0.0.1",3333);
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()){
            EventLoop loop = channelFuture.channel().eventLoop();
            ScheduledFuture<?> scheduledFuture = loop.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("operationComplete false");
                        appClientReconn.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },5, TimeUnit.SECONDS);
        }else{
            System.out.println("operationComplete  true");
        }
    }
}
