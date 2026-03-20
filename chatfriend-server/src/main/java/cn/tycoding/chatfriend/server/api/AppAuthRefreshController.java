package cn.tycoding.chatfriend.server.api;

import cn.tycoding.chatfriend.common.core.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AppAuthRefreshController {

    @PostMapping("/refresh")
    public R<String> refreshToken() {
        // Mock token generation logic for the new refresh mechanism
        return R.ok("eyJhbGciOiJIUzUxMiJ9.mock_refresh_token_payload");
    }
}
