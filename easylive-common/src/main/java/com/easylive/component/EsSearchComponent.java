package com.easylive.component;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.easylive.entity.config.AppConfig;
import com.easylive.entity.dto.VideoInfoEsDto;
import com.easylive.entity.enums.PageSize;
import com.easylive.entity.enums.SearchOrderTypeEnum;
import com.easylive.entity.po.UserInfo;
import com.easylive.entity.po.VideoInfo;
import com.easylive.entity.query.SimplePage;
import com.easylive.entity.query.UserInfoQuery;
import com.easylive.entity.vo.PaginationResultVO;
import com.easylive.exception.BusinessException;
import com.easylive.mapper.UserInfoMapper;
import com.easylive.utils.CopyTools;
import com.easylive.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("esSearchComponent")
@Slf4j
public class EsSearchComponent {
    @Resource
    private AppConfig appConfig;

    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    private Boolean isExistIndex() throws IOException {
        return elasticsearchClient.indices().exists(ExistsRequest.of(e -> e.index(appConfig.getEsIndexVideoName()))).value();
    }

    public void createIndex() {
        try {
            if (isExistIndex()) {
                return;
            }
            createIndex(appConfig.getEsIndexVideoAnalyzer());
        } catch (Exception e) {
            if (shouldFallbackToStandardAnalyzer(e, appConfig.getEsIndexVideoAnalyzer())) {
                try {
                    log.warn("ES analyzer [{}] 不可用，回退到 standard analyzer 创建索引", appConfig.getEsIndexVideoAnalyzer(), e);
                    createIndex("standard");
                    return;
                } catch (Exception fallbackException) {
                    log.error("ES analyzer 回退到 standard 后仍然初始化失败", fallbackException);
                    throw new BusinessException("初始化es失败");
                }
            }
            log.error("初始化es失败", e);
            throw new BusinessException("初始化es失败");
        }
    }

    private void createIndex(String videoAnalyzer) throws IOException {
        elasticsearchClient.indices().create(e -> e.index(appConfig.getEsIndexVideoName())
                .withJson(new StringReader(buildIndexJson(videoAnalyzer))));
    }

    private String buildIndexJson(String videoAnalyzer) {
        String analyzer = StringTools.isEmpty(videoAnalyzer) ? "standard" : videoAnalyzer;
        return String.format(Locale.ROOT,
                "{\"settings\":{\"analysis\":{\"analyzer\":{\"comma\":{\"type\":\"pattern\",\"pattern\":\",\"}}}},\"mappings\":{\"properties\":{\"videoId\":{\"type\":\"text\",\"index\":false},\"userId\":{\"type\":\"text\",\"index\":false},\"videoCover\":{\"type\":\"text\",\"index\":false},\"videoName\":{\"type\":\"text\",\"analyzer\":\"%s\"},\"tags\":{\"type\":\"text\",\"analyzer\":\"comma\"},\"playCount\":{\"type\":\"integer\",\"index\":false},\"danmuCount\":{\"type\":\"integer\",\"index\":false},\"collectCount\":{\"type\":\"integer\",\"index\":false},\"createTime\":{\"type\":\"date\",\"format\":\"yyyy-MM-dd HH:mm:ss\",\"index\":false}}}}",
                analyzer);
    }

    private boolean shouldFallbackToStandardAnalyzer(Exception e, String configuredAnalyzer) {
        if (StringTools.isEmpty(configuredAnalyzer) || "standard".equalsIgnoreCase(configuredAnalyzer)) {
            return false;
        }
        String errorMessage = e.getMessage();
        if (StringTools.isEmpty(errorMessage) && e.getCause() != null) {
            errorMessage = e.getCause().getMessage();
        }
        if (StringTools.isEmpty(errorMessage)) {
            return false;
        }
        String normalized = errorMessage.toLowerCase(Locale.ROOT);
        return normalized.contains("analyzer")
                && (normalized.contains("not found")
                || normalized.contains("failed to find")
                || normalized.contains("unknown"));
    }

    public void saveDoc(VideoInfo videoInfo) {
        try {
            if (docExist(videoInfo.getVideoId())) {
                UpdateDoc(videoInfo);
            } else {
                IndexDoc(videoInfo);
            }
        } catch (Exception e) {
            log.error("保存到es失败", e);
            throw new BusinessException("保存到es失败");
        }
    }

    private Boolean docExist(String id) throws IOException {
        return elasticsearchClient.exists(e -> e.index(appConfig.getEsIndexVideoName()).id(id)).value();
    }

    private void IndexDoc(VideoInfo videoInfo) {
        try {
            VideoInfoEsDto videoInfoEsDto = CopyTools.copy(videoInfo, VideoInfoEsDto.class);
            videoInfoEsDto.setCollectCount(0);
            videoInfoEsDto.setPlayCount(0);
            videoInfoEsDto.setDanmuCount(0);
            elasticsearchClient.index(e -> e.index(appConfig.getEsIndexVideoName()).id(videoInfo.getVideoId()).document(videoInfoEsDto));
        } catch (Exception e) {
            log.error("es更新视频失败", e);
            throw new BusinessException("es更新视频失败");
        }
    }

    private void UpdateDoc(VideoInfo videoInfo) {
        try {
            videoInfo.setLastUpdateTime(null);
            videoInfo.setCreateTime(null);
            Map<String, Object> dataMap = new HashMap<>();
            Field[] fields = videoInfo.getClass().getDeclaredFields();
            for (Field field : fields) {
                String methodName = "get" + StringTools.UpperCaseFirstLetter(field.getName());
                Method method = videoInfo.getClass().getMethod(methodName);

                Object object = method.invoke(videoInfo);
                if ((object != null && object instanceof String && !StringTools.isEmpty((String)object)) || (object != null && !(object instanceof String))) {
                    dataMap.put(field.getName(), object);
                }
            }

            if (dataMap.isEmpty()) {
                return;
            }
            elasticsearchClient.update(e -> e.index(appConfig.getEsIndexVideoName()).id(videoInfo.getVideoId()).doc(dataMap), Map.class);
        } catch (Exception e) {
            log.error("es更新视频失败", e);
            throw new BusinessException("es更新视频失败");
        }
    }

    public void updateDocCount(String videoId, String fieldName, Integer count) {
        try {
            elasticsearchClient.update(e -> e.index(appConfig.getEsIndexVideoName()).id(videoId)
                            .script(s -> s.inline(i -> i.source("ctx._source." + fieldName + " += params.count").params("count", co.elastic.clients.json.JsonData.of(count)))),
                    Map.class);
        } catch (Exception e) {
            log.error("更新数量到es失败", e);
            throw new BusinessException("更新数量到es失败");
        }
    }

    public void delDoc(String videoId) {
        try {
            elasticsearchClient.delete(e -> e.index(appConfig.getEsIndexVideoName()).id(videoId));
        } catch (IOException e) {
            log.error("从es删除视频失败", e);
            throw new BusinessException("从es删除视频失败");
        }
    }

    public PaginationResultVO<VideoInfo> search(Boolean highlight, String keyword, Integer orderType, Integer pageNo, Integer pageSize) {
        try {
            SearchOrderTypeEnum searchOrderTypeEnum = SearchOrderTypeEnum.getByType(orderType);
            
            Query query = MultiMatchQuery.of(m -> m.query(keyword).fields("videoName", "tags"))._toQuery();

            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? PageSize.SIZE10.getSize() : pageSize;
            
            int from = (pageNo - 1) * pageSize;
            int size = pageSize;
            
            SearchRequest.Builder searchBuilder = new SearchRequest.Builder()
                    .index(appConfig.getEsIndexVideoName())
                    .query(query)
                    .from(from)
                    .size(size);
                    
            if (highlight) {
                searchBuilder.highlight(h -> h
                        .fields("videoName", f -> f.preTags("<span style='color:red'>").postTags("</span>")));
            }
            
            searchBuilder.sort(s -> s.score(sc -> sc.order(SortOrder.Asc)));
            if (orderType != null && searchOrderTypeEnum != null) {
                searchBuilder.sort(s -> s.field(f -> f.field(searchOrderTypeEnum.getField()).order(SortOrder.Desc)));
            }

            SearchResponse<VideoInfoEsDto> searchResponse = elasticsearchClient.search(searchBuilder.build(), VideoInfoEsDto.class);

            long totalHits = searchResponse.hits().total() != null ? searchResponse.hits().total().value() : 0;
            Integer totalCount = (int) totalHits;
            
            List<VideoInfo> videoInfolist = new ArrayList<>();
            List<String> userIdList = new ArrayList<>();
            for (Hit<VideoInfoEsDto> hit : searchResponse.hits().hits()) {
                VideoInfoEsDto dto = hit.source();
                VideoInfo videoInfo = CopyTools.copy(dto, VideoInfo.class);
                if (highlight && hit.highlight().containsKey("videoName")) {
                    videoInfo.setVideoName(hit.highlight().get("videoName").get(0));
                }
                videoInfolist.add(videoInfo);
                userIdList.add(videoInfo.getUserId());
            }

            if (!userIdList.isEmpty()) {
                UserInfoQuery userInfoQuery = new UserInfoQuery();
                userInfoQuery.setUserIdList(userIdList);
                List<UserInfo> userInfoList = userInfoMapper.selectList(userInfoQuery);
                Map<String, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, Function.identity(), (data1, data2) -> data2));
                videoInfolist.forEach(item -> {
                    UserInfo userInfo = userInfoMap.get(item.getUserId());
                    item.setNickName(userInfo == null ? "" : userInfo.getNickName());
                });
            }

            SimplePage page = new SimplePage(pageNo, totalCount, size);
            return new PaginationResultVO<>(totalCount, page.getPageSize(), page.getPageNo(), page.getPageTotal(), videoInfolist);
        } catch (Exception e) {
            log.error("查询视频到es失败", e);
            throw new BusinessException("查询视频到es失败");
        }
    }
}
