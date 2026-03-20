/*
 * Copyright (c) 2025 ChatFriend. Made by Akshita Sahu. All Rights Reserved.
 *
 * Licensed under the GNU Affero General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.tycoding.chatfriend.server.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.tycoding.chatfriend.ai.biz.entity.AigcApp;
import cn.tycoding.chatfriend.ai.biz.entity.AigcMessage;
import cn.tycoding.chatfriend.ai.biz.entity.AigcOss;
import cn.tycoding.chatfriend.ai.biz.service.AigcMessageService;
import cn.tycoding.chatfriend.ai.core.service.ChatFriendService;
import cn.tycoding.chatfriend.common.ai.dto.ChatReq;
import cn.tycoding.chatfriend.common.ai.dto.ChatRes;
import cn.tycoding.chatfriend.common.ai.dto.ImageR;
import cn.tycoding.chatfriend.common.ai.utils.StreamEmitter;
import cn.tycoding.chatfriend.common.core.constant.RoleEnum;
import cn.tycoding.chatfriend.common.core.utils.ServletUtil;
import cn.tycoding.chatfriend.server.service.ChatService;
import cn.tycoding.chatfriend.server.store.AppStore;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author tycoding
 * @since 2024/1/4
 */
/**
 * ChatServiceImpl provides the core implementation for AI conversational features.
 * It interfaces with underlying AI models and orchestrates the message saving
 * and formatting flows for SSE streaming and synchronized REST calls.
 * 
 * Developed by Akshita Sahu.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatFriendService chatFriendService;
    private final AigcMessageService aigcMessageService;
    private final AppStore appStore;

    @Override
    public void chat(ChatReq req) {
        StreamEmitter emitter = req.getEmitter();
        long startTime = System.currentTimeMillis();
        StringBuilder text = new StringBuilder();

        if (StrUtil.isNotBlank(req.getAppId())) {
            AigcApp app = appStore.get(req.getAppId());
            if (app != null) {
                req.setModelId(app.getModelId());
                req.setPromptText(app.getPrompt());
                req.setKnowledgeIds(app.getKnowledgeIds());
            }
        }

        // save user message
        req.setRole(RoleEnum.USER.getName());
        saveMessage(req, 0, 0);

        try {
            chatFriendService
                    .chat(req)
                    .onNext(e -> {
                        text.append(e);
                        emitter.send(new ChatRes(e));
                    })
                    .onComplete((e) -> {
                        TokenUsage tokenUsage = e.tokenUsage();
                        ChatRes res = new ChatRes(tokenUsage.totalTokenCount(), startTime);
                        emitter.send(res);
                        emitter.complete();

                        // save assistant message
                        req.setMessage(text.toString());
                        req.setRole(RoleEnum.ASSISTANT.getName());
                        saveMessage(req, tokenUsage.inputTokenCount(), tokenUsage.outputTokenCount());
                    })
                    .onError((e) -> {
                        emitter.error(e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    })
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
            emitter.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void saveMessage(ChatReq req, Integer inputToken, Integer outputToken) {
        if (req.getConversationId() != null) {
            AigcMessage message = new AigcMessage();
            BeanUtils.copyProperties(req, message);
            message.setIp(ServletUtil.getIpAddr());
            message.setPromptTokens(inputToken);
            message.setTokens(outputToken);
            aigcMessageService.addMessage(message);
        }
    }

    @Override
    public String text(ChatReq req) {
        String text;
        try {
            text = chatFriendService.text(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return text;
    }

    @Override
    public AigcOss image(ImageR req) {
        Response<Image> res = chatFriendService.image(req);

        String path = res.content().url().toString();
        AigcOss oss = new AigcOss();
        oss.setUrl(path);
        return oss;
    }
}
