package com.easylive.web.component;

import com.easylive.component.RedisComponent;
import com.easylive.entity.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Resource
    private RedisComponent redisComponent;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        // 只关心用户在redis中的变化
        if (!key.startsWith(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE_PREFiX+Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREFiX)) {
            return;
        }
        Integer userKey = key.indexOf(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREFiX) + Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREFiX.length();
        String fileId = key.substring(userKey, userKey + Constants.length_10);
        redisComponent.decrementPlayOnlineCount(String.format(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE, fileId));
    }
}
