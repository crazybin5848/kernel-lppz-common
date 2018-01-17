package com.lppz.diamond.client.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static final StringDecoder DECODER = new StringDecoder(CharsetUtil.UTF_8);
    private static final Netty4ClientHandler CLIENTHANDLER = new Netty4ClientHandler();
    
    private String clientMsg;
    
    public ClientChannelInitializer(String clientMsg) {
		this.clientMsg = clientMsg;
	}

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("info", new SendConnectInfoHandler(clientMsg));
        pipeline.addLast("frame", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast("decoder", DECODER);

        pipeline.addLast("handler", CLIENTHANDLER);
    }
    
    public Netty4ClientHandler getClientHandler() {
    	return CLIENTHANDLER;
    }
}