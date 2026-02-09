package com.easylive.web.controller;

import com.easylive.component.RedisComponent;
import com.easylive.entity.dto.SysSettingDto;
import com.easylive.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sysSetting")
@Validated
@Slf4j
public class SysSettingController extends ABaseController {
    @Resource
    public RedisComponent redisComponent;

    @RequestMapping("/getSetting")
    public ResponseVO getSetting() {
        SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
        return getSuccessResponseVO(redisComponent);
    }
}
