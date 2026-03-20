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

package cn.tycoding.chatfriend.auth.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.tycoding.chatfriend.upms.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tycoding
 * @since 2024/1/5
 */
@Slf4j
@Component
public class PermissionService implements StpInterface {

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return AuthUtil.getPermissionNames();
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return AuthUtil.getRoleNames();
    }
}
