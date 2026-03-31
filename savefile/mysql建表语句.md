- 统一数据库排序`ALTER TABLE category_info CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;`

```
-- 用户表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(加密存储)',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(500) COMMENT '头像URL',
    `email` VARCHAR(100) COMMENT '邮箱',
    `bio` VARCHAR(255) COMMENT '个人简介',
    `role` TINYINT DEFAULT 1 COMMENT '角色: 0-管理员, 1-普通用户',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE `category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    `description` VARCHAR(255) COMMENT '分类描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 标签表
CREATE TABLE `tag` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 博客表
CREATE TABLE `blog` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '博客ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` LONGTEXT NOT NULL COMMENT '内容(Markdown)',
    `summary` VARCHAR(500) COMMENT '摘要',
    `cover_image` VARCHAR(500) COMMENT '封面图URL',
    `user_id` BIGINT NOT NULL COMMENT '作者ID',
    `category_id` BIGINT COMMENT '分类ID',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `like_count` INT DEFAULT 0 COMMENT '点赞量',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_create_time` (`create_time`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客表';

-- 博客标签关联表
CREATE TABLE `blog_tag` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `blog_id` BIGINT NOT NULL COMMENT '博客ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    UNIQUE KEY `uk_blog_tag` (`blog_id`, `tag_id`),
    FOREIGN KEY (`blog_id`) REFERENCES `blog`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `tag`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客标签关联表';

-- 评论表
CREATE TABLE `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `blog_id` BIGINT NOT NULL COMMENT '博客ID',
    `user_id` BIGINT NOT NULL COMMENT '评论者ID',
    `content` VARCHAR(1000) NOT NULL COMMENT '评论内容',
    `parent_id` BIGINT COMMENT '父评论ID(null表示一级评论)',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_blog_id` (`blog_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_parent_id` (`parent_id`),
    FOREIGN KEY (`blog_id`) REFERENCES `blog`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`parent_id`) REFERENCES `comment`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 博客点赞表
CREATE TABLE `blog_like` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `blog_id` BIGINT NOT NULL COMMENT '博客ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    UNIQUE KEY `uk_blog_user` (`blog_id`, `user_id`),
    FOREIGN KEY (`blog_id`) REFERENCES `blog`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客点赞表';

-- 评论点赞表
CREATE TABLE `comment_like` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `comment_id` BIGINT NOT NULL COMMENT '评论ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    UNIQUE KEY `uk_comment_user` (`comment_id`, `user_id`),
    FOREIGN KEY (`comment_id`) REFERENCES `comment`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞表';
CREATE TABLE `favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `blog_id` BIGINT NOT NULL COMMENT '博客ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_blog` (`user_id`, `blog_id`) COMMENT '用户-博客唯一索引，防止重复收藏',
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引，加速按用户查询',
  KEY `idx_blog_id` (`blog_id`) COMMENT '博客ID索引',
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_favorite_blog` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

```

```
create table `category_info` (  
    `category_id` int(11) not null auto_increment comment '自增分类ID',  
    `category_code` varchar(30) not null comment '分类编码',  
    `category_name` varchar(30) not null comment '分类名称',  
    `p_category_id` int(11) not null comment '父级分类ID',  
    `icon` varchar(50) default null comment '图标',  
    `background` varchar(50) default null comment '背景图',  
    `sort` tinyint not null comment '排序号',  
    primary key (`category_id`) using btree,  
    unique key `idx_key_category_code` (`category_code`) using btree  
) engine=InnoDB auto_increment=34 default charset=utf8mb4 row_format=dynamic comment='分类信息';
```

```
create table `video_info_post` (  
    `video_id` varchar(10) not null default '0' comment '视频ID',  
    `video_cover` varchar(50) not null comment '视频封面',  
    `video_name` varchar(100) not null comment '视频名称',  
    `user_id` varchar(10) not null comment '用户ID',  
    `create_time` datetime not null comment '创建时间',  
    `last_update_time` datetime not null comment '最后更新时间',  
    `p_category_id` int(11) not null comment '父级分类ID',  
    `category_id` int(11) default null comment '分类ID',  
    `status` tinyint(1) not null comment '0:转码中 1:转码失败 2:待审核 3:审核成功 4:审核失败',  
    `post_type` tinyint(4) not null comment '0:自制作 1:转载',  
    `origin_info` varchar(200) default null comment '原资源说明',  
    `tags` varchar(300) default null comment '标签',  
    `introduction` varchar(2000) default null comment '简介',  
    `interaction` varchar(5) default null comment '互动设置',  
    `duration` int(11) default null comment '持续时间（秒）',  
    primary key (`video_id`) using btree ,  
    key `idx_create_time` (`create_time`) using btree,  
    key `idx_user_id` (`user_id`) using btree,  
    key `idx_category_id` (`category_id`) using btree,  
    key `idx_pcategory_id` (`p_category_id`) using btree  
) engine=InnoDB default charset=utf8mb4 row_format=dynamic comment='视频信息';  
  
  
create table `video_info_file_post` (  
    `file_id` varchar(20) not null comment '唯一ID',  
    `upload_id` varchar(15) not null comment '上传ID',  
    `user_id` varchar(10) not null comment '用户ID',  
    `video_id` varchar(10) not null comment '视频ID',  
    `file_index` int(11) not null comment '文件索引',  
    `file_name` varchar(200) default null comment '文件名',  
    `file_size` bigint(20) default null comment '文件大小',  
    `file_path` varchar(100) default null comment '文件路径',  
    `update_type` tinyint(4) default null comment '0:无更新 1:有更新',  
    `transfer_result` tinyint(4) default null comment '0:转码中 1:转码成功 2:转码失败',  
    `duration` int(11) default null comment '持续时间（秒）',  
    primary key (`file_id`) using btree,  
    unique key `idx_key_upload_id` (`upload_id`, `user_id`) using btree,  
    key `idx_video_id` (`video_id`) using btree  
) engine=InnoDB default charset=utf8mb4 row_format=dynamic comment='视频文件信息';  
create table `video_info` (  
    `video_id` varchar(10) not null comment '视频ID',  
    `video_cover` varchar(50) not null comment '视频封面',  
    `video_name` varchar(100) not null comment '视频名称',  
    `user_id` varchar(10) not null comment '用户ID',  
    `create_time` datetime not null comment '创建时间',  
    `last_update_time` datetime not null comment '最后更新时间',  
    `p_category_id` int(11) not null comment '父级分类ID',  
    `category_id` int(11) default null comment '分类ID',  
    `post_type` tinyint(4) not null comment '0:自制作 1:转载',  
    `origin_info` varchar(200) default null comment '原资源说明',  
    `tags` varchar(300) default null comment '标签',  
    `introduction` varchar(2000) default null comment '简介',  
    `interaction` varchar(5) default null comment '互动设置',  
    `duration` int(11) default '0' comment '持续时间（秒）',  
    `play_count` int(11) default '0' comment '播放数量',  
    `like_count` int(11) default '0' comment '点赞数量',  
    `danmu_count` int(11) default '0' comment '弹幕数量',  
    `comment_count` int(11) default '0' comment '评论数量',  
    `coin_count` int(11) default '0' comment '投币数量',  
    `collect_count` int(11) default '0' comment '收藏数量',  
    `recommend_type` tinyint(1) default '0' comment '是否推荐0:未推荐 1:已推荐',  
    `last_play_time` datetime default null comment '最后播放时间',  
    primary key (`video_id`) using btree ,  
    key `idx_create_time` (`create_time`) using btree,  
    key `idx_user_id` (`user_id`) using btree,  
    key `idx_category_id` (`category_id`) using btree,  
    key `idx_pcategory_id` (`p_category_id`) using btree,  
    key `idx_recommend_type` (`recommend_type`) using btree,  
    key `idx_last_play_time` (`last_play_time`) using btree  
) engine=InnoDB default charset=utf8mb4 row_format=dynamic comment='视频信息';  
  
create table `video_info_file` (  
    `file_id` varchar(20) not null comment '唯一ID',  
    `user_id` varchar(10) not null comment '用户ID',  
    `video_id` varchar(10) not null comment '视频ID',  
    `file_name` varchar(200) default null comment '文件名',  
    `file_index` int(11) not null comment '文件索引',  
    `file_size` bigint(20) default null comment '文件大小',  
    `file_path` varchar(100) default null comment '文件路径',  
    `duration` int(11) default null comment '持续时间（秒）',  
    primary key (`file_id`) using btree,  
    key `idx_video_id` (`video_id`) using btree  
) engine=InnoDB default charset=utf8mb4 row_format=dynamic comment='视频文件信息';
```

```
CREATE TABLE `user_action`(  
    `action_id`     int(11)     NOT NULL AUTO_INCREMENT COMMENT '自增ID',  
    `video_id`      varchar(10) NOT NULL COMMENT '视频ID',  
    `video_user_id` varchar(10) NOT NULL COMMENT '视频用户ID',  
    `comment_id`    int(11)     NOT NULL DEFAULT '0' COMMENT '评论ID',  
    `action_type`   tinyint(1)  NOT NULL COMMENT '0:评论喜欢点赞 1:讨厌评论 2:视频点赞 3:视频收藏 4:视频投币',  
    `action_count`  int(11)     NOT NULL COMMENT '数量',  
    `user_id`       varchar(10) NOT NULL COMMENT '用户id',  
    `action_time`   datetime    NOT NULL COMMENT '操作时间',  
    PRIMARY KEY (`action_id`) USING BTREE,  
    UNIQUE KEY `idx_key_video_comment_type_user` (`video_id`, `comment_id`, `action_type`, `user_id`) USING BTREE,  
    KEY `idx_video_id` (`video_id`) USING BTREE,  
    KEY `idx_user_id` (`user_id`) USING BTREE,  
    KEY `idx_type` (`action_type`) USING BTREE,  
    KEY `idx_action_time` (`action_time`) USING BTREE  
) ENGINE=InnODB AUTO_INCREMENT=61 DEFAULT CHARSET=Utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户行为 点赞、评论';  
  
CREATE TABLE `video_danmu` (  
    `danmu_id` int(11)NOT NULL AUTO_INCREMENT COMMENT'自增ID',  
    `video_id` varchar(10) NOT NULL COMMENT '视频ID',  
    `file_id` varchar(20) NOT NULL COMMENT '唯一ID',  
    `user_id` varchar(15) NOT NULL COMMENT'用户ID',  
    `post_time` datetime DEFAULT NULL COMMENT '发布时间',  
    `text` varchar(300) DEFAULT NULL COMMENT '内容',  
    `mode` tinyint(1) DEFAULT NULL COMMENT '展示位置',  
    `color` varchar(10) DEFAULT NULL COMMENT '颜色',  
    `time` int(11) DEFAULT NULL COMMENT '展示时间',  
    primary key (`danmu_id`) using btree ,  
    key `idx_file_id` (`file_id`) using btree  
) ENGINE=InnODB AUTO_INCREMENT=19 DEFAULT CHARSET=Utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='视频弹幕';  
  
CREATE TABLE `video_comment`(  
    `comment_id`    int(11)     NOT NULL AUTO_INCREMENT COMMENT '评论ID',  
    `p_comment_id`  int(11)     NOT NULL COMMENT '父级评论ID',  
    `video_id`      varchar(10) NOT NULL COMMENT '视频ID',  
    `video_user_id` varchar(10) NOT NULL COMMENT '视频用户ID',  
    `content`       varchar(500) DEFAULT NULL COMMENT '回复内容',  
    `img_path`      varchar(150) DEFAULT NULL COMMENT '图片',  
    `user_id`       varchar(15) NOT NULL COMMENT '用户ID',  
    `reply_user_id` varchar(15)  DEFAULT NULL COMMENT '回复人ID',  
    `top_type`      tinyint(4)   DEFAULT '0' COMMENT '8:未置顶 1:置顶',  
    `post_time`     datetime    NOT NULL COMMENT '发布时间',  
    `like_count`    int(11)      DEFAULT '0' COMMENT '喜欢数量',  
    `hate_count`    int(11)      DEFAULT '0' COMMENT '讨厌数量',  
    PRIMARY KEY (`comment_id`) USING BTREE,  
    KEY `idx_post_time` (`post_time`) USING BTREE,  
    KEY `idx_top` (`top_type`) USING BTREE,  
    KEY `idx_p_id` (`p_comment_id`) USING BTREE,  
    KEY `idx_user_id` (`user_id`) USING BTREE,  
    KEY `idx_video_id` (`video_id`) USING BTREE  
) ENGINE=InnODB AUTO_INCREMENT=22 DEFAULT CHARSET=Utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='评论';
```

```
create table `user_focus` (  
    `user_id` varchar(10) not null comment '',  
    `focus_user_id` varchar(10) not null comment '',  
    `focus_time` datetime default null,  
    primary key (`user_id`, `focus_user_id`) using btree  
) engine =InnoDB default charset =utf8mb4 row_format =dynamic;
create table `user_video_series` (  
    `series_id` int(11) not null auto_increment comment '列表ID',  
    `series_name` varchar(100) not null comment '列表名称',  
    `series_description` varchar(200) default null comment '描述',  
    `user_id` varchar(10) not null comment '用户ID',  
    `sort` tinyint(4) not null comment '排序',  
    `update_time` timestamp null default null on update current_timestamp comment '更新时间',  
    primary key (`series_id`) using btree,  
    key `idx_user_id` (`user_id`) using btree  
) engine =InnoDB auto_increment=3 default charset =utf8mb4 row_format =dynamic comment ='用户视频序列归档';
create table `user_video_series_video` (  
    `series_id` int(11) not null auto_increment comment '列表ID',  
    `video_id` varchar(10) not null comment '视频ID',  
    `user_id` varchar(10) not null comment '用户ID',  
    `sort` tinyint(4) not null comment '排序',  
    primary key (`series_id`, `video_id`) using btree  
) engine =InnoDB default charset =utf8mb4 row_format =dynamic;
```

```
create table `user_message` (  
    `message_id` int(11) not null auto_increment comment '消息ID',  
    `user_id` varchar(10) not null comment '用户ID',  
    `video_id` varchar(10) default null comment '视频ID',  
    `message_type` tinyint(1) default null comment '消息类型',  
    `send_user_id` varchar(10) default null comment '发送人ID',  
    `read_type` tinyint(1) default null comment '0:未读 1:已读',  
    `create_time` datetime default null comment '创建时间',  
    `extend_json` text comment '扩展信息',  
    primary key (`message_id`) using btree ,  
    key `idx_user_id` (`user_id`) using btree ,  
    key `idx_read_type` (`read_type`) using btree ,  
    key `idx_message_type` (`message_type`) using btree  
) engine =InnoDB auto_increment=18 charset =utf8mb4 row_format =dynamic comment ='用户消息表';  
  
create table  `video_play_history` (  
    `user_id` varchar(10) not null comment '用户ID',  
    `video_id` varchar(10) not null comment '视频ID',  
    `file_index` int(11) not null comment '文件索引',  
    `last_update_time` datetime not null comment '最后更新时间',  
    primary key (`user_id`(4), `video_id`) using btree ,  
    key `idx_video_id` (`video_id`) using btree ,  
    key `idx_user_id` (`user_id`) using btree  
)engine =InnoDB default charset =utf8mb4 row_format =dynamic comment ='视频播放历史';  
  
create table `statistics_info` (  
    `statistics_date` varchar(10) not null comment '统计日期',  
    `user_id` varchar(10) not null comment '用户ID',  
    `data_type` tinyint(1) not null comment '数据统计类型',  
    `statistics_count` int(11) default null comment '统计数量',  
    primary key (`statistics_date`, `user_id`, `data_type`) using btree )engine =InnoDB default charset =utf8mb4 row_format =dynamic comment ='数据统计';
```