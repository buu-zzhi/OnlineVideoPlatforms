package com.easylive.component;


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
import com.easylive.utils.JsonUtils;
import com.easylive.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
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
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    private Boolean isExistIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(appConfig.getEsIndexVideoName());
        return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

    }

    public void createIndex() {
        try {
            if (isExistIndex()) {
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(appConfig.getEsIndexVideoName());
            request.settings("{\"analysis\" : {\n" +
                    "    \"analyzer\": {\n" +
                    "      \"comma\": {\n" +
                    "        \"type\": \"pattern\",\n" +
                    "        \"pattern\": \",\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }}", XContentType.JSON);

            request.mapping("{\"properties\": {\n" +
                    "    \"videoId\":{\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"userId\":{\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\":false\n" +
                    "    },\n" +
                    "    \"videoCover\":{\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\":false\n" +
                    "    },\n" +
                    "    \"videoName\":{\n" +
                    "      \"type\":\"text\",\n" +
                    "      \"analyzer\":\"ik_max_word\"\n" +
                    "     },\n" +
                    "    \"tags\":{\n" +
                    "      \"type\":\"text\",\n" +
                    "      \"analyzer\":\"comma\"\n" +
                    "    },\n" +
                    "    \"playCount\":{\n" +
                    "      \"type\":\"integer\",\n" +
                    "      \"index\":false\n" +
                    "    },\n" +
                    "    \"danmuCount\":{\n" +
                    "      \"type\":\"integer\",\n" +
                    "      \"index\":false\n" +
                    "    },\n" +
                    "    \"collectCount\":{\n" +
                    "      \"type\":\"integer\",\n" +
                    "      \"index\":false\n" +
                    "    },\n" +
                    "    \"createTime\":{\n" +
                    "      \"type\":\"date\",\n" +
                    "      \"format\": \"yyyy-MM-dd HH:mm:ss\",\n" +
                    "      \"index\": false\n" +
                    "    }\n" +
                    "  }}", XContentType.JSON);

            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            Boolean acknowledged = createIndexResponse.isAcknowledged();
            if (!acknowledged) {
                throw new BusinessException("初始化es失败");
            }
        } catch (Exception e) {
            log.error("初始化es失败", e);
            throw new BusinessException("初始化es失败");
        }
    }

    public void saveDoc(VideoInfo videoInfo) {
        try {
            if (docExist(videoInfo.getVideoId())) {
                UpdateDoc(videoInfo);
            } else {
                IndexDoc(videoInfo);
            }
        } catch (IOException e) {
            log.error("保存到es失败", e);
            throw  new BusinessException("保存到es失败");
        }
    }

    private Boolean docExist(String id) throws IOException {
        GetRequest request = new GetRequest(appConfig.getEsIndexVideoName(), id);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        return response.isExists();
    }

    private void IndexDoc(VideoInfo videoInfo) {
        try {
            VideoInfoEsDto videoInfoEsDto = CopyTools.copy(videoInfo, VideoInfoEsDto.class);
            videoInfoEsDto.setCollectCount(0);
            videoInfoEsDto.setPlayCount(0);
            videoInfoEsDto.setDanmuCount(0);
            IndexRequest request = new IndexRequest(appConfig.getEsIndexVideoName());
            request.id(videoInfo.getVideoId()).source(JsonUtils.convertObj2Json(videoInfoEsDto), XContentType.JSON);
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
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
                if (object != null && object instanceof String && !StringTools.isEmpty(object.toString()) || object != null && !(object instanceof String)) {
                    dataMap.put(field.getName(), object);
                }
            }

            if (dataMap.isEmpty()) {
                return;
            }
            UpdateRequest updateRequest = new UpdateRequest(appConfig.getEsIndexVideoName(), videoInfo.getVideoId());
            updateRequest.doc(dataMap);
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es更新视频失败", e);
            throw new BusinessException("es更新视频失败");
        }
    }

    public void updateDocCount(String videoId, String fieldName, Integer count) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(appConfig.getEsIndexVideoName(), videoId);
            Script script = new Script(ScriptType.INLINE, "painless", "ctx._source." + fieldName + " += params.count", Collections.singletonMap("count", count));
            updateRequest.script(script);
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("更新数量到es失败", e);
            throw new BusinessException("更新数量到es失败");
        }
    }

    public void delDoc(String videoId) {
        DeleteRequest request = new DeleteRequest(appConfig.getEsIndexVideoName(), videoId);
        try {
            restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("从es删除视频失败", e);
            throw new BusinessException("从es删除视频失败");
        }
    }

    public PaginationResultVO<VideoInfo> search(Boolean highlight, String keyword, Integer orderType, Integer pageNo, Integer pageSize) {
        try {
            SearchOrderTypeEnum searchOrderTypeEnum = SearchOrderTypeEnum.getByType(orderType);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "videoName", "tags"));
            if (highlight) { // 高亮
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                highlightBuilder.field("videoName");
                highlightBuilder.preTags("<span style='color:red'>");
                highlightBuilder.postTags("</span>");
                searchSourceBuilder.highlighter(highlightBuilder);
            }
            // 排序
            searchSourceBuilder.sort("_score", SortOrder.ASC);
            if (orderType != null) {
                searchSourceBuilder.sort(searchOrderTypeEnum.getField(), SortOrder.DESC);
            }
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? PageSize.SIZE10.getSize() : pageSize;
            searchSourceBuilder.from((pageNo - 1) * pageSize);
            SearchRequest searchRequest = new SearchRequest(appConfig.getEsIndexVideoName());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 将es结果塞入数组中
            SearchHits hits = searchResponse.getHits();
            Integer totalCount  =  (int) hits.getTotalHits().value;
            List<VideoInfo> videoInfolist = new ArrayList<>(); // 存储查询结果中的视频信息
            List<String> userIdList = new ArrayList<>();  // 查询用户对应的姓名
            for (SearchHit hit : hits.getHits()) {
                VideoInfo videoInfo = JsonUtils.convertJson2Obj(hit.getSourceAsString(), VideoInfo.class);
                if (hit.getHighlightFields().get("videoName") != null) {
                    videoInfo.setVideoName(hit.getHighlightFields().get("videoName").fragments()[0].toString());
                }
                videoInfolist.add(videoInfo);
                userIdList.add(videoInfo.getUserId());
            }

            UserInfoQuery userInfoQuery = new UserInfoQuery();
            userInfoQuery.setUserIdList(userIdList);
            List<UserInfo> userInfoList = userInfoMapper.selectList(userInfoQuery);
            Map<String, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, Function.identity(), (data1, data2)->data2));
            videoInfolist.forEach(item -> {
                UserInfo userInfo = userInfoMap.get(item.getUserId());
                item.setNickName(userInfo==null?"":userInfo.getNickName());
            });

            SimplePage page = new SimplePage(pageNo, totalCount, pageSize);
            PaginationResultVO<VideoInfo> resultVO = new PaginationResultVO<>(totalCount, page.getPageSize(), page.getPageNo(), page.getPageTotal(), videoInfolist);
            return resultVO;
        } catch (Exception e) {
            log.error("查询视频到es失败", e);
            throw new BusinessException("查询视频到es失败");
        }
    }
}
