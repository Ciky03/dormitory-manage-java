package cloud.ciky.job.service;

import cloud.ciky.base.constant.RedisConstants;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AuthService {

    @Value("${xxl.job.auth.url}")
    private String authUrl;

    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;

    public String getAccessToken() {
        String key = RedisConstants.Auth.LOGIN_TOKEN+"job";
        if(redisTemplate.hasKey(key)){
            return redisTemplate.opsForValue().get(key).toString();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic YWRtaW46NTYzNDE4NWFhMWZiZWQ2MjRkNDRkMDdmOGExNmRlMzE=");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            authUrl,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if(CharSequenceUtil.isBlank(response.getBody())){
            return null;
        }
        String str = JSONUtil.parseObj(response.getBody()).getJSONObject("data").getStr("access_token");
        if(CharSequenceUtil.isBlank(str)){
            return null;
        }
        redisTemplate.opsForValue().set(RedisConstants.Auth.LOGIN_TOKEN+"job",str,1, TimeUnit.HOURS);
        return str;
    }

}