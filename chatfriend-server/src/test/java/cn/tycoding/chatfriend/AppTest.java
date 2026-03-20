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

package cn.tycoding.chatfriend;

import cn.tycoding.chatfriend.common.core.properties.AuthProps;
import cn.tycoding.chatfriend.upms.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tycoding
 * @since 2024/4/4
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    @Autowired
    private AuthProps authProps;

    public static void main(String[] args) {
        String encode = AuthUtil.encode("chatfriend-salt", "123456");
        System.out.println(encode);
    }

    @Test
    public void t1() {
        System.out.println("-----");
    }
}
