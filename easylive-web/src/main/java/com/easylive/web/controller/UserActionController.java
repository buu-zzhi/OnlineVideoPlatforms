package com.easylive.web.controller;

import com.easylive.annotation.RecordUserMessage;
import com.easylive.entity.enums.MessageTypeEnum;
import com.easylive.web.annotation.GlobalInterceptor;
import com.easylive.entity.constants.Constants;
import com.easylive.entity.po.UserAction;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.service.UserActionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.*;

@RestController
@RequestMapping("/userAction")
@Validated
public class UserActionController extends  ABaseController {

    private final UserActionService userActionService;

    public UserActionController(UserActionService userActionService) {
        super();
        this.userActionService = userActionService;
    }

    @RequestMapping("/doAction")
    @RecordUserMessage(messageType = MessageTypeEnum.LIKE)
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO doAction(@NotEmpty String videoId,
                               @NotNull Integer actionType,
                                @Max(2) @Min(1) Integer actionCount,
                               Integer commentId) {
        UserAction userAction = new UserAction();
        userAction.setVideoId(videoId);
        userAction.setUserId(getTokenUserInfoDto().getUserId());
        userAction.setActionType(actionType);
        actionCount = actionCount == null ? Constants.ONE : actionCount;
        userAction.setActionCount(actionCount);

        commentId = commentId == null ? Constants.ZERO : commentId;
        userAction.setCommentId(commentId);

        userActionService.saveAction(userAction);
        return getSuccessResponseVO(null);
    }
}
