package com.easylive.component;

import com.easylive.entity.config.AppConfig;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.dto.SysSettingDto;
import com.easylive.entity.dto.TokenUserInfoDto;
import com.easylive.entity.dto.UploadingFileDto;
import com.easylive.entity.enums.DateTimePatternEnum;
import com.easylive.entity.po.CategoryInfo;
import com.easylive.entity.po.VideoInfoFilePost;
import com.easylive.redis.RedisUtils;
import com.easylive.utils.DateUtils;
import com.easylive.utils.StringTools;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private AppConfig appConfig;

    public String saveCheckCode(String code) {
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, Constants.REDIS_KEY_EXPIRES_ONE_MIN * 10);
        return checkCodeKey;
    }

    public String getCheckCode(String checkCodeKey) {
        return (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void cleanCheckCode(String checkCodeKey) {
        redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    /**
     * 自定义token并将 token: tokenUserInfoDto 保存至redis
     * @param tokenUserInfoDto
     */
    public void saveToKenInfo(TokenUserInfoDto tokenUserInfoDto) {
        String token = UUID.randomUUID().toString();
        tokenUserInfoDto.setExpireAt(System.currentTimeMillis() + Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7L);
        tokenUserInfoDto.setToken(token);
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB + token, tokenUserInfoDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7L);
    }

    public void cleanToken(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public TokenUserInfoDto getTokenInfo(String token) {
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    /**
     * 自定义token并将 token: account 保存至 redis
     * @param account
     */
    public String saveToKenInfo4Admin(String account) {
        String token = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN + token, account, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
        return token;
    }

    public String getTokenInfo4Admin(String token) {
        return (String) redisUtils.get(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public void cleanToken4Admin(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public void saveCategoryList(List<CategoryInfo> categoryInfoList) {
        redisUtils.set(Constants.REDIS_KEY_CATEGORY_LIST, categoryInfoList);
    }

    public List<CategoryInfo> getCategoryList() {
        return (List<CategoryInfo>) redisUtils.get(Constants.REDIS_KEY_CATEGORY_LIST);
    }

    /**
     * 将上传文件的信息保存至redis中
     * @param userId
     * @param fileName
     * @param chunks  视频切片数
     */
    public String savePreVideoFileInfo(String userId, String fileName, Integer chunks) {
        String uploadId = StringTools.getRandomString(Constants.length_15);
        UploadingFileDto fileDto = new UploadingFileDto();
        fileDto.setChunks(chunks);
        fileDto.setFileName(fileName);
        fileDto.setUploadId(uploadId);
        fileDto.setChunkIndex(0);
        String day = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMMDD.getPattern());
        String filePath = day + "/" + userId + uploadId;
        String folder = appConfig.getProjectFolder()+Constants.FILE_FOLDER+Constants.FILE_FOLDER_TEMP + filePath;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        fileDto.setFilePath(filePath);
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId+uploadId, fileDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
        return uploadId;
    }


    public void saveSysSettingDto() {
        SysSettingDto sysSettingDto = new SysSettingDto();
        redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
    }

    public SysSettingDto getSysSettingDto() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if (null == sysSettingDto) {
            saveSysSettingDto();
        }
        return new SysSettingDto();
    }

    public UploadingFileDto getUploadVideoFile(String userId, String uploadId) {
        return (UploadingFileDto) redisUtils.get(Constants.REDIS_KEY_UPLOADING_FILE + userId+uploadId);
    }

    public void updateVideoFileInfo(String userId, UploadingFileDto fileDto) {
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId+fileDto.getUploadId(), fileDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
    }

    public void delVideoFileInfo(String userId, String uploadId) {
        redisUtils.delete(Constants.REDIS_KEY_UPLOADING_FILE + userId+uploadId);
    }

    /**
     * 把需要删除的文件路径放到消息队列中
     * @param videoId
     * @param filePathList
     */
    public void addFile2DelQueue(String videoId, List<String> filePathList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_FILE_DEL+videoId, filePathList, Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }

    public List<String> getDelFileList(String videoId) {
        return redisUtils.getQueueList(Constants.REDIS_KEY_FILE_DEL+videoId);
    }

    public void cleanDelFileList(String videoId) {
        redisUtils.delete(Constants.REDIS_KEY_FILE_DEL+videoId);
    }

    public void addFile2TransferQueue(List<VideoInfoFilePost> addFileList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_QUEUE_TRANSFER, addFileList, -1);
    }

    public VideoInfoFilePost getFileFromTransferQueue() {
        return (VideoInfoFilePost) redisUtils.rpop(Constants.REDIS_KEY_QUEUE_TRANSFER);
    }

    public Integer reportVideoPlayOnline(String fileId, String deviceId) {
        // user:fileId:deviceId
        String userPlayOnlineKey = String.format(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER, fileId, deviceId);
        //count:fileId
        String playOnlineCountKey = String.format(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE, fileId);

        if (!redisUtils.keyExists(userPlayOnlineKey)) {
            redisUtils.setex(userPlayOnlineKey, fileId, Constants.REDIS_KEY_EXPIRES_ONE_SECOND*8);
            return redisUtils.incrementex(playOnlineCountKey, Constants.REDIS_KEY_EXPIRES_ONE_SECOND*10).intValue();
        }
        redisUtils.expire(userPlayOnlineKey, Constants.REDIS_KEY_EXPIRES_ONE_SECOND*8);
        redisUtils.expire(playOnlineCountKey, Constants.REDIS_KEY_EXPIRES_ONE_SECOND*10);
        Integer count = (Integer) redisUtils.get(playOnlineCountKey);
        return count==null?1:count;
    }
    public void decrementPlayOnlineCount(String key) {
        redisUtils.decrement(key);
    }
}
